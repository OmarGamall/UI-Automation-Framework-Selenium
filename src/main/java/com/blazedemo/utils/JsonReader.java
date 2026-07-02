package com.blazedemo.utils;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe JSON test data reader backed by a shared file cache.
 * DocumentContext instances are cached per filename and shared across threads for read operations.
 */
public class JsonReader {

    private static final String TEST_DATA_DIR = "test-data/";

    // Thread-safe cache: filename -> parsed DocumentContext
    private static final Map<String, DocumentContext> FILE_CACHE = new ConcurrentHashMap<>();

    // Pre-configured JsonPath using Jackson; missing paths return null instead of throwing exceptions
    private static final Configuration JSON_PATH_CONFIG = Configuration.builder()
            .jsonProvider(new JacksonJsonProvider())
            .mappingProvider(new JacksonMappingProvider())
            .options(Option.DEFAULT_PATH_LEAF_TO_NULL, Option.SUPPRESS_EXCEPTIONS)
            .build();

    private final DocumentContext documentContext;

    /**
     * Constructs a JsonReader for the given file, loading and caching it if not already present.
     *
     * @param jsonFileName filename without extension (e.g. "orders", "users")
     */
    public JsonReader(String jsonFileName) {
        this.documentContext = FILE_CACHE.computeIfAbsent(jsonFileName, JsonReader::loadJsonFile);
    }

    /**
     * Helper method to read the file from disk or classpath and parse it.
     */
    private static DocumentContext loadJsonFile(String fileName) {
        String relativePath = TEST_DATA_DIR + fileName + ".json";

        // 1. Classpath lookup
        try (InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(relativePath)) {
            if (is != null) {
                return JsonPath.using(JSON_PATH_CONFIG).parse(is);
            }
        } catch (IOException e) {
            // Ignore exception and fall through to filesystem fallback
        }

        // 2. Filesystem fallback using project-root-relative path
        String baseDir = System.getProperty("project.basedir", Paths.get("").toAbsolutePath().toString());
        String fallbackPath = Paths.get(baseDir, "src", "test", "resources", relativePath).toString();

        try (InputStream is = new FileInputStream(fallbackPath)) {
            return JsonPath.using(JSON_PATH_CONFIG).parse(is);
        } catch (IOException e) {
            // Fall through to throw exception below
        }

        // Fail fast if the file is not found anywhere
        throw new TestDataException(
                "Test data file not found: '" + fileName + ".json'. " +
                "Searched classpath path '" + relativePath + "' " +
                "and filesystem path '" + fallbackPath + "'."
        );
    }

    /**
     * Reads a value from the parsed JSON using a JsonPath expression.
     *
     * @param jsonPath JsonPath expression (e.g. "$.order.id", "$.users[0].email")
     * @param <T>      expected return type
     * @return the value at the path, or null if the path does not exist in the document
     */
    @SuppressWarnings("unchecked")
    public <T> T getJsonData(String jsonPath) {
        try {
            return documentContext.read(jsonPath);
        } catch (Exception e) {
            throw new TestDataException(
                    "Invalid JsonPath expression '" + jsonPath + "': " + e.getMessage(), e
            );
        }
    }

    // Custom exception for self-describing test data failures
    public static class TestDataException extends RuntimeException {
        public TestDataException(String message) {
            super(message);
        }
        public TestDataException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}

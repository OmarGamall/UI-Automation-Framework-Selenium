package testcases;

import org.testng.Assert;
import org.testng.annotations.Test;
import utils.JsonReader;
import utils.JsonReader.TestDataException;

import java.util.List;

public class JsonReaderTest {

    @Test(description = "Verify reading various data types using JsonPath")
    public void testReadBasicDataTypes() {
        JsonReader reader = new JsonReader("user-data");

        // Read string
        String username = reader.getJsonData("$.user.username");
        Assert.assertEquals(username, "testuser", "Username should match test-data value");

        // Read nested string
        String city = reader.getJsonData("$.user.address.city");
        Assert.assertEquals(city, "Cairo", "City should match test-data value");

        // Read list
        List<String> roles = reader.getJsonData("$.user.roles");
        Assert.assertNotNull(roles, "Roles list should not be null");
        Assert.assertEquals(roles.size(), 2, "Roles count should match");
        Assert.assertTrue(roles.contains("admin"), "Roles should contain 'admin'");

        // Read double
        Double price = reader.getJsonData("$.items[0].price");
        Assert.assertEquals(price, 10.5, "Price should match");

        // Read integer
        Integer itemId = reader.getJsonData("$.items[1].id");
        Assert.assertEquals(itemId, Integer.valueOf(2), "Item ID should match");
    }

    @Test(description = "Verify that thread-safe cache returns pre-parsed context without re-reading")
    public void testCachePerformanceAndConsistency() {
        JsonReader firstReader = new JsonReader("user-data");
        JsonReader secondReader = new JsonReader("user-data");

        // They should read exactly identical data
        String firstUser = firstReader.getJsonData("$.user.username");
        String secondUser = secondReader.getJsonData("$.user.username");

        Assert.assertEquals(firstUser, secondUser);
    }

    @Test(description = "Verify that querying non-existent paths returns null instead of throwing an exception")
    public void testMissingPathsReturnNull() {
        JsonReader reader = new JsonReader("user-data");

        // Missing leaf path
        String nonExistentKey = reader.getJsonData("$.user.nonexistent");
        Assert.assertNull(nonExistentKey, "Non-existent path should return null");

        // Missing nested leaf path
        String nonExistentNestedKey = reader.getJsonData("$.user.address.zipcode");
        Assert.assertNull(nonExistentNestedKey, "Non-existent nested path should return null");
    }

    @Test(description = "Verify that a malformed JsonPath expression throws TestDataException")
    public void testMalformedPathThrowsException() {
        JsonReader reader = new JsonReader("user-data");

        Assert.assertThrows(TestDataException.class, () -> {
            reader.getJsonData("$.items[invalid-syntax]");
        });
    }

    @Test(description = "Verify that trying to load a non-existent JSON file throws TestDataException")
    public void testMissingFileThrowsException() {
        Assert.assertThrows(TestDataException.class, () -> {
            new JsonReader("missing-file-name");
        });
    }
}

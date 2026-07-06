package com.automationexercise.models;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private String name;
    private Integer price;
    private String currency; // e.g., "Rs", "USD"
    private String category;
    private Boolean availability; // true -> In stock, false otherwise
    private String condition;
    private String brand;
}

package com.example.demo.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private int id;
    
    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;
    
    @NotNull(message = "Giá sản phẩm không được để trống")
    @Min(value = 1, message = "Giá sản phẩm phải từ 1 - 9999999")
    @Max(value = 9999999, message = "Giá sản phẩm phải từ 1 - 9999999")
    private Integer price;
    
    private String image; // Lưu tên file ảnh
    
    @NotBlank(message = "Vui lòng chọn danh mục")
    private String category;
    
    public Product(String name, Integer price, String image, String category) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.category = category;
    }
}

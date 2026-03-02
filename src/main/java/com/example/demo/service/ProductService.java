package com.example.demo.service;

import com.example.demo.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private List<Product> products = new ArrayList<>(Arrays.asList(
        new Product(1, "laptop 1", 30000, "laptop1.jpg", "Laptop"),
        new Product(2, "điện thoại 1", 20000, "phone1.jpg", "Điện thoại")
    ));
    
    public List<Product> getAllProducts() {
        return products;
    }
    
    public Product getProductById(int id) {
        return products.stream()
                .filter(product -> product.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    public void addProduct(Product product) {
        // Tự động tạo ID mới
        int newId = products.stream()
                .mapToInt(Product::getId)
                .max()
                .orElse(0) + 1;
        product.setId(newId);
        products.add(product);
    }
    
    public void updateProduct(int id, Product updatedProduct) {
        products.stream()
                .filter(product -> product.getId() == id)
                .findFirst()
                .ifPresent(product -> {
                    product.setName(updatedProduct.getName());
                    product.setPrice(updatedProduct.getPrice());
                    if(updatedProduct.getImage() != null)
                        product.setImage(updatedProduct.getImage());
                    product.setCategory(updatedProduct.getCategory());
                });
    }
    
    public void deleteProduct(int id) {
        products.removeIf(product -> product.getId() == id);
    }
    
    /**
     * Upload image file và trả về tên file đã lưu
     */
    public void updateImage(Product newProduct, MultipartFile imageProduct) {
        String contentType = imageProduct.getContentType();
        if (contentType == null || !imageProduct.getContentType().startsWith("Tệp tải lên không phải là hình ảnh!")) {
            // Validation: file phải là hình ảnh
        }
        
        if (!imageProduct.isEmpty()) {
            try {
                Path dirImages = Paths.get("static/images");
                if (!Files.exists(dirImages)) {
                    Files.createDirectories(dirImages);
                }
                
                String newFileName = UUID.randomUUID() + "_" + imageProduct.getOriginalFilename();
                Path pathFileUpload = dirImages.resolve(newFileName);
                Files.copy(imageProduct.getInputStream(), pathFileUpload, StandardCopyOption.REPLACE_EXISTING);
                
                newProduct.setImage(newFileName);
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        }
    }
}

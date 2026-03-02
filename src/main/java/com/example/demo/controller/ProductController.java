package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import com.example.demo.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryService categoryService;
    
    private static final String UPLOAD_DIR = "src/main/resources/static/images/";
    private static final long MAX_FILE_SIZE = 200 * 1024; // 200 KB
    
    // Hiển thị danh sách sản phẩm
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "Product";
    }
    
    // Hiển thị form thêm sản phẩm
    @GetMapping("/create")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "AddProduct";
    }
    
    // Xử lý thêm sản phẩm với validation
    @PostMapping("/create")
    public String addProduct(@Valid @ModelAttribute("product") Product product, 
                            BindingResult bindingResult,
                            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                            Model model) {
        
        // Kiểm tra validation errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "AddProduct";
        }
        
        // Xử lý upload file
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = imageFile.getOriginalFilename();
            
            // Kiểm tra độ dài tên file không quá 200 ký tự
            if (fileName != null && fileName.length() > 200) {
                model.addAttribute("imageError", "Tên hình ảnh không quá 200 kí tự");
                model.addAttribute("categories", categoryService.getAllCategories());
                return "AddProduct";
            }
            
            // Kiểm tra kích thước file không quá 200KB
            if (imageFile.getSize() > MAX_FILE_SIZE) {
                model.addAttribute("imageError", "Kích thước file không quá 200 KB");
                model.addAttribute("categories", categoryService.getAllCategories());
                return "AddProduct";
            }
            
            try {
                // Tạo thư mục nếu chưa tồn tại
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                
                // Lưu file
                Path path = Paths.get(UPLOAD_DIR + fileName);
                Files.write(path, imageFile.getBytes());
                product.setImage(fileName);
            } catch (IOException e) {
                model.addAttribute("imageError", "Lỗi khi upload hình ảnh");
                model.addAttribute("categories", categoryService.getAllCategories());
                return "AddProduct";
            }
        } else {
            product.setImage("no-image.jpg");
        }
        
        productService.addProduct(product);
        return "redirect:/products";
    }
    
    // Hiển thị form sửa sản phẩm
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Product product = productService.getProductById(id);
        if (product != null) {
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "UpdateProduct";
        }
        return "redirect:/products";
    }
    
    // Xử lý cập nhật sản phẩm với validation
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable int id, 
                               @Valid @ModelAttribute("product") Product product,
                               BindingResult bindingResult,
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                               Model model) {
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "UpdateProduct";
        }
        
        // Lấy sản phẩm cũ để giữ lại ảnh nếu không upload ảnh mới
        Product existingProduct = productService.getProductById(id);
        
        // Xử lý upload file mới
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = imageFile.getOriginalFilename();
            
            // Kiểm tra độ dài tên file không quá 200 ký tự
            if (fileName != null && fileName.length() > 200) {
                model.addAttribute("imageError", "Tên hình ảnh không quá 200 kí tự");
                model.addAttribute("categories", categoryService.getAllCategories());
                return "UpdateProduct";
            }
            
            // Kiểm tra kích thước file không quá 200KB
            if (imageFile.getSize() > MAX_FILE_SIZE) {
                model.addAttribute("imageError", "Kích thước file không quá 200 KB");
                model.addAttribute("categories", categoryService.getAllCategories());
                return "UpdateProduct";
            }
            
            try {
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                
                Path path = Paths.get(UPLOAD_DIR + fileName);
                Files.write(path, imageFile.getBytes());
                product.setImage(fileName);
            } catch (IOException e) {
                model.addAttribute("imageError", "Lỗi khi upload hình ảnh");
                model.addAttribute("categories", categoryService.getAllCategories());
                return "UpdateProduct";
            }
        } else {
            // Giữ lại ảnh cũ nếu không upload ảnh mới
            product.setImage(existingProduct.getImage());
        }
        
        productService.updateProduct(id, product);
        return "redirect:/products";
    }
    
    // Xử lý xóa sản phẩm
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}

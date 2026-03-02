package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {
    
    @Autowired
    private BookService bookService;
    
    @GetMapping("/home")
    public String index() {
        return "index";
    }
    
    // Hiển thị danh sách sách
    @GetMapping("/books")
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "Book";
    }
    
    // Hiển thị form thêm sách
    @GetMapping("/books/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        return "Add";
    }
    
    // Xử lý thêm sách
    @PostMapping("/books/add")
    public String addBook(@ModelAttribute Book book) {
        bookService.addBook(book);
        return "redirect:/books";
    }
    
    // Hiển thị form sửa sách
    @GetMapping("/books/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Book book = bookService.getBookById(id);
        if (book != null) {
            model.addAttribute("book", book);
            return "Update";
        }
        return "redirect:/books";
    }
    
    // Xử lý cập nhật sách
    @PostMapping("/books/edit/{id}")
    public String updateBook(@PathVariable int id, @ModelAttribute Book book) {
        bookService.updateBook(id, book);
        return "redirect:/books";
    }
    
    // Xử lý xóa sách
    @PostMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable int id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}

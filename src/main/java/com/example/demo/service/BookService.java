package com.example.demo.service;

import com.example.demo.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class BookService {
    private List<Book> books = new ArrayList<>(Arrays.asList(
        new Book(1, "J2EE", "Huy Cuong"),
        new Book(2, "Spring Boot", "Nguyen Van A")
    ));
    
    public List<Book> getAllBooks() {
        return books;
    }
    
    public Book getBookById(int id) {
        return books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    public void addBook(Book book) {
        // Tự động tạo ID mới
        int newId = books.stream()
                .mapToInt(Book::getId)
                .max()
                .orElse(0) + 1;
        book.setId(newId);
        books.add(book);
    }
    
    public void updateBook(int id, Book updatedBook) {
        books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .ifPresent(book -> {
                    book.setTitle(updatedBook.getTitle());
                    book.setAuthor(updatedBook.getAuthor());
                });
    }
    
    public void deleteBook(int id) {
        books.removeIf(book -> book.getId() == id);
    }
}

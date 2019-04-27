package com.yq.web.service;

import com.yq.web.entity.Book;
import com.yq.web.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p> book service</p>
 * @author youq  2019/4/27 18:50
 */
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> findByReader(String reader) {
        return bookRepository.findByReader(reader);
    }

    public void save(Book book) {
        bookRepository.save(book);
    }

}

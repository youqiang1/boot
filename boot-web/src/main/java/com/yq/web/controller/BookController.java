package com.yq.web.controller;

import com.yq.web.entity.Book;
import com.yq.web.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * <p> Book Controller</p>
 * @author youq  2019/4/27 18:50
 */
@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @RequestMapping(value="/{reader}", method= RequestMethod.GET)
    public String readerBooks(@PathVariable("reader") String reader, Model model) {
        List<Book> books = bookService.findByReader(reader);
        if (!CollectionUtils.isEmpty(books)) {
            model.addAttribute("books", books);
        }
        return "readingList";
    }

    @RequestMapping(value="/{reader}", method=RequestMethod.POST)
    public String addToBooks(@PathVariable("reader") String reader, Book book) {
        book.setReader(reader);
        bookService.save(book);
        return "redirect:/{reader}";
    }

}

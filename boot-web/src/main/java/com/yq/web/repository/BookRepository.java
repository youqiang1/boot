package com.yq.web.repository;

import com.yq.web.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p> 书籍Repository</p>
 * @author youq  2019/4/27 18:48
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findByReader(String reader);

}

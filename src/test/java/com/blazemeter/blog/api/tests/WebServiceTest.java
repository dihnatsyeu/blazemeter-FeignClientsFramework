package com.blazemeter.blog.api.tests;

import com.blazemeter.blog.api.client.BookStoreClient;
import com.blazemeter.blog.api.environment.MockServerManager;
import com.blazemeter.blog.api.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.*;

import java.util.List;

@SpringBootTest
public class WebServiceTest extends AbstractTestNGSpringContextTests {


    @Autowired
    protected MockServerManager serverManager;

    @Autowired
    protected BookStoreClient bookStoreClient;


    @BeforeClass
    public void startServer() {
        serverManager.start();
    }

    @AfterClass
    public void shutDown() {
        serverManager.shutDown();
    }

    @Test
    public void testService() {
        List<Book> books = bookStoreClient.getBooks();
        assert !books.isEmpty();
        Book bookToBuy = books.get(0);
        Book boughtBook = bookStoreClient.buyBook(books.get(0));
        assert bookToBuy.equals(boughtBook);
    }
}

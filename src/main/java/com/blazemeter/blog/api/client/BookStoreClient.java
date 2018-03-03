package com.blazemeter.blog.api.client;

import com.blazemeter.blog.api.model.Book;
import feign.Headers;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "bookStoreClient", url = "http://" + "${host}" + ":${port}", configuration = BookClientConfiguration.class)
public interface BookStoreClient {

    @RequestMapping(method = RequestMethod.GET, path = "getBooks")
    public List<Book> getBooks();

    @RequestMapping(method = RequestMethod.POST, path = "buyBook")
    @Headers("Content-Type: application/json")
    public Book buyBook(@RequestBody Book book);
}

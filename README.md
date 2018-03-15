# blazemeter-FeignClientsFramework

# RESTful web service under test

To see how framework with usage of Feign Clients works, we need a RESTful web service for testing.
In the real world, you don’t need to create a web service that you would like to test with Feign, as it already exists. 
But in the demo we need to bypass this and create a mock server with the desired methods and responses. 
In this, the [MockServer](http://www.mock-server.com/) will come to our help.
Define a new dependency in pom.xml:
``` 
<dependency>
	<groupId>org.mock-server</groupId>
	<artifactId>mockserver-netty</artifactId>
	<version>5.3.0</version>
</dependency>
 ```
Created mock server simulates a bookstore service. It has two methods: 
getBooks – to view the list of available books. Each book has an author, title and price.
buyBook - method to buy a book. The buyBbook request accepts a book entity that is being purchased. It responds with thecode 200 if 
the book is found and returns the book that is bought.
Here is what Book entity looks like:
  ```
public class Book {

	private String title;
	private String author;
	private int price;

	public String getTitle() {
    	return title;
	}

	public void setTitle(String title) {
    	this.title = title;
	}

	public String getAuthor() {
    	return author;
	}

	public void setAuthor(String author) {
    	this.author = author;
	}

	public int getPrice() {
    	return price;
	}

	public void setPrice(int price) {
    	this.price = price;
	}
}
```

MockServerManager(in src/main/java/environment package) starts, configures and shuts down the web server. 

```
@Component
public class MockServerManager {

    private ClientAndServer mockServer;

    @Value("${host}")
    private String host;

    @Value("${port}")
    private int port;

    public void start() {
        this.mockServer = ClientAndServer.startClientAndServer(port);
        MockServerClient client = new MockServerClient(host, port);
        client.when(HttpRequest.request()
                .withMethod("POST")
                .withPath("/buyBook")
                .withBody(json("{\"author\":\"Homer\", \"title\":\"The Odyssey\", " +
                        "\"price\":200}", MatchType.STRICT)))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader(Header.header("Content-Type", "application/json"))
                        .withBody(json("{\"author\":\"Homer\", \"title\":\"The Odyssey\", " +
                                "\"price\":200}")));
        client.when(HttpRequest.request()
                .withMethod("GET")
                .withPath("/getBooks"))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader(Header.header("Content-Type","application/json"))
                        .withBody(json("[{\"author\":\"Homer\", \"title\":\"The Odyssey\"," +
                                " \"price\":200}]")));
    }

    public void shutDown() {
        this.mockServer.stop();
    }
}
```

Let’s look at this code:

In the start () method we have the following:
The line “this.mockServer = ClientAndServer.startClientAndServer(port);” starts a local web server with host localhost 
and port configured in properties. Later the line "MockServerClient client = new MockServerClient(host, port);" starts
new MockServerClient with the host and port from the properties. Spring Boot gives us a great flexibility in managing 
properties:it automatically recognizes and loads into a context all the properties located in the resource folder 
from the file with the name “application.properties”. Property values can be accessed directly with 
@Value("{[propertyName]}") annotation. There are many ways you can load external properties into your application, 
you can read about them in the [documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html).
A bean where property values are read should be also loaded via Spring Boot application context by annotating class 
with @Component. There are more annotations that can indicate that a bean should be managed by Spring, 
like @Repository, @Service or @Controller. All of them add additional meaning to beans they are applied to and usually 
used in MVC applications (or to work with databases if @Repository is used).

The server behavior is configured according to the usage of the MockServerClient. The syntax is quite straightforward:
1) 	Setup ‘POST’ method. When the server met the ‘POST’ request with the path ‘buyBook’ and the body 
("{\"author\":\"Homer\", \"title\":\"The Odyssey\"}"  in JSON format, it responds with the status code 200, and, 
with the header "Content-Type", "application/json"  indicating that the response is in JSON format. It responds with 
the body of the book that is purchased: ("{\"author\":\"Homer\", \"title\":\"The Odyssey\", \"price\":200}"

2) 	Setup “GET” method. When the server met the GET request with the path ‘/getBooks’, it responds with the header 
"Content-Type", "application/json", indicating that the response in in JSON format, the body          
"[{\"author\":\"Homer\", \"title\":\"The Odyssey\", \"price\":200}]" and the status code 200.

 
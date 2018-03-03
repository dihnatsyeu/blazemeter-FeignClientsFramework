package com.blazemeter.blog.api.environment;

import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.MatchType;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static org.mockserver.model.JsonBody.json;

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
                .withBody(json("{\"author\":\"Homer\", \"title\":\"The Odyssey\", \"price\":200}", MatchType.STRICT)))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader(Header.header("Content-Type", "application/json"))
                        .withBody(json("{\"author\":\"Homer\", \"title\":\"The Odyssey\", \"price\":200}")));
        client.when(HttpRequest.request()
                .withMethod("GET")
                .withPath("/getBooks"))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader(Header.header("Content-Type","application/json"))
                        .withBody(json("[{\"author\":\"Homer\", \"title\":\"The Odyssey\", \"price\":200}]")));
    }

    public void shutDown() {
        this.mockServer.stop();
    }
}

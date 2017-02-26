package com.guptarakesh.word2url;

import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

public class SearchVerticle extends AbstractVerticle {

    private HttpServer httpServer = null;

    @Override
    public void start() throws Exception {
        System.out.println("SearchVerticle started");

        httpServer = vertx.createHttpServer();

        httpServer.requestHandler(new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest request) {

                String q = request.getParam("q");
                Map<String, String> urls = Word2UrlConfig.getInstance().getSingleUrls();
                HttpServerResponse response = request.response();

                try {
                    String googlePre = "http://www.google.com/search?q=";
                    String qReplace = urls.get(q);
                    String location;
                    if (qReplace != null && qReplace.length() > 0) {
                        location = qReplace;
                    } else {
                        location = googlePre + q;
                    }

                    response.setStatusCode(302);
                    response.putHeader("Location", location);

                    response.end();

                } catch (Exception e) {
                    response.end();
                }
            }
        });

        String port = System.getenv("PORT");
        if (port == null || port.isEmpty()) {
            port = "8081";
        }
        int portInt = Integer.valueOf(port);
        httpServer.listen(portInt);
    }

    @Override
    public void stop() throws Exception {
        System.out.println("SearchVerticle stopped");
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.factory.vertx();

        vertx.deployVerticle(new SearchVerticle(), new Handler<AsyncResult<String>>() {
            @Override
            public void handle(AsyncResult<String> stringAsyncResult) {
                System.out.println("AsyncResult hooking completed, ie deployment complete");
            }
        });

        System.out.println("server started");
    }
}
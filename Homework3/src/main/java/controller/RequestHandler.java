package controller;

import http.HttpMethod;
import http.request.Request;
import http.response.Response;
import service.QuotesService;

public class RequestHandler {
    private final QuotesService quotesService;

    public RequestHandler(QuotesService quotesService){
        this.quotesService = quotesService;
    }

    public Response handle(Request request) throws Exception {
        if(request.getPath().equals("/quotes") && request.getHttpMethod().equals(HttpMethod.GET)) {
            return (new QuotesController(request, quotesService)).doGet();
        } else if(request.getPath().equals("/save-quote") && request.getHttpMethod().equals(HttpMethod.POST)) {
            return (new QuotesController(request, quotesService)).doPost();
        }

        throw new Exception("Page: " + request.getPath() + ". Method: " + request.getHttpMethod() + " not found!");
    }
}

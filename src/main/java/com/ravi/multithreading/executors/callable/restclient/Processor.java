package com.ravi.multithreading.executors.callable.restclient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.concurrent.Callable;

public class Processor implements Callable<String> {

    private String skip = "&$skip=";
    private String top = "&$top=";
    String query = "https://services.odata.org/v4/TripPinServiceRW/People?";
    String jsonFormat = "$format=json&$select=FirstName";
    String finalQuery = query + jsonFormat;

    public Processor(int skip, int top) {
        this.skip = this.skip + "" + skip;
        this.top = this.top + "" + top;
        finalQuery = finalQuery + this.skip + this.top;
    }

    public String call() throws Exception {

        return getRequest();
    }

    private String getRequest() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        System.out.println("finalQuery:" + finalQuery);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(finalQuery, String.class);
        int statusCode = responseEntity.getStatusCode().value();
        System.out.println("Status code:" + statusCode);

        return responseEntity.getBody();
    }
}
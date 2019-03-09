package com.ravi.multithreading.executors.callable.restclient;

import org.apache.cxf.helpers.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.concurrent.Callable;

public class Processor implements Callable<String> {

    private String skip = "&$skip=";
    private String top = "&$top=";
    String query = "http://api.oceandrivers.com/v1.0/getWebCams/?";
    String jsonFormat = "$format=json";
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
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("dummy", "dummy"));
        final HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(new HttpGet(finalQuery), context);
        int statusCode = response.getStatusLine().getStatusCode();
        String value = IOUtils.toString(response.getEntity().getContent());
        return value;
    }
}
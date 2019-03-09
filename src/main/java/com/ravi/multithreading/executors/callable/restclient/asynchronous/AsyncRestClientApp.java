package com.ravi.multithreading.executors.callable.restclient.asynchronous;

import com.ravi.multithreading.executors.callable.restclient.Processor;
import org.apache.commons.lang3.time.StopWatch;
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
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AsyncRestClientApp {
    static int numberOfCoresAvailable = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws IOException {
        asynchExecute();
    }

    public static long asynchExecute() {
        Instant start = Instant.now();
        StopWatch watch = new StopWatch();
        watch.start();
        System.out.println("Making use of " + numberOfCoresAvailable + " available processor cores!!!");
        int records = 300;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfCoresAvailable);
        List<Future<String>> results = execution(executorService);
        executorService.shutdown();

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println("Time Taken: " + timeElapsed);
        watch.stop();
        long timeTaken = watch.getTime();
        System.out.println("Time Elapsed: " + timeTaken);
        return timeTaken;
    }

    private static List<Future<String>> execution(ExecutorService executorService) {

        Integer count = 300;
        getCount();
        int chunks = count / numberOfCoresAvailable;
        List<Future<String>> results = new ArrayList<>();
        int skip = 0;
        int top = chunks;
        for (int i = 1; i <= numberOfCoresAvailable; i++) {
            Future<String> result = executorService.submit(new Processor(skip, top));
            skip = skip + chunks;
            results.add(result);
        }
        try {
            for (Future<String> result : results) {
                //System.out.println("Result:" + result.get());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return results;
    }

    private static void getCount() {
        String countQuery = "http://api.oceandrivers.com:80/v1.0/getWebCams/";
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("dummy", "dummy"));
        final HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = null;
        String value = "0";
        try {
            response = client.execute(new HttpGet(countQuery), context);
            value = IOUtils.toString(response.getEntity().getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("Dummy Count#####" + value);
    }

}
package com.ravi.multithreading.executors.callable.restclient.synchronous;

import com.ravi.multithreading.executors.callable.restclient.Processor;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SynchronousRestClientApp {
    static int numberOfCoresAvailable = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws IOException {
        syncExecute();
    }

    public static long syncExecute() {
        Instant start = Instant.now();
        StopWatch watch = new StopWatch();
        watch.start();
        System.out.println("NumberOfCoresAvailable : " + numberOfCoresAvailable);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfCoresAvailable);
        execution(executorService);

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println("Time Taken: " + timeElapsed);
        watch.stop();
        long timeTaken = watch.getTime();
        System.out.println("Time Elapsed: " + timeTaken);
        return timeTaken;
    }

    private static void execution(ExecutorService executorService) {

        Integer count = getCount();

        int chunks = count / numberOfCoresAvailable;
        List<String> results = new ArrayList<>();
        int skip = 0;
        int top = chunks;
        try {
            for (int i = 1; i <= numberOfCoresAvailable; i++) {
                String result = new Processor(skip, top).call();
                // Future<String> result = executorService.submit(new Processor(skip, top));
                skip = skip + chunks;
                results.add(result);
            }
            for (String result : results) {
                System.out.println("Result:" + result);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        executorService.shutdown();
    }

    private static int getCount() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("https://services.odata.org/v4/(S(lhtzqh123lmakckjbqv0l1kb))/TripPinServiceRW/People/$count", String.class);
        return Integer.parseInt(response.getBody());
    }

}
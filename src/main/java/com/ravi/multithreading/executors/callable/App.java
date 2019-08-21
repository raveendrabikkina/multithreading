package com.ravi.multithreading.executors.callable;

import org.apache.commons.lang3.time.StopWatch;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class App {
    static int numberOfCoresAvailable = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        Instant start = Instant.now();
        StopWatch watch = new StopWatch();
        watch.start();
        System.out.println("NumberOfCoresAvailable : " + numberOfCoresAvailable);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfCoresAvailable);
        execution(executorService);//1
        execution(executorService);//2
        execution(executorService);//3
        executorService.shutdown();
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println("Time Taken: " + timeElapsed);
        watch.stop();
        System.out.println("Time Elapsed: " + watch.getTime()); // Prints: Time Elapsed: 2501
    }

    private static void execution(ExecutorService executorService) {
        List<Future<String>> results = new ArrayList<>(300);
        for (int i = 1; i <= 300; i++) {
            Future<String> result = executorService.submit(new Processor(i));
            results.add(result);
        }
        try {
            for (Future<String> result : results) {
                System.out.println(result.get());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

package com.ravi.multithreading.executors.callable.restclient;

import com.ravi.multithreading.executors.callable.restclient.asynchronous.AsyncRestClientApp;
import com.ravi.multithreading.executors.callable.restclient.synchronous.SynchronousRestClientApp;

public class PerformanceTestApp {

    public static void main(String[] args) {
        long t1 = SynchronousRestClientApp.syncExecute();
        long t2 = AsyncRestClientApp.asynchExecute();

        System.out.println("Asynchronous invocation is " + t1 / t2 + " times faster than Synchronous invocation of Rest API !!!");
    }
}

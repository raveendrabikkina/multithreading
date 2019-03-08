package com.ravi.multithreading.executors.callable;

import java.util.concurrent.Callable;

public class Processor implements Callable<String> {

    private int id;

    public Processor(int id) {
        this.id = id;
    }

    public String call() throws Exception {
        Thread.sleep(10);
        return "ID : " + id;
    }
}

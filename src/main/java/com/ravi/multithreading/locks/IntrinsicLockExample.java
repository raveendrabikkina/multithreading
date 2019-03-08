package com.ravi.multithreading.locks;

/**
 * IntrinsicLockExample
 * <p>
 * Reference:
 * https://docs.oracle.com/javase/tutorial/essential/concurrency/locksync.html
 * <p>
 * 1. Intrinsic lock for the Class object synchronized(IntrinsicLockExample.class)
 * 2. Intrinsic lock for that method's object: synchronized(this)
 * <p>
 * 1. When a Thread tries to access static synchronized method it acquires the intrinsic lock for the Class object associated with the class.
 * 2. When a Thread tries to access synchronized method it it automatically acquires the intrinsic lock for that method's object.
 * <p>
 * This example clearly shows that incrementCounter1 and incrementCounter2 can be accessed simultaneously as we are using synchronization provided by Java.
 * <p>
 * To solve this we need to use explicit lock/extrinsic.
 */
public class IntrinsicLockExample {
    public static int counter1 = 0;
    public static int counter2 = 0;

    public static void main(String[] args) {
        final IntrinsicLockExample intrinsicLockExample = new IntrinsicLockExample();
        long start = System.nanoTime();
        try {
            Thread t1 = new Thread(new Runnable() {
                public void run() {
                    System.out.println("Running Thread1");
                    intrinsicLockExample.incrementCounter1();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            Thread t2 = new Thread(new Runnable() {
                public void run() {
                    System.out.println("Running Thread2");
                    intrinsicLockExample.incrementCounter2();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            t1.start();
            t2.start();
            t1.join();
            t2.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(intrinsicLockExample.counter1);
        System.out.println(intrinsicLockExample.counter2);
        long finish = System.nanoTime();
        long timeElapsed = finish - start;
        System.out.println(timeElapsed / 1000000);
    }


    public static synchronized void incrementCounter1() {
        sleep(3);
        System.out.println("incrementCounter1");
        counter1++;
    }

    private static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void incrementCounter2() {
        System.out.println("incrementCounter2");
        counter2++;
        sleep(1);
    }
}

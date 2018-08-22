package ru.javawebinar.basejava;

import java.util.ArrayList;
import java.util.List;

public class MainConcurrency {

    public static final int THREADS_NUMBER = 10000;
    private int counter;
    private static final Object LOCK = new Object();

    private static final Integer VAR1 = 0;
    private static final Integer VAR2 = 0;

    public static void main(String[] args) throws InterruptedException {

        //whyTheFuckDoesntItWork();
        whyTheFuckDoesntItWork2();

//        System.out.println(Thread.currentThread().getName());
//        Thread thread0 = new Thread() {
//            @Override
//            public void run() {
//                System.out.println(getName() + ", " + getState());
//                throw new IllegalStateException();
//            }
//        };
//
//        thread0.start();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println(Thread.currentThread().getName() + ", " + Thread.currentThread().getState());
//            }
//            private void inc() {
//                synchronized (this) {
////                    counter++;
//                }
//            }
//        }).start();
//
//        System.out.println(thread0.getState());
//
//        final MainConcurrency mainConcurrency = new MainConcurrency();
//
//        List<Thread> threads = new ArrayList<>(THREADS_NUMBER);
//        for (int i = 0; i < THREADS_NUMBER; i++) {
//            Thread thread = new Thread(() -> {
//                for (int j = 0; j < 100; j++) {
//                    mainConcurrency.inc();
//                }
//            });
//            thread.start();
//            threads.add(thread);
//        }
//
//        threads.forEach(t -> {
//            try {
//                t.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//
//        System.out.println(mainConcurrency.counter);

//        final String lock1 = "lock1";
//        final String lock2 = "lock2";
//        deadLock(lock1, lock2);
//        deadLock(lock2, lock1);
    }

    private static void deadLock(Object lock1, Object lock2) {

        new Thread(() -> {
            System.out.println("Waiting " + lock1);
            synchronized (lock1) {
                System.out.println("Holding " + lock1);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Waiting " + lock2);
                synchronized (lock2) {
                    System.out.println("Holding " + lock2);
                }
            }
        }).start();
    }

    private static void myDeadLock(Integer var1, Integer var2) throws InterruptedException {
        synchronized (var1) {
            var1 = 1;
            var1.notifyAll();
            synchronized (var2) {
                var2.wait();
                var2 = 2;
            }
        }
    }

    private static void whyTheFuckDoesntItWork() {
        Thread thread2 = new Thread() {
            @Override
            public void run() {
                System.out.println("thread 2 is waiting for VAR2");
                synchronized (VAR2) {
                    System.out.println("thread 2 is holding VAR2");
                    System.out.println("thread 2 is waiting for VAR1");
                    synchronized (VAR1) {
                        System.out.println("thread 2 is holding VAR1");
                    }
                }
                System.out.println("thread 2 is done");
            }
        };

        System.out.println("thread main is waiting for VAR1");
        synchronized (VAR1) {
            System.out.println("thread main is holding VAR1");
            thread2.start();
            try {
                System.out.println("thread main is about to sleep");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread main is awaken");
            System.out.println("thread main is waiting for VAR2");
            synchronized (VAR2) {
                System.out.println("thread main is holding VAR2");
            }
        }
        System.out.println("thread main is done");
    }

    private static void whyTheFuckDoesntItWork2() {
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                System.out.println("thread 1 is waiting for VAR1");
                synchronized (VAR1) {
                    System.out.println("thread 1 is holding VAR1");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("thread 1 is waiting for VAR2");
                    synchronized (VAR2) {
                        System.out.println("thread 1 is holding VAR2");
                    }
                }
                System.out.println("thread 1 is done");
            }
        };

        Thread thread2 = new Thread() {
            @Override
            public void run() {
                System.out.println("thread 2 is waiting for VAR2");
                synchronized (VAR2) {
                    System.out.println("thread 2 is holding VAR2");
                    System.out.println("thread 2 is waiting for VAR1");
                    synchronized (VAR1) {
                        System.out.println("thread 2 is holding VAR1");
                    }
                }
                System.out.println("thread 2 is done");
            }
        };
        thread1.start();
        thread2.start();
    }

    private synchronized void inc() {
//        synchronized (this) {
//        synchronized (MainConcurrency.class) {
        counter++;
//                wait();
//                readFile
//                ...
//        }
    }
}

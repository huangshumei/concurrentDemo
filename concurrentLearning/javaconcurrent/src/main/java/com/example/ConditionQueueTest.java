package com.example;

public class ConditionQueueTest {

    public static void main(String[] args) {
        ConditionQueue queue = new ConditionQueue();
        try {
            queue.testRun();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ConditionQueue {
    private boolean isOk = false;

    public void testRun() throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            synchronized (this) {
                while (!isOk) {
                    try {
                        System.out.println(String.format("t1:%s wait", Thread.currentThread().getId()));
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(String.format("t1:%s 唤醒", Thread.currentThread().getId()));
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (this) {
                while (!isOk) {
                    try {
                        System.out.println(String.format("t2:%s wait", Thread.currentThread().getId()));
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(String.format("t2:%s 唤醒", Thread.currentThread().getId()));
                }
            }
        });
        thread1.start();
        thread2.start();
        Thread.sleep(2000);
        synchronized (this) {
            isOk = true;
            this.notifyAll();
        }
    }
}

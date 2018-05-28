package com.example;

import java.util.LinkedList;
import java.util.List;

public class RLockTest {

    public static void main(String[] args) {

        test1();
        test2();

    }

    /**
     * 读写锁测试代码
     */
    public static void test1() {
        List list = new LinkedList();
        for (int i = 0; i < 10000; i++) {
            list.add(i);
        }
        //初始化数据
        RWLockList rwLockList = new RWLockList(list);

        Thread writer = new Thread(new Runnable() {
            @Override public void run() {
                for (int i = 0; i < 10000; i++) {
                    rwLockList.put(i);
                }
            }
        });
        Thread reader1 = new Thread(new Runnable() {
            @Override public void run() {
                for (int i = 0; i < 10000; i++) {
                    rwLockList.get(i);
                }
            }
        });
        Thread reader2 = new Thread(new Runnable() {
            @Override public void run() {
                for (int i = 0; i < 10000; i++) {
                    rwLockList.get(i);
                }
            }
        });
        long begin = System.currentTimeMillis();
        writer.start();
        reader1.start();
        reader2.start();
        try {
            writer.join();
            reader1.join();
            reader2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("RWLockList take " + (System.currentTimeMillis() - begin) + "ms" + list.size());
        list.forEach((x) -> System.out.print(x + " "));
        System.out.println();
    }

    /**
     * 同步锁测试代码
     */
    public static void test2() {
        List list = new LinkedList();
        for (int i = 0; i < 10000; i++) {
            list.add(i);
        }
        SyncList syncList = new SyncList(list);//初始化数据
        Thread writerS = new Thread(new Runnable() {
            @Override public void run() {
                for (int i = 0; i < 10000; i++) {
                    syncList.put(i);
                }
            }
        });
        Thread reader1S = new Thread(new Runnable() {
            @Override public void run() {
                for (int i = 0; i < 10000; i++) {
                    syncList.get(i);
                }
            }
        });
        Thread reader2S = new Thread(new Runnable() {
            @Override public void run() {
                for (int i = 0; i < 10000; i++) {
                    syncList.get(i);
                }
            }
        });
        long begin1 = System.currentTimeMillis();
        writerS.start();
        reader1S.start();
        reader2S.start();
        try {
            writerS.join();
            reader1S.join();
            reader2S.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("SyncList take " + (System.currentTimeMillis() - begin1) + "ms" + list.size());
        list.forEach((x) -> System.out.print(x + " "));
    }
}

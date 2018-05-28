package com.example;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class RWLockList {//读写锁

    private List list;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public RWLockList(List list) {
        this.list = list;
    }

    public int get(int k) {
        readLock.lock();
        try {
            return (int)list.get(k);
        } finally {
            readLock.unlock();
        }
    }

    public void put(int value) {
        writeLock.lock();
        try {
            list.add(value);
        } finally {
            writeLock.unlock();
        }
    }
}

class SyncList {

    private List list;

    public SyncList(List list) {
        this.list = list;
    }

    public synchronized int get(int k) {
        return (int)list.get(k);
    }

    public synchronized void put(int value) {
        list.add(value);
    }

}
package com.mars.android.baselib.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {
    private ExecutorService cacheExecutor;
    private ExecutorService singleExecutor;
    private ExecutorService fixExecutor;
    private static final int CORESIZE = 3;

    private static class SingletonHolder {
        private static ThreadPoolManager INSTANCE = new ThreadPoolManager();
    }

    public static ThreadPoolManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private ThreadPoolManager() {
    }

    public ExecutorService getCachedThreadPool() {
        if (cacheExecutor == null) {
            cacheExecutor = Executors.newCachedThreadPool();
        }
        return cacheExecutor;
    }

    public ExecutorService getSingleThreadPool() {
        if (singleExecutor == null) {
            singleExecutor = Executors.newSingleThreadExecutor();
        }
        return singleExecutor;
    }

    public ExecutorService getFixedThreadPool() {
        if (fixExecutor == null) {
            fixExecutor = Executors.newFixedThreadPool(CORESIZE);
        }
        return fixExecutor;
    }
}

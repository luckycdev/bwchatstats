package dev.luckyc.bwchatstats.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Multithreading {
    private static final ExecutorService executorService = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("bwchatstats-%d").build());

    public static void runAsync(Runnable runnable) {
        executorService.submit(runnable);
    }
}
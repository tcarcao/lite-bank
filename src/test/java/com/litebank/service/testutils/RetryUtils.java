package com.litebank.service.testutils;

import java.util.concurrent.Callable;

public class RetryUtils {
    public static <T> T retry(Callable<T> callable, int numberOfTimes) {
        T value = null;
        var times = 1;
        do {
            try {
                value = callable.call();

                Thread.sleep(100 * times);
            } catch (Exception e) {
                try {
                    Thread.sleep(100 * times);
                } catch (InterruptedException ex) { }
            }
            times++;
        } while (value == null && times <= numberOfTimes);

        return value;
    }
}

package com.nestlabs.sdk.rest;

import com.nestlabs.sdk.EventHandler;

import java.util.Timer;
import java.util.TimerTask;

class RetryExecutor {
    private final Timer timer = new Timer();
    private final BackOff backOff;

    RetryExecutor(BackOff backOff) {
        this.backOff = backOff;
    }

    void reset() {
        backOff.reset();
    }

    <T> void schedule(final EventHandler<T> consumer, final T value) {
        long delay = backOff.nextInterval();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                consumer.handle(value);
            }
        }, delay);
    }

    void cancel() {
        timer.cancel();
    }
}

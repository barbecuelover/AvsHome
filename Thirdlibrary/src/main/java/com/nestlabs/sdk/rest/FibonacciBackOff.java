package com.nestlabs.sdk.rest;

/**
 * Exponential {@link BackOff} implementation that starts with initial delay and follows Fibonacci sequence
 * up to maximum delay.
 */
final class FibonacciBackOff implements BackOff {

    private static final long DEFAULT_DELAY_MILLIS = 60000;
    private static final long DEFAULT_INITIAL_DELAY_MILLIS = 1000;

    private long lastDelay = 0;
    private int f1 = 0;
    private int f2 = 1;
    private final long maxDelayMillis;
    private final long initialDelayMillis;

    private FibonacciBackOff(Builder builder) {
        maxDelayMillis = builder.getMaxDelayMillis();
        initialDelayMillis = builder.getInitialDelayMillis();
    }

    @Override
    public long nextInterval() {
        if (lastDelay < maxDelayMillis) {
            //Next Fibonacci#
            int temp = f2;
            f2 = f1 + f2;
            f1 = temp;

            lastDelay = f1 * initialDelayMillis;
            if (lastDelay > maxDelayMillis) lastDelay = maxDelayMillis;
        }
        return lastDelay;
    }

    @Override
    public void reset() {
        lastDelay = 0;
        f1 = 0;
        f2 = 1;
    }

    public static class Builder {
        private long maxDelayMillis = DEFAULT_DELAY_MILLIS;
        private long initialDelayMillis = DEFAULT_INITIAL_DELAY_MILLIS;

        long getMaxDelayMillis() {
            return maxDelayMillis;
        }

        public Builder setMaxDelayMillis(long maxDelayMillis) {
            this.maxDelayMillis = maxDelayMillis;
            return this;
        }

        long getInitialDelayMillis() {
            return initialDelayMillis;
        }

        public Builder setInitialDelayMillis(long initialDelayMillis) {
            this.initialDelayMillis = initialDelayMillis;
            return this;
        }

        public FibonacciBackOff build() {
            return new FibonacciBackOff(this);
        }
    }
}

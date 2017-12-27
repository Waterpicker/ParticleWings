package org.waterpicker.particlewings;

import com.google.common.collect.Range;

public class Cycle {
    private final Range<Integer> range;
    private int value;
    private boolean direction = true;

    public Cycle(int lower, int upper) {
        range = Range.closed(lower, upper);
        value = lower;
    }

    public void increment() {
        int temp = value + direction();

        if(range.contains(temp)) {
            value = temp;
        } else {
            direction = !direction;
            value = value + direction();
        }
    }

    public int get() {
        return value;
    }

    private int direction() {
        return direction ? 1 : -1;
    }
}

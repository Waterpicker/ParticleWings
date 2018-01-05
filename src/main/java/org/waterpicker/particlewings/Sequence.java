package org.waterpicker.particlewings;

import com.google.common.collect.Range;

public interface Sequence {
    public void increment();
    public int get();
}

class Oscillate implements Sequence {
    private final Range<Integer> range;
    private int value;
    private boolean direction = true;

    public Oscillate(int lower, int upper) {
        range = Range.closed(lower, upper);
        value = 0;
    }

    public void increment() {
        int temp = value + direction();

        if(range.contains(temp + range.lowerEndpoint())) {
            value = temp;
        } else {
            direction = !direction;
            value = value + direction();
        }
    }

    public int get() {
        return range.lowerEndpoint() + value;
    }

    private int direction() {
        return direction ? 1 : -1;
    }
}

class Static implements Sequence {
    private final int value;

    public Static(int value) {
        this.value = value;
    }

    @Override
    public void increment() {}

    @Override
    public int get() {
        return value;
    }
}

class Default extends Static {
    public Default() {
        super(0);
    }
}

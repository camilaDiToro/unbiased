package ar.edu.itba.paw.model;

import java.util.Arrays;
import java.util.Iterator;

public enum Positivity {

    POSITIVE("positive", 0.80),
    CONTROVERSIAL("controversial", 0.60),
    NEGATIVE("negative", 0.4);

    Positivity(String className, double value) {
        this.className = className;
        this.value = value;
    }

    private final String className;
    private final double value;

    public String toString() {
        return className;
    }

    public Positivity getPositivvity(double positivity) {
        Iterator<Positivity> it = Arrays.stream(Positivity.values()).iterator();
        while (it.hasNext()) {
            Positivity pos = it.next();
            if (positivity >= pos.value || !it.hasNext())
                return pos;
        }
        throw new IllegalStateException();
    }

}

package ar.edu.itba.paw.model;

import java.util.Arrays;
import java.util.Iterator;

public enum Positivity {

    POSITIVE("positive", 0.8, "home.newsCard.positive"),
    CONTROVERSIAL("controversial", 0.6, "home.newsCard.controversial"),
    NEGATIVE("negative", 0.4, "home.newsCard.negative");

    Positivity(String className, double value, String interCode) {
        this.className = className;
        this.value = value;
        this.interCode = interCode;
    }

    private final String className;
    private final double value;
    private final String interCode;

    public String toString() {
        return className;
    }

    public String getInterCode() {
        return interCode;
    }
    public static Positivity getPositivvity(double positivity) {
        Iterator<Positivity> it = Arrays.stream(Positivity.values()).iterator();
        while (it.hasNext()) {
            Positivity pos = it.next();
            if (positivity >= pos.value || !it.hasNext())
                return pos;
        }
        throw new IllegalStateException();
    }

}

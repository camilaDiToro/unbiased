package ar.edu.itba.paw.model.user;

import java.util.Arrays;
import java.util.Iterator;

public class PositivityStats {
    private final Positivity positivity;
    private final int upvotes;
    private final int downvotes;
    public PositivityStats(int upvotes, int downvotes) {
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.positivity = Positivity.getPositivity(upvotes/(double)(upvotes + downvotes));
    }
    public int getInteractions() {
        return upvotes + downvotes;
    }

    public int getPercentageUpvoted() {
        return (int)(((double)upvotes/(double)(upvotes + downvotes))*100);
    }

    public int getNetUpvotes() {
        return upvotes - downvotes;
    }
    public Positivity getPositivity() {
        return positivity;
    }
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
        public static Positivity getPositivity(double positivity) {
            Iterator<Positivity> it = Arrays.stream(Positivity.values()).iterator();
            while (it.hasNext()) {
                Positivity pos = it.next();
                if (positivity >= pos.value || !it.hasNext())
                    return pos;
            }
            throw new IllegalStateException();
        }

    }
}

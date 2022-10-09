package ar.edu.itba.paw.model.user;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Iterator;

@Entity
@Table(name = "user_positivity")
public class PositivityStats {

    private Positivity positivity;

    @Id
    private long user_id;

    @Column(nullable = false)
    private int upvotes;

    @Column(nullable = false)
    private int downvotes;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    /* package */ PositivityStats() {
        // Just for Hibernate
    }


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

        POSITIVE("positive", 0.8, "home.newsCard.positive", "looking-positivity.svg"),
        CONTROVERSIAL("controversial", 0.6, "home.newsCard.controversial", "controversial-positivity.svg"),
        NEGATIVE("negative", 0.4, "home.newsCard.negative", "angry-positivity.svg");

        Positivity(String className, double value, String interCode, String imageName) {
            this.className = className;
            this.value = value;
            this.interCode = interCode;
            this.imageName = imageName;
        }

        private final String className;
        private final double value;
        private final String interCode;

        private final String imageName;

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

        public String getImageName(){
            return imageName;
        }

    }
}

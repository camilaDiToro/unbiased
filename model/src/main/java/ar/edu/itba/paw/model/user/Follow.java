package ar.edu.itba.paw.model.user;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "follows")
public class Follow {


    Follow() {

    }

    public Follow(long userId, long follows) {
        this.follows = follows;
        this.userId = userId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "follows_seq")
    @SequenceGenerator(name="follows_seq", sequenceName = "follows_seq", allocationSize = 1)
    private Long id;


    @Column(name= "user_id", nullable = false)
    private long userId;

    @Column(name= "follows", nullable = false)
    private long follows;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getFollows() {
        return follows;
    }

    public void setFollows(long follows) {
        this.follows = follows;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, follows);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Follow))
            return false;
        Follow aux = (Follow) obj;
        return aux.follows == follows && aux.userId == userId;
    }
}

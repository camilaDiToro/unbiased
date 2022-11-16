package ar.edu.itba.paw.model.user;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "follows")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "follows_id_seq")
    @SequenceGenerator(name="follows_id_seq", sequenceName = "follows_id_seq", allocationSize = 1)
    private Long id;


    @Column(name= "user_id", nullable = false)
    private long userId;

    @Column(name= "follows", nullable = false)
    private long follows;

    /* package */ Follow() {
        //Just for Hibernate
    }

    public Follow(long userId, long follows) {
        this.follows = follows;
        this.userId = userId;
    }

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
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Follow))
            return false;
        final Follow aux = (Follow) obj;
        return aux.follows == follows && aux.userId == userId;
    }
}

package ar.edu.itba.paw.model;

public class LoggedUserParameters {
    private final Rating personalRating;
    private final boolean saved;

    public LoggedUserParameters(Rating personalRating, boolean saved) {
        this.personalRating = personalRating;
        this.saved = saved;
    }

    public Rating getPersonalRating() {
        return personalRating;
    }

    public boolean isSaved() {
        return saved;
    }
}

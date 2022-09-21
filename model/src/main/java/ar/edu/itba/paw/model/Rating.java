package ar.edu.itba.paw.model;

public enum Rating {
    UPVOTE {
        @Override
        public String toString() {
            return "upvoted";
        }
    },
    DOWNVOTE{
        @Override
        public String toString() {
            return "downvoted";
        }
    },
    NO_RATING{
        @Override
        public String toString() {
            return "no-rating";
        }
    };

    @Override
    public abstract String toString();


    public static Rating getRating(Boolean value) {
        if (value == null)
            return NO_RATING;
        if (value) {
            return Rating.UPVOTE;
        } else {
            return Rating.DOWNVOTE;
        }

    }
}

package ar.edu.itba.paw.model.user;

public enum Tier {
    PLATINUM,
    GOLD,
    DEFAULT;

    public static Tier getTier(final long followers) {

        if (followers <= 1) {
            return DEFAULT;
        } else if (followers <= 2) {
            return GOLD;
        } else {
            return PLATINUM;
        }
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}



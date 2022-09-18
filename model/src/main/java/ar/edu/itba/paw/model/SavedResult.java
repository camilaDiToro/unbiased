package ar.edu.itba.paw.model;

public class SavedResult {
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public SavedResult(boolean active) {
        this.active = active;
    }


}

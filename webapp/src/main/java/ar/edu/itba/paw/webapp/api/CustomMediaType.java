package ar.edu.itba.paw.webapp.api;

public enum CustomMediaType {

    ERROR_V1("application/vnd.campus.error.v1+json");

    private final String value;

    CustomMediaType(final String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

package ar.edu.itba.paw.webapp.api.exceptions;

public class InvalidGetNewsFilter extends CustomBadRequestException{

    private static final ApiErrorCode CODE = ApiErrorCode.INVALID_GET_NEWS_FILTER;
    private static final String DETALIS_MSG = "The filter %s is invalid";
    private final String details;

    public InvalidGetNewsFilter(String details) {
        super(CODE, String.format(DETALIS_MSG, details));
        this.details = details;
    }
}

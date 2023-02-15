package ar.edu.itba.paw.webapp.dto;

public class SimpleMessageDto {

    private String message;

    public static SimpleMessageDto fromString(final String message){
        final SimpleMessageDto dto = new SimpleMessageDto();
        dto.message = message;
        return dto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

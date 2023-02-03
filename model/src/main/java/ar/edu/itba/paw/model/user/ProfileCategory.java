package ar.edu.itba.paw.model.user;

import ar.edu.itba.paw.model.exeptions.InvalidFilterException;

public enum ProfileCategory {

    MY_POSTS("profileCategories.myPosts", "MY_POSTS"),
    SAVED("profileCategories.saved", "SAVED"),
    UPVOTED("profileCategories.upvoted", "UPVOTED"),
    DOWNVOTED("profileCategories.downvoted", "DOWNVOTED");

    private final String interCode;
    private final String description;

    ProfileCategory(String interCode, String description) {
        this.interCode = interCode;
        this.description = description;
    }

    public String getInterCode() {
        return interCode;
    }


    public long getId() {
        return ordinal();
    }

    public static ProfileCategory getByValue(String value){
        try{
            return ProfileCategory.valueOf(value);
        }catch (IllegalArgumentException e){
            throw new InvalidFilterException(String.format(InvalidFilterException.STRING_MSG, value),  e);
        }
    }

    public String getDescription() {
        return description;
    }
}
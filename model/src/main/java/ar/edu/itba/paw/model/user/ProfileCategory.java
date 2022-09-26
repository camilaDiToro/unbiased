package ar.edu.itba.paw.model.user;

import ar.edu.itba.paw.model.exeptions.InvalidCategoryException;

public enum ProfileCategory {
    MY_POSTS("profileCategories.myPosts"),

    SAVED("profileCategories.saved"),
    UPVOTED("profileCategories.upvoted"),
    DOWNVOTED("profileCategories.downvoted");

    private final String interCode;

    ProfileCategory(String interCode) {
        this.interCode = interCode;
    }

    public String getInterCode() {
        return interCode;
    }


    public long getId() {
        return ordinal();
    }

    public static ProfileCategory getById(long id) {
        for (ProfileCategory c : ProfileCategory.values()) {
            if (c.ordinal() == id) {
                return c;
            }
        }
        return null;
    }

    public static ProfileCategory getByInterCode(String description){
        for (ProfileCategory c : ProfileCategory.values()) {
            if (c.getInterCode().equals(description) ) {
                return c;
            }
        }
        return null;
    }

    public static ProfileCategory getByValue(String value){
        try{
            return ProfileCategory.valueOf(value);
        }catch (IllegalArgumentException e){
            throw new InvalidCategoryException();
        }
    }

}
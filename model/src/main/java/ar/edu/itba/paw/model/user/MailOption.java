package ar.edu.itba.paw.model.user;

import ar.edu.itba.paw.model.exeptions.InvalidCategoryException;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public enum MailOption {
// boolean follow, boolean comment, boolean followingPublished, boolean positivityChange
    FOLLOW("mailOption.follow"),
    COMMENT("mailOption.comment"),
    FOLLOWING_PUBLISHED("mailOption.folowingPublished"),
    POSITIVITY_CHANGED("mailOption.positivityChanged");

    private final String interCode;

    MailOption(String interCode) {
        this.interCode = interCode;

    }

    public String getInterCode() {
        return interCode;
    }



    public long getId() {
        return ordinal();
    }

    public static MailOption getById(long id) {
        for (MailOption c : MailOption.values()) {
            if (c.ordinal() == id) {
                return c;
            }
        }
        return null;
    }

    public static MailOption getByCode(String description){
        for (MailOption c : MailOption.values()) {
            if (c.getInterCode().equals(description) ) {
                return c;
            }
        }
        return null;
    }

    public static MailOption getByValue(String value){
        try{
            return MailOption.valueOf(value);
        }catch (IllegalArgumentException e){
            throw new InvalidCategoryException(e);
        }
    }

    public static Collection<MailOption> getEnumCollection(String[] optionsByCode) {
        return Arrays.stream(optionsByCode).map(MailOption::getByCode).collect(Collectors.toList());
    }

}
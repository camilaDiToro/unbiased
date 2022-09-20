package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.model.TextType;

@FunctionalInterface

public interface MAVBuilderSupplier {
    MyModelAndView.Builder supply(String view, String titleCode, TextType textType);
}

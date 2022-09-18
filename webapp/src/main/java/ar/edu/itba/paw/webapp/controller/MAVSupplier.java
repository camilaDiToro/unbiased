package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.TextType;
import org.springframework.web.servlet.ModelAndView;
@FunctionalInterface

public interface MAVSupplier {
    ModelAndView supply(String view, String titleCode, TextType textType);
}

package com.alex.studentmanagementsystem.utility;

import org.springframework.web.servlet.ModelAndView;

public class CreateView {

    // instance variables
    private final ModelAndView modelAndView = new ModelAndView();

    // constructors
    public CreateView(String viewName) {
        this.modelAndView.setViewName(viewName);
    }

    public CreateView(Object object, String viewName) {
        this.modelAndView
            .addObject(object)
            .setViewName(viewName);
    }

    public CreateView(String attributeName, Object object, String viewName) {
        this.modelAndView
            .addObject(attributeName, object)
            .setViewName(viewName);
    }


    // methods
    public ModelAndView getModelAndView() {
        return modelAndView;
    }


}

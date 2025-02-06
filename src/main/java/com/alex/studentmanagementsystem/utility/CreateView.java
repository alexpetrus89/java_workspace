package com.alex.studentmanagementsystem.utility;

import org.springframework.web.servlet.ModelAndView;

public class CreateView {

    // instance variables
    private ModelAndView modelAndView = new ModelAndView();

    // No-arg constructor
    public CreateView() {}

    // constructor
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

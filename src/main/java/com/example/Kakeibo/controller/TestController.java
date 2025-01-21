package com.example.Kakeibo.controller;

import com.example.Kakeibo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;

@Controller
public class TestController {

    @Autowired
    TestService testService;

    @GetMapping
    public ModelAndView top() throws ParseException {
        ModelAndView mav = new ModelAndView();

        String test = testService.findAll();
        mav.addObject("test", test);
        mav.setViewName("test");
        return mav;
    }

}

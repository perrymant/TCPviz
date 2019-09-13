package com.bath.tcpviz.dis.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ExceptionController implements ErrorController {
    private final static String PATH = "/error";

    @Override
    @RequestMapping(PATH)
    public String getErrorPath() {
        return "redirect:/";
    }
}
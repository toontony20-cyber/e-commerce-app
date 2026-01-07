package com.springbootfirstproject.ecommerceapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/payment")
    public String payment() {
        return "payment";
    }

    @GetMapping("/orders")
    public String orders() {
        return "orders";
    }
}

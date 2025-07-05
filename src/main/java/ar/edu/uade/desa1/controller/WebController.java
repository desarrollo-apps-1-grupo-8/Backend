package ar.edu.uade.desa1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/qr")
    public String qrGenerator() {
        return "qr-display";
    }
} 
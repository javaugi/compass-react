package com.okta.developer.jugtours.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Profile("prod")
public class RedirectController {

    private final Logger log = LoggerFactory.getLogger(RedirectController.class);

    @GetMapping("/private")
    public String redirectToRoot() {
        log.info("redirectToRoot private ");
        return "redirect:/";
    }
}

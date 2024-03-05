package com.sheldon.devlabcodesanbox.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sheldon
 * @version 1.0
 * @className MainController
 * @date 2024/3/5 18:43
 * @description TODO
 */
@RestController
public class MainController {

    @GetMapping("/")
    public String index() {
        return "Hello, World!";
    }

}

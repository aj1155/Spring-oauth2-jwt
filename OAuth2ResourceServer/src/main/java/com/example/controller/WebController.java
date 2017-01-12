package com.example.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Created by on 01.02.16.
 *
 * @author David Steiman
 */
@RestController
@RequestMapping("/foo")
public class WebController {

    @RequestMapping(method = RequestMethod.GET)
    public String readFoo() {
        return "read foo " + UUID.randomUUID().toString();
    }

    @PreAuthorize("hasAuthority('FOO_ADMIN')")
    @RequestMapping(value = "/admin",method = RequestMethod.GET)
    public String writeFoo() {
        return "admin foo " + UUID.randomUUID().toString();
    }
}

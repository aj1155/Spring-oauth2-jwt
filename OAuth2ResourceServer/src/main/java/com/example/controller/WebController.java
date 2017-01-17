package com.example.controller;

import com.example.service.JWTAuthenticationService;
import com.example.service.JwtFilter;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

/**
 * Created by on 2017.01.12
 *
 * @author Manki Kim
 */
@RestController
@RequestMapping("/foo")
public class WebController {

    @Autowired
    private JWTAuthenticationService jwtAuthenticationService;

    @Autowired
    private JwtFilter jwtFilter;

    @RequestMapping(method = RequestMethod.GET)
    public String readFoo(ServletRequest request) throws InvalidJwtException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        Map<String,Object> token = jwtFilter.filterToken(httpRequest);
        System.out.println(token.toString());
        return "read foo " + UUID.randomUUID().toString();
    }


    @PreAuthorize("hasAuthority('FOO_ADMIN')")
    @RequestMapping(value = "/admin",method = RequestMethod.GET)
    public String writeFoo() {
        return "admin foo " + UUID.randomUUID().toString();
    }
}

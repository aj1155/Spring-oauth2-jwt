package com.example.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Created by on 2017.01.12
 *
 * @author Manki Kim
 */
@RestController
@RequestMapping("/foo")
public class WebController {

    @RequestMapping(method = RequestMethod.GET)
    public String readFoo(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        String authToken = httpRequest.getHeader("Authorization");
        System.out.println("토큰:"+authToken);
        //final Claims claims = Jwts.parser().parseClaimsJwt(authToken).getBody();
        //System.out.println(claims.toString());
        return "read foo " + UUID.randomUUID().toString();
    }


    @PreAuthorize("hasAuthority('FOO_ADMIN')")
    @RequestMapping(value = "/admin",method = RequestMethod.GET)
    public String writeFoo() {
        return "admin foo " + UUID.randomUUID().toString();
    }
}

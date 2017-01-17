package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Manki Kim on 2017-01-17.
 */
@Service
public class JwtFilter {

    @Autowired
    private TokenStore tokenStore;

    private final String authorizationSchema = "bearer";
    private final int authorizationSchemaLength = 6;

    public Map<String,Object> filterToken(HttpServletRequest request){
        String token = validateToken(request);
        if(token!=null){
            OAuth2AccessToken oauthToken = tokenStore.readAccessToken(token);
            Map<String,Object> filterToken = new HashMap<String,Object>();
            filterToken.put("scope",oauthToken.getScope());
            filterToken.put("user_name",oauthToken.getAdditionalInformation().get("user_name"));
            filterToken.put("authorities",oauthToken.getAdditionalInformation().get("authorities"));
            return filterToken;
        }
        return null;
    }

    private String validateToken(HttpServletRequest request){
        String stringToken = request.getHeader("Authorization");
        if (stringToken == null) {
            System.out.println("Check again for tokens");
            return null;
        }

        if (stringToken.indexOf(authorizationSchema) == -1) {
            System.out.println("We need token type of bearer");
            return null;
        }
        stringToken = stringToken.substring(authorizationSchemaLength).trim();
        return stringToken;
    }
}

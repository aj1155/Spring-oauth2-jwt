package com.example.config.jwt;


import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Manki Kim on 2017-01-17.
 */
public class CustomTokenEnhancer implements TokenEnhancer {
    
    @Override
    public OAuth2AccessToken enhance(
            OAuth2AccessToken accessToken,
            OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("user_name", authentication.getName());
        List<String> authority_names = authentication.getAuthorities().parallelStream().map(ga -> ga.getAuthority()).collect(Collectors.toList());
        additionalInfo.put("authorities", authority_names);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}

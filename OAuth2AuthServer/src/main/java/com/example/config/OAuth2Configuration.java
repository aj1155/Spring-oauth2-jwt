package com.example.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

/**
 * Created by on 2017.01.12
 *
 * @author Manki Kim
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2Configuration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /*
        clients.inMemory()
                .withClient("foo")
                .scopes("develop")
                .secret("bar")
                .autoApprove(true)
                .authorities("FOO_READ", "FOO_WRITE")
                .authorizedGrantTypes("implicit","refresh_token", "password", "authorization_code");
        */
        clients.jdbc(dataSource); //check table named 'oauth_client_details'
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore()).tokenEnhancer(jwtTokenEnhancer()).authenticationManager(authenticationManager);
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtTokenEnhancer());
    }


    /*JwtAccessTokenConverter in OauthServer is like a filter to make nomal token to jwt
     * so we need the way how to make it
     * you can make .jks file using keytool
     */

    //Todo Jwts.builder() ...
    @Bean
    protected JwtAccessTokenConverter jwtTokenEnhancer() {

        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        /* KeyStoreKeyFactory constructor need three arguments first is the Resource about jks so you just write the path
         * second is the storepass about the .jks that you made before
         */
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("test.jks"), "kimjava".toCharArray());
        /* getKeyPair demands two arguments
         * first is alias that you made before in keytool
         * second is keypass it also you mad before in keytool
         */
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("liliput", "kimjava".toCharArray()));
        return converter;
    }
}
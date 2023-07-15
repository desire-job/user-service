package com.gmail.apachdima.desirejob.userservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final ClientDetailsService authClientDetailsService;
    private final UserDetailsService userDetailsServiceImpl;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;
    private final DataSource dataSource;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        String useJwt = environment.getProperty("spring.security.oauth.jwt");
        if (useJwt != null && "true".equalsIgnoreCase(useJwt.trim())) {
            endpoints
                .tokenStore(tokenStore())
                .tokenEnhancer(jwtConverter())
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsServiceImpl)
                .setClientDetailsService(authClientDetailsService);
        } else {
            endpoints.authenticationManager(authenticationManager);
        }
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
            .tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()")
            .passwordEncoder(passwordEncoder)
            .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(authClientDetailsService);
    }

    @Bean
    public TokenStore tokenStore() {
        String useJwt = environment.getProperty("spring.security.oauth.jwt");
        if (useJwt != null && "true".equalsIgnoreCase(useJwt.trim())) {
            return new JwtTokenStore(jwtConverter());
        } else {
            return new InMemoryTokenStore();
        }
    }

    @Bean
    protected JwtAccessTokenConverter jwtConverter() {
        String pwd = environment.getProperty("spring.security.oauth.jwt.keystore.password");
        String alias = environment.getProperty("spring.security.oauth.jwt.keystore.alias");
        String keystore = environment.getProperty("spring.security.oauth.jwt.keystore.name");

        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(keystore), pwd.toCharArray());
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair(alias));
        return converter;
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setClientDetailsService(authClientDetailsService);
        tokenServices.setAuthenticationManager(authenticationManager);
        return tokenServices;
    }
}

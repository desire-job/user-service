package com.gmail.apachdima.desirejob.userservice.model;

import com.gmail.apachdima.desirejob.commonservice.constant.CommonConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serial;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "oauth_clients")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthClient implements ClientDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "client_", unique = true, nullable = false)
    private String clientId;

    @Column(name = "client_secret", nullable = false)
    private String clientSecret;

    @Column(name = "resources")
    private String resources;

    @Column(name = "scope", nullable = false)
    private String scope;

    @Column(name = "grant_types", nullable = false)
    private String grantTypes;

    @Column(name = "redirect_uris")
    private String redirectUris;

    @Column(name = "authorities")
    private String authorities;

    @Column(name = "access_token_validity")
    private Integer accessTokenValidity;

    @Column(name = "refresh_token_validity")
    private Integer refreshTokenValidity;

    @Column(name = "additional_information")
    private String additionalInformation;

    @Column(name = "auto_approve")
    private String autoApprove;

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    @Override
    public Set<String> getResourceIds() {
        return Objects.nonNull(resources)
            ? new HashSet<>(Arrays.asList(resources.split(CommonConstant.COMMA.getValue())))
            : null;
    }

    @Override
    public Set<String> getScope() {
        return Objects.nonNull(scope)
            ? new HashSet<>(Arrays.asList(scope.split(CommonConstant.COMMA.getValue())))
            : null;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return Objects.nonNull(grantTypes)
            ? new HashSet<>(Arrays.asList(grantTypes.split(CommonConstant.COMMA.getValue())))
            : null;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return Objects.nonNull(redirectUris)
            ? new HashSet<>(Arrays.asList(redirectUris.split(CommonConstant.COMMA.getValue())))
            : null;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Objects.nonNull(authorities)
            ? Arrays.stream(authorities.split(CommonConstant.COMMA.getValue()))
                .map(value -> new SimpleGrantedAuthority("ROLE_".concat(value)))
                .collect(Collectors.toSet())
            : null;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValidity;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValidity;
    }

    @Override
    public boolean isSecretRequired() {
        return Objects.nonNull(clientSecret);
    }

    @Override
    public boolean isScoped() {
        return StringUtils.isNotEmpty(scope);
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return true;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return null;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setResources(Set<String> resources) {
        this.resources = resources.stream().collect(Collectors.joining(CommonConstant.COMMA.getValue()));
    }

    public void setScope(Set<String> scope) {
        this.scope = scope.stream().collect(Collectors.joining(CommonConstant.COMMA.getValue()));
    }

    public void setGrantTypes(Set<String> grantTypes) {
        this.grantTypes = grantTypes.stream().collect(Collectors.joining(CommonConstant.COMMA.getValue()));
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(CommonConstant.COMMA.getValue()));
    }

    public void setRedirectUris(Set<String> redirectUris) {
        this.redirectUris = redirectUris.stream().collect(Collectors.joining(CommonConstant.COMMA.getValue()));
    }

    public void setAccessTokenValidity(Integer accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    public void setRefreshTokenValidity(Integer refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }
}

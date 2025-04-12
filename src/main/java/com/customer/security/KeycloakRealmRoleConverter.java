package com.customer.security;

import java.util.Collection;
import java.util.List;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;

public class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {


  @Override
  public Collection<GrantedAuthority> convert(Jwt source) {
    return List.of();
  }
}

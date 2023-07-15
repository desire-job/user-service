package com.gmail.apachdima.desirejob.userservice.service.impl;

import com.gmail.apachdima.desirejob.commonservice.constant.Model;
import com.gmail.apachdima.desirejob.commonservice.constant.message.CommonError;
import com.gmail.apachdima.desirejob.commonservice.exception.EntityNotFoundException;
import com.gmail.apachdima.desirejob.userservice.repository.AuthClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthClientDetailsService implements ClientDetailsService {

    private final AuthClientRepository authClientRepository;
    private final MessageSource messageSource;

    @Override
    public ClientDetails loadClientByClientId(String clientId) {
        Object[] params = new Object[]{Model.AUTH_CLIENT.getName(), Model.Field.ID.getFieldName(), clientId};
        return authClientRepository
            .findByClientId(clientId)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    messageSource.getMessage(CommonError.ENTITY_NOT_FOUND.getKey(), params, Locale.ENGLISH)));
    }
}

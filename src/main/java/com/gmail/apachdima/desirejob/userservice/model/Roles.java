package com.gmail.apachdima.desirejob.userservice.model;

import java.util.Set;

public enum Roles {

    ADMIN, MANAGER, USER;

    public static Set<String> getAdminGrantedRoles() {
        return Set.of(Roles.MANAGER.name(), Roles.USER.name());
    }
}

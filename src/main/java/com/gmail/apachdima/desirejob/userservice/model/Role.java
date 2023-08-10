package com.gmail.apachdima.desirejob.userservice.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "keycloak_role")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Role {

    @Id
    @Column(name = "id")
    private String roleId;

    @Column(name = "client_realm_constraint")
    private String clientRealmConstraint;

    @Column(name = "client_role")
    private boolean clientRole;

    @Column(name = "description")
    private String description;

    @Column(name = "name")
    private String name;

    @Column(name = "realm_id")
    private String realmId;

    @Column(name = "client")
    private String client;

    @Column(name = "realm")
    private String realm;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
        name = "composite_role",
        joinColumns = @JoinColumn(name = "composite"),
        inverseJoinColumns = @JoinColumn(name = "child_role"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Role> childRoles;
}

package com.gmail.apachdima.desirejob.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "credential")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Credential {

    @Id
    @Column(name = "id")
    private String credentialId;

    @Column(name = "salt")
    private byte[] salt;

    @Column(name = "type")
    private String type;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_date")
    private long createdAt;

    @Column(name = "user_label")
    private String userLabel;

    @Column(name = "secret_data")
    private String secretData;

    @Column(name = "credential_data")
    private String credentialData;

    @Column(name = "priority")
    private int priority;
}

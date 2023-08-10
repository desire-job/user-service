package com.gmail.apachdima.desirejob.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_attribute")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserAttribute {

    @Id
    @Column(name = "id")
    private String userAttributeId;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

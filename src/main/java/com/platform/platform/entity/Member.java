package com.platform.platform.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
public class Member {

    @Id
    private Long id;

    private String loginId;
    private String name;
    private String password;

}

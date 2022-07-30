package com.platform.platform.security;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class RolePrivilege {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "PRIVILEGE_ID")
    private Privilege privilege;

    @CreationTimestamp
    private LocalDateTime registrationDate;

    public void takePrivilege(Privilege privilege) {
        this.privilege = privilege;
        privilege.getRolePrivileges().add(this);
    }
}

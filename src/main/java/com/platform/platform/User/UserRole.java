package com.platform.platform.User;

import com.platform.platform.security.Role;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USERROLE_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    private Role role;

    public void takeRole(Role role) {
        this.role = role;
        role.getUsers().add(this);
    }
}

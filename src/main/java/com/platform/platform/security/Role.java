package com.platform.platform.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.platform.platform.User.User;
import com.platform.platform.User.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ROLE_ID")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "role")
    private List<UserRole> users = new ArrayList<>();
    @OneToMany(mappedBy = "role")
    private List<RolePrivilege> rolePrivileges = new ArrayList<>();

    void addRolePrivilege(RolePrivilege rolePrivilege) {
        rolePrivilege.setRole(this);
        this.rolePrivileges.add(rolePrivilege);
    }
}

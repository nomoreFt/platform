package com.platform.platform.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PRIVILEGE_ID")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "privilege", fetch = FetchType.EAGER)
    private List<RolePrivilege> rolePrivileges = new ArrayList<>();


}

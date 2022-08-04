package com.platform.platform.security;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PRIVILEGE_ID")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "privilege", fetch = FetchType.LAZY)
    private List<RolePrivilege> rolePrivileges = new ArrayList<>();


}

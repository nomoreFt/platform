package com.platform.platform.User;

import com.platform.platform.security.PrivilegeRepository;
import com.platform.platform.security.Role;
import com.platform.platform.security.RolePrivilege;
import com.platform.platform.security.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thymeleaf.model.IProcessingInstruction;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getUser(email)
                .orElseThrow(() -> new UsernameNotFoundException("User is not found, email = " + email));
        List<RolePrivilege> RolePrivileges = user.getUserRoles().stream().map(userRole -> userRole.getRole().getRolePrivileges()).flatMap(Collection::stream).collect(Collectors.toList());


        user.setAuthorities(
                Stream.concat(
                        getRoles(user.getUserRoles()).stream(),
                        getPrivileges(RolePrivileges).stream()
                        ).collect(Collectors.toList())
        );
        return user;
    }

    private List<SimpleGrantedAuthority> getRoles(List<UserRole> userRoles) {
        return userRoles.stream()
                .map(userRole-> userRole.getRole().getName())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private List<SimpleGrantedAuthority> getPrivileges(List<RolePrivilege> roles) {
        return roles.stream()
                .map(rolePrivilege -> rolePrivilege.getPrivilege().getName())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}

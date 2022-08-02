package com.platform.platform.User;

import com.platform.platform.PlatformApplication;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

//Bean @Autowired 주입, @MockBean 같은 것들 위함
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PlatformApplication.class)
class CustomUserDetailServiceTest {
    private static final String USER1_EMAIL = "user1@gmail.com";
    private static final String USER2_EMAIL = "user2@gmail.com";
    private static final String USER3_EMAIL = "user3@gmail.com";

    @Autowired
    private CustomUserDetailService userDetailService;

    @Nested //테스트 단위별로 묶는 것
    class loadUserByUsername {
        @Test
        @DisplayName("throw UsernameNotFoundException when user not found with email")
        void errorCase() {
            UsernameNotFoundException error = assertThrows(UsernameNotFoundException.class,
                    () -> userDetailService.loadUserByUsername("not-found@gmail.com"));

            assertThat(error.getMessage()).isEqualTo("User is not found, email = not-found@gmail.com");
        }

        @Test
        @DisplayName("given user1 is temporary user, when get role, then has temporary_user role and communication authority")
        void checkAuthorityAsTemporaryUser() {
            UserDetails user1 = userDetailService.loadUserByUsername(USER1_EMAIL);

            assertThat(user1.getAuthorities())
                    .extracting(GrantedAuthority::getAuthority)
                    .contains("ROLE_TEMPORARY_USER", "COMMUNICATION_AUTHORITY");
        }

    }
}
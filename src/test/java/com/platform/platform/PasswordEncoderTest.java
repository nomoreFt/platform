package com.platform.platform;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PlatformApplication.class)
public class PasswordEncoderTest {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void test() {
        String encode = passwordEncoder.encode("1234");
        assertThat(passwordEncoder.matches("1234", "$2a$10$OWTopZTMCWXQIhjMVf2P3uC9fpQq59RYcTatZfidUq1z5roE6wmJ.")).isTrue();
    }
}

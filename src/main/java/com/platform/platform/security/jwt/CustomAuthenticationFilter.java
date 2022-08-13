package com.platform.platform.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.platform.User.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.InputMismatchException;
/*
로그인 API를 요청했을 때 (JSON으로 사용자의 아이디, 비밀번호를 보내준다.) AuthenticationFilter로 제일 먼저 요청이 오게 되고, UserPasswordAuthenticationToken을 발급한다.
 */

@Log4j2
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public CustomAuthenticationFilter(final AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final UsernamePasswordAuthenticationToken authRequest;
        try {
            final User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            authRequest = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        } catch (IOException exception) {
            throw new InputMismatchException();
        }
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}

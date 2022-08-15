package com.platform.platform.security.jwt;

import com.platform.platform.User.CustomUserDetailService;
import com.platform.platform.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtils;
    private final CustomUserDetailService service;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //1.Request로부터 Authorization Header값 추출
        //(JWT를 발급받고 Authorization Header에 넣어서 다시 요청보낸 것.
        String authorizationHeader = request.getHeader("Authorization");

        String token = null;
        String userEmail = null;

        //2.Header에 값이 있고, Bearer로 시작하면 token 추출(Bearer 다음부터 끝까지)
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            userEmail = jwtUtils.extractUserEmail(token);
        }

        //3.token이 정상적이란 의미. authentication이 비어있으면 최초인증이므로
        //userEmail을 통해서 SpringSecurity Authentication에 필요한 정보 Set
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User userDetails = (User) service.loadUserByUsername(userEmail);

            if (jwtUtils.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        //4. 할 일을 다 했으니 filterChain을 태운다.
        filterChain.doFilter(request,response);
    }
}

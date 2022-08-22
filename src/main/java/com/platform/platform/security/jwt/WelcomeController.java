package com.platform.platform.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WelcomeController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    @GetMapping("/")
    public String welcome() {

        return "Welcome to site";
    }

    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken((authRequest.getUserEmail()), authRequest.getPassword()));
        } catch (Exception exception) {
            throw new Exception("invalid username/password");
        }
        return jwtUtil.generateToken(authRequest.getUserEmail());
    }

}

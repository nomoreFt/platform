# platform

## 설정

### Gradle

```java
	// Spring Security + JWT
	implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'io.jsonwebtoken:jjwt:0.9.1'
```
* security와 jwt 추가


### SecurityConfig

기존의 `websecurityconfigureradapter`를 상속하여 구현하는 방식이 사장된다고 해서, 
새로운 방법으로 설정을 했다.

참고 : https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService userDetailService;
    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers("/authenticate")
                .permitAll().anyRequest().authenticated()
                .and().exceptionHandling().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

```
* @EnableWebSecurity : `WebSecurityConfiguration.class, SpringWebMvcImportSelector.class, OAuth2ImportSelector.class,
  HttpSecurityConfiguration.class`를 import 한다.

    ```java
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Documented
    @Import({ WebSecurityConfiguration.class, SpringWebMvcImportSelector.class, OAuth2ImportSelector.class,
            HttpSecurityConfiguration.class })
    @EnableGlobalAuthentication
    @Configuration
    public @interface EnableWebSecurity {
    
        /**
         * Controls debugging support for Spring Security. Default is false.
         * @return if true, enables debug support with Spring Security
         */
        boolean debug() default false;
    
    }
    ```
* PasswordEncoder : 서버에 저장되는 user password를 암호화 한다. (다른 Encoder 선택 가능)
  * authenticationManager : authenticate 메서드 authentication을 set한다. (이후 `SecurityContextHolder`에서 꺼내 사용할 수 있다.) 
    * 실제 검증은 `authenticationProvider`가 호출하여 진행하고, `UserDetailsService`의 `loadUserByUsername 메서드`으로 검증한다.
* filterChain : 검증할 filter를 설정한다. 현재 설정된 것은
  * `csrf().disable()` : 사이트간 위변조 전송을 방지하는 것인데, rest api 서버면 jwt 토큰으로 인증을 주고 받고, 서버의 session은 저장 안하기 때문에 disable()
  * `authorizeRequests().antMatchers("/authenticate").permitAll()` : /authenticate로 들어오면 모두 허가한다. /authenticate로 token 발급주게 할 것이기 때문이다.
  * `http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);` : 모든 필터 이전에 jwt 검증을 하게 한다.(토큰 없으면 인증 X)


### JwtFilter

```java
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
```
* 







![](../../../../../var/folders/67/v2hfg63s6hqgn0fzpz3z454r0000gn/T/TemporaryItems/NSIRD_screencaptureui_sKjalp/스크린샷 2022-08-15 오후 10.50.10.png)

![](../../../../../var/folders/67/v2hfg63s6hqgn0fzpz3z454r0000gn/T/TemporaryItems/NSIRD_screencaptureui_k3gYX7/스크린샷 2022-08-15 오후 10.49.56.png)
![](../../../../../var/folders/67/v2hfg63s6hqgn0fzpz3z454r0000gn/T/TemporaryItems/NSIRD_screencaptureui_uWo6JC/스크린샷 2022-08-15 오후 10.54.57.png)

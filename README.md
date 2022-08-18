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


### 








![](../../../../../var/folders/67/v2hfg63s6hqgn0fzpz3z454r0000gn/T/TemporaryItems/NSIRD_screencaptureui_sKjalp/스크린샷 2022-08-15 오후 10.50.10.png)

![](../../../../../var/folders/67/v2hfg63s6hqgn0fzpz3z454r0000gn/T/TemporaryItems/NSIRD_screencaptureui_k3gYX7/스크린샷 2022-08-15 오후 10.49.56.png)
![](../../../../../var/folders/67/v2hfg63s6hqgn0fzpz3z454r0000gn/T/TemporaryItems/NSIRD_screencaptureui_uWo6JC/스크린샷 2022-08-15 오후 10.54.57.png)

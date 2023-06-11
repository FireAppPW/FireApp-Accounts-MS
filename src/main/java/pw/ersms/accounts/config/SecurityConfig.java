package pw.ersms.accounts.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //allow all requests (even if the user doesnt have credentials)

        http
                .csrf()
                .disable()
                .cors().configurationSource(c -> {
                    CorsConfiguration corsCfg = new CorsConfiguration();
                    corsCfg.applyPermitDefaultValues();
                    corsCfg.addAllowedOriginPattern("*");
                    corsCfg.addAllowedMethod(CorsConfiguration.ALL);
                    return corsCfg;
                })
                .and()
                .authorizeHttpRequests()
//                .requestMatchers("/account/register",
//                        "/account/verify**",
//                        "/account/login",
//                        "/swagger-ui/**",
//                        "/v3/api-docs/**")
                .requestMatchers("/*")
                .permitAll()
//                .anyRequest()
//                .authenticated()
                .and()
//                .authorizeHttpRequests((authz) -> authz
//                        .requestMatchers("/account/login/**").permitAll()
//                        .requestMatchers("/account/**").hasAnyAuthority("SysAdmin", "FireAdmin", "User")
//                        .anyRequest().permitAll()
//                )
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            response.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    ex.getMessage()
                            );
                        }
                );

//        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

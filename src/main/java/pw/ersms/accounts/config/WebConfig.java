//package pw.ersms.accounts.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//
//@Configuration
//public class WebConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.cors(httpSecurityCorsConfigurer ->
//                httpSecurityCorsConfigurer.configurationSource(request ->
//                        new CorsConfiguration().applyPermitDefaultValues()
//                )
//        );
//        return http.build();
//    }
//}
package org.bot0ff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Map;

import static org.springframework.web.servlet.function.RequestPredicates.GET;
import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.ServerResponse.ok;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

//    //точка входа глобально
//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf().disable()
//                .formLogin().and()
//                .authorizeHttpRequests()
//                .requestMatchers("/public/**").permitAll()
//                .anyRequest().authenticated().and()
//                .exceptionHandling()
//                .authenticationEntryPoint((request, response, authException) -> {
//                    response.sendRedirect("http://localhost:8080/public/sign-in.html");
//                }).and()
//                .build();
//    }

    //точка входа сконфигурирована в фильтре
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        BasicAuthenticationEntryPoint basicAuthenticationEntryPoint = new BasicAuthenticationEntryPoint();
        basicAuthenticationEntryPoint.setRealmName("Realm");

        return http
                .httpBasic().authenticationEntryPoint(((request, response, authException) -> {
                    authException.printStackTrace();
                    basicAuthenticationEntryPoint.commence(request, response, authException);
                })).and()
                .authorizeHttpRequests()
                .anyRequest().authenticated().and()
                .exceptionHandling()
                .authenticationEntryPoint(basicAuthenticationEntryPoint)
                .and()
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return route(GET("/get"),
                req -> ok().body(Map.of("id", "One")));
    }
}

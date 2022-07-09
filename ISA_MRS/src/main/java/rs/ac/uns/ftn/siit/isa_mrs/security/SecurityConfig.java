package rs.ac.uns.ftn.siit.isa_mrs.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import rs.ac.uns.ftn.siit.isa_mrs.security.filter.CustomAuthenticationFilter;
import rs.ac.uns.ftn.siit.isa_mrs.security.filter.CustomAuthorizationFilter;

import java.util.Arrays;
import java.util.List;

import static rs.ac.uns.ftn.siit.isa_mrs.util.Paths.*;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomUserDetailsServiceImpl userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtGenerator jwtGenerator;
    private final JwtConfig jwtConfig;
    private final JwtDecoder jwtDecoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.POST, REQUEST_CONTROLLER + SIGN_UP);
        web.ignoring().antMatchers(HttpMethod.PUT, USER_CONTROLLER + "/activate");
        web.ignoring().antMatchers(HttpMethod.POST, USER_CONTROLLER + "/resendVerification");
        web.ignoring().antMatchers(HttpMethod.POST, PHOTO_CONTROLLER + "/upload");
        web.ignoring().antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "/favicon.ico", "/**/*.html",
                "/**/*.css", "/**/*.js");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean(), jwtGenerator);
        CustomAuthorizationFilter customAuthorizationFilter = new CustomAuthorizationFilter(jwtConfig, jwtDecoder);
        http
                .cors()
                .and()
                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(customAuthenticationFilter)
                .authorizeRequests()
                .antMatchers(LOGIN).permitAll()
                .antMatchers(HttpMethod.POST,REQUEST_CONTROLLER + SIGN_UP).permitAll()
                .antMatchers(HttpMethod.PUT, USER_CONTROLLER + "/activate").permitAll()
                .antMatchers(HttpMethod.POST, USER_CONTROLLER + "/resendVerification").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterAfter(customAuthorizationFilter, CustomAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin(CROSS_ORIGIN);
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-type", "X-Auth-Token"));
        corsConfiguration.setExposedHeaders(List.of("X-Auth-Token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}

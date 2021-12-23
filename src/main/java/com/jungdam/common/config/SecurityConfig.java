package com.jungdam.common.config;

import com.jungdam.auth.application.CustomOAuth2UserService;
import com.jungdam.auth.filter.TokenAuthenticationFilter;
import com.jungdam.auth.handler.OAuth2AuthenticationFailureHandler;
import com.jungdam.auth.handler.OAuth2AuthenticationSuccessHandler;
import com.jungdam.auth.handler.RestAuthenticationEntryPoint;
import com.jungdam.auth.handler.TokenAccessDeniedHandler;
import com.jungdam.auth.infrastructure.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.jungdam.auth.token.AuthTokenProvider;
import com.jungdam.common.properties.AuthProperties;
import com.jungdam.common.properties.CorsProperties;
import com.jungdam.member.domain.vo.Role;
import com.jungdam.member.infrastructure.MemberRefreshTokenRepository;
import com.jungdam.member.infrastructure.MemberRepository;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // TODO : 운용 진행시, 해당 코드 삭제 필요
    private static final String[] AUTH_WHITELIST = {
        "/v2/api-docs",
        "/swagger-resources",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/security",
        "/swagger-ui.html",
        "/webjars/**",
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/health"
    };
    private static final String USER_PERMISSION_URL = "/api/**";
    private static final String ADMIN_PERMISSION_URL = "/api/**/admin/**";
    private static final String OAUTH2_AUTHORIZATION_ENDPOINT = "/oauth2/authorization";
    private static final String OAUTH2_REDIRECT_ENDPOINT = "/*/oauth2/code/*";
    private static final String CORS_SPLIT_REGEX = ",";
    private static final String CORS_ALL_URL = "/**";

    private final CorsProperties corsProperties;
    private final AuthProperties authProperties;
    private final AuthTokenProvider tokenProvider;
    private final CustomOAuth2UserService oAuth2UserService;
    private final TokenAccessDeniedHandler tokenAccessDeniedHandler;
    private final MemberRefreshTokenRepository memberRefreshTokenRepository;
    private final MemberRepository memberRepository;

    public SecurityConfig(final CorsProperties corsProperties, final AuthProperties authProperties,
        final AuthTokenProvider tokenProvider,
        final CustomOAuth2UserService oAuth2UserService,
        final TokenAccessDeniedHandler tokenAccessDeniedHandler,
        final MemberRefreshTokenRepository memberRefreshTokenRepository,
        final MemberRepository memberRepository) {
        this.corsProperties = corsProperties;
        this.authProperties = authProperties;
        this.tokenProvider = tokenProvider;
        this.oAuth2UserService = oAuth2UserService;
        this.tokenAccessDeniedHandler = tokenAccessDeniedHandler;
        this.memberRefreshTokenRepository = memberRefreshTokenRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .cors()
            .configurationSource(corsConfigurationSource());

        http
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .exceptionHandling()
            .authenticationEntryPoint(new RestAuthenticationEntryPoint())
            .accessDeniedHandler(tokenAccessDeniedHandler);

        http
            .authorizeRequests()
            .antMatchers(AUTH_WHITELIST).permitAll()
            .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
            .antMatchers(USER_PERMISSION_URL).hasAnyAuthority(Role.USER.getRole())
            .antMatchers(ADMIN_PERMISSION_URL).hasAnyAuthority(Role.ADMIN.getRole())
            .anyRequest().authenticated();

        http
            .oauth2Login()
            .authorizationEndpoint()
            .baseUri(OAUTH2_AUTHORIZATION_ENDPOINT)
            .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
            .and()
            .redirectionEndpoint()
            .baseUri(OAUTH2_REDIRECT_ENDPOINT)
            .and()
            .userInfoEndpoint()
            .userService(oAuth2UserService)
            .and()
            .successHandler(oAuth2AuthenticationSuccessHandler())
            .failureHandler(oAuth2AuthenticationFailureHandler());

        http
            .addFilterBefore(tokenAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(
            tokenProvider,
            authProperties,
            memberRefreshTokenRepository,
            oAuth2AuthorizationRequestBasedOnCookieRepository(),
            memberRepository
        );
    }

    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(
            oAuth2AuthorizationRequestBasedOnCookieRepository());
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedHeaders(
            Arrays.asList(corsProperties.getAllowedHeaders().split(CORS_SPLIT_REGEX))
        );
        corsConfig.setAllowedMethods(
            Arrays.asList(corsProperties.getAllowedMethods().split(CORS_SPLIT_REGEX))
        );
        corsConfig.setAllowedOrigins(
            Arrays.asList(corsProperties.getAllowedOrigins().split(CORS_SPLIT_REGEX))
        );

        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(corsConfig.getMaxAge());

        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();
        corsConfigSource.registerCorsConfiguration(CORS_ALL_URL, corsConfig);
        return corsConfigSource;
    }
}
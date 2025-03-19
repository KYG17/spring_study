package com.gn.mvc.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //환경설정파일
@EnableWebSecurity //스프링 시큐리티를 사용 할 것입니다 라고 선언
public class WebSecurityConfig {
	
	//요청중에 정적 리소스가 있는 경우 ->Security 비활성화
	@Bean
	WebSecurityCustomizer configure() {
		return web -> web.ignoring()
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}
	//특정 요청이 들어왔을 때 어떻게 처리할 것인가!
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService customUserDetailsService) throws Exception {
		// http 방식으로 /login, /signup, /logout은 일단 허락. 나머지 부분으로 갈때는 인증 받은 분(authenticated)만 받아주세요. 로그인은 로그인 페이지로, 성공하면 /board로. 로그아웃할때는 인증정보 지워주고, 로그아웃 성공하면 로그인쪽으로, 로그아웃 성공하면 세션 정보를 싹 날려주세요.
		http.userDetailsService(customUserDetailsService)
		.authorizeHttpRequests(requests -> requests
				.requestMatchers("/login", "/signup", "/logout", "/").permitAll()
				.anyRequest().authenticated())
		.formLogin(login -> login.loginPage("/login")
								.successHandler(new MyLoginSuccessHandler())
								.failureHandler(new MyLoginFailureHandler()))
		.logout(logout -> logout.clearAuthentication(true)
							.logoutSuccessUrl("/login")
							.invalidateHttpSession(true));
		return http.build();
	}
	
	//비밀번호 암호화에 사용될 Bean 등록
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	                                  
	
	//AuthenticationManager(인증 관리)
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
		
	}
	
}

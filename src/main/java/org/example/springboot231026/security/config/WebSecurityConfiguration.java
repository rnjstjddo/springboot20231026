package org.example.springboot231026.security.config;

import org.example.springboot231026.kakao.service.KakaoLoginService;
import org.example.springboot231026.security.handler.Custom403Handler;
import org.example.springboot231026.security.handler.CustomAuthFailureHandler;
import org.example.springboot231026.security.service.MemberUserDetailsService;
import org.hibernate.loader.collection.OneToManyJoinWalker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true, securedEnabled=true)//
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    //자동로그인
    @Autowired
    private DataSource ds;

    @Autowired
    private MemberUserDetailsService ms;

    @Autowired
    private KakaoLoginService ks;

    @Autowired
    private PasswordEncoder pe;

    //소셜로그인
    @Autowired
    private OAuth2UserService os;

    @Autowired
    private CustomAuthFailureHandler customAuthFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //System.out.println("security-config클래스 WebSecurityConfiguration 오버라이딩 configure() 진입 파라미터 HttpSecurity ");

        http.httpBasic().and().authorizeRequests().antMatchers("/auth/**", "/oauth/**", "/js/**", "/image/**", "/css/**", "/",
                "/member/**", "/guestbook/**", "/uploadEx", "/display", "/removeFile", "/uploadAjax", "/dogsell/list",
                "/dogselldisplay", "/dogsellremoveFile", "/dogselluploadAjax", "/main", "/main/", "/post/list", "/home/**", "/profile").permitAll();
        //http.httpBasic().and().authorizeRequests().antMatchers("/**").permitAll();

        http.authorizeRequests().anyRequest().authenticated();
        http.formLogin().loginPage("/member/login").defaultSuccessUrl("/home/home")
                //.failureHandler(customAuthFailureHandler());
                .failureHandler(customAuthFailureHandler);
        http.csrf().disable();
        http.logout().logoutUrl("/member/logout").logoutSuccessUrl("/home/home");
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
        //자동로그인설정
        http.rememberMe().key("12345678").tokenRepository(persistentTokenRepository())
                .userDetailsService(ms).tokenValiditySeconds(60 * 60 * 24 * 30);//30일지정

        http.oauth2Login().defaultSuccessUrl("/member/updateSocialJoin").userInfoEndpoint().userService(os);

        //http.oauth2Login().userInfoEndpoint().userService(os);


    }

    //자동로그인쿠키저장소
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        //System.out.println("security-config클래스 WebSecurityConfiguration @Bean PersistentTokenRepository 생성");
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(ds);
        return repo;
    }

    //인증서버로부터 받은 사용자정보를 이용해서 Member엔티티객체 생성후 Manager를 사용해서 인증요청시 인자로 전달후 Authentication 생성
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        //System.out.println("security-config클래스 WebSecurityConfiguration @Bean AuthenticationManager 생성");
        return super.authenticationManagerBean();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //System.out.println("security-config클래스 WebSecurityConfiguration 오버라이딩 configure() 진입 ");

        auth.userDetailsService(ms).passwordEncoder(pe);
    }


    /*//순환참조문제로 별도의 클래스로 분리시킴
    @Bean
    public PasswordEncoder passwordEncoder(){
        System.out.println("security-config클래스 WebSecurityConfiguration @Bean PasswordEncoder 생성");

       return new BCryptPasswordEncoder();
    }
*/

    //
/*
    @Bean
    public SimpleUrlAuthenticationFailureHandler customAuthFailureHandler() {
        return new CustomAuthFailureHandler();
    }
*/


    //실패핸들러추가
    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        //System.out.println("security-config클래스 WebSecurityConfiguration @Bean AccessDeniedHandler 생성");
        return new Custom403Handler();
    }
//        System.out.println("security-config클래스 WebSecurityConfiguration @Bean PasswordEncoder 생성 ");
}

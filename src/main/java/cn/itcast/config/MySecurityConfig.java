package cn.itcast.config;

import cn.itcast.service.user.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter {
//    *
//     * 授权
//     * @param http
//     * @throws Exception
//
@Autowired
UserDetailsServiceImpl userDetailsService;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    private static final String KEY = "waylau.com";
    @Override
    protected void configure(HttpSecurity http) throws Exception {
/* super.configure(http);*/

        http.authorizeRequests().antMatchers("/toLogin","/admin/css/**","/admin/images/**")
                .permitAll()
               /*前面需添加一个方法才能实行这个东西*/
                .antMatchers("/admin/**").hasAnyAuthority("USER")
                .antMatchers("/comm/**").hasAnyAuthority("ADMIN")
                .antMatchers("/site/**").hasAnyAuthority("ADMIN")
               /* 都这个其实就是所有的请求必须进行验证*/
                 .anyRequest()
                 .authenticated()
                .and()
                .rememberMe().rememberMeParameter("remeber_me").tokenValiditySeconds(6000*20).rememberMeCookieName("cookie")
                .and().csrf().disable();// 关闭csrf处理;
              http.formLogin().usernameParameter("username")
           .passwordParameter("password").loginPage("/toLogin").loginProcessingUrl("/login").successForwardUrl("/success/login");
             /* 记住我功能、cookie没有效果*/
/*      http.rememberMe().rememberMeParameter("remeber_me").tokenValiditySeconds(6000*20).rememberMeCookieName("cookie1");*/
            http.logout().logoutSuccessUrl("/toLogin");

    }

//    *
//     * 认证
//     * @param auth
//     * @throws Exception

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
/* super.configure(auth);*/

        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        /*auth.inMemoryAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .withUser("admin").password(new BCryptPasswordEncoder().encode("123456"))
                .roles("USER")
                .and()
                .withUser("root").password(new BCryptPasswordEncoder().encode("123456"))
                .roles("ADMIN");*/
    }


}

package cn.itcast.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter {
//    *
//     * 授权
//     * @param http
//     * @throws Exception
//

    @Override
    protected void configure(HttpSecurity http) throws Exception {
/* super.configure(http);*/

        http.authorizeRequests().antMatchers("/toLogin","/error/**",
                "/static/**","/mapper/**","/site/**")
                .permitAll()

                /*.antMatchers("/admin/**").hasRole("USER")
                .antMatchers("/comm/**").hasRole("USER")*/
               /* 这个其实就是所有的请求都必须进行验证*/
                 .anyRequest()
                 .authenticated()
                .and().csrf().disable();// 关闭csrf处理;
           http.formLogin().usernameParameter("username")
           .passwordParameter("password").loginPage("/toLogin").loginProcessingUrl("/login");
    }

//    *
//     * 认证
//     * @param auth
//     * @throws Exception

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
/* super.configure(auth);*/

        auth.inMemoryAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .withUser("admin").password(new BCryptPasswordEncoder().encode("123456"))
                .roles("USER")
                .and()
                .withUser("root").password(new BCryptPasswordEncoder().encode("123456"))
                .roles("ADMIN");
    }


}

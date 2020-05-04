package org.lele.common.config;

import org.lele.common.service.UserDetailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author lele
 * 公共认证配置
 */
@Order(2)
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter{
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService(){
        return new UserDetailServiceImpl();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService( userDetailsService() )
                .passwordEncoder(passwordEncoder());
    }

    /**
     * 若是使用sessionImple实现，redis相关主要配置两点：
     *      1、将原本CurrentHashMap实现的session用redis来代替。
     *      2、用户登出后，在redis中去除对应的session。
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                .defaultSuccessUrl("/doc.html");
               /* .and()
                .sessionManagement()
                    //.maximumSessions(1)
                    //.sessionRegistry(sessionRegistryImpl())
                    //.expiredUrl("/error")
                    //.maxSessionsPreventsLogin(true)
                    //.and()*/

                        //.successForwardUrl("/doc.html") // 定义成功跳转的post路径
    }
}
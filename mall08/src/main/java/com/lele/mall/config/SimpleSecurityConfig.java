package com.lele.mall.config;/*
 * com.lele.mall.config
 * @author: lele
 * @date: 2020-04-18
 */

import com.lele.mall.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configurable
@EnableWebSecurity
public class SimpleSecurityConfig extends WebSecurityConfigurerAdapter {

    // 引入自定义userDetails
    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        //return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance(); // 不对密码进行加密
    }

    /**
     * 通过http对象配置具体的认证规则、路由权限规则
     *  这里用http对象代替xm配置，注意这里每个and()之间的配置都相当于原来xml中一个标签包含的配置。
     * @param http
     * @throws Exception
     */
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .formLogin()// 使用默认的表单登录
                    .permitAll()
                    .and()
                .authorizeRequests()
                    .antMatchers("/test/*","/order/*") // 放行swagger
                    .authenticated();
        }

    /**
     * 用户认证规则，即用户传递何种信息才可以登录
     *  myUserDetailsService.loadUserByUsername 通过传入username，返回我们的user数据，
     *  passwordEncoder 会对user数据中的password进行BCrypt算法加密。
     * @param auth
     * @throws Exception
     */
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(myUserDetailsService);
            //.passwordEncoder(passwordEncoder());
        }

}



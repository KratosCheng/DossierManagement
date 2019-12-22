package edu.njusoftware.dossiermanagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private String[] urlWhiteList = {
            "/",
            "/index",
            "/login",
            "/login-error",
            "/user/**",
            "/case/**"
    };

    @Qualifier("userServiceImpl")
    @Autowired
    private UserDetailsService userService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        //校验用户
        auth.userDetailsService( userService ).passwordEncoder( new PasswordEncoder() {
            //对密码进行加密
            @Override
            public String encode(CharSequence charSequence) {
                System.out.println(charSequence.toString());
                return DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
            }

            //对密码进行判断匹配
            @Override
            public boolean matches(CharSequence charSequence, String s) {
                String encode = DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
                boolean res = s.equals( encode );
                return res;
            }
        } );

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(urlWhiteList).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
//                .loginPage("/templates/login").failureUrl("/login?error=true") // 自定义登录界面
//                .and().rememberMe() // 启用 remember me
                .and()
                .logout().permitAll().invalidateHttpSession(true).deleteCookies("JSESSIONID")
                .and()
                .httpBasic();
//        http.authorizeRequests().anyRequest().permitAll().and().logout().permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // security默认拦截了静态资源。在这可以放行
        web.ignoring().antMatchers("/**/*.js", "/lang/*.json", "/**/*.css", "/**/*.js", "/**/*.png");
    }
}

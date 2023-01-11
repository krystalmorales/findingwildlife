package learn.findingwildlife.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtConverter converter;

    public SecurityConfig(JwtConverter converter) {
        this.converter = converter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.cors();

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/*").permitAll()
                .antMatchers(HttpMethod.POST, "/authenticate", "/create_account").permitAll()
                .antMatchers(HttpMethod.GET, "/finding-wildlife/trail", "/finding-wildlife/sighting/date/**","/finding-wildlife/sighting/sightingId/*","/finding-wildlife/trailId/*","/finding-wildlife/organism/*",
                 "/finding-wildlife/park", "/finding-wildlife/park/*", "/finding-wildlife/park-organism/*","/finding-wildlife/sighting/national-park/*",
                 "/finding-wildlife/trail-review", "/finding-wildlife/trail-review/**").permitAll()
                .antMatchers(HttpMethod.GET,"/finding-wildlife/sighting/userId/*", "/finding-wildlife/trail-review/userId/*").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.POST, "/finding-wildlife/organism", "/finding-wildlife/park-organism").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/finding-wildlife/sighting", "/finding-wildlife/trail-review").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.PUT, "/finding-wildlife/sighting/*", "/finding-wildlife/trail-review/*").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.PUT, "/finding-wildlife/organism/*", "/finding-wildlife/park-organism/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/finding-wildlife/organism/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/finding-wildlife/sighting/**", "/finding-wildlife/trail-review/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/**").denyAll()
                .and()
                .addFilter(new JwtRequestFilter(authenticationManager(), converter))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}


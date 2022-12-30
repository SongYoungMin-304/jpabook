package jpashop.jpabook.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{


    @Override
    public Authentication authenticate(Authentication authentication) {

       /* String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        log.info("흠"+authentication.getDetails());


        if(!"root".equals(username) || !"1234".equals(password)) {
            log.info("Invalid UserName or Password");
            throw new BadCredentialsException(username);
        }

        log.info("정상...");

        //user.getAuthorities()*/

        log.info("정상...");

        return new UsernamePasswordAuthenticationToken("1", "2");

    }

    @Override
    public boolean supports(Class<?> authentication) {
       return true;
    }
}

package jpashop.jpabook.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;


@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider{


    /*
     * jpa 회원 가입 기능 관련 개발 이후 적용 예정..
     */
    @Override
    public Authentication authenticate(Authentication authentication) {

       String username = authentication.getName();
       String password = (String) authentication.getCredentials();

        if(!"root".equals(username) || !"1234".equals(password)) {
            log.info("Invalid UserName or Password");
            throw new BadCredentialsException(username);
        }

        return new CustomAuthenticationToken(username, password, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
       return true;
    }
}

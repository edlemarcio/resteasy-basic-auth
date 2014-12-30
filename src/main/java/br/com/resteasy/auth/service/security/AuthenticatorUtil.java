package br.com.resteasy.auth.service.security;

import br.com.resteasy.auth.dao.UserDao;
import br.com.resteasy.auth.model.User;

import javax.security.auth.login.LoginException;
import java.security.GeneralSecurityException;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by enrique1 on 12/29/14.
 */
public class AuthenticatorUtil {

    public String login(String username, String password ) throws LoginException {

        Optional<User> user = UserDao.getByNameAndPasswd(username, password);
        user.orElseThrow(() -> new LoginException("Don't Come Here Again!"));

        return UUID.randomUUID().toString();

    }

    public void logout(String authToken) throws GeneralSecurityException {
	throw new GeneralSecurityException( "Invalid service key and authorization token match." );
    }
}

package br.com.resteasy.auth.dao;

import br.com.resteasy.auth.model.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by enrique1 on 12/27/14.
 */
public class UserDao {

    private static Map<Integer, User> users = new HashMap<>();

    static {
        for (int cii = 1; cii <= 5; cii++) {
            User user = new User();
            user.setId(cii);
            user.setUserName("username"+cii);
            user.setPassword("passwordForUser"+cii);
            user.setPerfil("USER");
            user.setFirstName("demo "+cii);
            user.setLastName("user "+cii);
            user.setUri("/user-management/users/"+cii);
            user.setLastModified(new Date());
            users.put(user.getId(), user);
        }
        for (int cii = 6; cii <= 10; cii++) {
            User user = new User();
            user.setId(cii);
            user.setUserName("username"+cii);
            user.setPassword("passwordForUser"+cii);
            user.setPerfil("ADMIN");
            user.setFirstName("demo "+cii);
            user.setLastName("user "+cii);
            user.setUri("/user-management/users/"+cii);
            user.setLastModified(new Date());
            users.put(user.getId(), user);
        }
    }

    public static Optional<User> get(Integer id) {
	return Optional.ofNullable(users.get(id));
    }

    public static void update(User user) {
	user.setLastModified(new Date());
        users.put(user.getId(), user);
    }

    public static Date getLastModifiedById(Integer id) {
	return users.get(id).getLastModified();
    }

    public static Optional<User> getByNameAndPasswd(String username, String password) {
        return users.values()
                    .stream()
                    .filter((User user) -> user.getUserName().equals(username) && user.getPassword().equals(password))
                    .findFirst();
    }
}

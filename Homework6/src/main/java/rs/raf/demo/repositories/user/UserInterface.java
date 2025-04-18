package rs.raf.demo.repositories.user;

import rs.raf.demo.entities.User;

public interface UserInterface {
    User findByUsername(String username);
    User save(User user);
}

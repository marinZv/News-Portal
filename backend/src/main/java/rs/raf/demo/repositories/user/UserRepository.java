package rs.raf.demo.repositories.user;

import rs.raf.demo.entities.User;

import java.util.List;

public interface UserRepository {
    User findUser(String email);
    User addUser(User user);

    List<User> allUsers();
    void deleteUser(String email);
    User updateUser(User user, String email);
    void changeUserActivity(String email);
}

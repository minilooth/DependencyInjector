package by.minilooth.dependencyinjectortest.interfaces;

import by.minilooth.dependencyinjectortest.models.Role;
import by.minilooth.dependencyinjectortest.models.User;

import java.util.List;

public interface UserService {

    public void addUser(User user);
    public void deleteUser(User user);
    public User getById(Integer id);
    public List<User> getAll();

}

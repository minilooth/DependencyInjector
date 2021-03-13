package by.minilooth.dependencyinjectortest.impls;

import by.minilooth.dependencyinjector.annotations.Inject;
import by.minilooth.dependencyinjectortest.interfaces.RoleService;
import by.minilooth.dependencyinjectortest.interfaces.UserService;
import by.minilooth.dependencyinjectortest.models.Role;
import by.minilooth.dependencyinjectortest.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

    private final List<User> users;

    private RoleService roleService;

    @Inject
    public UserServiceImpl(RoleService roleService) {
        this.roleService = roleService;
        this.users = new ArrayList<>();
    }

    public RoleService getRoleService() {
        return roleService;
    }

    @Override
    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public void deleteUser(User user) {
        users.remove(user);
    }

    @Override
    public User getById(Integer id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<User> getAll() {
        return this.users;
    }

}

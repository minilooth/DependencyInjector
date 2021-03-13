package by.minilooth.dependencyinjectortest.impls;

import by.minilooth.dependencyinjectortest.interfaces.RoleService;
import by.minilooth.dependencyinjectortest.models.Role;

import java.util.ArrayList;
import java.util.List;

public class RoleServiceImpl implements RoleService {

    private final List<Role> roles;

    public RoleServiceImpl() {
        this.roles = new ArrayList<>();
    }

    @Override
    public void addRole(Role role) {
        roles.add(role);
    }

    @Override
    public void deleteRole(Role role) {
        roles.remove(role);
    }

    @Override
    public Role getById(Integer id) {
        return roles.stream().filter(r -> r.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Role> getAll() {
        return this.roles;
    }

}

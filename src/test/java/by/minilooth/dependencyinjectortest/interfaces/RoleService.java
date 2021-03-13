package by.minilooth.dependencyinjectortest.interfaces;

import by.minilooth.dependencyinjectortest.models.Role;

import java.util.List;

public interface RoleService {

    public void addRole(Role role);
    public void deleteRole(Role role);
    public Role getById(Integer id);
    public List<Role> getAll();

}

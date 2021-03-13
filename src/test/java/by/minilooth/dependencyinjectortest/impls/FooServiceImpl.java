package by.minilooth.dependencyinjectortest.impls;

import by.minilooth.dependencyinjector.annotations.Inject;
import by.minilooth.dependencyinjectortest.interfaces.FooService;
import by.minilooth.dependencyinjectortest.interfaces.RoleService;
import by.minilooth.dependencyinjectortest.interfaces.UserService;

public class FooServiceImpl implements FooService {

    private UserService userService;
    private RoleService roleService;

    @Inject
    public FooServiceImpl(UserService userService) {
        this.userService = userService;
        this.roleService = null;
    }

    @Inject
    public FooServiceImpl(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    public void foo() {}

}

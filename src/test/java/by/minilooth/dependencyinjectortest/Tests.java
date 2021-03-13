package by.minilooth.dependencyinjectortest;

import by.minilooth.dependencyinjector.exceptions.BindingNotFoundException;
import by.minilooth.dependencyinjector.exceptions.TooManyConstructorsException;
import by.minilooth.dependencyinjector.impls.InjectorImpl;
import by.minilooth.dependencyinjectortest.impls.FooServiceImpl;
import by.minilooth.dependencyinjectortest.impls.RoleServiceImpl;
import by.minilooth.dependencyinjectortest.impls.UserServiceImpl;
import by.minilooth.dependencyinjector.interfaces.Injector;
import by.minilooth.dependencyinjector.interfaces.Provider;
import by.minilooth.dependencyinjectortest.interfaces.FooService;
import by.minilooth.dependencyinjectortest.interfaces.RoleService;
import by.minilooth.dependencyinjectortest.interfaces.UserService;
import org.junit.Test;

import static org.junit.Assert.*;

public class Tests {

    @Test
    public void injectionTest() {
        Injector injector = new InjectorImpl();

        injector.bindSingleton(RoleService.class, RoleServiceImpl.class);
        injector.bind(UserService.class, UserServiceImpl.class);

        Provider<UserService> userServiceProvider = injector.getProvider(UserService.class);

        assertNotNull(userServiceProvider);
        assertNotNull(userServiceProvider.getInstance());
        assertSame(UserServiceImpl.class, userServiceProvider.getInstance().getClass());
    }

    @Test
    public void innerInjectionTest() {
        Injector injector = new InjectorImpl();

        injector.bindSingleton(RoleService.class, RoleServiceImpl.class);
        injector.bind(UserService.class, UserServiceImpl.class);

        Provider<UserService> userServiceProvider = injector.getProvider(UserService.class);
        UserServiceImpl userService = (UserServiceImpl) userServiceProvider.getInstance();

        assertNotNull(userServiceProvider);
        assertNotNull(userServiceProvider.getInstance());
        assertSame(UserServiceImpl.class, userServiceProvider.getInstance().getClass());
        assertNotNull(userService.getRoleService());
    }

    @Test(expected = TooManyConstructorsException.class)
    public void tooManyConstructorsTest() {
        Injector injector = new InjectorImpl();

        injector.bindSingleton(RoleService.class, RoleServiceImpl.class);
        injector.bind(UserService.class, UserServiceImpl.class);
        injector.bind(FooService.class, FooServiceImpl.class);

        Provider<FooService> userServiceProvider = injector.getProvider(FooService.class);
    }

    @Test(expected = BindingNotFoundException.class)
    public void bindingNotFoundTest() {
        Injector injector = new InjectorImpl();

        injector.bind(UserService.class, UserServiceImpl.class);

        Provider<UserService> userServiceProvider = injector.getProvider(UserService.class);
    }

    @Test
    public void getNullProviderTest() {
        Injector injector = new InjectorImpl();

        Provider<RoleService> roleServiceProvider = injector.getProvider(RoleService.class);

        assertNull(roleServiceProvider.getInstance());
    }

}

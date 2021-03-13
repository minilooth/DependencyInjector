package by.minilooth.dependencyinjector.impls;

import by.minilooth.dependencyinjector.annotations.Inject;
import by.minilooth.dependencyinjector.exceptions.BindingNotFoundException;
import by.minilooth.dependencyinjector.exceptions.ConstructorNotFoundException;
import by.minilooth.dependencyinjector.exceptions.TooManyConstructorsException;
import by.minilooth.dependencyinjector.interfaces.Injector;
import by.minilooth.dependencyinjector.interfaces.Provider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InjectorImpl implements Injector {

    private final Map<Class<?>, Class<?>> singletons;
    private final Map<Class<?>, Class<?>> prototypes;

    private final Map<Class<?>, Object> singletonsInstances;

    private final Object monitor = new Object();

    public InjectorImpl() {
        singletons = new ConcurrentHashMap<>();
        prototypes = new ConcurrentHashMap<>();
        singletonsInstances = new ConcurrentHashMap<>();
    }

    @SuppressWarnings("unchecked")
    public <T> Provider<T> getProvider(Class<T> type) {
        if (prototypes.containsKey(type)) {
            T object = createPrototypeInstance(type);

            return () -> object;
        }
        return () -> {
            if (!singletonsInstances.containsKey(type)) {
                createSingletonInstance(type);
            }

            synchronized (monitor) {
                return (T) singletonsInstances.getOrDefault(type, null);
            }
        };
    }

    public <T> void bind(Class<T> intf, Class<? extends T> impl) {
        prototypes.put(intf, impl);
    }

    public <T> void bindSingleton(Class<T> intf, Class<? extends T> impl) {
        singletons.put(intf, impl);
    }

    private <T> void createSingletonInstance(Class<T> type) {
        if (!singletons.containsKey(type)) {
            return;
        }

        Class<?> clazz = singletons.get(type);
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        if (constructors.length == 0) {
            throw new ConstructorNotFoundException("Constructor not found for " + type.toString());
        }

        Long countOfAnnotatedConstructors = Arrays.stream(constructors)
                .filter(c -> c.isAnnotationPresent(Inject.class)).count();

        try {
            if (countOfAnnotatedConstructors > 0) {
                if (countOfAnnotatedConstructors > 1) {
                    throw new TooManyConstructorsException("Too many constructors to inject for " + type.toString());
                } else {
                    Constructor<?> constructor = constructors[0];
                    Parameter[] parameters = constructor.getParameters();

                    List<Object> args = new ArrayList<>();

                    for (Parameter parameter : parameters) {
                        Object instance = getProvider(parameter.getType()).getInstance();

                        if (instance == null) {
                            throw new BindingNotFoundException("Unable to find binding for " + type.toString());
                        } else {
                            args.add(instance);
                        }
                    }

                    singletonsInstances.put(type, constructor.newInstance(args.toArray()));
                }
            } else {
                singletonsInstances.put(type, clazz.getDeclaredConstructor().newInstance());
            }
        }
        catch (NoSuchMethodException | InstantiationException |
                IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T createPrototypeInstance(Class<T> type) {
        Class<?> clazz = prototypes.get(type);
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        if (constructors.length == 0) {
            throw new ConstructorNotFoundException("Constructor not found for " + type.toString());
        }

        Long countOfAnnotatedConstructors = Arrays.stream(constructors)
                .filter(c -> c.isAnnotationPresent(Inject.class)).count();

        try {
            if (countOfAnnotatedConstructors > 0) {
                if (countOfAnnotatedConstructors > 1) {
                    throw new TooManyConstructorsException("Too many constructors to inject for " + type.toString());
                } else {
                    Constructor<?> constructor = constructors[0];
                    Parameter[] parameters = constructor.getParameters();

                    List<Object> args = new ArrayList<>();

                    for (Parameter parameter : parameters) {
                        Object instance = getProvider(parameter.getType()).getInstance();

                        if (instance == null) {
                            throw new BindingNotFoundException("Unable to find binding for " + type.toString());
                        } else {
                            args.add(instance);
                        }
                    }

                    return (T) constructor.newInstance(args.toArray());
                }
            } else {
                return (T) clazz.getDeclaredConstructor().newInstance();
            }
        }
        catch (NoSuchMethodException | InstantiationException |
                IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}

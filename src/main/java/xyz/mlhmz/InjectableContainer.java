package xyz.mlhmz;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import xyz.mlhmz.exceptions.InjectableInitializationFailed;
import xyz.mlhmz.exceptions.InjectableInvalidException;
import xyz.mlhmz.exceptions.InjectableNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The InjectableContainer holds all classes that can be injected.
 * <p/>
 * Its function resembles, together with the {@link Injectable} the IoC Principal.
 * <p/>
 * Every instance that is injected into and get out of the Container, acts as singleton.
 */
public class InjectableContainer {
    private static final HashMap<Class<?>, Object> instances = new HashMap<>();

    private InjectableContainer() {
    }

    /**
     * Gets with Reflections all Classes marked with {@link Injectable} and passes them into
     * {@link #putIntoInstancesMap(Class)}
     */
    public static void init() {
        Reflections reflections = new Reflections(InjectableContainer.class.getPackageName(), new TypeAnnotationsScanner(), new SubTypesScanner(false));
        reflections.getTypesAnnotatedWith(Injectable.class).forEach(InjectableContainer::putIntoInstancesMap);
    }

    /**
     * Instantiates a class and puts it into the {@link #instances} {@link HashMap} identified the actual class input.
     * <p/>
     * If the class has an overlying interface it, the first interface will be picked and also be put into the map.
     * <br/>
     * If this is unintended, it can be deactivated in the {@link Injectable} enum
     *
     * @param clazz Class that's instance should be injected
     * @throws InjectableInitializationFailed when the initialization failed
     */
    private static void putIntoInstancesMap(Class<?> clazz) {
        if (clazz.isInterface()) {
            throw new InjectableInitializationFailed(String.format("The class '%s' cant be an injectable, because it is an interface.", clazz.getCanonicalName()));
        }
        try {
            Injectable annotation = clazz.getAnnotation(Injectable.class);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            if (annotation.resolveIfc()) {
                Arrays.stream(clazz.getInterfaces()).findFirst().ifPresent(ifc -> instances.put(ifc, instance));
            }
            instances.put(clazz, instance);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new InjectableInitializationFailed(e.getMessage());
        }
    }

    /**
     * Gets a class instance out of the {@link #instances} {@link HashMap}
     *
     * @param clazz {@link Class} that should be fetched from the {@link #instances} {@link HashMap}
     * @return Actual Class Instance
     * @param <T> Generic of the class, that should be fetched
     */
    public static <T> T get(Class<T> clazz) {
        Object instance = instances.get(clazz);
        if (instance == null) {
            throw new InjectableNotFoundException();
        }
        if (!clazz.isInstance(instance)) {
            throw new InjectableInvalidException();
        }
        return clazz.cast(instance);
    }

}

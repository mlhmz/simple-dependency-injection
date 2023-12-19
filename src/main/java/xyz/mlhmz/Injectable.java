package xyz.mlhmz;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The injectable annotation is responsive for marking classes to be put into the {@link InjectableContainer}
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Injectable {
    /**
     * Defines if the Interface of the Injectable should be resolved.
     * <br/>
     * If enabled, the class can be fetched out of the {@link InjectableContainer} by the interface
     */
    boolean resolveIfc() default true;
}

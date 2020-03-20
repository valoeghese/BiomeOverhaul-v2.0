package tk.valoeghese.worldcomet;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicates that the API class or method is experimental, and subject to change.
 */
@Retention(CLASS)
@Target({ TYPE, METHOD })
public @interface Experimental {
}

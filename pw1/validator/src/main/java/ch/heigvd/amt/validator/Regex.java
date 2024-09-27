package ch.heigvd.amt.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used on fields to indicate that they must be in a given regex.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Regex {
    /**
     * The regex to match.
     * @return the regex to match.
     */
    String value();
}

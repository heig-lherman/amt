package ch.heigvd.amt.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used on fields to indicate that they must be in a given range.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Range {
    /**
     * The minimum value of the range.
     * @return the minimum value of the range.
     */
    double min();

    /**
     * The maximum value of the range.
     * @return the maximum value of the range.
     */
    double max();
}

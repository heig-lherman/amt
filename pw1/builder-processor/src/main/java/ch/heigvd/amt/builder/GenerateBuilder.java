package ch.heigvd.amt.builder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This compile-time annotation is used to trigger the BuilderProcessor and to generate a builder class.
 */
@Target(ElementType.TYPE)
public @interface GenerateBuilder {

}

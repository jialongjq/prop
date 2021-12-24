package data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Decorador per suportar el parse de jsons. 
 * 
 * @author Roberto Navarro Morales
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonBaseDeduce {
	public String value() default "";
}

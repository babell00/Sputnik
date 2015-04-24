package uk.co.gajzler.annotations;

import uk.co.gajzler.enums.BeanScope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {
    public String name() default "";
    public BeanScope scope() default BeanScope.SINGLETON;
}

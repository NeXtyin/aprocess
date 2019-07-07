package com.test.yin.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author leilongling@kungeek.com
 * @version 3.2
 * date 2019/6/12.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface Optional {
    String stringValue() default "\"\"";

    int intValue() default 0;

    float floatValue() default 0f;

    boolean booleanValue() default false;
}

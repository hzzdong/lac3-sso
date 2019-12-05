package com.linkallcloud.sso.client.support;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Pt {

    String code() default "";

    String url() default "";
}

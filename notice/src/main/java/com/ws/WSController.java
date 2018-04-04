package com.ws;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface WSController {
/*
 * 方法参数只能是Map类型
 * 
 */
	String value() default "";
}

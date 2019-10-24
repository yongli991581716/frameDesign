package com.framedesign.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 抖动注解
 *
 * @author liyong
 * @date 2019-10-23
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface PreShake {
}

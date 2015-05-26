package com.tongca.ui.binding.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UIView {
    /**
     * @return the id representing view, default 0
     */
    public int value() default 0;

	/**
	 * @return the id representing view, default 0
	 */
	public int viewById() default 0;

	/**
	 * 
	 * @return the tag representing view, default ""
	 */
	public String viewByTag() default "";

}

package com.ccy.domain.validate;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target( { ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueCheckValidation.class)
public @interface UniqueCheck {

	String message() default "Data aleardy exists !!";
	
	Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
    String[] columnName();
	
}




package com.ccy.domain.validate;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target( { TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = ConfirmPasswordValidation.class)
public @interface ConfimPassowrd {

	String message() default "Password and Confirm Password do not match";
	
	Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
	
}




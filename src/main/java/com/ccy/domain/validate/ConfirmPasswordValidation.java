package com.ccy.domain.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ccy.domain.User;

public class ConfirmPasswordValidation implements ConstraintValidator<ConfimPassowrd, User> {
	
	private final Logger LOG = LoggerFactory.getLogger(ConfirmPasswordValidation.class);

	@Override
	public void initialize(ConfimPassowrd arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(User arg0, ConstraintValidatorContext arg1) {
		if (arg0.getPassword() != null && arg0.getConfirmPassowrd() != null){
			if (!arg0.getPassword().equals(arg0.getConfirmPassowrd())) {	
				LOG.info("Password and Confirm Password do not match");
				return false;
			}
		}
		return true;
	}

}

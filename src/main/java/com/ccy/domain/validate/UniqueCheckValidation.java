package com.ccy.domain.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.ccy.domain.User;

public class UniqueCheckValidation implements ConstraintValidator<UniqueCheck, User> {

	String[] colNames; 
			
	@Override
	public void initialize(UniqueCheck uniqueCheck) {
		this.colNames = uniqueCheck.columnName();
	}

	@Override
	public boolean isValid(User o, ConstraintValidatorContext arg1) {
		
		if (colNames.length == 0){
			return true;
		}
		
		return false;//User.findByFieldName(o,colNames).isEmpty();
	}



}

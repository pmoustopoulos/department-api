package com.ainigma100.departmentapi.util.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SexValidator implements ConstraintValidator<Sex, String> {

    public void initialize(Sex sex) {
        // used only if your annotation has attributes
    }

    public boolean isValid(String sex, ConstraintValidatorContext constraintContext) {
        // Bean Validation specification recommends considering null values as
        // being valid. If null is not a valid value for an element, it should
        // be annotated with @NotNull explicitly.
        if (sex == null) {
            return true;
        }

        // we used equalsIgnoreCase because we do not care if the value is a capital letter or not
        return sex.equalsIgnoreCase("F") || sex.equalsIgnoreCase("M");

    }
}

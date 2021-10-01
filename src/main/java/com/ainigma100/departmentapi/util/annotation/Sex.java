package com.ainigma100.departmentapi.util.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = SexValidator.class)
@Documented
public @interface Sex
{

    String message() default "sex value is not valid. Value must be M or F";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

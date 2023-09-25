package org.model.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {BankAccountValidator.class})
@Documented
public @interface ValidBankAccount {

    String message() default "{org.model.transactions.BankAccount.ValidBankAccount.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}

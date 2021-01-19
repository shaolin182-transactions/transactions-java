package org.model.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {TransactionCategoryValidator.class})
@Documented
public @interface ValidTransactionCategory {

    String message() default "{org.model.transactions.TransactionCategory.ValidTransactionCategory.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}

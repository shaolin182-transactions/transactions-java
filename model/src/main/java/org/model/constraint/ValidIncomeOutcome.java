package org.model.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {IncomeOutComeValidator.class})
@Documented
public @interface ValidIncomeOutcome {

    String message() default "{org.model.transactions.TransactionDetail.ValidIncomeOutcome.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}

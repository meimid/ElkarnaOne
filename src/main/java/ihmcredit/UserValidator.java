package ihmcredit;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Repository
public class UserValidator implements Validator {

	public boolean supports(final Class<?> clazz) {
		return OperationBean.class.equals(clazz);
	}

	public void validate(final Object target, final Errors errors) {

		final OperationBean foo = (OperationBean) target;

		if (StringUtils.isEmpty(foo.getNumCompte())) {
			// errors.rejectValue("numCompte", "numCompte[emptyMessage]");
			// errors.rejectValue("numCompte", "numCompte[emptyMessage]");
			System.out.println("numCompte " + null);
			// ValidationUtils.rejectIfEmptyOrWhitespace(errors, "NumCompte",
			// "lable.compte.null", new Object[] { "NumCompte" }, "");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "numCompte",
			        "lable.compte.null");

		} else if (foo.getNumCompte().length() < 1
		        || foo.getNumCompte().length() > 6) {
			System.out.println("numCompte 1 " + null);
			errors.rejectValue("numCompte", "numCompte[invalidLength]");
			System.out.println("numCompte 1 " + null);
		}

		/*
		 * if (foo.getAge() == null) { errors.rejectValue("age",
		 * "age[emptyMessage]"); } else if (foo.getAge() < 1 || foo.getAge() >
		 * 110) { errors.rejectValue("age", "age[invalidAge]"); }
		 */
	}

	/*
	 * ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName",
	 * "name.required");
	 */

	// do "complex" validation here

}
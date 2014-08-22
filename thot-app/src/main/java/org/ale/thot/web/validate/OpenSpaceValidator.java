package org.ale.thot.web.validate;

import org.ale.app.INameValidator;
import org.ale.thot.model.OpenSpaceFormData;
import org.springframework.validation.Errors;

public class OpenSpaceValidator {

    public static INameValidator nameValidator;

	public static void validate(OpenSpaceFormData formData, Errors errors) {
		if (formData.getTitle() == null || formData.getTitle().isEmpty() ) {
            errors.rejectValue("title", null, "Title cannot be empty!");
		}

		String speaker = formData.getSpeaker();
        if(speaker == null
                ||
            (speaker != null && speaker.startsWith("@") && !nameValidator.isValid(speaker))) {
        	errors.rejectValue("speaker", null, "Speaker twitter name is incorrect");
        }
	}
}
package org.immregistries.dqa.validator.engine.rules.patient;

import java.util.ArrayList;
import java.util.List;

import org.immregistries.dqa.validator.engine.ValidationRule;
import org.immregistries.dqa.validator.engine.ValidationRuleResult;
import org.immregistries.dqa.validator.engine.issues.MessageAttribute;
import org.immregistries.dqa.validator.engine.issues.ValidationIssue;
import org.immregistries.dqa.validator.model.DqaMessageReceived;
import org.immregistries.dqa.validator.model.DqaPatient;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatientIsUnderage extends ValidationRule<DqaPatient> {
 
	private static final Logger logger = LoggerFactory
		.getLogger(PatientIsUnderage.class);

	@Override
	protected final Class[] getDependencies() {
		return new Class[] {
			PatientExists.class, 
			PatientBirthDateIsValid.class
		};
	}
	
	/*
	 * This is the money: 
	 */
	
	@Override
	protected ValidationRuleResult executeRule(DqaPatient target, DqaMessageReceived m) {
		
		List<ValidationIssue> issues = new ArrayList<ValidationIssue>();
		boolean passed = false;
		
		if (/* protect first */target != null && target.getBirthDate() != null
				&& m.getMessageHeader() != null
				&& m.getMessageHeader().getMessageDate() != null) {
			
			DateTime eighteenYearsBeforeSubmission = (new DateTime(m.getMessageHeader().getMessageDate().getTime())).minusYears(18);
			DateTime birthDate = new DateTime(target.getBirthDate());
			
			boolean underage = birthDate.isAfter(eighteenYearsBeforeSubmission);
			logger.info("Eighteen years before submission: " + datr.toString(eighteenYearsBeforeSubmission));
			logger.info("patient birth date: " + datr.toString(birthDate));
			
			if (/* patient is underage */underage) {
				issues.add(MessageAttribute.PatientBirthDateIsUnderage.build(datr.toString(birthDate)));
				passed = true;
			}
		}
		
		return buildResults(issues, passed);
	}
}

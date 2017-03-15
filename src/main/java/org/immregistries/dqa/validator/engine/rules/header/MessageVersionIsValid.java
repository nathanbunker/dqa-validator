package org.immregistries.dqa.validator.engine.rules.header;

import java.util.ArrayList;
import java.util.List;

import org.immregistries.dqa.validator.engine.ValidationRule;
import org.immregistries.dqa.validator.engine.ValidationRuleResult;
import org.immregistries.dqa.validator.engine.rules.patient.PatientIsUnderage;
import org.immregistries.dqa.validator.issue.MessageAttribute;
import org.immregistries.dqa.validator.issue.ValidationIssue;
import org.immregistries.dqa.vxu.DqaMessageHeader;
import org.immregistries.dqa.vxu.DqaMessageReceived;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageVersionIsValid extends ValidationRule<DqaMessageHeader> {

	private static final Logger logger = LoggerFactory
			.getLogger(MessageVersionIsValid.class);
	
	@Override
	protected final Class[] getDependencies() {
		return new Class[] {
		// PatientExists.class,
		};
	}

	@Override
	protected ValidationRuleResult executeRule(DqaMessageHeader target,
			DqaMessageReceived mr) {

		List<ValidationIssue> issues = new ArrayList<ValidationIssue>();
		boolean passed = false;
		
		String version = target.getMessageVersion();
		
		if (common.isEmpty(version)) {
			issues.add(MessageAttribute.MessageVersionIsMissing.build());
		} else {
			//We want to evaluate the starting three characters...  2.5.1 should evaluate as 2.5, etc. 
			String evalVersion = version;
			if (evalVersion.length() > 3) {
				evalVersion = evalVersion.substring(0,3);
//				logger.info("eval version: " + evalVersion);
			}
			switch (evalVersion) {
				case "2.5":
				case "2.4":
				case "2.3":
					passed = true;
					break;
				default:
					issues.add(MessageAttribute.MessageVersionIsUnrecognized.build(version));
			}
		}

		return buildResults(issues, passed);
	}

}
package org.immregistries.dqa.validator.engine.rules.patient;

import org.immregistries.dqa.validator.engine.ValidationRule;
import org.immregistries.dqa.validator.engine.ValidationRuleResult;
import org.immregistries.dqa.validator.detection.Detection;
import org.immregistries.dqa.validator.detection.ValidationDetection;
import org.immregistries.dqa.vxu.DqaMessageReceived;
import org.immregistries.dqa.vxu.DqaPatient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PatientNameTypeIsValid extends ValidationRule<DqaPatient> {
	
	public PatientNameTypeIsValid() {
		ruleDetections.addAll(Arrays.asList(
				Detection.PatientNameTypeCodeIsMissing
		));
	}

	@Override
	protected ValidationRuleResult executeRule(DqaPatient target, DqaMessageReceived m) {
		List<ValidationDetection> issues = new ArrayList<ValidationDetection>();
		boolean passed = true;
		
		if (target != null && target.getName() != null) {
			String type = target.getName().getType();
			if (this.common.isEmpty(type)) {
				issues.add(Detection.PatientNameTypeCodeIsMissing.build(target));
			}
			
			//TODO: code received stuff. 
//	    	handleCodeReceived(target.getName().getType(), PotentialIssues.Field.PATIENT_NAME_TYPE_CODE);
		}
		
		return buildResults(issues, passed);
	}
	
}

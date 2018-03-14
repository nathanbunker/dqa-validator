package org.immregistries.dqa.validator.engine.rules.patient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.immregistries.dqa.validator.engine.ValidationRule;
import org.immregistries.dqa.validator.engine.ValidationRuleResult;
import org.immregistries.dqa.validator.issue.VxuField;
import org.immregistries.dqa.validator.issue.IssueType;
import org.immregistries.dqa.validator.issue.Detection;
import org.immregistries.dqa.validator.issue.ValidationIssue;
import org.immregistries.dqa.vxu.DqaMessageReceived;
import org.immregistries.dqa.vxu.DqaPatient;

public class PatientAliasIsValid extends ValidationRule<DqaPatient> {

	@Override
	protected final Class[] getDependencies() {
		return new Class[] {
			PatientExists.class, 
		};
	}
	
	public PatientAliasIsValid() {
		this.ruleDetections.add(Detection.buildIssue(VxuField.PATIENT_ALIAS, IssueType.MISSING).getIssue());
	}
	
	@Override
	protected ValidationRuleResult executeRule(DqaPatient target, DqaMessageReceived m) {
		
		List<ValidationIssue> issues = new ArrayList<ValidationIssue>();
		boolean passed = false;
		
		String aliasFirst = target.getAliasFirst();
		String aliasLast = target.getAliasLast();
		
		if (common.isEmpty(aliasLast + aliasFirst)) {
			issues.add(Detection.buildIssue(VxuField.PATIENT_ALIAS, IssueType.MISSING));
			passed = false;
		}
		
		return buildResults(issues, passed);
	}
}

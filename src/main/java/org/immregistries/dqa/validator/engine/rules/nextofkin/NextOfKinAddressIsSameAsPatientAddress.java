package org.immregistries.dqa.validator.engine.rules.nextofkin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.immregistries.dqa.validator.engine.ValidationRule;
import org.immregistries.dqa.validator.engine.ValidationRuleResult;
import org.immregistries.dqa.validator.engine.rules.patient.PatientExists;
import org.immregistries.dqa.validator.issue.Detection;
import org.immregistries.dqa.validator.issue.ValidationIssue;
import org.immregistries.dqa.vxu.DqaMessageReceived;
import org.immregistries.dqa.vxu.DqaNextOfKin;
import org.immregistries.dqa.vxu.DqaAddress;

public class NextOfKinAddressIsSameAsPatientAddress extends ValidationRule<DqaNextOfKin> {

	@Override
	protected final Class[] getDependencies() {
		return new Class[] { PatientExists.class, NextOfKinAddressIsValid.class };
	}

	public NextOfKinAddressIsSameAsPatientAddress() {
		this.ruleDetections.addAll(Arrays.asList(Detection.NextOfKinAddressIsDifferentFromPatientAddress));
	}
	
	
	@Override
	protected ValidationRuleResult executeRule(DqaNextOfKin target, DqaMessageReceived message) {
		List<ValidationIssue> issues = new ArrayList<ValidationIssue>();
		boolean passed = true;

		DqaAddress p = message.getPatient().getPatientAddressList().get(0);
		DqaAddress n = target.getAddress();
		if (!p.getCity().equals(n.getCity())
				|| !p.getState().equals(n.getState())
				|| !p.getStreet().equals(n.getStreet())
				|| !p.getStreet2().equals(p.getStreet2())) {
			
			issues.add(Detection.NextOfKinAddressIsDifferentFromPatientAddress.build(n.toString()));
			passed = false;
		}

		return buildResults(issues, passed);
	}
}

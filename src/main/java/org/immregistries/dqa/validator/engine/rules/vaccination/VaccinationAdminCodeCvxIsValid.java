package org.immregistries.dqa.validator.engine.rules.vaccination;

import org.apache.commons.lang3.StringUtils;
import org.immregistries.dqa.validator.engine.ValidationRule;
import org.immregistries.dqa.validator.engine.ValidationRuleResult;
import org.immregistries.dqa.validator.detection.ValidationDetection;
import org.immregistries.dqa.vxu.DqaMessageReceived;
import org.immregistries.dqa.vxu.DqaVaccination;
import org.immregistries.dqa.vxu.VxuField;

import java.util.ArrayList;
import java.util.List;

public class VaccinationAdminCodeCvxIsValid extends ValidationRule<DqaVaccination> {

	@Override
	protected final Class[] getDependencies() {
		return new Class[] {VaccinationUseCvx.class};
	}
	
	public VaccinationAdminCodeCvxIsValid() {
		ruleDetections.addAll(codr.getDetectionsForField(VxuField.VACCINATION_ADMIN_CODE));
	}

	@Override
	protected ValidationRuleResult executeRule(DqaVaccination target, DqaMessageReceived m) {
		List<ValidationDetection> issues = new ArrayList<ValidationDetection>();
		boolean passed = true;

		String cvxCode = target.getAdminCvxCode();

		if (StringUtils.isNotBlank(cvxCode)) {
			issues.addAll(codr.handleCode(cvxCode, VxuField.VACCINATION_ADMIN_CODE, target));

			passed = (issues.size() == 0);
		}

		return buildResults(issues, passed);
	}

}

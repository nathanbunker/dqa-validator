package org.immregistries.dqa.validator.engine.rules.vaccination;

import java.util.ArrayList;
import java.util.List;
import org.immregistries.dqa.validator.detection.ValidationReport;
import org.immregistries.dqa.validator.engine.ValidationRule;
import org.immregistries.dqa.validator.engine.ValidationRuleResult;
import org.immregistries.dqa.vxu.DqaMessageReceived;
import org.immregistries.dqa.vxu.DqaVaccination;
import org.immregistries.dqa.vxu.VxuField;

public class VaccinationAdministeredUnitIsValid extends ValidationRule<DqaVaccination> {

  @Override
  protected final Class[] getDependencies() {
    return new Class[] {VaccinationAdministeredAmtIsValid.class, VaccinationIsAdministered.class};
  }

  public VaccinationAdministeredUnitIsValid() {
    ruleDetections.addAll(codr.getDetectionsForField(VxuField.VACCINATION_ADMINISTERED_UNIT));
  }

  @Override
  protected ValidationRuleResult executeRule(DqaVaccination target, DqaMessageReceived m) {

    List<ValidationReport> issues = new ArrayList<ValidationReport>();
    boolean passed = false;

    issues.addAll(codr.handleCode(target.getAmountUnit(), VxuField.VACCINATION_ADMINISTERED_UNIT,
        target));

    passed = (issues.size() == 0);
    return buildResults(issues, passed);

  }
}

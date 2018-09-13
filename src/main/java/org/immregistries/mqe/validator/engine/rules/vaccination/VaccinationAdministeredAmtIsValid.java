package org.immregistries.mqe.validator.engine.rules.vaccination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.immregistries.mqe.validator.detection.Detection;
import org.immregistries.mqe.validator.detection.ValidationReport;
import org.immregistries.mqe.validator.engine.ValidationRule;
import org.immregistries.mqe.validator.engine.ValidationRuleResult;
import org.immregistries.mqe.vxu.MqeMessageReceived;
import org.immregistries.mqe.vxu.MqeVaccination;

public class VaccinationAdministeredAmtIsValid extends ValidationRule<MqeVaccination> {
	
  @Override
  protected final Class[] getDependencies() {
    return new Class[] {VaccinationIsAdministered.class};
  }

  public VaccinationAdministeredAmtIsValid() {
    ruleDetections.addAll(Arrays.asList(Detection.VaccinationAdministeredAmountIsMissing,
        Detection.VaccinationAdministeredAmountIsValuedAsUnknown,
        Detection.VaccinationAdministeredAmountIsValuedAsZero,
        Detection.VaccinationAdministeredAmountIsInvalid));
  }

  @Override
  protected ValidationRuleResult executeRule(MqeVaccination target, MqeMessageReceived m) {

    List<ValidationReport> issues = new ArrayList<ValidationReport>();
    boolean passed = false;

    String administeredAmount = target.getAmount();

    if (target.isAdministered()) {
      if (this.common.isEmpty(administeredAmount) || "999".equals(administeredAmount)) {
        issues.add(Detection.VaccinationAdministeredAmountIsMissing.build(target));
        issues.add(Detection.VaccinationAdministeredAmountIsValuedAsUnknown.build(target));
      } else {
        try {
          float amount = Float.parseFloat(target.getAmount());
          if (amount == 0) {
            issues.add(Detection.VaccinationAdministeredAmountIsValuedAsZero.build(target));
          } else {
            passed = true;
          }
        } catch (NumberFormatException nfe) {
          issues.add(Detection.VaccinationAdministeredAmountIsInvalid.build(target.getAmount(),
              target));
        }
      }
    }

    return buildResults(issues, passed);
  }
}
package org.immregistries.mqe.validator.engine.rules.nextofkin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.immregistries.mqe.validator.detection.Detection;
import org.immregistries.mqe.validator.detection.ValidationReport;
import org.immregistries.mqe.validator.engine.ValidationRule;
import org.immregistries.mqe.validator.engine.ValidationRuleResult;
import org.immregistries.mqe.validator.engine.rules.patient.PatientIsUnderage;
import org.immregistries.mqe.validator.engine.rules.patient.PatientNameIsValid;
import org.immregistries.mqe.vxu.MqeMessageReceived;
import org.immregistries.mqe.vxu.MqeNextOfKin;
import org.immregistries.mqe.vxu.MqePatient;

public class NextOfKinNameIsNotSameAsPatient extends ValidationRule<MqeNextOfKin> {

  @Override
  protected final Class[] getDependencies() {

    return new Class[] {PatientIsUnderage.class, PatientNameIsValid.class,
        NextOfKinNameIsValid.class};
  }

  public NextOfKinNameIsNotSameAsPatient() {
    this.addRuleDocumentation(Arrays.asList(Detection.PatientGuardianNameIsSameAsUnderagePatient));
  }

  @Override
  protected ValidationRuleResult executeRule(MqeNextOfKin target, MqeMessageReceived m) {

    List<ValidationReport> issues = new ArrayList<ValidationReport>();
    boolean passed = true;

    MqePatient patient = m.getPatient();

    if (target.isResponsibleRelationship() && areEqual(target.getNameLast(), patient.getNameLast())
        && areEqual(target.getNameFirst(), patient.getNameFirst())
        && areEqual(target.getNameMiddle(), patient.getNameMiddle())
        && areEqual(target.getNameSuffix(), patient.getNameSuffix())) {

      issues.add(Detection.PatientGuardianNameIsSameAsUnderagePatient.build(target));
      passed = false;
    }

    return buildResults(issues, passed);
  }

  /**
   * Just a little hack.
   */
  protected boolean areEqual(String one, String two) {
    return one != null && one.equals(two);
  }

}

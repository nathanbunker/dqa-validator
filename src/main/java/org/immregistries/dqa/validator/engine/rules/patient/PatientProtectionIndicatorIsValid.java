package org.immregistries.dqa.validator.engine.rules.patient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.immregistries.dqa.validator.detection.Detection;
import org.immregistries.dqa.validator.detection.ValidationReport;
import org.immregistries.dqa.validator.engine.ValidationRule;
import org.immregistries.dqa.validator.engine.ValidationRuleResult;
import org.immregistries.dqa.vxu.DqaMessageReceived;
import org.immregistries.dqa.vxu.DqaPatient;
import org.immregistries.dqa.vxu.VxuField;

public class PatientProtectionIndicatorIsValid extends ValidationRule<DqaPatient> {

  public PatientProtectionIndicatorIsValid() {
    ruleDetections.addAll(codr.getDetectionsForField(VxuField.PATIENT_PROTECTION_INDICATOR));
    ruleDetections.addAll(Arrays.asList(Detection.PatientProtectionIndicatorIsValuedAsYes,
        Detection.PatientProtectionIndicatorIsValuedAsNo));
  }

  @Override
  protected ValidationRuleResult executeRule(DqaPatient target, DqaMessageReceived m) {
    List<ValidationReport> issues = new ArrayList<>();
    boolean passed;

    String protectionCode = target.getProtectionCode();

    // TODO: QUESTION: Should "Missing" be handled here, or in the codeHandler?
    // I'm thinking... here. No compelling reason yet. just a feeling.
    // TODO: need to create test for protection indicator

    issues.addAll(this.codr.handleCode(protectionCode, VxuField.PATIENT_PROTECTION_INDICATOR,
        target));

    if ("Y".equals(protectionCode)) {
      issues.add(Detection.PatientProtectionIndicatorIsValuedAsYes.build((protectionCode), target));
    } else if ("N".equals(protectionCode)) {
      issues.add(Detection.PatientProtectionIndicatorIsValuedAsNo.build((protectionCode), target));
    }

    passed = (issues.size() == 0);

    return buildResults(issues, passed);
  }

}

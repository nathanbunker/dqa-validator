package org.immregistries.mqe.validator.engine.rules.patient;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.immregistries.mqe.validator.detection.Detection;
import org.immregistries.mqe.validator.detection.ImplementationDetail;
import org.immregistries.mqe.validator.detection.ValidationReport;
import org.immregistries.mqe.validator.engine.ValidationRule;
import org.immregistries.mqe.validator.engine.ValidationRuleResult;
import org.immregistries.mqe.vxu.MqeMessageReceived;
import org.immregistries.mqe.vxu.MqePatient;

public class PatientHasResponsibleParty extends ValidationRule<MqePatient> {

  @Override
  protected final Class[] getDependencies() {
    return new Class[] {PatientExists.class};
  }

  public PatientHasResponsibleParty() {
    this.addRuleDetection(Detection.PatientGuardianResponsiblePartyIsMissing);
    ImplementationDetail id =
        this.addRuleDetection(Detection.PatientGuardianResponsiblePartyIsMissing);
    id.setImplementationDescription(
        "Responsible party missing and/or responsible party relationship code missing.");
    id.setHowToFix("The guardian/parent is missing. Please review the patient guardian/parent and ensure that a name is "
        + "entered or please ask your software vendor to ensure that the name of the guardian/parent responsible for the "
        + "patient is sent properly in the message. ");
    id.setWhyToFix("The name of the guardian/parent can be used for patient matching and as a contact for reminder/recall activities.  ");
  }

  @Override
  protected ValidationRuleResult executeRule(MqePatient target, MqeMessageReceived mr) {
    List<ValidationReport> issues = new ArrayList<ValidationReport>();
    boolean passed = true;

    if (target.getResponsibleParty() == null
        || StringUtils.isBlank(target.getResponsibleParty().getRelationshipCode())) {
      issues.add(Detection.PatientGuardianResponsiblePartyIsMissing.build(target));
      passed = false;
    }

    return buildResults(issues, passed);
  }
}

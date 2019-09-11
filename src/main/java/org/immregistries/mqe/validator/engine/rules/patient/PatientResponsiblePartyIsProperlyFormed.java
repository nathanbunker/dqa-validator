package org.immregistries.mqe.validator.engine.rules.patient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.immregistries.mqe.validator.address.SmartyStreetResponse;
import org.immregistries.mqe.validator.detection.Detection;
import org.immregistries.mqe.validator.detection.ImplementationDetail;
import org.immregistries.mqe.validator.detection.ValidationReport;
import org.immregistries.mqe.validator.engine.ValidationRule;
import org.immregistries.mqe.validator.engine.ValidationRuleResult;
import org.immregistries.mqe.vxu.MqeMessageReceived;
import org.immregistries.mqe.vxu.MqeNextOfKin;
import org.immregistries.mqe.vxu.MqePatient;

public class PatientResponsiblePartyIsProperlyFormed extends ValidationRule<MqePatient> {

  @Override
  protected final Class[] getDependencies() {
    return new Class[] {PatientHasResponsibleParty.class};
  }

  public PatientResponsiblePartyIsProperlyFormed() {
    this.addRuleDetections(Arrays.asList(Detection.PatientGuardianAddressStateIsMissing,
        Detection.PatientGuardianAddressCityIsMissing, Detection.PatientGuardianAddressZipIsMissing,
        Detection.PatientGuardianNameFirstIsMissing, Detection.PatientGuardianNameLastIsMissing,
        Detection.PatientGuardianNameIsSameAsUnderagePatient,
        Detection.PatientGuardianPhoneIsMissing));

    {
      ImplementationDetail id =
          this.addRuleDetection(Detection.PatientGuardianAddressStateIsMissing);
      // TODO Complete ImplementationDescription
      id.setImplementationDescription("");
      // TODO Complete HowToFix
      id.setHowToFix("");
      // TODO Complete WhyToFix
      id.setWhyToFix("");
    }
    {
      ImplementationDetail id =
          this.addRuleDetection(Detection.PatientGuardianAddressCityIsMissing);
      // TODO Complete ImplementationDescription
      id.setImplementationDescription("");
      // TODO Complete HowToFix
      id.setHowToFix("");
      // TODO Complete WhyToFix
      id.setWhyToFix("");
    }
    {
      ImplementationDetail id =
          this.addRuleDetection(Detection.PatientGuardianAddressZipIsMissing);
      // TODO Complete ImplementationDescription
      id.setImplementationDescription("");
      // TODO Complete HowToFix
      id.setHowToFix("");
      // TODO Complete WhyToFix
      id.setWhyToFix("");
    }
    {
      ImplementationDetail id =
          this.addRuleDetection(Detection.PatientGuardianNameFirstIsMissing);
      // TODO Complete ImplementationDescription
      id.setImplementationDescription("");
      // TODO Complete HowToFix
      id.setHowToFix("");
      // TODO Complete WhyToFix
      id.setWhyToFix("");
    }
    {
      ImplementationDetail id =
          this.addRuleDetection(Detection.PatientGuardianNameLastIsMissing);
      // TODO Complete ImplementationDescription
      id.setImplementationDescription("");
      // TODO Complete HowToFix
      id.setHowToFix("");
      // TODO Complete WhyToFix
      id.setWhyToFix("");
    }

    {
      ImplementationDetail id =
          this.addRuleDetection(Detection.PatientGuardianNameIsSameAsUnderagePatient);
      id.setImplementationDescription(
          "Patient first and last name match the guardian first and last name.");
    }
    {
      ImplementationDetail id =
          this.addRuleDetection(Detection.PatientGuardianPhoneIsMissing);
      // TODO Complete ImplementationDescription
      id.setImplementationDescription("");
      // TODO Complete HowToFix
      id.setHowToFix("");
      // TODO Complete WhyToFix
      id.setWhyToFix("");
    }


    if (props.isAddressCleanserEnabled()) {
      this.addRuleDetection(Detection.PatientGuardianAddressIsInvalid);
      ImplementationDetail id = this.addRuleDetection(Detection.PatientGuardianAddressIsInvalid);
      id.setImplementationDescription(
          "Patient Guardian Address is invalid according to Smarty Streets.");
      id.setHowToFix("");
      // TODO Complete WhyToFix
      id.setWhyToFix("");
    }
  }

  @Override
  protected ValidationRuleResult executeRule(MqePatient target, MqeMessageReceived mr) {
    List<ValidationReport> issues = new ArrayList<ValidationReport>();
    boolean passed = true;

    if (target.getResponsibleParty() != null) {
      MqeNextOfKin guardian = target.getResponsibleParty();

      String tFirst = guardian.getNameFirst();
      String tLast = guardian.getNameLast();
      String pFirst = target.getNameFirst();
      String pLast = target.getNameLast();

      if (props.isAddressCleanserEnabled()) {
        if (guardian.getAddress() != null && !guardian.getAddress().isClean()) {
          ValidationReport r = Detection.PatientGuardianAddressIsInvalid.build(target);
          List<SmartyStreetResponse> rList =
              SmartyStreetResponse.codesFromDpv(guardian.getAddress().getCleansingResultCode());
          if (rList.size() > 0) {
            StringBuilder b = new StringBuilder(":");
            for (SmartyStreetResponse rz : rList) {
              b.append(" ").append(rz.title);
            }
            r.setAdditionalMessage(b.toString());
          }
          issues.add(r);
        }
      }
      if (guardian.getAddress() != null) {
        if (this.common.isEmpty(guardian.getAddress().getStateCode())) {
          issues.add(Detection.PatientGuardianAddressStateIsMissing.build(target));
        }
        if (this.common.isEmpty(guardian.getAddress().getCity())) {
          issues.add(Detection.PatientGuardianAddressCityIsMissing.build(target));
        }
        if (this.common.isEmpty(guardian.getAddress().getZip())) {
          issues.add(Detection.PatientGuardianAddressZipIsMissing.build(target));
        }
      } else {
        issues.add(Detection.PatientGuardianAddressIsMissing.build(target));
      }

      if (this.common.isEmpty(tFirst)) {
        issues.add(Detection.PatientGuardianNameFirstIsMissing.build(target));
      }
      if (this.common.isEmpty(tLast)) {
        issues.add(Detection.PatientGuardianNameLastIsMissing.build(target));
      }

      if (StringUtils.isNotBlank(pFirst) && StringUtils.isNotBlank(pLast) && pFirst.equals(tFirst)
          && pLast.equals(tLast)) {
        issues.add(Detection.PatientGuardianNameIsSameAsUnderagePatient.build(target));
      }

      if (this.common.isEmpty(guardian.getPhoneNumber())) {
        issues.add(Detection.PatientGuardianPhoneIsMissing.build(target));
      }

      passed = (issues.size() == 0);

    }

    return buildResults(issues, passed);
  }

}

package org.immregistries.mqe.validator.engine.rules.patient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.immregistries.mqe.validator.detection.Detection;
import org.immregistries.mqe.validator.detection.ImplementationDetail;
import org.immregistries.mqe.validator.detection.ValidationReport;
import org.immregistries.mqe.validator.engine.ValidationRule;
import org.immregistries.mqe.validator.engine.ValidationRuleResult;
import org.immregistries.mqe.vxu.MqeMessageReceived;
import org.immregistries.mqe.vxu.MqePatient;

public class PatientBirthPlaceIsValid extends ValidationRule<MqePatient> {

  @Override
  protected final Class[] getDependencies() {
    return new Class[] {PatientExists.class,};
  }

  public PatientBirthPlaceIsValid() {
    this.addRuleDetections(Arrays.asList(Detection.PatientBirthPlaceIsMissing));
    {
      ImplementationDetail id = this.addRuleDetection(Detection.PatientBirthPlaceIsMissing);
      // TODO Complete ImplementationDescription
      id.setImplementationDescription("");
      // TODO Complete HowToFix
      id.setHowToFix("");
      // TODO Complete WhyToFix
      id.setWhyToFix("");
    }
  }

  @Override
  protected ValidationRuleResult executeRule(MqePatient target, MqeMessageReceived mr) {

    List<ValidationReport> issues = new ArrayList<ValidationReport>();
    boolean passed = true;

    String place = target.getBirthPlace();

    if (this.common.isEmpty(place)) {
      issues.add(Detection.PatientBirthPlaceIsMissing.build(target));
    }

    return buildResults(issues, passed);
  }


}

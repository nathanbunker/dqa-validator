package org.immregistries.mqe.validator.engine.rules.vaccination;

import java.util.ArrayList;
import java.util.List;
import org.immregistries.mqe.validator.detection.Detection;
import org.immregistries.mqe.validator.detection.ImplementationDetail;
import org.immregistries.mqe.validator.detection.ValidationReport;
import org.immregistries.mqe.validator.engine.ValidationRule;
import org.immregistries.mqe.validator.engine.ValidationRuleResult;
import org.immregistries.mqe.vxu.MqeMessageReceived;
import org.immregistries.mqe.vxu.MqeVaccination;
import org.immregistries.mqe.vxu.VxuField;
import org.immregistries.mqe.vxu.hl7.Observation;

/**
 * Created by Allison on 5/9/2017.
 */
public class ObservationValueTypeIsValid extends ValidationRule<MqeVaccination> {

  public ObservationValueTypeIsValid() {
    {
      ImplementationDetail id =
          this.addRuleDetection(Detection.ObservationValueTypeIsDeprecated);
      id.setHowToFix("The observation value type is recognized but should not longer be used. Please contact your software vendor and request that they review the observation value types that are being sent to ensure that the latest and best types are being sent. ");
      id.setWhyToFix("The observation value type allows the transmission of additional information that is helpful to understand an immunization event or the patient medical history. Sending the wrong observation value type might cause the IIS to not recognize or understand medically relevant information.");
    }
    {
      ImplementationDetail id =
          this.addRuleDetection(Detection.ObservationValueTypeIsInvalid);
      id.setHowToFix("The observation value type should not be used. Please contact your software vendor and request that they review the observation value types that are being sent to ensure that the latest and best types are being sent. ");
      id.setWhyToFix("The observation value type allows the transmission of additional information that is helpful to understand an immunization event or the patient medical history. Sending the wrong observation value type might cause the IIS to not recognize or understand medically relevant information.");
    }
    {
      ImplementationDetail id =
          this.addRuleDetection(Detection.ObservationValueTypeIsMissing);
      id.setHowToFix("The observation value type must be indicated. Please contact your software vendor and request that they ensure that the format of the observation structure is meeting standards.");
      id.setWhyToFix("The observation value type allows the transmission of additional information that is helpful to understand an immunization event or the patient medical history. Not sending the observation value type means the IIS will not recieve this information.");
    }
    {
      ImplementationDetail id =
          this.addRuleDetection(Detection.ObservationValueTypeIsUnrecognized);
      id.setImplementationDescription("Code submitted is not recognized as either valid or invalid because it is unknown to this system. ");
      id.setHowToFix("The observation value type is not recognized. Please contact your software vendor and request that they review the observation value types that are being sent to ensure that the latest and best types are being sent. ");
      id.setWhyToFix("The observation value type allows the transmission of additional information that is helpful to understand an immunization event or the patient medical history. Sending the wrong observation value type might cause the IIS to not recognize or understand medically relevant information.");
    }
  }

  @Override
  protected ValidationRuleResult executeRule(MqeVaccination target, MqeMessageReceived m) {
    List<ValidationReport> issues = new ArrayList<>();
    boolean passed;

    for (Observation o : target.getObservations()) {
      issues.addAll(codr.handleCodeOrMissing(o.getValueTypeCode(), VxuField.OBSERVATION_VALUE_TYPE, target));
    }

    passed = issues.size() == 0;

    return buildResults(issues, passed);
  }
}

package org.immregistries.mqe.validator.engine.rules.vaccination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.immregistries.codebase.client.generated.Code;
import org.immregistries.codebase.client.reference.CodesetType;
import org.immregistries.codebase.client.reference.CvxConceptType;
import org.immregistries.mqe.validator.detection.Detection;
import org.immregistries.mqe.validator.detection.ImplementationDetail;
import org.immregistries.mqe.validator.detection.ValidationReport;
import org.immregistries.mqe.validator.engine.ValidationRule;
import org.immregistries.mqe.validator.engine.ValidationRuleResult;
import org.immregistries.mqe.vxu.MqeMessageReceived;
import org.immregistries.mqe.vxu.MqeVaccination;

public class VaccinationIsForeign extends ValidationRule<MqeVaccination> {

  public VaccinationIsForeign() {
    this.addRuleDetections(Arrays.asList(Detection.VaccinationAdminCodeIsForeign,
        Detection.VaccinationHistoricalCodeIsForeign));
    {
      ImplementationDetail id = this.addRuleDetection(Detection.VaccinationAdminCodeIsForeign);
      id.setImplementationDescription("Administered Vaccination has a foreign CVX vaccine code.");
      // TODO Complete HowToFix
      id.setHowToFix("");
      // TODO Complete WhyToFix
      id.setWhyToFix("");    }
    {
      ImplementationDetail id = this.addRuleDetection(Detection.VaccinationHistoricalCodeIsForeign);
      id.setImplementationDescription("Historical Vaccination has a foreign CVX vaccine code.");
      // TODO Complete HowToFix
      id.setHowToFix("");
      // TODO Complete WhyToFix
      id.setWhyToFix("");    }

  }

  @Override
  protected ValidationRuleResult executeRule(MqeVaccination target, MqeMessageReceived m) {

    List<ValidationReport> issues = new ArrayList<ValidationReport>();
    boolean passed = false;

    String cvxCode = target.getAdminCvxCode();
    boolean administered = target.isAdministered();

    Code vaccineCode = this.repo.getCodeFromValue(cvxCode, CodesetType.VACCINATION_CVX_CODE);
    if (vaccineCode != null) {
      CvxConceptType concept = CvxConceptType.getBy(vaccineCode.getConceptType());

      if (CvxConceptType.FOREIGN_VACCINE == concept) {
        if (administered) {
          issues.add(Detection.VaccinationAdminCodeIsForeign.build(target));
        } else {
          issues.add(Detection.VaccinationHistoricalCodeIsForeign.build(target));
        }
      }
      passed = true;
    }

    return buildResults(issues, passed);

  }
}

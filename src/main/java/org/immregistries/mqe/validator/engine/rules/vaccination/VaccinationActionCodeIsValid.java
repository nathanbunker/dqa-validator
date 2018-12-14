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
import org.immregistries.mqe.vxu.VxuField;

public class VaccinationActionCodeIsValid extends ValidationRule<MqeVaccination> {


  public VaccinationActionCodeIsValid() {
    this.addRuleDocumentation(codr.getDetectionsForField(VxuField.VACCINATION_ACTION_CODE));
  }

  @Override
  protected ValidationRuleResult executeRule(MqeVaccination target, MqeMessageReceived m) {

    List<ValidationReport> issues = new ArrayList<ValidationReport>();
    boolean passed = true;

    issues
        .addAll(this.codr.handleCode(target.getAction(), VxuField.VACCINATION_ACTION_CODE, target));

    if (issues.size() > 0) {
      passed = false;
    } else {
      String actionCode = target.getActionCode();
      
	     if (this.common.isEmpty(actionCode)) {
	    	 issues.add(Detection.VaccinationActionCodeIsMissing.build(target));
	    	 passed = false;
	     } else {
		      if (target.isActionAdd()) {
		        issues.add(Detection.VaccinationActionCodeIsValuedAsAdd.build((actionCode), target));
		        issues
		            .add(Detection.VaccinationActionCodeIsValuedAsAddOrUpdate.build((actionCode), target));
		      } else if (target.isActionUpdate()) {
		        issues.add(Detection.VaccinationActionCodeIsValuedAsUpdate.build((actionCode), target));
		        issues
		            .add(Detection.VaccinationActionCodeIsValuedAsAddOrUpdate.build((actionCode), target));
		      } else if (target.isActionDelete()) {
		        issues.add(Detection.VaccinationActionCodeIsValuedAsDelete.build((actionCode), target));
		      } else {
		    	  issues.add(Detection.VaccinationActionCodeIsInvalid.build(target));
		    	  passed = false;
		      }
	     }
    }

    return buildResults(issues, passed);

  }
}

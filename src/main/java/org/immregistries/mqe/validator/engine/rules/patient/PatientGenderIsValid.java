package org.immregistries.mqe.validator.engine.rules.patient;

import java.util.ArrayList;
import java.util.List;

import org.immregistries.mqe.validator.detection.Detection;
import org.immregistries.mqe.validator.detection.ValidationReport;
import org.immregistries.mqe.validator.engine.ValidationRule;
import org.immregistries.mqe.validator.engine.ValidationRuleResult;
import org.immregistries.mqe.vxu.MqeMessageReceived;
import org.immregistries.mqe.vxu.MqePatient;
import org.immregistries.mqe.vxu.VxuField;

public class PatientGenderIsValid extends ValidationRule<MqePatient> {

  @Override
  protected final Class[] getDependencies() {
    return new Class[] {PatientExists.class};
  }

  public PatientGenderIsValid() {
	  this.ruleDetections.addAll(codr.getDetectionsForField(VxuField.PATIENT_GENDER));
  }

  @Override
  protected ValidationRuleResult executeRule(MqePatient target, MqeMessageReceived m) {

	    List<ValidationReport> issues = new ArrayList<ValidationReport>();
	    boolean passed = true;
	    String patientGenderString = target.getSex();
	    
	    if (this.common.isEmpty(patientGenderString)) {
	        issues.add(Detection.PatientGenderIsMissing.build((patientGenderString), target));
	        passed = false;
	    }
	    else {
	    	issues.addAll(codr.handleCode(patientGenderString, VxuField.PATIENT_GENDER, target));
	    	passed = issues.isEmpty();
	    }
	    
	    return buildResults(issues, passed);
  }
}
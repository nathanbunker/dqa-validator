package org.immregistries.mqe.validator;

import java.util.List;
import java.util.Map;
import org.immregistries.mqe.hl7util.SeverityLevel;
import org.immregistries.mqe.validator.detection.Detection;
import org.immregistries.mqe.validator.detection.ValidationReport;

/**
 * The intention of this class is to operate on the list of Issues that are produced from this
 * process.
 *
 * This will handle things like the concept of a date cliff where we don't really care about dates
 * after a certain point, so we downgrade some of the issue levels for vaccines administered after
 * that date.
 *
 * This class will also handle custom issue levels at the IIS level, or at the profile level.
 *
 * TODO: come up with some notation for specifying the level for an issue.
 *
 * @author Josh Hull
 */
public class PostProcessor {

  public List<ValidationReport> transformList(List<ValidationReport> listIn,
      Map<Detection, SeverityLevel> overrides) {
    return null;
  }
}

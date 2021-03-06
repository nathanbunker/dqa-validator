package org.immregistries.mqe.validator.engine.rules.vaccination;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.immregistries.mqe.validator.detection.Detection;
import org.immregistries.mqe.validator.engine.ValidationRuleResult;
import org.immregistries.mqe.vxu.MqeMessageHeader;
import org.immregistries.mqe.vxu.MqeMessageReceived;
import org.immregistries.mqe.vxu.MqeVaccination;
import org.immregistries.mqe.vxu.hl7.Observation;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Allison on 5/9/2017.
 */
public class ObservationValueTypeIsValidTester {

  private ObservationValueTypeIsValid rule = new ObservationValueTypeIsValid();

  // Parts required for the test
  private MqeMessageHeader mh = new MqeMessageHeader();
  private MqeMessageReceived mr = new MqeMessageReceived();
  private MqeVaccination v = new MqeVaccination();
  private Observation o = new Observation();

  private static final Logger logger = LoggerFactory
      .getLogger(ObservationValueTypeIsValidTester.class);

  /**
   * Sets up the objects needed for the test.
   */
  @Before
  public void setUpTheObjects() {
    o.setValueTypeCode("CE"); // CE = Coded Entry

    mh.setMessageDate(new Date());
    mr.setMessageHeader(mh);
    setObservationsAndVaccinations();
  }

  /**
   * Test the basic rule with a valid type code.
   * (should be true)
   */
  @Test
  public void testRule() {
    ValidationRuleResult r = rule.executeRule(v, mr);
    logger.info(r.getValidationDetections().toString());
    assertTrue(r.isRulePassed());
  }

  @Test
  public void testRuleMissingType() {
    o.setValueTypeCode(null);
    setObservationsAndVaccinations();

    ValidationRuleResult r = rule.executeRule(v, mr);
    logger.info(r.getValidationDetections().toString());
    assertEquals(1, r.getValidationDetections().size());
    assertEquals(Detection.ObservationValueTypeIsMissing,
        r.getValidationDetections().get(0).getDetection());
    
    o.setValueTypeCode("");
    setObservationsAndVaccinations();

    r = rule.executeRule(v, mr);
    logger.info(r.getValidationDetections().toString());
    assertEquals(1, r.getValidationDetections().size());
    assertEquals(Detection.ObservationValueTypeIsMissing,
        r.getValidationDetections().get(0).getDetection());
  }

  /**
   * Test the rule with an unrecognized type code.
   */
  @Test
  public void testRuleUnrecognizedType() {
    o.setValueTypeCode("abc");
    setObservationsAndVaccinations();

    ValidationRuleResult r = rule.executeRule(v, mr);
    logger.info(r.getValidationDetections().toString());
    assertEquals(1, r.getValidationDetections().size());
    assertEquals(Detection.ObservationValueTypeIsUnrecognized,
        r.getValidationDetections().get(0).getDetection());
  }
  
  @Test
  public void testRuleIgnoredType() {
    o.setValueTypeCode("CF");
    setObservationsAndVaccinations();

    ValidationRuleResult r = rule.executeRule(v, mr);
    logger.info(r.getValidationDetections().toString());
    assertEquals(1, r.getValidationDetections().size());
    assertEquals(Detection.ObservationValueTypeIsIgnored,
        r.getValidationDetections().get(0).getDetection());
  }

  /**
   * Set the observation/vaccination we're currently looking at.
   */
  private void setObservationsAndVaccinations() {
    List<Observation> obs = new ArrayList<>();
    obs.add(o);
    v.setObservations(obs);
  }
}

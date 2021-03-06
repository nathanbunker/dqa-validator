package org.immregistries.mqe.validator.engine.rules.patient;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Date;
import org.immregistries.mqe.validator.engine.ValidationRuleResult;
import org.immregistries.mqe.vxu.MqeMessageHeader;
import org.immregistries.mqe.vxu.MqeMessageReceived;
import org.immregistries.mqe.vxu.MqePatient;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Allison on 5/4/2017.
 */
public class PatientDeathIndicatorIsValidTester {

  private PatientDeathIndicatorIsValid rule = new PatientDeathIndicatorIsValid();

  // Parts required for the test
  private MqeMessageHeader mh = new MqeMessageHeader();
  private MqeMessageReceived mr = new MqeMessageReceived();
  private MqePatient p = new MqePatient();

  /**
   * Sets up the objects for the test.
   */
  @Before
  public void setUpTheObjects() {
    p.setDeathIndicator("N");

    mh.setMessageDate(new Date());
    mr.setMessageHeader(mh);
    mr.setPatient(p);
  }

  /**
   * Test the basic rule, with a valid death indicator (N indicator + no death date)
   * (should be true)
   */
  @Test
  public void testRule() {
    ValidationRuleResult r = rule.executeRule(p, mr);
    assertTrue(r.isRulePassed());
  }

  /**
   * Test the basic rule, with a valid death indicator (Y indicator + death date)
   * (should be true)
   */
  @Test
  public void testRuleIfDead() {
    p.setDeathIndicator("Y");
    p.setDeathDate(new Date());

    ValidationRuleResult r = rule.executeRule(p, mr);
    assertTrue(r.isRulePassed());
  }

  /**
   * Test when the death indicator isn't set but there's a death date anyway
   * (should be false)
   * <p>
   * NOTE: if the death indicator IS set but the death date is blank, that's considered
   * a missing death date and is handled by the death date code
   */
  @Test
  public void testRuleIfNoDeathDate() {
    p.setDeathDateString("19000101");

    ValidationRuleResult r = rule.executeRule(p, mr);
    assertFalse(r.isRulePassed());
  }

  /**
   * Test null death indicator
   * (should be false??)
   */
  @Test
  public void testRuleNullIndicator() {
    p.setDeathIndicator(null);
    p.setDeathDateString("19000101");
    ValidationRuleResult r = rule.executeRule(p, mr);
    assertFalse(r.isRulePassed());
  }

  /**
   * Test unrecognized death indicator (something other than the standard Y or N)
   * (should be true--the indicator is really "is Y" vs. "is not Y")
   */
  @Test
  public void testRuleUnrecognizedIndicator() {
    p.setDeathIndicator("sdhjf");

    ValidationRuleResult r = rule.executeRule(p, mr);
    assertTrue(r.isRulePassed());
  }
}

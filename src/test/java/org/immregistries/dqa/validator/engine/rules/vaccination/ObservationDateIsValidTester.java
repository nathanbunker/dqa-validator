package org.immregistries.dqa.validator.engine.rules.vaccination;

import org.immregistries.dqa.validator.engine.ValidationRuleResult;
import org.immregistries.dqa.validator.issue.Detection;
import org.immregistries.dqa.vxu.DqaMessageHeader;
import org.immregistries.dqa.vxu.DqaMessageReceived;
import org.immregistries.dqa.vxu.DqaVaccination;
import org.immregistries.dqa.vxu.hl7.Observation;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Allison on 5/9/2017.
 */
public class ObservationDateIsValidTester {
    private ObservationDateIsValid rule = new ObservationDateIsValid();

    // Parts required for the test
    private DqaMessageHeader mh = new DqaMessageHeader();
    private DqaMessageReceived mr = new DqaMessageReceived();
    private DqaVaccination v = new DqaVaccination();
    private Observation o = new Observation();

    private static final Logger logger = LoggerFactory.getLogger(ObservationDateIsValidTester.class);
    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    /**
     * Sets up the objects needed for the test. Set the observation date to yesterday's date.
     */
    @Before
    public void setUpTheObjects() {
        o.setObservationDateString(format.format(addDays(new Date(), -1)));

        mh.setMessageDate(new Date());
        mr.setMessageHeader(mh);
        setObservationsAndVaccinations();
    }

    /**
     * Test the basic rule with a valid date.
     * (should be true)
     */
    @Test
    public void testRule() {
        ValidationRuleResult r = rule.executeRule(v, mr);
        logger.info(r.getIssues().toString());
        assertTrue(r.isRulePassed());
    }

    /**
     * Test the rule with a null date
     * (should be false)
     */
    @Test
    public void testRuleNullDate() {
        o.setObservationDateString(null);

        ValidationRuleResult r = rule.executeRule(v, mr);
        logger.info(r.getIssues().toString());
        assertTrue(1 == r.getIssues().size()
                && Detection.ObservationDateTimeOfObservationIsMissing == r.getIssues().get(0).getIssue());
    }

    /**
     * Test the rule with a null date
     * (should be false)
     */
    @Test
    public void testRuleEmptyDate() {
        o.setObservationDateString("");

        ValidationRuleResult r = rule.executeRule(v, mr);
        logger.info(r.getIssues().toString());
        assertTrue(1 == r.getIssues().size()
                && Detection.ObservationDateTimeOfObservationIsMissing == r.getIssues().get(0).getIssue());
    }

    /**
     * Set the observation/vaccination we're currently looking at.
     */
    private void setObservationsAndVaccinations() {
        List<Observation> obs = new ArrayList<>();
        obs.add(o);
        v.setObservations(obs);
    }

    /**
     * Conveniently add or subtract days from a date.
     *
     * @param d         date we're currently messing with
     * @param daysToAdd number of days to add (can be positive or negative)
     * @return original date, plus or minus whatever we wanted to add to it
     */
    private Date addDays(Date d, Integer daysToAdd) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DAY_OF_YEAR, daysToAdd);
        return cal.getTime();
    }
}
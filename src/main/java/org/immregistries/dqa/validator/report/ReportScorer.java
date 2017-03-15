package org.immregistries.dqa.validator.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.immregistries.dqa.validator.DqaMessageService;
import org.immregistries.dqa.validator.DqaMessageServiceResponse;
import org.immregistries.dqa.validator.issue.IssueObject;
import org.immregistries.dqa.validator.issue.IssueType;
import org.immregistries.dqa.validator.issue.MessageAttribute;
import org.immregistries.dqa.validator.issue.VxuField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ReportScorer {
	INSTANCE;
	private static final Logger logger = LoggerFactory.getLogger(ReportScorer.class);
	
	private ReportDefinitionBuilder defbuilder = ReportDefinitionBuilder.INSTANCE;
	private MessageResponseEvaluator eval = MessageResponseEvaluator.INSTANCE;
    private DqaMessageService vlad = DqaMessageService.INSTANCE;
    
	public VxuScoredReport getDefaultReportForMessage(String vxuText) {
		ReportDefinition defaultDef = defbuilder.getDeafult();
		DqaMessageServiceResponse response = vlad.processMessage(vxuText);
		DqaMessageMetrics msgMetrics = eval.toMetrics(response.getValidationResults());
		return getScoredReport(defaultDef, msgMetrics);
	}
	
	public VxuScoredReport getScoredReport(ReportDefinition def, DqaMessageMetrics measures) {
		VxuScoredReport report = new VxuScoredReport();
		List<ReportCompletenessSectionDefinition> sdList = def.getQualitySections();
		for (ReportCompletenessSectionDefinition sd : sdList) {
			//Score the section
			VxuCompletenessSectionScore scoredSection = evaluateCompletenessSection(sd, measures);
			//Add it to the report. 
			report.getScoreGroups().add(scoredSection);
			//append scored section to the report score. 
			report.appendScoreToReportScore(scoredSection.getSectionScore());
		}
		
		return report;
	}
	
	protected VxuCompletenessSectionScore evaluateCompletenessSection(ReportCompletenessSectionDefinition section, DqaMessageMetrics measures) {
		VxuCompletenessSectionScore reportSection = new VxuCompletenessSectionScore();
		reportSection.setLabel(section.getLabel());
		IssueObject sectionObject = section.getSectionObject();
		Integer denominator = measures.getObjectCounts().get(sectionObject);
		
		reportSection.setObjectCount(denominator);
		
		List<DqaReportFieldDefinition> fields = section.getReportFields();
		
		for (DqaReportFieldDefinition d : fields) {
			VxuFieldScore fieldScore = scoreField(d, measures.getAttributeCounts(), denominator);
			reportSection.appendScoresToSectionScore(fieldScore.getFieldScore());
			reportSection.getScores().add(fieldScore);
		}
		
		return reportSection;
	}
	
	protected VxuFieldScore scoreField(DqaReportFieldDefinition fieldDef, Map<MessageAttribute, Integer> evaluated, int expectedFieldCount) {
		VxuFieldScore fieldScore = new VxuFieldScore();
		fieldScore.setReportFieldDefinition(fieldDef);
		fieldScore.getFieldScore().setPotential(fieldDef.getWeight());
		fieldScore.setExpectedCount(expectedFieldCount);
		
		Map<IssueType, Integer> counts = getIssueCounts(fieldDef, evaluated);
		List<FieldIssueScore> issueScores = scoreIssues(fieldDef, counts, expectedFieldCount);
		
		if (fieldDef.isCheckForPresent()) {
			//score for how many were present...
			FieldIssueScore issueScore = scorePresent(counts, expectedFieldCount, fieldDef.getWeight());
			int numMissing = issueScore.getIssueCount();
			int numPresent = expectedFieldCount - numMissing;
			logger.info("checking 'PRESENT' for " + fieldDef.getLabel() + " numMissing: " + issueScore.getIssueCount());
			fieldScore.setPresentCount(numPresent);
			if (numMissing > 0) {
				issueScores.add(issueScore);
			}
		}
		
		fieldScore.getIssueScores().addAll(issueScores);
		int issueDemerits = getIssueDemeritTotal(issueScores, fieldDef.getWeight());
		fieldScore.getFieldScore().setScored(fieldDef.getWeight() - issueDemerits);
		
		return fieldScore;
	}
	
	/**
	 * The issue demerit can never be more than the total field score, 
	 * and never less than zero. 
	 * @param issues
	 * @param totalScore
	 * @return
	 */
	protected int getIssueDemeritTotal(List<FieldIssueScore> issues, int maxDemerits) {
		int scoreDemerit = 0;
		for (FieldIssueScore fieldScore : issues) {
			logger.info("getIssueDemeritTotal adding demerit: " + fieldScore.getIssueDemerit() );
			scoreDemerit = scoreDemerit + fieldScore.getIssueDemerit();
		}
		
		if (scoreDemerit > maxDemerits) {
			scoreDemerit = maxDemerits;
		}
		
		if (scoreDemerit < 0) {
			scoreDemerit = 0;
		}
		
		return scoreDemerit;
	}
	
	private final ReportIssue missing = new ReportIssue();
	
	{ 
		missing.setType(IssueType.MISSING);
		missing.setMultiplierPercent(100);
	}
	
	protected List<FieldIssueScore> scoreIssues(DqaReportFieldDefinition definition, Map<IssueType, Integer> issueCounts, Integer fieldCount) {
		List<FieldIssueScore> scores = new ArrayList<>();
		//here, we're doing the math. we've got the issues.  we've got the definition. Now it's time for the business. 
		int baseScore = definition.getWeight();
		
		for (ReportIssue issue : definition.getIssues()) {
			IssueType type = issue.getType();
			Integer issueCount = issueCounts.get(type);
			logger.info("scoreField " + definition.getField() + " issue type: " + issue.getType() + " issue count: " + issueCount);
			if (issueCount != null && issueCount > 0) {
				FieldIssueScore scored = scoreIssue(issue, issueCount, fieldCount, baseScore);
				scores.add(scored);
			}
		}
		
		
		
		return scores;
	}
	
	protected FieldIssueScore scoreIssue(ReportIssue issue, int issueCount, int fieldCount, int fieldPotentialScore) {
		FieldIssueScore issueScore = new FieldIssueScore();
		issueScore.setIssueType(issue.getType());
		issueScore.setIssueCount(issueCount);
		double demeritPercent = scoreIssuePercent(issue, issueCount, fieldCount);
		issueScore.setIssuePercentDemerit(demeritPercent);
		int scoreDemerit = new Double(fieldPotentialScore * demeritPercent).intValue();
		logger.info("Score demerit: " + scoreDemerit);
		issueScore.setIssueDemerit(scoreDemerit);
		return issueScore;
	}
	
	protected FieldIssueScore scorePresent(Map<IssueType, Integer> fieldIssues, int fieldCount, int fieldPotentialScore) {
		Integer issueCount = fieldIssues.get(missing.getType());
		if (issueCount == null) {
			issueCount = new Integer(0);
		}
		return scoreIssue(missing, issueCount, fieldCount, fieldPotentialScore);
	}
	
	protected double calculateRangedScore(double score, double lower, double upper) {
		
		if (upper == 0 && lower == 1) {
			return score;
		}
		
		//recalculate the root score based on the range. 
		if (score >= upper) {
			return 1;
		}
		
		if (score <= lower) {
			return 0;
		}
		
		//get the range factor: 
		double totalRange = (upper - lower);
		
		//This is the length of the scale.  we need to divide by it to get the ranged value.  
		return (score-lower) / totalRange;
	}
	
	/**
	 * These are generally going to return negative values. 
	 * @param issue
	 * @param count
	 * @param fieldCount
	 * @return
	 */
	protected double scoreIssuePercent(ReportIssue issue, int issueCount, int fieldCount) {
		
		double rangeLower = new Double(issue.getLowerPercentBoundary()) / 100;
		double rangeUpper = new Double(issue.getUpperPercentBoundary()) / 100;
		
		double rootScore  = new Double(issueCount) / new Double(fieldCount);

		double rangedScore = calculateRangedScore(rootScore, rangeLower, rangeUpper);
		
		double multplierPercent = issue.getMultiplierPercent();
		double factor = multplierPercent/100;
		double calculated = (factor * rangedScore);
		
		if (logger.isInfoEnabled()) {
			
			String format= "scoreIssuePercent " +
					" issue[%20s]"+
					" fieldCount[%6d]"+
					" issueCount[%6d]"+
					" factor[%2.2f]" +
					" rootScore[%+2.2f]" +
					" rangeUpper[%2.2f]" +
					" rangeLower[%2.2f]" +
					" rangedScore[%2.2f]" +
					" calculated[%+2.2f]";
			
			String msg = String.format(format, issue.getType(), fieldCount, issueCount, factor, rootScore, rangeUpper, rangeLower, rangedScore, calculated);
			logger.info(msg);
		}
		
		return calculated;
		
	}
	
	protected Map<IssueType, Integer> getIssueCounts(DqaReportFieldDefinition d, Map<MessageAttribute, Integer> measures) {
		
		Map<IssueType, Integer> map = new HashMap<>();
		
		VxuField field = d.getField();
		List<ReportIssue> lookfor = new ArrayList<>();
		lookfor.addAll(d.getIssues());
		if (d.isCheckForPresent()) {
			lookfor.add(missing);
		}
		
		for (ReportIssue thisType : lookfor) {
			IssueType type = thisType.getType();
			MessageAttribute attribute = MessageAttribute.get(field, type);
			Integer countOfIssue = measures.get(attribute);
			logger.info("getIssueCounts field: " + field + " type " + type + " attribute: " + attribute + " count: " + countOfIssue);
			if (countOfIssue != null) {
				map.put(type, countOfIssue);
			} else {
				map.put(type, new Integer(0));
			}
		}
		
		return map;
	}
}
package org.immregistries.dqa.validator.detection;

import org.immregistries.dqa.hl7util.ApplicationErrorCode;
import org.immregistries.dqa.hl7util.builder.AckERRCode;

public enum DetectionType {
  EXCEPTION("exception"),
  BEFORE_BIRTH("is before birth"),
  DEPRECATED("is deprecated"),
  IGNORED("is ignored"),
  IN_FUTURE("is in future"),
  INCOMPLETE("is incomplete"),
  INVALID("is invalid"),
  MISSING("is missing"),
  REPEATED("is repeated"),
  UNRECOGNIZED("is unrecognized"),
  UNEXPECTED("is unexpected"),

  //Valued As types:
  VALUED_AS("is valued as"),
  VALUED_AS_NO("is valued as no"),
  VALUED_AS_YES("is valued as yes"),
  VALUED_AS_ADD("is valued as add"),
  VALUED_AS_ADD_OR_UPDATE("is valued as add or update"),
  VALUED_AS_DELETE("is valued as delete"),
  VALUED_AS_UPDATE("is valued as update"),
  VALUED_AS_FOREIGN("is valued as foreign"),
  VALUED_AS_ZERO("is valued as zero"),
  VALUED_AS_UNKNOWN("is valued as unknown"),
  VALUED_AS_COMPLETED("is valued as completed"),
  VALUED_AS_REFUSED("is valued as refused"),
  VALUED_AS_RESTRICTED("is valued as restricted"),
  VALUED_AS_NOT_ADMINISTERED("is valued as not administered"),
  VALUED_AS_ADMINISTERED("is valued as administered"),
  VALUED_AS_PARTIALLY_ADMINISTERED("is valued as partially administered"),
  VALUED_AS_HISTORICAL("is valued as historical"),


  // These are the issue types that are not widely used...

  OUT_OF_ORDER("out of order"),
  NON_STANDARD("is non-standard"),
  NOT_PRECISE("is not precise"),
  MISSING_TIMEZONE("is missing timezone"),
  UNSUPPORTED("is unsupported"),
  TOO_SHORT("is too short"),
  TOO_LONG("is too long"),
  UNEXPECTEDLY_SHORT("is unexpectedly short"),
  UNEXPECETDLY_LONG("is unexpectedly long"),
  INCORRECT("is incorrect"),
  INCONSISTENT("is inconsistent"),
  OUT_OF_DATE("is out-of-date"),
  VERY_LONG_AGO("is very long ago"),
  AFTER_SUBMISSION("is after submission"),
  UNEXPECTED_FORMAT("is an unexpected format"),
  NOT_USABLE("is not usable")

  // These are the REALLY specific ones

  ,
  UNDERAGE("is underage"),
  DIFFERENT_FROM_PATIENT_ADDRESS("is different from patient address"),
  MAY_BE_AN_INITIAL("may be initial"),
  MAY_INCLUDE_MIDDLE_INITIAL("may include middle initial"),
  HAS_JUNK_NAME("has junk name"),
  MAY_BE_TEST_NAME("may be test name"),
  KNOWN_TEST_NAME("is a known test name"),
  INVALID_PREFIXES("has invalid prefixes"),
  ARE_INCONSISTENT("are inconsistent"),
  CONFLICTS_COMPLETION_STATUS("conflicts completion status"),
  UNEXPECTED_FOR_DATE_ADMINISTERED("is unexpected for date administered"),
  INVALID_FOR_DATE_ADMINISTERED("is invalid for date administered"),
  AFTER_ADMIN_DATE("is after admin date"),
  NOT_SAME_AS_ADMIN_DATE("is not admin date"),
  BEFORE_PUBLISHED_DATE("is before published date"),
  BEFORE_VERSION_DATE("is before version date"),
  NOT_RESPONSIBLE_PARTY("is not responsible party"),
  ADMINISTERED_BUT_APPEARS_TO_HISTORICAL("is administered but appears to historical"),
  HISTORICAL_BUT_APPEARS_TO_BE_ADMINISTERED("is historical but appears to be administered"),
  INVALID_FOR_VACCINE("is invalid for vaccine indicated"),
  INVALID_FOR_BODY_SITE("is invalid for body site indicated"),
  DIFF_FROM_START("is different from start date"),
  REPORTED_LATE("is reported late"),
  ON_LAST_DAY_OF_MONTH("is on last day of month"),
  ON_FIRST_DAY_OF_MONTH("is on first day of month"),
  ON_FIFTEENTH_DAY_OF_MONTH("is on 15th day of month"),
  BEFORE_OR_AFTER_VALID_DATE_FOR_AGE("is before or after when valid for patient age"),
  BEFORE_OR_AFTER_EXPECTED_DATE_FOR_AGE("is before or after when expected for patient age"),
  BEFORE_OR_AFTER_LICENSED_DATE_RANGE("is before or after licensed vaccine range"),
  BEFORE_OR_AFTER_EXPECTED_DATE_RANGE("is before or after expected vaccine usage range"),
  VALUED_BAD_ADDRESS("is valued bad address"),
  AFTER_SYSTEM_ENTRY_DATE("is after system entry date"),
  AFTER_PATIENT_DEATH_DATE("is after patient death date"),
  AFTER_MESSAGE_SUBMITTED("is after message submitted"),
  AFTER_LOT_EXPIRATION("is after lot expiration date"),
  MAY_BE_PREVIOUSLY_REPORTED("may be variation of previously reported codes"),
  NOT_VACCINE("is not vaccine"),
  NOT_SPECIFIC("is not specific"),
  NOT_VALUED_LEGAL("is not valued legal"),
  MAY_BE_TEMPORARY_NEWBORN_NAME("may be temporary newborn name"),
  SAME_AS_UNDERAGE_PATIENT("is same as underage patient"),
  MISSING_AND_MULTIPLE_BIRTH_INDICATED("is missing and multiple birth indicated");

  private String wording;

  DetectionType(String wording) {
    this.wording = wording;
  }

  public String getText() {
    return wording;
  }

  public AckERRCode getAckErrCode() {
    switch (this) {
      case UNRECOGNIZED:
        return AckERRCode.CODE_103_TABLE_VALUE_NOT_FOUND;
      case INVALID:
        return AckERRCode.CODE_102_DATA_TYPE_ERROR;
      case MISSING:
        return AckERRCode.CODE_101_REQUIRED_FIELD_MISSING;
      case EXCEPTION:
        return AckERRCode.CODE_207_APPLICATION_INTERNAL_ERROR;
      default:
        return AckERRCode.CODE_0_MESSAGE_ACCEPTED;
    }
  }

  public ApplicationErrorCode  getApplicationErrorCode() {
    switch (this) {
      case ADMINISTERED_BUT_APPEARS_TO_HISTORICAL:
      case ARE_INCONSISTENT:
      case CONFLICTS_COMPLETION_STATUS:
      case DEPRECATED:
      case DIFFERENT_FROM_PATIENT_ADDRESS:
      case DIFF_FROM_START:
      case BEFORE_OR_AFTER_EXPECTED_DATE_FOR_AGE:
      case BEFORE_OR_AFTER_LICENSED_DATE_RANGE:
      case BEFORE_OR_AFTER_EXPECTED_DATE_RANGE:
      case BEFORE_OR_AFTER_VALID_DATE_FOR_AGE:
        return ApplicationErrorCode.ILLOGICAL_VALUE_ERROR;
      case AFTER_SYSTEM_ENTRY_DATE:
      case AFTER_ADMIN_DATE:
      case AFTER_LOT_EXPIRATION:
      case AFTER_MESSAGE_SUBMITTED:
      case AFTER_PATIENT_DEATH_DATE:
      case AFTER_SUBMISSION:
      case BEFORE_BIRTH:
      case BEFORE_PUBLISHED_DATE:
      case BEFORE_VERSION_DATE:
        return ApplicationErrorCode.ILLOGICAL_DATE_ERROR;
      case EXCEPTION:
      case HAS_JUNK_NAME:
      case HISTORICAL_BUT_APPEARS_TO_BE_ADMINISTERED:
      case IGNORED:
      case INCOMPLETE:
      case INCONSISTENT:
      case INCORRECT:
      case INVALID:
      case INVALID_FOR_BODY_SITE:
      case INVALID_FOR_DATE_ADMINISTERED:
      case INVALID_FOR_VACCINE:
      case INVALID_PREFIXES:
      case IN_FUTURE:
      case KNOWN_TEST_NAME:
      case MAY_BE_AN_INITIAL:
      case MAY_BE_PREVIOUSLY_REPORTED:
      case MAY_BE_TEMPORARY_NEWBORN_NAME:
      case MAY_BE_TEST_NAME:
      case MAY_INCLUDE_MIDDLE_INITIAL:
      case MISSING:
      case MISSING_AND_MULTIPLE_BIRTH_INDICATED:
      case MISSING_TIMEZONE:
      case NON_STANDARD:
      case NOT_PRECISE:
      case NOT_RESPONSIBLE_PARTY:
      case NOT_SAME_AS_ADMIN_DATE:
      case NOT_SPECIFIC:
      case NOT_VACCINE:
      case NOT_VALUED_LEGAL:
      case ON_FIFTEENTH_DAY_OF_MONTH:
      case ON_FIRST_DAY_OF_MONTH:
      case ON_LAST_DAY_OF_MONTH:
      case OUT_OF_DATE:
      case OUT_OF_ORDER:
      case REPEATED:
      case REPORTED_LATE:
      case SAME_AS_UNDERAGE_PATIENT:
      case TOO_LONG:
      case TOO_SHORT:
      case UNDERAGE:
      case UNEXPECETDLY_LONG:
      case UNEXPECTED:
      case UNEXPECTEDLY_SHORT:
      case UNEXPECTED_FORMAT:
      case UNEXPECTED_FOR_DATE_ADMINISTERED:
      case UNRECOGNIZED:
      case UNSUPPORTED:
      case VALUED_AS:
      case VALUED_BAD_ADDRESS:
      case VERY_LONG_AGO:
      default:
        return ApplicationErrorCode.INVALID_VALUE;
    }
  }
}

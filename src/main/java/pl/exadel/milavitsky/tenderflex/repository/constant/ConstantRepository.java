package pl.exadel.milavitsky.tenderflex.repository.constant;

public class ConstantRepository {

    /**
     * Constants common
     */
    public static final String ID = "id";
    public static final String IS_DELETED = "is_deleted";
    public static final String PERCENT = "%";
    public static final String STATUS = "status";
    public static final String CURRENCY = "currency";


    /**
     * Constants for user entity
     */
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String DATE_OF_REGISTRATION = "date_of_registration";
    public static final String ROLE = "role";
    public static final String LAST_LOGIN_DATE = "last_login_date";

    /**
     * Constants for tender entity
     */
    public static final String TYPE_OF_TENDER = "type_of_tender";
    public static final String DESCRIPTION_OF_THE_PROCUREMENT = "description_of_the_procurement";
    public static final String MINIMUM_TENDER_VALUE = "minimum_tender_value";
    public static final String MAXIMUM_TENDER_VALUE = "maximum_tender_value";
    public static final String PUBLICATION_DATE = "publication_date";
    public static final String DEADLINE_FOR_OFFER_SUBMISSION = "deadline_for_offer_submission";
    public static final String DEADLINE_FOR_SIGNING_CONTRACT_SUBMISSION = "deadline_for_signing_contract_submission";
    public static final String CONTRACT = "contract";
    public static final String AWARD_DECISION = "award_decision";
    public static final String REJECT_DECISION = "reject_decision";


    /**
     * Constants for offer entity
     */
    public static final String ID_TENDER = "id_tender";
    public static final String bid_price = "bid_price";
    public static final String document = "document" ;
    public static final String send_date = "send_date";

    /**
     * Constants for company
     */
    public static final String OFFICIAL_NAME = "official_name";
    public static final String NATIONAL_REGISTRATION_NUMBER = "national_registration_number";
    public static final String COUNTRY = "country";
    public static final String CITY = "city";
    public static final String NAME = "name";
    public static final String SURNAME = "surname" ;
    public static final String PHONE_NUMBER = "phone_number" ;

    /**
     *  Constants for cpv code
     */
    public static final String CPV_CODE = "cpv_code";
    public static final String CPV_DESCRIPTION = "cpv_description";
}


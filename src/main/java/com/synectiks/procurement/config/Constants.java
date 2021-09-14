package com.synectiks.procurement.config;

/**
 * Application constants.
 */

public final class Constants {

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String PROGRESS_STAGE_NEW = "NEW";
    public static final String PROGRESS_STAGE_PENDING_FOR_APPROVAL = "PENDING_FOR_APPROVAL";
    public static final String PROGRESS_STAGE_APPROVED = "APPROVED";
    public static final String PROGRESS_STAGE_REJECTED = "REJECTED";
    
    public static final String DATE_FORMAT_YYYY_MM_DD = "YYYY-MM-DD";
    public static final String DATE_FORMAT_MM_DD_YYYY = "MM-DD-YYYY";
    public static final String DATE_FORMAT_DD_MM_YYYY = "DD-MM-YYYY";
    public static final String DEFAULT_DATE_FORMAT =  DATE_FORMAT_YYYY_MM_DD;
    public static final int DEFAULT_DUE_DAYS = 5;
    
    public static final String  SECURITY_SERVICE_URL ="http://localhost:8094/security/roles/create";
    
    public static final String LOCAL_REQUISITION_FILE_STORAGE_DIRECTORY = "requisition_files"; 
    
    public static final String RULE_APPROVE_REQUISITION = "APPROVE_REQUISITION"; 
    
    private Constants() {
    }
}

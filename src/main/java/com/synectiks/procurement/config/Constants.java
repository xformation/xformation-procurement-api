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
    public static final String LOCAL_PROFILE_IMAGE_STORAGE_DIRECTORY = "profile_images";
    
    public static final String RULE_APPROVE_REQUISITION = "APPROVE_REQUISITION"; 
    public static final String RULE_APPROVE_PURCHASEORDER = "APPROVE_PURCHASEORDER"; 
    public static final String RULE_APPROVE_INVOICE = "APPROVE_INVOICE"; 
    public static final String RULE_REQUISITION_TYPE = "REQUISITION_TYPE"; 
    public static final String REQUISITION_TYPE_STANDARD = "standard"; 
    public static final String REQUISITION_TYPE_NON_STANDARD = "nonstandard"; 
    
    public static final String FILE_TYPE_IMAGE = "IMAGE";
    public static final String FILE_TYPE_PDF = "PDF";
    public static final String FILE_TYPE_TXT = "TXT";
    public static final String FILE_TYPE_JSON = "JSON";
    public static final String FILE_TYPE_DOC = "DOC";
    public static final String FILE_TYPE_DOCX = "DOCX";
    public static final String FILE_TYPE_XLSX = "XLSX";
    public static final String FILE_TYPE_XLS = "XLS";
    
    public static final String FILE_STORAGE_LOCATION_LOCAL = "LOCAL";
    public static final String FILE_STORAGE_LOCATION_S3 = "S3";
    public static final String FILE_STORAGE_LOCATION_AZURE = "AZURE";
    
    public static final String IDENTIFIER_PROFILE_IMAGE = "PROFILE_IMAGE";
    public static final String IDENTIFIER_REQUISITION_EXTRA_BUDGETORY_FILE = "REQUISITION_EXTRA_BUDGETORY_FILE";
    
    private Constants() {
    }
}

package uk.nhs.digital.ps.test.acceptance.models;

public enum Granularity {
    AMBULANCE_TRUSTS("Ambulance Trusts"),
    CANCER_NETWORKS("Cancer networks"),
    CARE_TRUSTS("Care Trusts"),
    CENSUS_AREA_STATISTICS_WARDS("Census Area Statistics Wards"),
    CLINICAL_COMMISSIONING_GROUPS("Clinical Commissioning Groups"),
    COMMUNITY_HEALTH_SERVICES("Community health services"),
    COUNCILS_WITH_SOCIAL_SERVICES_RESPONSIBILITIES("Councils with Adult Social Services Responsibilities (CASSRs)"),
    COUNTRY("Country"),
    COUNTY("County"),
    DENTAL_PRACTICES("Dental practices"),
    GOVERNMENT_OFFICE_REGIONS("Government Office Regions"),
    GP_PRACTICES("GP practices"),
    HEALTH_EDUCATION_ENGLAND_REGION("Health Education England Region"),
    HOSPITAL_AND_COMMUNITY_HEALTH_SERVICES("Hospital and Community Health Services"),
    HOSPITAL_TRUSTS("Hospital Trusts"),
    INDEPENDENT_SECTOR_HEALTH_CARE_PROVIDERS("Independent Sector Health Care Providers"),
    LOCAL_AUTHORITIES("Local Authorities"),
    MENTAL_HEALTH_TRUSTS("Mental Health Trusts"),
    MIDDLE_LAYER_SUPER_OUTPUT_AREAS("Middle Layer Super Output Areas"),
    NHS_HEALTH_BOARDS("NHS Health Boards"),
    NHS_TRUSTS("NHS Trusts"),
    PARLIAMENTARY_CONSTITUENCY("Parliamentary constituency"),
    PHARMACIES_AND_CLINICS("Pharmacies and clinics"),
    PRIMARY_CARE_ORGANISATIONS("Primary Care Organisations"),
    PRIMARY_CARE_TRUSTS("Primary Care Trusts"),
    REGIONAL_HEALTH_BODY("Regional health body"),
    REGIONS("Regions"),
    STRATEGIC_HEALTH_AUTHORITIES("Strategic Health Authorities"),
    SUSTAINABILITY_AND_TRANSFORMATION_PARTNERSHIPS("Sustainability and Transformation Partnerships"),;

    private final String displayValue;

    Granularity(final String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}

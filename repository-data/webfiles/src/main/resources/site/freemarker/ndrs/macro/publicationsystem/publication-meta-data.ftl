<#ftl output_format="HTML">

<#include "../../../include/imports.ftl">

<#function publicationMetaData document publiclyAccessible>
    <@fmt.message key="headers.publication-date" var="publicationDateHeader"/>
    <#assign publicationDate>
        <@formatRestrictableDate value=document.nominalPublicationDate/>
    </#assign>
    <#assign headerMetaData = [{
        "title": publicationDateHeader,
        "value": publicationDate
    }]/>
    <#assign metaData = []/>

    <#if publiclyAccessible>
        <#if document.fullTaxonomyList?has_content>
            <#assign fullTaxonomyList = document.fullTaxonomyList/>
        <#elseif document.parentDocument?has_content && document.parentDocument.fullTaxonomyList?has_content>
            <#assign fullTaxonomyList = document.parentDocument.fullTaxonomyList/>
        </#if>
        <#if fullTaxonomyList?has_content>
            <#assign metaData += [{
                "key": "keywords",
                "value": fullTaxonomyList?join(",")
            }]/>
        </#if>

        <#assign geographicCoverage = document.geographicCoverage/>
        <#if document.parentDocument?has_content && document.parentDocument.geographicCoverage?has_content>
            <#assign geographicCoverage = document.parentDocument.geographicCoverage/>
        </#if>
        <#if geographicCoverage?has_content>
            <@fmt.message key="headers.geographical-coverage" var="geographicalCoverageHeader"/>
            <#assign geographicalCoverageVal = ""/>
            <#list geographicCoverage as geographicCoverageItem>
                <#assign geographicalCoverageVal += geographicCoverageItem + geographicCoverageItem?is_last?then("", ", ")/>
            </#list>
            <#assign headerMetaData += [{
                "title": geographicalCoverageHeader,
                "value": geographicalCoverageVal
            }]/>
        </#if>

        <#if document.granularity?has_content>
            <#assign granularity = document.granularity/>
        <#elseif document.parentDocument?has_content && document.parentDocument.granularity?has_content>
            <#assign granularity = document.parentDocument.granularity/>
        </#if>
        <#if granularity?has_content>
            <@fmt.message key="headers.geographical-granularity" var="geographicalGranularityHeader"/>
            <#assign geographicalGranularity = ""/>
            <#list granularity as granularityItem>
                <#assign geographicalGranularity += granularityItem + granularityItem?is_last?then("", ", ")/>
            </#list>
            <#assign headerMetaData += [{
                "title": geographicalGranularityHeader,
                "value": geographicalGranularity
            }]/>
        </#if>

        <#if document.coverageStart??>
            <@fmt.message key="headers.date-range" var="coverageStartHeader"/>
            <#if document.coverageStart?? && document.coverageEnd??>
                <#assign coverageStart>
                    <@formatCoverageDates start=document.coverageStart.time end=document.coverageEnd.time/>
                </#assign>
                <#assign coverageStartSchemaFormat>
                    <@formatCoverageDates start=document.coverageStart.time end=document.coverageEnd.time schemaFormat=true/>
                </#assign>
                <#assign metaData += [{
                    "key": "temporalCoverage",
                    "value": coverageStartSchemaFormat
                }]/>
            <#else>
                <#assign coverageStart = "(Not specified)"/>
            </#if>
            <#assign headerMetaData += [{
                "title": coverageStartHeader,
                "value": coverageStart
            }]/>
        </#if>
    </#if>

    <#return {
        "headerMetaData": headerMetaData,
        "metaData": metaData
    }/>
</#function>

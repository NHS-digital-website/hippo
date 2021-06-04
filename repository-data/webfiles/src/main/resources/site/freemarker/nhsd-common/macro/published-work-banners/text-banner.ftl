<#ftl output_format="HTML">
<#include "../../../include/imports.ftl">

<#--  TODO: The national statistics stamp will be covered by a tech debt ticket once
      design has been approved  -->

<#--  <#macro nationalStatsStamp document>
    <#if (document.informationType)?has_content>
        <#list document.informationType as type>
            <#if type == "National statistics">
                <div class="article-header__stamp">
                    <img src="<@hst.webfile path="images/national-statistics-logo.svg"/>"
                         alt="A logo for National Statistics"
                         title="National Statistics"
                         class="image-icon image-icon--large"/>
                </div>
                <#break>
            </#if>
        </#list>
    </#if>
</#macro>  -->

<#macro publicationDate document>
    <div class="nhsd-o-hero__meta-data-item-title"><@fmt.message key="labels.publication-date"/>:</div>
    <div class="nhsd-o-hero__meta-data-item-description" data-uipath="ps.dataset.nominal-date" itemprop="datePublished">
        <@fmt.formatDate value=document.publicationDate.time?date type="date" pattern="d MMM yyyy" timeZone="${getTimeZone()}" />
    </div>
</#macro>

<#macro textBanner document topTitle="" topTitleLink="">
    <#local hasGeographicCoverage = document.geographicCoverage?has_content />
    <#local hasGranularity = document.geographicGranularity?has_content />
    <#local hasPublicationDate = document.publicationDate?? />

    <div class="nhsd-o-hero nhsd-!t-bg-blue nhsd-!t-col-white">
        <div class="nhsd-t-grid nhsd-!t-no-gutters ">
            <div class="nhsd-t-row ">
                <div class="nhsd-t-col-xs-12 nhsd-t-col-s-8 nhsd-!t-no-gutters">
                    <div class="nhsd-o-hero__content-box  nhsd-o-hero__content-box--left-align">
                        <div class="nhsd-o-hero__content">

                            <#--  <@nationalStatsStamp document />  -->

                            <#if topTitle?is_string && topTitle?length gt 0 &&
                                topTitleLink?is_string && topTitleLink?length gt 0>
                                <p class="nhsd-t-body-s">
                                    Part of 
                                    <a class="nhsd-a-link nhsd-a-link--col-white" href="${topTitleLink}">
                                       ${topTitle}
                                    </a>
                                </p>
                            </#if>

                            <span class="nhsd-t-heading-xl nhsd-!t-col-white" data-uipath="document.title">${document.title}</span>

                            <p class="nhsd-t-heading-s nhsd-!t-col-white" data-uipath="ps.publication.information-types">
                                <#if (document.informationType)?has_content>
                                    <#list document.informationType as type>${type}<#sep>, </#list>
                                </#if>
                            </p>

                            <div class="nhsd-o-hero__meta-data nhsd-!t-margin-bottom-6">

                                <#if hasPublicationDate>
                                    <div class="nhsd-o-hero__meta-data-item">
                                        <@publicationDate document />
                                    </div>
                                </#if>

                                <#if hasGeographicCoverage>
                                    <div class="nhsd-o-hero__meta-data-item">
                                        <div class="nhsd-o-hero__meta-data-item-title"><@fmt.message key="labels.geographic-coverage"/>:</div>
                                        <div class="nhsd-o-hero__meta-data-item-description">
                                            <#list document.geographicCoverage as geographicCoverageItem>${geographicCoverageItem}<#sep>, </#list>
                                        </div>
                                    </div>
                                </#if>

                                <#if hasGranularity>
                                    <div class="nhsd-o-hero__meta-data-item">
                                        <div class="nhsd-o-hero__meta-data-item-title"><@fmt.message key="labels.geographic-granularity"/>:</div>
                                        <div class="nhsd-o-hero__meta-data-item-description">
                                            <#list document.geographicGranularity as granularityItem>${granularityItem}<#sep>, </#list>
                                        </div>
                                    </div>
                                </#if>

                                <#if document.coverageStart??>
                                    <div class="nhsd-o-hero__meta-data-item">
                                        <div class="nhsd-o-hero__meta-data-item-title"><@fmt.message key="labels.date-range"/>:</div>
                                        <div class="nhsd-o-hero__meta-data-item-description">
                                            <#if document.coverageStart?? && document.coverageEnd??>
                                                <@formatCoverageDates start=document.coverageStart.time end=document.coverageEnd.time/>
                                            <#else>
                                                (Not specified)
                                            </#if>
                                        </div>
                                    </div>
                                </#if>
                            </div>
                            <a class="nhsd-a-button nhsd-a-button--invert" onClick="window.print()">
                                <span class="nhsd-a-button__label">Download this page as a PDF</span>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="nhsd-a-digiblocks nhsd-a-digiblocks--pos-tr nhsd-a-digiblocks--col-light-blue">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 550 550"><g><g transform="translate(222, 224)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon></g><g transform="translate(328.5, 367.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon></g><g transform="translate(151, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#3C4D57"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#32434C"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#313D45"></polygon></g><g transform="translate(80, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon></g></g><g><g transform="translate(186.5, 203.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon></g><g transform="translate(186.5, 285.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(222, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(9, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(257.5, 449.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#F5D507"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F2CB0C"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EEC000"></polygon></g></g><g><g transform="translate(186.5, 203.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#6D7B86"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#62717A"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#5C6B75"></polygon></g><g transform="translate(399.5, 326.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon></g><g transform="translate(222, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon></g></g><g><g transform="translate(328.5, 162.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#F5D507"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F2CB0C"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EEC000"></polygon></g><g transform="translate(399.5, 244.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(115.5, 162.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#6D7B86"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#62717A"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#5C6B75"></polygon></g><g transform="translate(186.5, 244.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon></g><g transform="translate(328.5, 326.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon></g><g transform="translate(257.5, 326.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#3C4D57"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#32434C"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#313D45"></polygon></g></g><g><g transform="translate(328.5, 244.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#DADFDF"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#CDD5D6"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#C5CDCF"></polygon></g><g transform="translate(257.5, 285.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon></g><g transform="translate(44.5, 203.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#6D7B86"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#62717A"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#5C6B75"></polygon></g><g transform="translate(151, 265)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon></g></g><g><g transform="translate(435, 142)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon></g></g><g><g transform="translate(328.5, 39.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon></g><g transform="translate(222, 19)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(257.5, 80.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#DADFDF"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#CDD5D6"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#C5CDCF"></polygon></g></g></svg>
        </div>
    </div>
</#macro>

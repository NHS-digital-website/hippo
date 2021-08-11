<#ftl output_format="HTML">

<#include "./publicationsystem/publication-meta-data.ftl">

<#macro detailsBanner banner publiclyAccessible showDownload=true>
    <#if banner??>
        <div class="nhsd-o-hero nhsd-!t-bg-blue nhsd-!t-col-white">
            <div class="nhsd-t-grid nhsd-!t-no-gutters">
                <div class="nhsd-t-row">
                    <div class="nhsd-t-col-xs-12 nhsd-t-col-s-8 nhsd-!t-no-gutters">
                        <#if banner.parentDocument?has_content && banner.parentDocument.informationType?has_content>
                            <#assign informationType = banner.parentDocument.informationType/>
                        <#elseif banner.informationType?has_content>
                            <#assign informationType = banner.informationType/>
                        </#if>
                        <#if informationType??>
                            <@nationalStatsStamp informationTypes=informationType/>
                        </#if>

                        <div class="nhsd-o-hero__content-box nhsd-o-hero__content-box--left-align">
                            <div class="nhsd-o-hero__content">
                                <#if banner.parentDocument??>
                                    <@hst.link hippobean=banner.parentDocument.selfLinkBean var="parentLink"/>
                                    <p class="nhsd-t-body-s"><@fmt.message key="labels.publication"/>, Part of <a class="nhsd-a-link nhsd-a-link--col-white"
                                                                                                                  href="<@hst.link hippobean=banner.parentDocument.selfLinkBean/>"
                                                                                                                  onClick="${getOnClickMethodCall(banner.parentDocument.class.name, parentLink)}"
                                                                                                                  itemprop="url"><span itemprop="name">${banner.parentDocument.title}</span></a></p>
                                <#else>
                                    <p class="nhsd-t-body-s"><@fmt.message key="labels.publication"/></p>
                                </#if>

                                <h1 class="nhsd-t-heading-xl nhsd-!t-col-white" data-uipath="document.title">${banner.title}</h1>

                                <#assign informationTypes = banner.informationType/>
                                <#if banner.parentDocument?has_content && banner.parentDocument.informationType?has_content>
                                    <#assign informationTypes = banner.parentDocument.informationType/>
                                </#if>
                                <#if informationTypes?has_content>
                                    <p class="nhsd-t-body" data-uipath="ps.publication.information-types">
                                        <#list informationTypes as type>${type}<#sep>, </#list>
                                    </p>
                                </#if>

                                <#assign metaData = publicationMetaData(banner, publiclyAccessible) />

                                <#if metaData["headerMetaData"]?size gt 0>
                                    <div class="nhsd-o-hero__meta-data">
                                        <#list metaData["headerMetaData"] as metaDataItem>
                                            <div class="nhsd-o-hero__meta-data-item">
                                                <div class="nhsd-o-hero__meta-data-item-title">${metaDataItem["header"]}</div>
                                                <div class="nhsd-o-hero__meta-data-item-description" data-uipath="ps.publication.${slugify(metaDataItem["header"]?trim?replace(':', ''))}">${metaDataItem["value"]}</div>
                                            </div>
                                        </#list>
                                    </div>
                                </#if>

                                <#if metaData["metaData"]?size gt 0>
                                    <#list metaData["metaData"] as metaData>
                                        <meta itemprop="${metaData.key}" content="${metaData.value}"/>
                                    </#list>
                                </#if>

                                <#if publiclyAccessible && showDownload>
                                    <a class="nhsd-a-button nhsd-a-button--invert nhsd-!t-margin-top-6 nhsd-!t-margin-bottom-0 js-print-pdf-button" id="print-pdf-button">
                                        <span class="nhsd-a-button__label"><@fmt.message key="labels.download-pdf"/></span>
                                    </a>
                                </#if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="nhsd-a-digiblocks nhsd-a-digiblocks--pos-tr nhsd-a-digiblocks--col-light-blue">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 550 550"><g><g transform="translate(222, 224)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon></g><g transform="translate(328.5, 367.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon></g><g transform="translate(151, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#3C4D57"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#32434C"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#313D45"></polygon></g><g transform="translate(80, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon></g></g><g><g transform="translate(186.5, 203.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon></g><g transform="translate(186.5, 285.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(222, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(9, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(257.5, 449.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#F5D507"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F2CB0C"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EEC000"></polygon></g></g><g><g transform="translate(186.5, 203.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#6D7B86"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#62717A"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#5C6B75"></polygon></g><g transform="translate(399.5, 326.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon></g><g transform="translate(222, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon></g></g><g><g transform="translate(328.5, 162.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#F5D507"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F2CB0C"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EEC000"></polygon></g><g transform="translate(399.5, 244.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(115.5, 162.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#6D7B86"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#62717A"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#5C6B75"></polygon></g><g transform="translate(186.5, 244.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon></g><g transform="translate(328.5, 326.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon></g><g transform="translate(257.5, 326.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#3C4D57"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#32434C"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#313D45"></polygon></g></g><g><g transform="translate(328.5, 244.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#DADFDF"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#CDD5D6"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#C5CDCF"></polygon></g><g transform="translate(257.5, 285.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon></g><g transform="translate(44.5, 203.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#6D7B86"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#62717A"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#5C6B75"></polygon></g><g transform="translate(151, 265)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon></g></g><g><g transform="translate(435, 142)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon></g></g><g><g transform="translate(328.5, 39.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon></g><g transform="translate(222, 19)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(257.5, 80.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#DADFDF"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#CDD5D6"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#C5CDCF"></polygon></g></g></svg>
            </div>
        </div>
    </#if>
</#macro>

<#macro nationalStatsStamp informationTypes>
    <#list informationTypes as type>
        <#if type == "National statistics">
            <div class="nhsd-o-hero__stamp">
                <img src="<@hst.webfile path="images/national-statistics-logo.svg"/>" data-uipath="ps.publication.national-statistics" alt="National Statistics" />
            </div>
            <#break>
        </#if>
    </#list>
</#macro>

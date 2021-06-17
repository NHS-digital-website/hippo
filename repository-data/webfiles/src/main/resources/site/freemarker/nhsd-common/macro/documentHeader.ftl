<#ftl output_format="HTML">

<#include "../../include/imports.ftl">
<#include "headerMetadata.ftl">

<#macro documentHeader document doctype header_icon_arg='' title="" summary="" topics="" hasSchemaOrg=true metadata={} downloadPDF=false>

    <#assign custom_title = title />
    <!-- checking whether simulating doc in order to avoid console errors from NewsHub and EventHub docs -->
    <#if ! custom_title?has_content && document != "simulating_doc"  >
      <#assign custom_title = document.title />
    </#if>

    <#assign custom_summary = summary />
    <#assign hasDocumentSummary = false />
    <#if ! custom_summary?has_content && document != "simulating_doc"  >
      <#assign hasDocumentSummary = document.summary?? && document.summary.content?has_content />
      <#if hasDocumentSummary >
        <#assign custom_summary = document.summary />
      </#if>
    </#if>

    <#assign hasBannerControls = document != "simulating_doc" && document.bannercontrols?? && document.bannercontrols?has_content />
    <#assign hasPageIcon = document != "simulating_doc" && document.pageIcon?? && document.pageIcon?has_content />

    <#assign headerIcon = header_icon_arg>
    <#if ! header_icon_arg?has_content>
      <#if hasBannerControls && document.bannercontrols.icon?has_content>
        <#assign headerIcon = document.bannercontrols.icon />
      <#elseif hasPageIcon>
        <#assign headerIcon = document.pageIcon />
      </#if>
    </#if>

    <#assign hasFinalPageIcon = headerIcon?has_content />
    <#assign hasTopics = topics?has_content />

    <div class="nhsd-o-hero nhsd-!t-bg-blue nhsd-!t-col-white nhsd-!t-margin-bottom-6" aria-label="Document Header">
        <div class="nhsd-t-grid nhsd-!t-no-gutters">
            <div class="nhsd-t-row">
                <div class="nhsd-t-col-xs-12 nhsd-t-col-s-8 nhsd-!t-no-gutters">
                    <div class="nhsd-o-hero__content-box  nhsd-o-hero__content-box--left-align">
                        <div class="nhsd-o-hero__content" itemscope itemtype="http://schema.org/Text">

                            <#if doctype == "news">
                              <p class="nhsd-t-body-s">${doctype?capitalize}</p>
                            </#if>
                            
                            <#assign titleProp = "itemprop=name">
                            <#if hasSchemaOrg>
                                <#assign titleProp = "itemprop=name">
                                <#if doctype == "news">
                                    <#assign titleProp = "itemprop=headline">
                                </#if>
                            </#if>

                            <p id="top" 
                               class="nhsd-t-heading-xl nhsd-!t-col-white" 
                               data-uipath="document.title" ${hasSchemaOrg?then(titleProp, '')} ${headerStyle}
                            >
                               ${custom_title}
                            </p>

                            <#if hasDocumentSummary>
                                <span data-uipath="website.${doctype}.summary">
                                    <@hst.html hippohtml=custom_summary contentRewriter=brContentRewriter/>
                                </span>
                            <#else>
                                <#assign schemaProp = "" />
                                <#if hasSchemaOrg>
                                    <#assign schemaProp = "itemprop=description" />
                                </#if>
                                <#if custom_summary?has_content>
                                    <span ${schemaProp} class="nhsd-t-body" data-uipath="website.${doctype}.summary">${custom_summary}</span>
                                </#if>
                            </#if>

                            <#--  ALL COMMENTED OUT CODE COMES FROM DOCTYPES THAT ARE YET TO BE REBRANDED  -->
                            
                            <#--  <#if doctype == "intranet-task">
                                <#if document.introduction?has_content>
                                    <@hst.html hippohtml=document.introduction contentRewriter=gaContentRewriter />
                                </#if>
                                <#if document.priorityActions?has_content>
                                    <div class="ctabtn--div">
                                        <#list document.priorityActions as action>
                                            <#if action.link.linkType == "internal">
                                                <@hst.link hippobean=action.link.link var="priorityActionLink"/>
                                            <#else>
                                                <#assign priorityActionLink=action.link.link/>
                                            </#if>
                                            <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, action.action) />
                                            <div class="ctabtn--inline" aria-labelledby="ctabtn-${slugify(action.action)}">
                                                <a href="${priorityActionLink}"
                                                    class="ctabtn--white-button"
                                                    id="ctabtn-${slugify(action.action)}"
                                                    onClick="${onClickMethodCall}"
                                                    onKeyUp="return vjsu.onKeyUp(event)">
                                                ${action.action}
                                                </a>
                                            </div>
                                        </#list>
                                    </div>
                                </#if>
                            </#if>  -->

                            <#--  <#if hasFinalPageIcon>
                                <div class="column--one-third column--reset local-header__icon">
                                <#if hasBannerControls && document.bannercontrols.icon?has_content || document != "simulating_doc" && hasPageIcon >
                                    
                                    <@hst.link hippobean=headerIcon.original fullyQualified=true var="image" />
                                    <#if image?ends_with("svg")>
                                        <#assign colour = 'ffcd60'>
                                        <#if hasBannerControls && document.bannercontrols.iconcolor?has_content>
                                            <#assign colour = document.bannercontrols.iconcolor?replace("#","")>
                                        </#if>
                                        <#assign imageUrl = '${image?replace("/binaries", "/svg-magic/binaries")}' />
                                        <#assign imageUrl += "?colour=${colour}" />
                                        <img src="${imageUrl}" alt="${custom_title}" aria-hidden="true" />
                                    <#else>
                                        <img src="${image}" alt="${custom_title}" aria-hidden="true" />
                                    </#if>
                                <#else>
                                    
                                    <img src="<@hst.webfile path="${headerIcon}" fullyQualified=true/>" alt="${custom_title}" aria-hidden="true">
                                </#if>
                                </div>
                            </#if>  -->

                            <#--  <#if hasTopics>
                                <div class="detail-list-grid">
                                    <div class="grid-row">
                                        <div class="column column--reset">
                                            <dl class="detail-list">
                                                <dt class="detail-list__key">Topics:</dt>
                                                <dd class="detail-list__value">
                                                    <#if hasSchemaOrg>
                                                    <span itemprop="keywords" ><#list document.taxonomyTags as tag>${tag} <#sep>, </#list></span>
                                                    <#else>
                                                    <span><#list document.taxonomyTags as tag>${tag} <#sep>, </#list></span>
                                                    </#if>
                                                </dd>
                                            </dl>
                                        </div>
                                    </div>
                                </div>
                            </#if>  -->

                            <div class="nhsd-o-hero__meta-data nhsd-!t-margin-bottom-6">
                                <@headerMetadata metadata doctype />
                            </div>

                            <#if downloadPDF>
                                <a class="nhsd-a-button nhsd-a-button--invert" href="#">
                                    <span class="nhsd-a-button__label">Download this page as a PDF</span>
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
</#macro>

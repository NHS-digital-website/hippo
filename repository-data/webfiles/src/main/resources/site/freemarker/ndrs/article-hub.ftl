<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.ApiEndpoint" -->

<#include "../include/imports.ftl">
<#include "../common/macro/sections/codeSection.ftl">
<#include "../common/macro/furtherInformationSection.ftl">
<#include "../publicationsystem/macro/structured-text.ftl">
<#include "../common/macro/sections/sections.ftl">
<#include "../common/macro/stickyNavSections.ftl">
<#include "../common/macro/fileMetaAppendix.ftl">
<#include "../common/macro/component/lastModified.ftl">
<#include "../common/macro/fileIconByMimeType.ftl">
<#include "../common/macro/contentPixel.ftl">
<#include "macro/heroes/hero.ftl">

<#include "./macro/metaTags.ftl">
<@metaTags></@metaTags>

<#assign hasApiEndpointContent = document.requestname?? && document.uriaddress?? && document.summary?? />
<#assign hasAuthnAuthsContent = document.authnauths?? && document.authnauths.content?has_content />
<#assign hasParameters = document.apiendpointparams?? && document.apiendpointparams?has_content />
<#assign hasSampleRequest = document.samplerequest?? && document.samplerequest?has_content />
<#assign hasSampleResponse = document.sampleresponse?? && document.sampleresponse?has_content />
<#assign hasResponseDefinition = document.responsedefinitions?? && document.responsedefinitions?has_content />
<#assign hasStatusErrorCode = document.statuserrorcodes?? && document.statuserrorcodes?has_content />
<#assign sectionTitlesFound = countSectionTitles(document.sections) />
<#assign renderNav = sectionTitlesFound gte 1 || hasAuthnAuthsContent || hasApiEndpointContent />
<#assign hasSectionContent = document.sections?has_content />
<#assign hasShortsummary = document.shortsummary?? />
<#assign hasTopLink = document.includeTopLink?? && document.includeTopLink />
<#assign navigationController = document.navigationController />

<#assign apimethodValue = document.apimethod?upper_case />
<#if apimethodValue??>
<#assign colourClass = "colour-class-${apimethodValue?lower_case}" />
</#if>

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.requestname></@contentPixel>
<div class="grid-row">

    <div class="hero-inner-bg bg-fix">
        <@hero {
            "title": document.title,
            "colour": "darkBlue"
        }>
            <div class="nhsd-!t-margin-top-6">
                <#if hasSessions>
                    <#-- [FTL-BEGIN] List of date ranges -->
                    <#list document.events as event>
                        <@fmt.formatDate value=event.startdatetime.time type="Date" pattern="yyyy-MM-dd" var="comparableStartDate" timeZone="${getTimeZone()}" />
                        <@fmt.formatDate value=event.enddatetime.time type="Date" pattern="yyyy-MM-dd" var="comparableEndDate"  timeZone="${getTimeZone()}"/>
                        <#assign validDate = (comparableStartDate?? && comparableEndDate??) />
                        <#if event.enddatetime.time?date gt .now?date>
                            <#assign hasFutureEvent = true>
                        </#if>
                        <div itemscope itemtype="http://schema.org/Event">
                            <#if document.events?size gt 1 && validDate>
                                <p class="nhsd-t-heading-s">Session ${event?counter}</p>
                            </#if>
                            <#if validDate>
                                <div class="nhsd-o-hero__meta-data nhsd-!t-margin-bottom-6">
                                    <div class="nhsd-o-hero__meta-data-item">
                                        <div class="nhsd-o-hero__meta-data-item-title">Date:</div>
                                        <div class="nhsd-o-hero__meta-data-item-description" data-uipath="">
                                            <@fmt.formatDate value=event.startdatetime.time type="Date" pattern="EEEE d MMMM yyyy" timeZone="${getTimeZone()}" />
                                            <#if comparableStartDate != comparableEndDate>
                                                - <@fmt.formatDate value=event.enddatetime.time type="Date" pattern="EEEE d MMMM yyyy" timeZone="${getTimeZone()}" />
                                            </#if>
                                        </div>
                                    </div>
                                    <div class="nhsd-o-hero__meta-data-item">
                                        <div class="nhsd-o-hero__meta-data-item-title">Time:</div>
                                        <div class="nhsd-o-hero__meta-data-item-description" data-uipath="">
                                            <@fmt.formatDate value=event.startdatetime.time type="Date" pattern="h:mm a" timeZone="${getTimeZone()}" /> to <@fmt.formatDate value=event.enddatetime.time type="Date" pattern="h:mm a" timeZone="${getTimeZone()}" />
                                        </div>
                                    </div>
                                </div>
                                <@schemaMeta "${document.title}" event.startdatetime.time?date event.enddatetime.time?date_if_unknown />
                            </#if>
                        </div>
                    </#list>
                    <#-- [FTL-END] List of date ranges -->
                <#else>
                    <@schemaMeta "${document.title}" />
                </#if>
                <#if document.apiMasterParent?has_content && document.apiMasterParent.title??>
                    <p class="article-header__subtitle">
                        This is part of
                        <a href="<@hst.link hippobean=document.apiMasterParent/>"
                            title="${document.apiMasterParent.title}">
                            <span itemprop="subjectof" itemscope itemtype="https://schema.org/webapi">${document.apiMasterParent.title}</span>
                        </a>
                    </p>
                </#if>
            <#if hasShortsummary>
                <div class="article-header__subtitle" data-uipath="document.shortsummary">${document.shortsummary}</div>
            </#if>
        </div>
        </@hero>
    </div>
<article class="article article--service">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <#if navigationController != "withoutNavWide">
            <div class="column column--one-third page-block--sticky-nav page-block--sidebar article-section-nav-outer-wrapper">
                <!-- start sticky-nav -->
                <div id="sticky-nav" class="sticky-nav-leftfix">
                    <#assign links = [] />
                    <#if document.requestname?? && document.uriaddress?? && document.summary??>
                        <#assign links += [{ "url": "#" + "Endpoint", "title": "Endpoint" }] />
                    </#if>
                    <#if document.authnauths?? && document.authnauths.content?has_content>
                        <#assign links += [{ "url": "#" + "authns", "title": "Authorisation and Authentication" }] />
                    </#if>
                    <#if hasParameters>
                        <#assign links += [{ "url": "#" + "Parameter", "title": "Parameters" }] />
                    </#if>
                    <#if hasSampleRequest>
                        <#assign links += [{ "url": "#" + "SampleRequest", "title": "Sample Request" }] />
                    </#if>
                    <#if hasSampleResponse>
                        <#assign links += [{ "url": "#" + "SampleResponse", "title": "Sample Response" }] />
                    </#if>
                    <#if hasResponseDefinition>
                        <#assign links += [{ "url": "#" + "responseDefination", "title": "Response Definitions" }] />
                    </#if>
                    <#if hasStatusErrorCode>
                        <#assign links += [{ "url": "#" + "statusErrorCode", "title": "Status and error codes" }] />
                    </#if>
                    <@stickyNavSections getStickySectionNavLinks({"document": document, "includeTopLink": hasTopLink, "sections": links})></@stickyNavSections>
                </div>
                <!-- end sticky-nav -->
            </div>
            </#if>

            <div class="column ${(navigationController != "withoutNavWide")?then("column--two-thirds", "column--wide-mode")} page-block page-block--main">

                <#if hasApiEndpointContent>
                    <div class="article-section">
                        <#if document.summary?has_content>
                            <div id="Endpoint" class="article-section article-section--highlighted">
                                <h2 data-uipath="website.contentblock.summary.title">Endpoint</h2>
                                <div data-uipath="website.contentblock.summary.content" class="rich-text-content">
                                    <@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/>
                                </div>
                                <div>
                                    <div class="api-endpoint-table">
                                        <div class="${colourClass}" >${apimethodValue}</div>
                                        <div class="api-endpoint-url" itemprop="url" itemscope itemtype="https://schema.org/webapi">${document.uriaddress}</div>
                                    </div>
                                </div>
                            </div>
                        </#if>
                    </div>
                </#if>
                <#if hasAuthnAuthsContent>
                    <div class="article-section">
                        <div id="authns" class="article-section article-section--highlighted">
                            <h2 data-uipath="website.contentblock.auth.title">Authorisation and Authentication</h2>
                            <#if document.authnauths.title?has_content>
                                <h3 data-uipath="website.contentblock.auth.subtitle">${document.authnauths.title}</h3>
                            </#if>
                            <#if document.authnauths.content?has_content>
                                <div data-uipath="website.contentblock.auth.content" class="rich-text-content">
                                    <@hst.html hippohtml=document.authnauths.content contentRewriter=gaContentRewriter/>
                                </div>
                            </#if>
                        </div>
                    </div>
                </#if>
                <#if hasParameters>
                    <div class="article-section">
                        <div id="Parameter" class="article-section article-section--highlighted">
                            <h2 data-uipath="website.contentblock.parameter.title">Parameters</h2>
                            <div>
                                <table data-disablesort="true">
                                    <thead>
                                        <tr>
                                            <th scope="col">Name</th>
                                            <th scope="col">Parameter Type</th>
                                            <th scope="col">Mandatory/ Optional</th>
                                            <th scope="col">Path</th>
                                            <th scope="col">Description</th>
                                        </tr>
                                    </thead>
                                    <#list document.apiendpointparams as apiParam >
                                        <tbody>
                                            <tr>
                                                <td itemprop="name" itemscope itemtype="https://schema.org/StructuredValue"><code class="parameter-codeinline">${apiParam.name}</code></td>
                                                <td itemprop="value" itemscope itemtype="https://schema.org/StructuredValue">${parametertypeList[apiParam.parametertype]}</td>
                                                <td>${apiParam.ismandatory?then('Mandatory','Optional')}</td>
                                                <td><code>${apiParam.path}</code></td>
                                                <td itemprop="description" itemscope itemtype="https://schema.org/StructuredValue"><@hst.html hippohtml=apiParam.description contentRewriter=gaContentRewriter/></td>
                                            </tr>
                                        </tbody>
                                    </#list>
                                </table>
                            </div>
                        </div>
                    </div>
                </#if>
                <#if hasSampleRequest>
                    <div class="article-section">
                        <div id="SampleRequest" class="article-section article-section--highlighted">
                            <h2 data-uipath="website.contentblock.samplerequest.title">Sample Request</h2>
                            <#list document.samplerequest as samReq >
                                <#if samReq.content?has_content>
                                    <div data-uipath="website.contentblock.samplerequest.content" class="rich-text-content">
                                        <@hst.html hippohtml=samReq.content contentRewriter=gaContentRewriter/>
                                    </div>
                                </#if>
                                <#if samReq.webcode.codetext?has_content>
                                    <@codeSection section=samReq.webcode/>
                                </#if>
                            </#list>
                        </div>
                    </div>
                </#if>
                <#if hasSampleResponse>
                    <div class="article-section">
                        <div id="SampleResponse" class="article-section article-section--highlighted">
                            <h2 data-uipath="website.contentblock.sampleresponse.title">Sample Response</h2>
                            <#list document.sampleresponse as samRes >
                                <#if samRes.content?has_content>
                                    <div data-uipath="website.contentblock.sampleresponse.content" class="rich-text-content">
                                        <@hst.html hippohtml=samRes.content contentRewriter=gaContentRewriter/>
                                    </div>
                                </#if>
                                <#if samRes.webcode.codetext?has_content>
                                    <@codeSection section=samRes.webcode/>
                                </#if>
                            </#list>
                        </div>
                    </div>
                </#if>
                <#if hasResponseDefinition>
                    <div class="article-section">
                        <div id="responseDefination" class="article-section article-section--highlighted">
                            <h2 data-uipath="website.contentblock.responsedefinition.responseitem">Response Definitions</h2>
                            <#if document.resdefcontent?has_content>
                                <p><@hst.html hippohtml=document.resdefcontent contentRewriter=gaContentRewriter/></p>
                            </#if>
                            <div>
                                <table data-disablesort="true">
                                    <thead>
                                        <tr>
                                            <th>Name</th>
                                            <th>Description</th>
                                            <th>Data type</th>
                                        </tr>
                                    </thead>
                                    <#list document.responsedefinitions as resDef >
                                        <tbody>
                                            <tr>
                                                <td><code class="parameter-codeinline">${resDef.responseitem}</code></td>
                                                <td><@hst.html hippohtml=resDef.description contentRewriter=gaContentRewriter/></td>
                                                <td class="noword-wrap">${resDef.datatype}</td>
                                            </tr>
                                        </tbody>
                                    </#list>
                                </table>
                            </div>
                        </div>
                    </div>
                </#if>
                <#if hasStatusErrorCode>
                    <div class="article-section">
                        <div id="statusErrorCode" class="article-section article-section--highlighted">
                            <h2 data-uipath="website.contentblock.statuserrorcode.title">Status and error codes</h2>
                        <div>
                            <table data-disablesort="true">
                                <thead>
                                    <tr>
                                        <th>HTTP Code</th>
                                        <th>Meaning</th>
                                        <th>Description</th>
                                        <th>Diagnostics</th>
                                    </tr>
                                </thead>
                                <#list document.statuserrorcodes as errorCode >
                                    <tbody>
                                        <tr>
                                            <#if errorCode.httpcode?has_content>
                                            <td><code>${errorCode.httpcode}</code></td>
                                            <#else>
                                            <td><code>N/A</code></td>
                                            </#if>
                                            <td><code>${errorCode.meaning}</code></td>
                                            <td><@hst.html hippohtml=errorCode.description contentRewriter=gaContentRewriter/></td>
                                            <td><@hst.html hippohtml=errorCode.howtofixthis contentRewriter=gaContentRewriter/></td>
                                        </tr>
                                    </tbody>
                                </#list>
                            </table>
                          </div>
                      </div>
                    </div>
                </#if>
                <#if hasSectionContent>
                    <div class="article-section">
                        <@sections document.sections></@sections>
                    </div>
                </#if>
                <@lastModified document.lastModified></@lastModified>
            </div>
        </div>
    </div>
</article>

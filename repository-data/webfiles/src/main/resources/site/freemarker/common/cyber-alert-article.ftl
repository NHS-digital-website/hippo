<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/documentHeader.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>
<@hst.setBundle basename="rb.doctype.cyberalerts,rb.generic.headers,publicationsystem.headers,emails"/>

<#-- Define section headers, nav ids and titles -->
<@fmt.message key="headers.summary" var="summaryHeader" />
<@fmt.message key="headers.affected-platforms" var="affectedPlatformsHeader" />
<@fmt.message key="headers.threat-details" var="threatDetailsHeader" />
<@fmt.message key="headers.threat-updates" var="threatUpdatesHeader" />
<@fmt.message key="headers.remediation-advice" var="remediationIntroHeader" />
<@fmt.message key="headers.remediation-steps" var="remediationStepsHeader" />
<@fmt.message key="headers.indicators-of-compromise" var="indicatorsOfCompromiseHeader" />
<@fmt.message key="headers.ncsc-link" var="ncscLinkHeader" />
<@fmt.message key="headers.source-of-update" var="sourceOfUpdateHeader" />
<@fmt.message key="headers.services" var="servicesHeader" />
<@fmt.message key="headers.topics" var="topicsHeader" />
<@fmt.message key="headers.cve" var="cveHeader" />
<@fmt.message key="headers.acknowledgement" var="acknowledgementHeader" />

<@fmt.message key="labels.threat-id" var="threatIdLabel" />
<@fmt.message key="labels.category" var="categoryLabel" />
<@fmt.message key="labels.threat-severity" var="threatSeverityLabel" />
<@fmt.message key="labels.threat-vector" var="threatVectorLabel" />
<@fmt.message key="labels.date-published" var="datePublishedLabel" />
<@fmt.message key="labels.date-lastupdated" var="dateLastUpdatedLabel" />
<@fmt.message key="email.cyberattack" var="emailLabel" />
<@fmt.message key="email.cyberattack.title" var="emailTitleLabel" />
<@fmt.message key="headers.archived" var="archiveContentHeading" />
<@fmt.message key="texts.archived" var="archiveContentBody" />


<#assign hasSummaryContent = document.summary?? && document.summary?has_content />
<#assign hasSectionContent = document.sections?? && document.sections?has_content />
<#assign hasThreatAffects = document.threatAffects?? && document.threatAffects?has_content />
<#assign hasThreatUpdates = document.threatUpdates?? && document.threatUpdates?has_content />
<#assign hasRemediationIntro = document.remediationIntro?? && document.remediationIntro.content?has_content />
<#assign hasRemediationSteps = document.remediationSteps?? && document.remediationSteps?has_content />
<#assign hasIndicatorsOfCompromise = document.indicatorsCompromise?? && document.indicatorsCompromise?has_content />
<#assign hasNcscLink = document.ncscLink?? && document.ncscLink?has_content />
<#assign hasSourceOfUpdate = document.sourceOfThreatUpdates?? && document.sourceOfThreatUpdates?has_content />
<#assign hasServices = document.services?? && document.services?has_content />
<#assign hasTopics = document.keys?? && document.keys?has_content />
<#assign hasCVE = document.cveIdentifiers?? && document.cveIdentifiers?has_content />
<#assign hasAcknowledgement = document.cyberAcknowledgements?? && document.cyberAcknowledgements?has_content />


<#assign links = [] />
<#if hasThreatAffects><#assign links += [{ "url": "#" + slugify(affectedPlatformsHeader), "title": affectedPlatformsHeader }] /></#if>
<#if hasSectionContent>
<#list document.sections as section>
    <#if section.title?has_content && section.sectionType == 'website-section'>
        <#assign isNumberedList = false />
        <#if section.isNumberedList??>
            <#assign isNumberedList = section.isNumberedList />
        </#if>
        <#assign links += [{ "url": "#" + slugify(section.title), "title": section.title, "isNumberedList": isNumberedList?c }] />
    </#if>
</#list>
</#if>
<#if hasThreatUpdates><#assign links += [{ "url": "#" + slugify(threatUpdatesHeader), "title": threatUpdatesHeader }] /></#if>
<#if hasRemediationIntro><#assign links += [{ "url": "#" + slugify(remediationIntroHeader), "title": remediationIntroHeader }] /></#if>
<#if hasRemediationSteps><#assign links += [{ "url": "#" + slugify(remediationStepsHeader), "title": remediationStepsHeader }] /></#if>
<#if hasIndicatorsOfCompromise><#assign links += [{ "url": "#" + slugify(indicatorsOfCompromiseHeader), "title": indicatorsOfCompromiseHeader }] /></#if>
<#if hasNcscLink><#assign links += [{ "url": "#" + slugify(ncscLinkHeader), "title": ncscLinkHeader }] /></#if>
<#if hasSourceOfUpdate><#assign links += [{ "url": "#" + slugify(sourceOfUpdateHeader), "title": sourceOfUpdateHeader }] /></#if>
<#if hasCVE><#assign links += [{ "url": "#" + slugify(cveHeader), "title": cveHeader }] /></#if>
<#if hasAcknowledgement><#assign links += [{ "url": "#" + slugify(acknowledgementHeader), "title": acknowledgementHeader }] /></#if>

<#assign metadata = [] />
<#if document.threatId?has_content><#assign metadata += [ { "key": threatIdLabel, "value": document.threatId } ] /></#if>
<#if document.category?has_content><#assign metadata += [ { "key": categoryLabel, "value": document.category, "type": "list" } ] /></#if>
<#if document.severity?has_content><#assign metadata += [ { "key": threatSeverityLabel, "value": document.severity } ] /></#if>
<#if document.threatvector?has_content><#assign metadata += [ { "key": threatVectorLabel, "value": document.threatvector, "type": "list" } ] /></#if>
<#if document.publishedDate?has_content><#assign metadata += [ { "key": datePublishedLabel, "value": document.publishedDate.time, "type": "date" } ] /></#if>

<article class="article article--cyber-alert">

    <@documentHeader document 'cyberalert' "" '' document.shortsummary '' false metadata></@documentHeader>

    <div class="cyber-alert-minus-margin grid-wrapper grid-wrapper--full-width grid-wrapper--wide grid-wrapper--emphasis-yellow">
        <div class="grid-wrapper">
            <div class="article-header__inner">
                Report a cyber attack: call <a href="tel:004403003035222" title="Contact us by telephone">0300 303 5222</a>
                or email <a href="mailto:${emailLabel}" title="${emailTitleLabel}">${emailLabel}</a>
            </div>
        </div>
    </div>

    <div class="grid-wrapper grid-wrapper--article" id="document-content">
        <div class="grid-row">
            <div class="column column--no-padding">
                <div class="update-box-group">
                    <#if document.archiveContent?? && document.archiveContent >
                      <#assign section = { "emphasisType": "Important", "heading": archiveContentHeading, "bodyCustom": archiveContentBody  } />
                      <@emphasisBox section=section />
                    </#if>
                </div>
            </div>
        </div>

        <div class="grid-row">

            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <!-- start sticky-nav -->
                <div id="sticky-nav">
                    <@stickyNavSections getStickySectionNavLinks({"sections": links, "includeSummary": true })></@stickyNavSections>
                </div>
                <!-- end sticky-nav -->
            </div>

            <div class="column column--two-thirds page-block page-block--main">
                <#if hasSummaryContent>
                    <div  id="${slugify(summaryHeader)}" class="article-section article-section--summary article-section--reset-top">
                        <h2>${summaryHeader}</h2>
                        <div data-uipath="website.publishedwork.summary"><@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/></div>
                    </div>
                </#if>

                <#if hasThreatAffects>
                    <div id="${slugify(affectedPlatformsHeader)}" class="article-section">
                        <h2>${affectedPlatformsHeader}</h2>
                        The following platforms are known to be affected:
                        <ul>
                            <#list document.threatAffects as item>
                                <li>
                                    <#if item.platformAffected??>
                                        <#if item.platformAffected.url??>
                                            <a href="${item.platformAffected.url}">
                                        </#if>
                                        ${item.platformAffected.title}
                                        <#if item.platformAffected.url??>
                                            </a>
                                        </#if>
                                    </#if>
                                    <#if item.versionsAffected?? && item.versionsAffected?size gt 0>
                                        Versions:
                                        <#list item.versionsAffected as version>
                                            ${version}<#sep>,
                                        </#list>
                                    </#if>
                                    <@hst.html hippohtml=item.platformText contentRewriter=gaContentRewriter/>
                                </li>

                            </#list>
                        </ul>
                    </div>
                </#if>


                <#if hasSectionContent>
                    <div id="${slugify(threatDetailsHeader)}" class="article-section">
                        <h2>${threatDetailsHeader}</h2>
                        <@sections document.sections></@sections>
                    </div>
                </#if>


                <#if hasThreatUpdates>
                    <div id="${slugify(threatUpdatesHeader)}" class="article-section">
                        <h2>${threatUpdatesHeader}</h2>

                        <table>
                            <thead>
                                <tr>
                                    <th>Date</th>
                                    <th>Update</th>
                                </tr>
                            </thead>
                            <tbody>

                                <#list document.threatUpdates?sort_by("date")?reverse as item>
                                    <tr>
                                        <td><@fmt.formatDate value=item.date.time?date type="date" pattern="d MMM yyyy" timeZone="${getTimeZone()}" /></td>
                                        <td>
                                            <span class="strong">${item.title}</span>
                                            <@hst.html hippohtml=item.content contentRewriter=gaContentRewriter/>
                                        </td>
                                    </tr>
                                </#list>
                            </tbody>
                        </table>

                    </div>
                </#if>


                <#if hasRemediationIntro>
                    <div id="${slugify(remediationIntroHeader)}" class="article-section">
                        <h2>${remediationIntroHeader}</h2>
                        <@hst.html hippohtml=document.remediationIntro contentRewriter=gaContentRewriter/>
                    </div>
                </#if>


                <#if hasRemediationSteps>
                    <div id="${slugify(remediationStepsHeader)}" class="article-section">
                        <h2>${remediationStepsHeader}</h2>

                        <table>
                            <thead>
                                <tr>
                                    <th>Type</th>
                                    <th>Step</th>
                                </tr>
                            </thead>
                            <tbody>
                                <#list document.remediationSteps as item>
                                    <tr>
                                        <td><#if item.type??><span class="tag">${item.type}</span></#if></td>
                                        <td><@hst.html hippohtml=item.step contentRewriter=gaContentRewriter/><br /><#if item.link??><a href="${item.link}" onClick="logGoogleAnalyticsEvent('Link click','Cyber alert','${item.link}');" onKeyUp="return vjsu.onKeyUp(event)">${item.link}</a></#if></td>
                                    </tr>
                                </#list>
                            </tbody>
                        </table>
                    </div>
                </#if>


                <#if hasIndicatorsOfCompromise>
                    <div id="${slugify(indicatorsOfCompromiseHeader)}" class="article-section">
                        <h2>${indicatorsOfCompromiseHeader}</h2>
                        <@sections document.indicatorsCompromise></@sections>
                    </div>
                </#if>


                <#if hasNcscLink>
                    <div id="${slugify(ncscLinkHeader)}" class="article-section">
                        <h2>${ncscLinkHeader}</h2>
                        <a href="${document.ncscLink}" onClick="logGoogleAnalyticsEvent('Link click','Cyber alert','${document.ncscLink}');" onKeyUp="return vjsu.onKeyUp(event)">${document.ncscLink}</a>
                    </div>
                </#if>

                 <#if hasSourceOfUpdate>
                    <div id="${slugify(sourceOfUpdateHeader)}" class="article-section">
                        <h2>${sourceOfUpdateHeader}</h2>
                        <ul>
                            <#list document.sourceOfThreatUpdates as item>
                                <li><a href="${item}" onClick="logGoogleAnalyticsEvent('Link click','Cyber alert','${item}');" onKeyUp="return vjsu.onKeyUp(event)">${item}</a></li>
                            </#list>
                        </ul>
                    </div>
                </#if>

                <#if hasServices>
                    <div id="${slugify(servicesHeader)}" class="article-section">
                        <h2>${servicesHeader}</h2>
                        <ul>
                            <#list document.services as item>
                                <li>
                                    <a class="cta__title cta__button" href="<@hst.link hippobean=item/>">
                                        ${item.title}
                                    </a>
                                </li>
                            </#list>
                        </ul>
                    </div>
                </#if>


                <#if hasCVE>
                    <div id="${slugify(cveHeader)}" class="article-section">
                        <h2>${cveHeader}</h2>
                        <ul>
                            <#list document.cveIdentifiers as item>
                                <li>
                                    <div>${item.cveIdentifier}</div>
                                    <div><#if item.cveStatus??>Status: ${item.cveStatus}</#if></div>
                                    <div><@hst.html hippohtml=item.cveText contentRewriter=gaContentRewriter/></div>
                                </li>
                            </#list>
                        </ul>
                    </div>
                </#if>


                <#if hasAcknowledgement>
                    <div id="${slugify(acknowledgementHeader)}" class="article-section">
                        <h2>${acknowledgementHeader}</h2>
                        <#list document.cyberAcknowledgements as item>
                            <div class="emphasis-box emphasis-box-emphasis" aria-label="Emphasis">

                                <div class="emphasis-box__content">
                                    <a href="${item.linkAddress}">${item.linkAddress}</a>


                                    <div data-uipath="website.contentblock.emphasis.content">
                                        Response by: <@fmt.formatDate value=item.responseDatetime.time?date type="date" pattern="d MMM yyyy HH:mm:ss" timeZone="${getTimeZone()}" />
                                    </div>

                                </div>
                            </div>
                        </#list>
                    </div>
                </#if>

                <div class="article-section muted">
                    <@lastModified document.lastModified false></@lastModified>
                </div>

            </div>
        </div>
    </div>
</article>

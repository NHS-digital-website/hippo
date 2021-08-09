<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/heroes/hero-options.ftl">
<#include "macro/heroes/hero.ftl">
<#include "macro/contentPixel.ftl">

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

<@hst.html hippohtml=document.summary var="summaryTextArea"/>
<#assign hasSummaryContent = document.summary?? && document.summary?has_content && summaryTextArea?? && summaryTextArea?has_content />
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
<#assign cveUrl = "https://cve.mitre.org/cgi-bin/cvename.cgi?name=" />


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

<#function cveStatusColour status="">
    <#if status="Published">
        <#return "light-green">
    <#elseif status="Rejected">
        <#return "light-red">
    <#elseif status="Reserved">
        <#return "light-blue">
    <#--  Fallback in case new status added  -->
    <#else>
        <#return "light-grey">
    </#if>
</#function>

<#assign heroOptions = getHeroOptions(document)/>
<#assign heroOptions += {"colour": "darkBlue"}/>

<#assign metadata = [] />
<#if document.threatId?has_content><#assign metadata += [ { "title": threatIdLabel, "value": document.threatId } ] /></#if>
<#if document.category?has_content><#assign metadata += [ { "title": categoryLabel, "value": document.category } ] /></#if>
<#if document.severity?has_content><#assign metadata += [ { "title": threatSeverityLabel, "value": document.severity } ] /></#if>
<#if document.threatvector?has_content><#assign metadata += [ { "title": threatVectorLabel, "value": document.threatvector } ] /></#if>
<#if document.publishedDate?has_content>
    <@fmt.formatDate value=document.publishedDate.time type="Date" pattern="d MMMM yyyy h:mm a" timeZone="${getTimeZone()}" var="date" />
    <#assign metadata += [ { "title": datePublishedLabel, "value": date } ] />
</#if>
<#assign heroOptions += {"metaData": metadata}/>

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<article>
    <@hero heroOptions/>

    <div class="nhsd-m-notification-banner nhsd-m-notification-banner--warning nhsd-m-notification-banner--irremovable nhsd-!t-margin-bottom-8">
        <div class="nhsd-a-box nhsd-a-box--bg-light-yellow">
            <div class="nhsd-t-grid">
                <div class="nhsd-t-row">
                    <div class="nhsd-t-col-12">
                        <div class="nhsd-m-notification-banner__row">

                            <div class="nhsd-m-notification-banner__left-col">
                                <span class="nhsd-a-icon nhsd-a-icon--size-xl ">
                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                        <path d="M8,16c-4.4,0-8-3.6-8-8s3.6-8,8-8s8,3.6,8,8S12.4,16,8,16z M8,2.3C4.9,2.3,2.3,4.9,2.3,8s2.6,5.7,5.7,5.7 s5.7-2.6,5.7-5.7S11.1,2.3,8,2.3z M8,12.9c-0.8,0-1.4-0.7-1.4-1.4C6.6,10.7,7.2,10,8,10c0.8,0,1.4,0.7,1.4,1.4 C9.4,12.2,8.8,12.9,8,12.9z M9.1,3.4l-0.3,6H7.1l-0.3-6H9.1z" />
                                    </svg>
                                </span>
                            </div>

                            <div class="nhsd-m-notification-banner__middle-col">
                                Report a cyber attack: call
                                <a class="nhsd-a-link"
                                   href="tel:004403003035222"
                                   onClick="logGoogleAnalyticsEvent('Link click','Cyber alert','tel:004403003035222');"
                                   onKeyUp="return vjsu.onKeyUp(event)"
                                   title="Contact us by telephone"
                                >
                                    0300 303 5222
                                    <span class="nhsd-t-sr-only"></span>
                                </a>
                                or email
                                <a class="nhsd-a-link"
                                   href="mailto:${emailLabel}"
                                   onClick="logGoogleAnalyticsEvent('Link click','Cyber alert','mailto:${emailLabel}');"
                                   onKeyUp="return vjsu.onKeyUp(event)"
                                   title="${emailTitleLabel}"
                                >
                                    ${emailLabel}
                                    <span class="nhsd-t-sr-only"></span>
                                </a>
                            </div>

                            <div class="nhsd-m-notification-banner__right-col">
                                <span class="nhsd-a-icon nhsd-a-icon--size-s nhsd-a-icon--col-white">
                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                        <polygon points="13.9,1 8,6.9 2.1,1 1,2.1 6.9,8 1,13.9 2.1,15 8,9.1 13.9,15 15,13.9 9.1,8 15,2.1 "/>
                                    </svg>
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="nhsd-t-grid" id="document-content">
        <div class="grid-row">
            <div class="nhsd-t-col-12">
                <#if document.archiveContent?? && document.archiveContent>
                    <#assign section = { "emphasisType": "Important", "heading": archiveContentHeading, "bodyCustom": archiveContentBody  } />
                    <@emphasisBox section=section />
                </#if>
            </div>
        </div>

        <div class="nhsd-t-row">

            <div class="nhsd-t-col-xs-12 nhsd-t-col-s-4" id="sticky-nav">
                <@stickyNavSections getStickySectionNavLinks({"sections": links, "includeSummary": true })></@stickyNavSections>
            </div>

            <div class="nhsd-t-col-xs-12 nhsd-t-col-s-8">
                <#if hasSummaryContent>
                    <div id="${slugify(summaryHeader)}">
                        <p class="nhsd-t-heading-xl">${summaryHeader}</p>

                        <div data-uipath="website.publishedwork.summary">
                            <@hst.html hippohtml=document.summary contentRewriter=brContentRewriter/>
                        </div>
                    </div>
                </#if>

                <#if hasThreatAffects>
                    <#if hasSummaryContent>
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>

                    <#assign platformAffectedWithDoc = false />
                    <#assign platformTextList = [] />

                    <div id="${slugify(affectedPlatformsHeader)}">
                        <p class="nhsd-t-heading-xl">${affectedPlatformsHeader}</p>
                        <p class="nhsd-t-body">The following platforms are known to be affected:</p>

                        <div class="nhsd-o-card-list">
                            <div class="nhsd-t-grid nhsd-!t-no-gutters">
                                <div class="nhsd-t-row nhsd-o-card-list__items nhsd-!t-margin-bottom-0">
                                    <#list document.threatAffects as item>
                                        <#if item.platformAffected??>

                                            <#if platformAffectedWithDoc = false>
                                                <#assign platformAffectedWithDoc = true />
                                            </#if>

                                            <div class="nhsd-t-col-12 nhsd-!t-padding-left-0 nhsd-!t-padding-right-0">
                                                <div class="nhsd-m-card">
                                                    <#if item.platformAffected.url??>
                                                    <a class="nhsd-a-box-link"
                                                       href="${item.platformAffected.url}"
                                                       onClick="${getOnClickMethodCall(document.class.name, item.platformAffected.url)}"
                                                       onKeyUp="return vjsu.onKeyUp(event)"
                                                       aria-label="${item.platformAffected.title}"
                                                    >
                                                    </#if>
                                                        <div class="nhsd-a-box nhsd-a-box--bg-light-grey">
                                                            <div class="nhsd-m-card__content_container">
                                                                <div class="nhsd-m-card__content-box">

                                                                    <span class="nhsd-m-card__date nhsd-!t-margin-bottom-0">
                                                                        <#if item.versionsAffected?? && item.versionsAffected?size gt 0>
                                                                            Versions:
                                                                            <#list item.versionsAffected as version>
                                                                                ${version}<#sep>,
                                                                            </#list>
                                                                        </#if>
                                                                    </span>

                                                                    <p class="nhsd-t-heading-s">
                                                                        ${item.platformAffected.title}
                                                                    </p>

                                                                    <@hst.html hippohtml=item.platformText contentRewriter=brContentRewriter/>
                                                                </div>

                                                                <#if item.platformAffected.url??>
                                                                    <div class="nhsd-m-card__button-box">
                                                                        <span class="nhsd-a-icon nhsd-a-arrow nhsd-a-icon--size-s">
                                                                            <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                                                                <path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/>
                                                                            </svg>
                                                                        </span>
                                                                    </div>
                                                                <#else>
                                                                    <div style="padding-bottom: 1.111rem"></div>
                                                                </#if>
                                                            </div>
                                                        </div>
                                                    <#if item.platformAffected.url??>
                                                    </a>
                                                    </#if>
                                                </div>
                                            </div>
                                        <#else>
                                            <@hst.html hippohtml=item.platformText var="platformText" />
                                            <#if platformText?has_content>
                                                <#assign platformTextList = platformTextList + [item.platformText] />
                                            </#if>
                                        </#if>
                                    </#list>
                                </div>
                            </div>
                        </div>
                    </div>

                    <#if platformTextList?has_content>
                        <#if platformAffectedWithDoc>
                            <p class="nhsd-t-body">The following platforms are also known to be affected:</p>
                        </#if>
                        <#list platformTextList as platformText>
                            <@hst.html hippohtml=platformText contentRewriter=brContentRewriter />
                        </#list>
                    </#if>

                    <#--  If no cards and only bullet points then place horizontal rule  -->
                    <#if !platformAffectedWithDoc && platformTextList?has_content>
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>
                </#if>

                <#if hasSectionContent>
                    <#if !hasThreatAffects || (hasSummaryContent && !hasThreatAffects)>
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>

                    <div id="${slugify(threatDetailsHeader)}">
                        <p class="nhsd-t-heading-xl">${threatDetailsHeader}</p>
                        <@sections document.sections></@sections>
                    </div>
                </#if>

                <#if hasThreatUpdates>
                    <#if hasSectionContent ||
                        (hasThreatAffects && !hasSectionContent) ||
                        (hasSummaryContent && !hasSectionContent && !hasThreatAffects)
                    >
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>

                    <div id="${slugify(threatUpdatesHeader)}">
                        <p class="nhsd-t-heading-xl">${threatUpdatesHeader}</p>

                        <div class="nhsd-m-table nhsd-t-body">
                            <table data-responsive>
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
                                                <span class="nhsd-t-body">${item.title}</span>
                                                <@hst.html hippohtml=item.content contentRewriter=brContentRewriter/>
                                            </td>
                                        </tr>
                                    </#list>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </#if>


                <#if hasRemediationIntro>
                    <#if hasThreatUpdates ||
                        (hasSectionContent && !hasThreatUpdates) ||
                        (hasThreatAffects && !hasThreatUpdates && !hasSectionContent) ||
                        (hasSummaryContent && !hasThreatUpdates && hasSectionContent && !hasThreatAffects)
                    >
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>

                    <div id="${slugify(remediationIntroHeader)}">
                        <p class="nhsd-t-heading-xl">${remediationIntroHeader}</p>
                        <@hst.html hippohtml=document.remediationIntro contentRewriter=brContentRewriter/>
                    </div>
                </#if>


                <#if hasRemediationSteps>
                    <#if hasRemediationIntro ||
                        (hasThreatUpdates && !hasRemediationIntro) ||
                        (hasSectionContent && !hasRemediationIntro && !hasThreatUpdates) ||
                        (hasThreatAffects && !hasRemediationIntro && !hasThreatUpdates && !hasSectionContent) ||
                        (hasSummaryContent && !hasRemediationIntro && !hasThreatUpdates && hasSectionContent && !hasThreatAffects)
                    >
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>

                    <div id="${slugify(remediationStepsHeader)}">
                        <p class="nhsd-t-heading-xl">${remediationStepsHeader}</p>

                        <div class="nhsd-m-table nhsd-t-body">
                            <table data-responsive>
                                <thead>
                                    <tr>
                                        <th>Type</th>
                                        <th>Step</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <#list document.remediationSteps as item>
                                        <tr>
                                            <td><#if item.type??><span class="nhsd-t-body">${item.type}</span></#if></td>
                                            <td><@hst.html hippohtml=item.step contentRewriter=brContentRewriter/><br /><#if item.link??><a class="nhsd-a-link" href="${item.link}" onClick="logGoogleAnalyticsEvent('Link click','Cyber alert','${item.link}');" onKeyUp="return vjsu.onKeyUp(event)">${item.link}</a></#if></td>
                                        </tr>
                                    </#list>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </#if>


                <#if hasIndicatorsOfCompromise>
                    <#if hasRemediationSteps ||
                        (hasRemediationIntro && !hasRemediationSteps)||
                        (hasThreatUpdates && !hasRemediationSteps && !hasRemediationIntro) ||
                        (hasSectionContent && !hasRemediationSteps && !hasRemediationIntro && !hasThreatUpdates) ||
                        (hasThreatAffects && !hasRemediationSteps && !hasRemediationIntro && !hasThreatUpdates && !hasSectionContent) ||
                        (hasSummaryContent && !hasRemediationSteps && !hasRemediationIntro && !hasThreatUpdates && hasSectionContent && !hasThreatAffects)
                    >
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>

                    <div id="${slugify(indicatorsOfCompromiseHeader)}">
                        <p class="nhsd-t-heading-xl">${indicatorsOfCompromiseHeader}</p>
                        <@sections document.indicatorsCompromise></@sections>
                    </div>
                </#if>

                <#if hasNcscLink>
                    <#if hasIndicatorsOfCompromise ||
                        (hasRemediationSteps && !hasIndicatorsOfCompromise) ||
                        (hasRemediationIntro && !hasIndicatorsOfCompromise && !hasRemediationSteps)||
                        (hasThreatUpdates && !hasIndicatorsOfCompromise && !hasRemediationSteps && !hasRemediationIntro) ||
                        (hasSectionContent && !hasIndicatorsOfCompromise && !hasRemediationSteps && !hasRemediationIntro && !hasThreatUpdates) ||
                        (hasThreatAffects && !hasIndicatorsOfCompromise && !hasRemediationSteps && !hasRemediationIntro && !hasThreatUpdates && !hasSectionContent) ||
                        (hasSummaryContent && !hasIndicatorsOfCompromise && !hasRemediationSteps && !hasRemediationIntro && !hasThreatUpdates && hasSectionContent && !hasThreatAffects)
                    >
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>

                    <div id="${slugify(ncscLinkHeader)}">
                        <p class="nhsd-t-heading-xl">${ncscLinkHeader}</p>
                        <a class="nhsd-a-link"
                           href="${document.ncscLink}"
                           onClick="logGoogleAnalyticsEvent('Link click','Cyber alert','${document.ncscLink}');"
                           onKeyUp="return vjsu.onKeyUp(event)"
                        >
                            ${document.ncscLink}
                        </a>
                    </div>
                </#if>

                 <#if hasSourceOfUpdate>
                    <#if hasNcscLink ||
                        (hasIndicatorsOfCompromise && !hasNcscLink) ||
                        (hasRemediationSteps && !hasNcscLink && !hasIndicatorsOfCompromise) ||
                        (hasRemediationIntro && !hasNcscLink && !hasIndicatorsOfCompromise && !hasRemediationSteps)||
                        (hasThreatUpdates && !hasNcscLink && !hasIndicatorsOfCompromise && !hasRemediationSteps && !hasRemediationIntro) ||
                        (hasSectionContent && !hasNcscLink && !hasIndicatorsOfCompromise && !hasRemediationSteps && !hasRemediationIntro && !hasThreatUpdates) ||
                        (hasThreatAffects && !hasNcscLink && !hasIndicatorsOfCompromise && !hasRemediationSteps && !hasRemediationIntro && !hasThreatUpdates && !hasSectionContent) ||
                        (hasSummaryContent && !hasNcscLink && !hasIndicatorsOfCompromise && !hasRemediationSteps && !hasRemediationIntro && !hasThreatUpdates && hasSectionContent && !hasThreatAffects)
                    >
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>

                    <div id="${slugify(sourceOfUpdateHeader)}">
                        <p class="nhsd-t-heading-xl">${sourceOfUpdateHeader}</p>
                        <ul class="nhsd-t-list nhsd-t-list--bullet nhsd-t-list--loose">
                            <#list document.sourceOfThreatUpdates as item>
                                <li>
                                    <a class="nhsd-a-link"
                                       href="${item}"
                                       onClick="logGoogleAnalyticsEvent('Link click','Cyber alert','${item}');"
                                       onKeyUp="return vjsu.onKeyUp(event)"
                                    >
                                        ${item}
                                    </a>
                                </li>
                            </#list>
                        </ul>
                    </div>
                </#if>

                <#if hasServices>
                    <#if hasSourceOfUpdate ||
                        (hasNcscLink && !hasSourceOfUpdate) ||
                        (hasIndicatorsOfCompromise && !hasSourceOfUpdate && !hasNcscLink) ||
                        (hasRemediationSteps && !hasSourceOfUpdate && !hasNcscLink && !hasIndicatorsOfCompromise) ||
                        (hasRemediationIntro && !hasSourceOfUpdate && !hasNcscLink && !hasIndicatorsOfCompromise && !hasRemediationSteps)||
                        (hasThreatUpdates && !hasSourceOfUpdate && !hasNcscLink && !hasIndicatorsOfCompromise && !hasRemediationSteps && !hasRemediationIntro) ||
                        (hasSectionContent && !hasSourceOfUpdate && !hasNcscLink && !hasIndicatorsOfCompromise && !hasRemediationSteps && !hasRemediationIntro && !hasThreatUpdates) ||
                        (hasThreatAffects && !hasSourceOfUpdate && !hasNcscLink && !hasIndicatorsOfCompromise && !hasRemediationSteps && !hasRemediationIntro && !hasThreatUpdates && !hasSectionContent) ||
                        (hasSummaryContent && !hasSourceOfUpdate && !hasNcscLink && !hasIndicatorsOfCompromise && !hasRemediationSteps && !hasRemediationIntro && !hasThreatUpdates && hasSectionContent && !hasThreatAffects)
                    >
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>

                    <div id="${slugify(servicesHeader)}">
                        <p class="nhsd-t-heading-xl">${servicesHeader}</p>
                        <ul class="nhsd-t-list nhsd-t-list--bullet nhsd-t-list--loose">
                            <#list document.services as item>
                                <@hst.link hippobean=item var="serviceLink"/>
                                <li>
                                    <a class="nhsd-a-link" href="${serviceLink}" onClick="${getOnClickMethodCall(document.class.name, serviceLink)}" onKeyUp="return vjsu.onKeyUp(event)">
                                        ${item.title}
                                    </a>
                                </li>
                            </#list>
                        </ul>
                    </div>
                </#if>


                <#if hasCVE>
                    <#if hasServices ||
                        (hasSourceOfUpdate && !hasServices) ||
                        (hasNcscLink && !hasServices && !hasSourceOfUpdate) ||
                        (hasIndicatorsOfCompromise && !hasServices && !hasSourceOfUpdate && !hasNcscLink) ||
                        (hasRemediationSteps && !hasServices && !hasSourceOfUpdate && !hasNcscLink && !hasIndicatorsOfCompromise) ||
                        (hasRemediationIntro && !hasServices && !hasSourceOfUpdate && !hasNcscLink && !hasIndicatorsOfCompromise && !hasRemediationSteps)||
                        (hasThreatUpdates && !hasServices && !hasSourceOfUpdate && !hasNcscLink && !hasIndicatorsOfCompromise && !hasRemediationSteps && !hasRemediationIntro) ||
                        (hasSectionContent && !hasServices && !hasSourceOfUpdate && !hasNcscLink && !hasIndicatorsOfCompromise && !hasRemediationSteps && !hasRemediationIntro && !hasThreatUpdates) ||
                        (hasThreatAffects && !hasServices && !hasSourceOfUpdate && !hasNcscLink && !hasIndicatorsOfCompromise && !hasRemediationSteps && !hasRemediationIntro && !hasThreatUpdates && !hasSectionContent) ||
                        (hasSummaryContent && !hasServices && !hasSourceOfUpdate && !hasNcscLink && !hasIndicatorsOfCompromise && !hasRemediationSteps && !hasRemediationIntro && !hasThreatUpdates && hasSectionContent && !hasThreatAffects)
                    >
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>

                    <div id="${slugify(cveHeader)}">
                        <p class="nhsd-t-heading-xl">${cveHeader}</p>

                        <div class="nhsd-o-card-list">
                            <div class="nhsd-t-grid nhsd-!t-no-gutters">
                                <div class="nhsd-t-row nhsd-o-card-list__items">
                                    <#list document.cveIdentifiers as item>
                                        <div class="nhsd-t-col-12 nhsd-!t-padding-left-0 nhsd-!t-padding-right-0">
                                            <div class="nhsd-m-card">
                                                <a class="nhsd-a-box-link nhsd-a-box-link--focus-orange"
                                                   href="${cveUrl + item.cveIdentifier}"
                                                   onClick="logGoogleAnalyticsEvent('Link click','Cyber alert','${cveUrl + item.cveIdentifier}');"
                                                   onKeyUp="return vjsu.onKeyUp(event"
                                                   aria-label="${item.cveIdentifier}"
                                                >
                                                    <div class="nhsd-a-box nhsd-a-box--bg-white nhsd-a-box--border-grey">
                                                        <div class="nhsd-m-card__content_container">
                                                            <div class="nhsd-m-card__content-box">

                                                                <#if item.cveStatus?? && item.cveStatus != "Not Known">
                                                                    <div class="nhsd-m-card__tag-list">
                                                                        <span class="nhsd-a-tag nhsd-a-tag--bg-dark-grey">Status</span>
                                                                        <span class="nhsd-a-tag nhsd-a-tag--bg-${cveStatusColour(item.cveStatus)}">${item.cveStatus}</span>
                                                                    </div>
                                                                </#if>

                                                                <p class="nhsd-t-heading-s">${item.cveIdentifier}</p>
                                                                <@hst.html hippohtml=item.cveText contentRewriter=brContentRewriter/>
                                                            </div>

                                                            <div class="nhsd-m-card__button-box">
                                                                <span class="nhsd-a-icon nhsd-a-arrow nhsd-a-icon--size-s nhsd-a-icon--col-black">
                                                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                                                        <path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/>
                                                                    </svg>
                                                                </span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </a>
                                            </div>
                                        </div>
                                    </#list>
                                </div>
                            </div>
                        </div>
                    </div>
                </#if>


                <#if hasAcknowledgement>
                    <#if !hasCVE ||
                        (hasServices && !hasCVE) ||
                        (hasSourceOfUpdate && !hasCVE && !hasServices) ||
                        (hasNcscLink && !hasCVE && !hasServices && !hasSourceOfUpdate) ||
                        (hasIndicatorsOfCompromise && !hasCVE && !hasServices && !hasSourceOfUpdate && !hasNcscLink) ||
                        (hasRemediationSteps && !hasCVE && !hasServices && !hasSourceOfUpdate && !hasNcscLink && !hasIndicatorsOfCompromise) ||
                        (hasRemediationIntro && !hasCVE && !hasServices && !hasSourceOfUpdate && !hasNcscLink && !hasIndicatorsOfCompromise && !hasRemediationSteps)||
                        (hasThreatUpdates && !hasCVE && !hasServices && !hasSourceOfUpdate && !hasNcscLink && !hasIndicatorsOfCompromise && !hasRemediationSteps && !hasRemediationIntro) ||
                        (hasSectionContent && !hasCVE && !hasServices && !hasSourceOfUpdate && !hasNcscLink && !hasIndicatorsOfCompromise && !hasRemediationSteps && !hasRemediationIntro && !hasThreatUpdates) ||
                        (hasThreatAffects && !hasCVE && !hasServices && !hasSourceOfUpdate && !hasNcscLink && !hasIndicatorsOfCompromise && !hasRemediationSteps && !hasRemediationIntro && !hasThreatUpdates && !hasSectionContent) ||
                        (hasSummaryContent && !hasCVE && !hasServices && !hasSourceOfUpdate && !hasNcscLink && !hasIndicatorsOfCompromise && !hasRemediationSteps && !hasRemediationIntro && !hasThreatUpdates && hasSectionContent && !hasThreatAffects)
                    >
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>

                    <div id="${slugify(acknowledgementHeader)}">
                        <p class="nhsd-t-heading-xl">${acknowledgementHeader}</p>

                        <div class="nhsd-o-card-list">
                            <div class="nhsd-t-grid nhsd-t-grid--nested">
                                <div class="nhsd-t-row nhsd-o-card-list__items nhsd-t-row--centred">
                                    <#list document.cyberAcknowledgements as item>
                                        <div class="nhsd-t-col-xs-12 nhsd-t-col-s-6">
                                            <div class="nhsd-m-card">
                                                <a class="nhsd-a-box-link"
                                                   href="${item.linkAddress}"
                                                   onClick="${getOnClickMethodCall(document.class.name, item.linkAddress)}"
                                                   onKeyUp="return vjsu.onKeyUp(event)"
                                                   aria-label="${item.linkAddress}"
                                                >
                                                    <div class="nhsd-a-box nhsd-a-box--bg-<#if item?is_odd_item>dark-grey<#else>blue</#if>">
                                                        <div class="nhsd-m-card__content_container">
                                                            <div class="nhsd-m-card__content-box">
                                                                <span class="nhsd-m-card__date nhsd-t-word-break">Response by: <@fmt.formatDate value=item.responseDatetime.time?date type="date" pattern="d MMM yyyy HH:mm:ss" timeZone="${getTimeZone()}" /></span>
                                                                <p class="nhsd-t-heading-s nhsd-t-word-break">${item.linkAddress}</p>
                                                            </div>

                                                            <div class="nhsd-m-card__button-box">
                                                                <span class="nhsd-a-icon nhsd-a-icon--size-s nhsd-a-icon--col-white nhsd-a-arrow">
                                                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                                                        <path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/>
                                                                    </svg>
                                                                </span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </a>
                                            </div>
                                        </div>
                                    </#list>
                                </div>
                            </div>
                        </div>
                    </div>
                </#if>

                <div class="nhsd-!t-margin-top-6">
                    <@lastModified document.lastModified false></@lastModified>
                </div>
            </div>
        </div>
    </div>
</article>

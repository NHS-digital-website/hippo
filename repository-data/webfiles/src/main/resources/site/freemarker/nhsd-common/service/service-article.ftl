<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Service" -->

<#include "../../include/imports.ftl">
<#include "../macro/sections/sections.ftl">
<#include "../macro/stickyNavSections.ftl">
<#include "../macro/furtherInformationSection.ftl">
<#include "../macro/metaTags.ftl">
<#include "../macro/component/lastModified.ftl">
<#include "../macro/latestblogs.ftl">
<#include "../macro/component/calloutBox.ftl">
<#include "../macro/contentPixel.ftl">
<#import "../app-layout-head.ftl" as alh>
<#include "../macro/heroes/hero.ftl">
<#include "../macro/heroes/hero-options.ftl">

<@hst.headContribution category="metadata">
    <meta name="robots" content="${document.noIndexControl?then("noindex","index")}"/>
</@hst.headContribution>

<#-- Add meta tags -->
<@metaTags></@metaTags>

<#if document.rawMetadata?has_content>
    <#list document.rawMetadata as md>
        <@hst.headContribution category="metadata">
            ${md?no_esc}
        </@hst.headContribution>
    </#list>
</#if>

<@hst.setBundle basename="rb.generic.headers,publicationsystem.headers"/>
<#assign hasSummaryContent = document.summary.content?has_content />
<#assign hasSectionContent = document.sections?has_content />
<#assign hasPriorityActions = document.priorityActions?has_content />
<#assign hasChildPages = childPages?has_content />
<#assign hasIntroductionContent = document.introduction?? />
<#assign hasContactDetailsContent = document.contactdetails?? && document.contactdetails.content?has_content />
<#assign idsuffix = slugify(document.title) />
<#assign navStatus = document.navigationController />
<#assign hasBannerImage = document.image?has_content />
<#assign custom_summary = document.summary />
<#assign sectionTitlesFound = countSectionTitles(document.sections) />
<#assign renderNav = (sectionTitlesFound gte 1 || hasChildPages) || sectionTitlesFound gt 1 || hasContactDetailsContent />

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<article>

    <#assign heroType = "default" />
    <#if hasBannerImage>
        <#assign heroType = "image" />
    </#if>

    <@hero getHeroOptions(document) heroType />

    <div class="nhsd-t-grid nhsd-!t-margin-top-8">
        <div class="nhsd-t-row">
            <#if navStatus == "withNav" && renderNav>
                <div class="nhsd-t-col-xs-12 nhsd-t-col-s-3">
                    <!-- start sticky-nav -->
                        <#assign links = [{ "url": "#top", "title": "Top of page" }] />
                        <#if document.latestNews?has_content >
                            <#assign links += [{ "url": "#related-articles-latest-news-${idsuffix}", "title": 'Latest news' }] />
                        </#if>
                        <#assign links += getStickySectionNavLinks({ "document": document, "childPages": childPages, "includeTopLink": false }) />
                        <#if document.relatedEvents?has_content >
                            <#assign links += [{ "url": "#related-articles-events-${idsuffix}", "title": 'Forthcoming events' }] />
                        </#if>
                        <@stickyNavSections links=links></@stickyNavSections>
                    <!-- end sticky-nav -->
                    <#-- Restore the bundle -->
                    <@hst.setBundle basename="rb.generic.headers,publicationsystem.headers"/>
                </div>
            </#if>

            <div class="${(navStatus == "withNav" || navStatus == "withoutNav")?then("nhsd-t-col-xs-12 nhsd-t-col-s-8 nhsd-t-off-s-1", "nhsd-t-col-12")}">
                <#if document.updates?has_content>
                    <#assign item = {} />
                    <#list document.updates as update>
                        <#assign item += update />
                        <#assign item += {"calloutType":"update", "index":update?index} />
                        <@calloutBox item document.class.name />
                    </#list>
                </#if>

                <#if document.priorityActions?has_content>
                    <div class="nhsd-o-card-list">
                        <div class="nhsd-t-grid nhsd-!t-no-gutters">
                            <div class="nhsd-t-row nhsd-o-card-list__items ">
                                <#list document.priorityActions as action>
                                    <div class="nhsd-t-col-12 nhsd-!t-no-gutters">
                                        <div class="nhsd-m-card">
                                            <@hst.link hippobean=action.link.link var="priorityActionInternalLink"/>
                                            <a <#if action.link.linkType == "internal">
                                               href="${priorityActionInternalLink}"
                                               onClick="${getOnClickMethodCall(document.class.name, priorityActionInternalLink)}"
                                               <#else>
                                               href="${action.link.link}"
                                               onClick="${getOnClickMethodCall(document.class.name, action.link.link)}"
                                               </#if>
                                               onKeyUp="return vjsu.onKeyUp(event)"
                                               class="nhsd-a-box-link"
                                               aria-label="${action.action}"
                                            >
                                                <div class="nhsd-a-box nhsd-a-box--bg-dark-green">
                                                    <div class="nhsd-m-card__content_container">
                                                        <div class="nhsd-m-card__content-box">
                                                            <p class="nhsd-t-heading-s">${action.action}</p>

                                                            <#if action.additionalInformation?has_content>
                                                                <p class="nhsd-t-body-s">${action.additionalInformation}</p>
                                                            </#if>
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
                </#if>

                <#if document.latestNews?has_content>
                    <#assign latestNews = document.latestNews />
                    <#if document.relatedNews?has_content>
                        <#assign latestNews += document.relatedNews />
                    </#if>
                    <@latestblogs latestNews 'Service' 'latest-news-' + idsuffix 'Latest news' false />
                </#if>

                <#if hasIntroductionContent>
                    <@hst.html hippohtml=document.introduction contentRewriter=brContentRewriter/>
                </#if>

                <#if hasSectionContent>
                    <@sections document.sections></@sections>
                </#if>

                <#if hasContactDetailsContent>
                    <#if hasSectionContent>
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>
                    <div id="${slugify('Contact details')}">
                        <p class="nhsd-t-heading-xl"><@fmt.message key="headers.contact-details" /></p>
                        <div class="nhsd-m-contact-us nhsd-!t-margin-bottom-6">
                            <div class="nhsd-a-box nhsd-a-box--bg-light-blue-10">
                                <div class="nhsd-m-contact-us__content">
                                    <@hst.html hippohtml=document.contactdetails contentRewriter=brContentRewriter/>
                                </div>
                            </div>
                        </div>
                    </div>
                </#if>

                <#if !document.latestNews?has_content && document.relatedNews?has_content>
                    <@latestblogs document.relatedNews 'Service' 'related-news-' + idsuffix 'Related news' false />
                </#if>

                <#if hasChildPages>
                    <#if !(hasContactDetailsContent || document.relatedNews?has_content)>
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>
                    <@furtherInformationSection childPages></@furtherInformationSection>
                </#if>

                <@latestblogs document.relatedEvents 'Service' 'events-' + idsuffix 'Forthcoming events' false />

                <@lastModified document.lastModified></@lastModified>
            </div>
        </div>
    </div>
</article>
<#if document.htmlCode?has_content>
    ${document.htmlCode?no_esc}
</#if>

<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Service" -->

<#include "../../include/imports.ftl">
<#include "../macro/documentHeader.ftl">
<#include "../macro/sections/sections.ftl">
<#include "../macro/stickyNavSections.ftl">
<#include "../macro/furtherInformationSection.ftl">
<#include "../macro/metaTags.ftl">
<#include "../macro/component/lastModified.ftl">
<#include "../macro/latestblogs.ftl">
<#include "../macro/component/calloutBox.ftl">

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

<#assign hasSectionContent = document.sections?has_content />
<#assign hasPriorityActions = document.priorityActions?has_content />
<#assign hasTopTasks = document.toptasks?has_content && !hasPriorityActions />
<#assign hasChildPages = childPages?has_content />
<#assign hasIntroductionContent = document.introduction?? />
<#assign hasContactDetailsContent = document.contactdetails?? && document.contactdetails.content?has_content />
<#assign idsuffix = slugify(document.title) />

<#assign sectionTitlesFound = countSectionTitles(document.sections) />
<#assign renderNav = (sectionTitlesFound gte 1 || hasChildPages) || sectionTitlesFound gt 1 || hasContactDetailsContent />

<article class="article article--service">

    <@documentHeader document 'service' '' '' '' '' false></@documentHeader>

    <div class="grid-wrapper grid-wrapper--article">

        <#if document.updates?has_content>
            <div class="grid-row">
                <div class="column column--no-padding">
                    <div class="callout-box-group">
                        <#assign item = {} />
                        <#list document.updates as update>
                            <#assign item += update />
                            <#assign item += {"calloutType":"update", "index":update?index} />
                            <@calloutBox item />
                        </#list>
                    </div>
                </div>
            </div>
        </#if>


        <div class="grid-row">

            <#if renderNav>
            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <!-- start sticky-nav -->
                <div id="sticky-nav">

                    <#assign links = [{ "url": "#top", "title": "Top of page" }] />
                    <#if document.latestNews?has_content >
                          <#assign links += [{ "url": "#related-articles-latest-news-${idsuffix}", "title": 'Latest news' }] />
                    </#if>
                    <#assign links += getStickySectionNavLinks({ "document": document, "childPages": childPages, "includeTopLink": false }) />
                    <#if document.relatedEvents?has_content >
                          <#assign links += [{ "url": "#related-articles-events-${idsuffix}", "title": 'Forthcoming events' }] />
                    </#if>
                    <@stickyNavSections links=links></@stickyNavSections>
                </div>
                <!-- end sticky-nav -->
                <#-- Restore the bundle -->
                <@hst.setBundle basename="rb.generic.headers,publicationsystem.headers"/>
            </div>
            </#if>

            <div class="column column--two-thirds page-block page-block--main">

                <#if document.priorityActions?has_content>
                    <div class="grid-row no-top-margin">
                        <div class="column column--reset">
                            <ul class="priority-actions" aria-label="Priority Actions">
                                <#list document.priorityActions as action>
                                    <li class="priority-action">
                                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class="priority-action__arrow" aria-hidden="true">
                                            <path d="M12 2a10 10 0 0 0-9.95 9h11.64L9.74 7.05a1 1 0 0 1 1.41-1.41l5.66 5.65a1 1 0 0 1 0 1.42l-5.66 5.65a1 1 0 0 1-1.41 0 1 1 0 0 1 0-1.41L13.69 13H2.05A10 10 0 1 0 12 2z"></path>
                                        </svg>
                                        <div class="priority-action__content">
                                            <#if action.link.linkType == "internal">
                                                <a href="<@hst.link hippobean=action.link.link />" class="priority-action__link">${action.action}</a>
                                            <#else>
                                                <a href="${action.link.link}" class="priority-action__link">${action.action}</a>
                                            </#if>
                                            <#if action.additionalInformation?has_content>
                                                <p class="priority-action__description">${action.additionalInformation}</p>
                                            </#if>
                                        </div>
                                    </li>
                                </#list>
                            </ul>
                        </div>
                    </div>
                </#if>

                <#if hasTopTasks>
                    <div class="article-section article-section--highlighted">
                        <div class="callout callout--attention">
                            <h2>Top tasks</h2>
                            <div class="rich-text-content">
                                <#list document.toptasks as toptask>
                                    <@hst.html hippohtml=toptask contentRewriter=gaContentRewriter/>
                                </#list>
                            </div>
                        </div>
                    </div>
                </#if>


                <@latestblogs document.latestNews 'Service' 'latest-news-' + idsuffix 'Latest news' />

                <#if hasIntroductionContent>
                <div class="article-section no-border article-section--introduction">
                    <div class="rich-text-content">
                        <@hst.html hippohtml=document.introduction contentRewriter=gaContentRewriter/>
                    </div>
                </div>
                </#if>

                <#if hasSectionContent>
                <@sections document.sections></@sections>
                </#if>

                <#if hasContactDetailsContent>
                <div class="article-section article-section--contact" id="${slugify('Contact details')}">
                    <h2><@fmt.message key="headers.contact-details" /></h2>
                    <div class="rich-text-content">
                        <@hst.html hippohtml=document.contactdetails contentRewriter=gaContentRewriter/>
                    </div>
                </div>
                </#if>

                <#if !document.latestNews?has_content && document.relatedNews?has_content >
                  <@latestblogs document.relatedNews 'Service' 'related-news-' + idsuffix 'Related news' />
                </#if>

                <#if hasChildPages>
                    <@furtherInformationSection childPages></@furtherInformationSection>
                </#if>

                <@latestblogs document.relatedEvents 'Service' 'events-' + idsuffix 'Forthcoming events' />

                <@lastModified document.lastModified></@lastModified>
            </div>
        </div>
    </div>
</article>

<#if document.htmlCode?has_content>
  ${document.htmlCode?no_esc}
</#if>

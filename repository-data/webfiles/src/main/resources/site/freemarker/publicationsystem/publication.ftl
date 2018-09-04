<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/structured-text.ftl">
<#include "./macro/publicationHeader.ftl">
<#include "../common/macro/sections/sections.ftl">
<#include "../common/macro/sectionNav.ftl">
<#include "../common/macro/fileMetaAppendix.ftl">
<@hst.setBundle basename="publicationsystem.labels,publicationsystem.headers"/>
<#-- @ftlvariable name="publication" type="uk.nhs.digital.ps.beans.Publication" -->

<#-- Add meta tags -->
<#include "../common/macro/metaTags.ftl">
<@metaTags></@metaTags>

<#macro restrictedContentOfUpcomingPublication>
    <@publicationHeader publication=publication restricted=true/>

    <div class="grid-wrapper grid-wrapper--article" aria-label="Document Content">
        <div class="grid-row">
            <div class="column column--two-thirds page-block page-block--main">
                <div class="article-section">
                    <p data-uipath="ps.publication.upcoming-disclaimer" class="strong" itemprop="description">(Upcoming, not yet published)</p>
                </div>
            </div>
        </div>
    </div>
</#macro>

<@fmt.message key="headers.summary" var="summaryHeader" />
<@fmt.message key="headers.related-links" var="relatedLinksHeader" />
<@fmt.message key="headers.key-facts" var="keyFactsHeader" />
<@fmt.message key="headers.administrative-sources" var="administrativeResourcesHeader" />
<@fmt.message key="headers.datasets" var="datasetsHeader" />
<@fmt.message key="headers.resources" var="resourcesHeader" />

<#function getSectionNavLinks index>
    <#assign links = [] />

    <#if index?has_content>
        <#list index as i>
            <#assign links += [{ "url": "#" + slugify(i), "title": i }] />
        </#list>
    </#if>

    <#return links />
</#function>

<#macro fullContentOfPubliclyAvailablePublication>
    <@publicationHeader publication=publication/>

    <div class="grid-wrapper grid-wrapper--article" aria-label="Document Content">
        <div class="grid-row">
            <#if index?has_content && index?size gt 1>
            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <div id="sticky-nav">
                    <@sectionNav getSectionNavLinks(index)></@sectionNav>
                </div>
            </div>
            </#if>

            <div class="column column--two-thirds page-block page-block--main">
                <div id="summary" class="article-section article-section--summary no-border">
                    <h2>${summaryHeader}</h2>
                    <div itemprop="description"><@structuredText item=publication.summary uipath="ps.publication.summary" /></div>
                </div>

                <#if publication.keyFacts.elements?has_content || keyFactImageSections?has_content>
                    <div class="article-section article-section--highlighted" id="key-facts">
                        <div class="callout callout--attention">
                            <h2>${keyFactsHeader}</h2>
                            <#if publication.keyFacts.elements?has_content>
                                <@structuredText item=publication.keyFacts uipath="ps.publication.key-facts" />
                            </#if>

                            <#if keyFactImageSections?has_content>
                                <div data-uipath="ps.publication.key-fact-images">
                                    <@sections sections=keyFactImageSections />
                                </div>
                            </#if>
                        </div>
                    </div>
                </#if>

                <#if publication.administrativeSources?has_content>
                    <div class="article-section" id="administrative-sources">
                        <h2>${administrativeResourcesHeader}</h2>
                        <p itemprop="isBasedOn" data-uipath="ps.publication.administrative-sources">
                            ${publication.administrativeSources}
                        </p>
                    </div>
                </#if>

                <#if publication.datasets?has_content>
                    <div class="article-section" id="data-sets">
                        <h2>${datasetsHeader}</h2>
                        <ul data-uipath="ps.publication.datasets">
                            <#list publication.datasets as dataset>
                                <li itemprop="hasPart" itemscope itemtype="http://schema.org/Dataset"><a itemprop="url" href="<@hst.link hippobean=dataset.selfLinkBean/>" title="${dataset.title}"><span itemprop="name">${dataset.title}</span></a></li>
                            </#list>
                        </ul>
                    </div>
                </#if>

                <#if publication.attachments?has_content || publication.resourceLinks?has_content>
                    <div class="article-section" id="resources">
                        <h2>${resourcesHeader}</h2>
                        <ul data-uipath="ps.publication.resources" class="list">
                        <#list publication.attachments as attachment>
                            <li class="attachment" itemprop="hasPart" itemscope itemtype="http://schema.org/MediaObject">
                                <@externalstorageLink attachment.resource; url>
                                <a title="${attachment.text}" href="${url}" onClick="logGoogleAnalyticsEvent('Download attachment','Publication','${attachment.resource.filename}');" onKeyUp="return vjsu.onKeyUp(event)" itemprop="contentUrl"><span itemprop="name">${attachment.text}</span></a>
                                </@externalstorageLink>
                                <@fileMetaAppendix attachment.resource.length, attachment.resource.mimeType></@fileMetaAppendix>
                            </li>
                        </#list>
                        <#list publication.resourceLinks as link>
                            <li>
                                <a href="${link.linkUrl}" onClick="logGoogleAnalyticsEvent('Link click','Publication','${link.linkUrl}');" onKeyUp="return vjsu.onKeyUp(event)" title="${link.linkText}">${link.linkText}</a>
                            </li>
                        </#list>
                        </ul>
                    </div>
                </#if>

                <#if publication.relatedLinks?has_content>
                    <div class="article-section" id="related-links">
                        <h2>${relatedLinksHeader}</h2>
                        <ul data-uipath="ps.publication.related-links" class="list">
                            <#list publication.relatedLinks as link>
                                <li>
                                    <a href="${link.linkUrl}" onClick="logGoogleAnalyticsEvent('Link click','Publication','${link.linkUrl}');" onKeyUp="return vjsu.onKeyUp(event)" title="${link.linkText}">${link.linkText}</a>
                                </li>
                            </#list>
                        </ul>
                    </div>
                </#if>
            </div>
        </div>
    </div>
</#macro>

<#-- ACTUAL TEMPLATE -->
<#if publication?? >
    <article class="article article--publication" itemscope itemtype="http://schema.org/PublicationIssue">
        <#if publication.publiclyAccessible>
            <@fullContentOfPubliclyAvailablePublication/>
        <#else>
            <@restrictedContentOfUpcomingPublication/>
        </#if>
    </article>
</#if>

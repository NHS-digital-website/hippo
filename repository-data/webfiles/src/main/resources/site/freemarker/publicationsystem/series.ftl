<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "../common/macro/fileMetaAppendix.ftl">
<#include "./macro/structured-text.ftl">
<@hst.setBundle basename="publicationsystem.headers,publicationsystem.labels"/>

<#-- Add meta tags -->
<#include "../common/macro/metaTags.ftl">
<@metaTags></@metaTags>

<article class="article article--legacy-series" itemscope itemtype="http://schema.org/Series">
    <#if series??>
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
        <div class="local-header article-header article-header--detailed" aria-label="Series Title">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <span class="article-header__label"><@fmt.message key="labels.series"/></span>
                    <h1 class="local-header__title" data-uipath="document.title" itemprop="name">${series.title}</h1>
                    <#-- <hr class="hr hr--short hr--light"> -->
                </div>
            </div>
        </div>
    </div>

    <div class="grid-wrapper grid-wrapper--article" aria-label="Series Content">
        <div class="grid-row">
            <div class="column column--two-thirds page-block page-block--main">
                <#-- [FTL-BEGIN] mandatory 'Summary' section -->
                <div class="article-section article-section--summary" id="section-summary">
                    <h2><@fmt.message key="headers.summary"/></h2>
                    <div class="rich-text-content" itemprop="description">
                        <@structuredText item=series.summary uipath="ps.series.summary" />
                    </div>
                </div>
                <#-- [FTL-END] mandatory 'Summary' section -->

                <#if publications?has_content>
                <div class="article-section">
                    <#if series.showLatest>
                        <h3 class="flush push--bottom"><@fmt.message key="headers.latest-version"/></h3>
                        <ul class="list list--reset cta-list" data-uipath="ps.series.publications-list.latest">
                            <@publicationItem publication=publications?first/>
                        </ul>

                        <#if upcomingPublications?has_content>
                            <@upcomingPublicationList/>
                        </#if>

                        <#if publications?size gt 1>
                            <h3 class="flush push--bottom"><@fmt.message key="headers.previous-versions"/></h3>
                            <ul class="list list--reset cta-list" data-uipath="ps.series.publications-list.previous">
                                <#list publications[1..] as publication>
                                    <@publicationItem publication=publication/>
                                </#list>
                            </ul>
                        </#if>
                    <#else>
                        <h3 class="flush push--bottom"><@fmt.message key="headers.versions"/></h3>
                        <ul class="list list--reset cta-list" data-uipath="ps.series.publications-list">
                            <#list publications as publication>
                                <@publicationItem publication=publication/>
                            </#list>
                        </ul>

                        <#if upcomingPublications?has_content>
                            <@upcomingPublicationList/>
                        </#if>
                    </#if>
                </div>
                </#if>

                <#if series.attachments?has_content || series.resourceLinks?has_content>
                    <div class="article-section" id="resources">
                        <h2><@fmt.message key="headers.resources"/></h2>
                        <ul data-uipath="ps.series.resources" class="list">
                        <#list series.attachments as attachment>
                            <li class="attachment" itemprop="hasPart" itemscope itemtype="http://schema.org/MediaObject">
                                <@externalstorageLink attachment.resource; url>
                                <a itemprop="contentUrl" title="${attachment.text}" href="${url}" onClick="logGoogleAnalyticsEvent('Download attachment','Series','${attachment.resource.filename}');" onKeyUp="return vjsu.onKeyUp(event)"><span itemprop="name">${attachment.text}</span></a>
                                </@externalstorageLink>
                                <@fileMetaAppendix attachment.resource.length, attachment.resource.mimeType></@fileMetaAppendix>
                            </li>
                        </#list>
                        <#list series.resourceLinks as link>
                            <li>
                                <a href="${link.linkUrl}" onClick="logGoogleAnalyticsEvent('Link click','Series','${link.linkUrl}');" onKeyUp="return vjsu.onKeyUp(event)" title="${link.linkText}">${link.linkText}</a>
                            </li>
                        </#list>
                        </ul>
                    </div>
                </#if>
            </div>
        </div>
    </div>
    <#else>
      <span>${error}</span>
    </#if>
</article>

<#macro publicationItem publication>
<li itemprop="hasPart" itemscope itemtype="http://schema.org/PublicationIssue">
    <article class="cta">
        <a href="<@hst.link hippobean=publication.selfLinkBean/>" title="${publication.title}" class="cta__button" itemprop="url"><h4 itemprop="name">${publication.title}</h4></a>
        <#if publication.class.name == "uk.nhs.digital.ps.beans.Publication">
            <p class="cta__text"><@truncate text=publication.summary.firstParagraph size="300"/></p>
        </#if>
    </article>
</li>
</#macro>

<#macro upcomingPublicationList>
    <h3 class="flush push--bottom"><@fmt.message key="headers.upcoming"/></h3>
    <ul class="list list--reset cta-list" data-uipath="ps.series.publications-list.upcoming">
        <!--Only next 4 upcoming publications-->
        <#local count = (upcomingPublications?size < 4)?then(upcomingPublications?size, 4)/>
        <#list upcomingPublications[0..count-1] as publication>
        <li itemprop="hasPart" itemscope itemtype="http://schema.org/PublicationIssue">
            <article class="cta">
                <a href="<@hst.link hippobean=publication.selfLinkBean/>" title="${publication.title}" class="cta__button" itemprop="url"><h4 itemprop="name">${publication.title}</h4></a>
                <p class="cta__text"><@formatRestrictableDate value=publication.nominalPublicationDate/></p>
            </article>
        </li>
        </#list>
    </ul>
</#macro>

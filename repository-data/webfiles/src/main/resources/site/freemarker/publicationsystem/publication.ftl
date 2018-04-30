<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/structured-text.ftl">
<#include "./macro/pageIndex.ftl">
<#include "./macro/publicationHeader.ftl">
<#include "./macro/sections/imageSection.ftl">
<#include "../common/macro/pubsBreadcrumb.ftl">
<#assign formatFileSize="uk.nhs.digital.ps.directives.FileSizeFormatterDirective"?new() />
<@hst.setBundle basename="publicationsystem.labels,publicationsystem.headers"/>
<#-- @ftlvariable name="publication" type="uk.nhs.digital.ps.beans.Publication" -->

<#-- Add meta tags -->
<#include "../common/macro/metaTags.ftl">
<@metaTags></@metaTags>

<@pubsBreadcrumb "Publications"></@pubsBreadcrumb>

<#macro restrictedContentOfUpcomingPublication>
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
        <div class="local-header article-header article-header--detailed">
            <div class="grid-wrapper">
                <div class="article-header__inner">

                    <@nationalStatsStamp publication=publication/>

                    <h1 class="local-header__title" data-uipath="document.title">${publication.title}</h1>

                    <hr class="hr hr--short hr--light">

                    <div class="article-header__detail-lines">
                        <div class="article-header__detail-line">
                            <dl class="article-header__detail-list">
                                <dt class="article-header__detail-list-key"><@fmt.message key="headers.publication-date"/></dt>
                                <dd class="article-header__detail-list-value" data-uipath="ps.publication.nominal-publication-date">
                                    <@formatRestrictableDate value=publication.nominalPublicationDate/>
                                </dd>
                            </dl>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="grid-wrapper grid-wrapper--article" aria-label="Document Content">
            <div class="grid-row">
                <div class="column column--two-thirds page-block page-block--main">
                    <div class="article-section">
                        <p data-uipath="ps.publication.upcoming-disclaimer" class="strong">(Upcoming, not yet published)</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</#macro>

<#macro fullContentOfPubliclyAvailablePublication>
    <@publicationHeader publication=publication/>

    <div class="grid-wrapper grid-wrapper--article <#if publication.pages?has_content>article-section</#if>" aria-label="Document Content">
        <div class="grid-row">
            <@pageIndex index=index/>

            <div class="column column--two-thirds page-block page-block--main">
                <div id="Summary" class="article-section article-section--summary no-border">
                    <h2><@fmt.message key="headers.summary"/></h2>
                    <@structuredText item=publication.summary uipath="ps.publication.summary" />
                </div>

                <#if publication.keyFacts.elements?has_content || publication.keyFactImages?has_content>
                <div class="article-section article-section--highlighted" id="Key Facts">
                    <div class="callout callout--attention">
                        <h2><@fmt.message key="headers.key-facts"/></h2>
                        <#if publication.keyFacts.elements?has_content>
                            <@structuredText item=publication.keyFacts uipath="ps.publication.key-facts" />
                        </#if>

                        <#if publication.keyFactImages?has_content>
                            <div data-uipath="ps.publication.key-fact-images">
                                <#list publication.keyFactImages as image>
                                    <@imageSection image/>
                                </#list>
                            </div>
                        </#if>
                    </div>
                </#if>

                <#if publication.administrativeSources?has_content>
                    <div class="article-section" id="Administrative Sources">
                        <h2><@fmt.message key="headers.administrative-sources"/></h2>
                        <p data-uipath="ps.publication.administrative-sources">
                            ${publication.administrativeSources}
                        </p>
                    </div>
                </#if>

                <#if publication.datasets?has_content>
                    <div class="article-section" id="Data Sets">
                        <h2><@fmt.message key="headers.datasets"/></h2>
                        <ul data-uipath="ps.publication.datasets">
                            <#list publication.datasets as dataset>
                                <li><a href="<@hst.link hippobean=dataset.selfLinkBean/>" title="${dataset.title}">${dataset.title}</a></li>
                            </#list>
                        </ul>
                    </div>
                </#if>

                <#if publication.attachments?has_content || publication.resourceLinks?has_content>
                    <div class="article-section" id="Resources">
                        <h2><@fmt.message key="headers.resources"/></h2>
                        <ul data-uipath="ps.publication.resources" class="list">
                        <#list publication.attachments as attachment>
                            <li class="attachment">
                                <@externalstorageLink attachment.resource; url>
                                <a title="${attachment.text}" href="${url}" onClick="logGoogleAnalyticsEvent('Download attachment','Publication','${attachment.resource.filename}');">${attachment.text}</a>;
                                </@externalstorageLink>
                                <span class="fileSize">[size: <@formatFileSize bytesCount=attachment.resource.length/>]</span>
                            </li>
                        </#list>
                        <#list publication.resourceLinks as link>
                            <li>
                                <a href="${link.linkUrl}" onClick="logGoogleAnalyticsEvent('Link click','Publication','${link.linkUrl}');" title="${link.linkText}">${link.linkText}</a>
                            </li>
                        </#list>
                        </ul>
                    </div>
                </#if>

                <#if publication.relatedLinks?has_content>
                    <div class="article-section" id="Related Links">
                        <h2><@fmt.message key="headers.related-links"/></h2>
                        <ul data-uipath="ps.publication.related-links" class="list">
                            <#list publication.relatedLinks as link>
                                <li>
                                    <a href="${link.linkUrl}" onClick="logGoogleAnalyticsEvent('Link click','Publication','${link.linkUrl}');" title="${link.linkText}">${link.linkText}</a>
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
    <article class="article">
        <#if publication.publiclyAccessible>
            <@fullContentOfPubliclyAvailablePublication/>
        <#else>
            <@restrictedContentOfUpcomingPublication/>
        </#if>
    </article>
</#if>

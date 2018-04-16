<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/structured-text.ftl">
<#include "./macro/pageIndex.ftl">
<#include "./macro/publicationHeader.ftl">
<#include "./macro/sections/imageSection.ftl">
<#assign formatFileSize="uk.nhs.digital.ps.directives.FileSizeFormatterDirective"?new() />
<@hst.setBundle basename="publicationsystem.labels,publicationsystem.headers"/>
<#-- @ftlvariable name="publication" type="uk.nhs.digital.ps.beans.Publication" -->

<#macro restrictedContentOfUpcomingPublication>
    <section class="document-header">
        <div class="document-header__inner">
            <h1 data-uipath="document.title">${publication.title}</h1>
            <div class="media push--top push--bottom">
                <div class="media__icon media__icon--calendar"></div>
                <@nominalPublicationDate publication/>
            </div>
        </div>
    </section>
    <section class="document-content">
        <div class="layout layout--large">
            <div class="layout__item layout-2-3">
                <p data-uipath="ps.publication.upcoming-disclaimer">(Upcoming, not yet published)</p>
            </div>
        </div>
    </section>
</#macro>

<#macro fullContentOfPubliclyAvailablePublication>
    <@publicationHeader publication=publication/>

    <@pageIndex index=index/>

    <section class="document-content" aria-label="Document Content">
        <div>
            <h2 id="Summary"><@fmt.message key="headers.summary"/></h2>
            <@structuredText item=publication.summary uipath="ps.publication.summary" />

            <#if publication.keyFacts.elements?has_content || publication.keyFactImages?has_content>
                <h2 id="Key Facts"><@fmt.message key="headers.key-facts"/></h2>
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
            </#if>

            <#if publication.administrativeSources?has_content>
                <h2 id="Administrative Sources"><@fmt.message key="headers.administrative-sources"/></h2>
                <p data-uipath="ps.publication.administrative-sources">
                    ${publication.administrativeSources}
                </p>
            </#if>

            <#if publication.bodySections?has_content>
                <@sections sections=publication.bodySections />
            </#if>

            <#if publication.datasets?has_content>
                <h2 id="Data Sets"><@fmt.message key="headers.datasets"/></h2>
                <ul data-uipath="ps.publication.datasets">
                    <#list publication.datasets as dataset>
                        <li>
                            <a href="<@hst.link hippobean=dataset.selfLinkBean/>" title="${dataset.title}">${dataset.title}</a>
                        </li>
                    </#list>
                </ul>
            </#if>

            <#if publication.attachments?has_content || publication.resourceLinks?has_content>
                <h2 id="Resources"><@fmt.message key="headers.resources"/></h2>
                <ul data-uipath="ps.publication.resources">
                    <#list publication.attachments as attachment>
                        <li class="attachment">
                            <a title="${attachment.text}" href="<@hst.link hippobean=attachment.resource/>" onClick="logGoogleAnalyticsEvent('Download attachment','Publication','${attachment.resource.filename}');">${attachment.text}</a>;
                            <span class="fileSize">size: <@formatFileSize bytesCount=attachment.resource.length/></span>
                        </li>
                    </#list>
                    <#list publication.resourceLinks as link>
                        <li>
                            <a href="${link.linkUrl}" onClick="logGoogleAnalyticsEvent('Link click','Publication','${link.linkUrl}');">${link.linkText}</a>
                        </li>
                    </#list>
                </ul>
            </#if>

            <#if publication.relatedLinks?has_content>
                <h3><@fmt.message key="headers.related-links"/></h3>
                <ul data-uipath="ps.publication.related-links">
                    <#list publication.relatedLinks as link>
                    <li>
                        <a href="${link.linkUrl}" onClick="logGoogleAnalyticsEvent('Link click','Publication','${link.linkUrl}');">${link.linkText}</a>
                    </li>
                    </#list>
                </ul>
            </#if>
        </div>
    </section>
</#macro>

<#-- ACTUAL TEMPLATE -->
<#if publication?? >
    <#if publication.publiclyAccessible>
        <@fullContentOfPubliclyAvailablePublication/>
    <#else>
        <@restrictedContentOfUpcomingPublication/>
    </#if>
</#if>

<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "../common/macro/stickyNavSections.ftl">
<#include "../common/macro/fileMetaAppendix.ftl">

<@hst.setBundle basename="publicationsystem.labels,publicationsystem.headers"/>

<#-- Add meta tags -->
<#include "../common/macro/metaTags.ftl">
<@metaTags></@metaTags>

<#assign hassections = document.sections?has_content />
<#assign foundSubsectionTitle = countSectionTitles(document.sections) gte 1 />
<#-- Only render the nav if there is at least 1 subsection with title -->
<#assign renderNav = hassections && foundSubsectionTitle />

<@fmt.message key="headers.ci-landing-actions" var="actionsHeader" />
<#assign resourcesHeader = "Resources" />

<article class="article article--service">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="local-header article-header">
                <h1 class="local-header__title" data-uipath="document.title">${document.title}</h1>
            </div>
        </div>

        <div class="grid-row">
            <#if renderNav>
            <div class="column column--one-third page-block--sticky-nav page-block--sidebar article-section-nav-outer-wrapper">
                <!-- start sticky-nav -->
                <div id="sticky-nav">
                    <#assign links = [{ "url": "#" + slugify(actionsHeader), "title": actionsHeader }] />
                    <#list document.sections as section>
                        <#if section.title?has_content>
                            <#assign links += [{ "url": "#" + slugify(section.title), "title": section.title }] />
                        </#if>
                    </#list>
                    <@stickyNavSections getStickySectionNavLinks({"sections": links})></@stickyNavSections>
                </div>
                <!-- end sticky-nav -->
            </div>
            </#if>

            <div class="column column--two-thirds page-block page-block--main">
                <#-- [FTL-BEGIN] Actions sections -->
                <div class="article-section article-section--highlighted" id="${slugify(actionsHeader)}">
                    <div class="callout callout--attention">
                        <h2>${actionsHeader}</h2>

                        <ul class="list">
                            <@hst.link var="homelink" path="/" />
                            <@hst.link var="cihome" path="/data-and-information#clinicalindicators" />

                            <li><a href="${cihome}" title="Back to Data & Information">Back to Data & Information</a></li>

                            <#if document.actionLinkRelPath??>
                                <li>
                                    <a href="${homelink}${document.actionLinkRelPath}" title="${document.actionLinkName}">${document.actionLinkName}</a>
                                </li>
                            </#if>
                        </ul>
                    </div>
                </div>
                <#-- [FTL-END] Actions sections -->

                <div data-uipath="ps.cilanding.sections" class="article-section">
                    <#if hassections>
                    <#-- [FTL-BEGIN] Optional Sub sections -->
                    <#list document.sections as section>
                    <#assign hasTitle = section.title?has_content />
                    <div ${hasTitle?then('id=${slugify(section.title)}', "")} class="article-section ${hasTitle?then("", "no-top-padding")}">
                        <#-- Render title -->
                        <#if hasTitle>
                        <h2 data-uipath="ps.cilanding.section-title">${section.title}</h2>
                        </#if>

                        <#-- Render content -->
                        <#if section.content?has_content>
                            <div data-uipath="ps.cilanding.content">
                                <@hst.html hippohtml=section.content />
                            </div>
                        </#if>

                        <#assign hasAttachments = section.attachments?has_content />
                        <#assign hasRelatedLinks = section.relatedLinks?has_content />

                        <#if hasAttachments || hasRelatedLinks>
                            <h3>${resourcesHeader}</h3>

                            <div data-uipath="ps.cilanding.resources">
                                <ul class="list">
                                <#if hasAttachments>
                                    <#list section.attachments as attachment>
                                        <li class="attachment">
                                            <@externalstorageLink attachment.resource; url>
                                            <a href="${url}" onClick="logGoogleAnalyticsEvent('Download attachment','CI landing page','${document.title} - ${attachment.resource.filename}');" onKeyUp="return vjsu.onKeyUp(event)" title="${attachment.text}">${attachment.text}</a>
                                            <@fileMetaAppendix attachment.resource.length, attachment.resource.mimeType></@fileMetaAppendix>
                                            </@externalstorageLink>
                                        </li>
                                    </#list>
                                </#if>

                                <#if hasRelatedLinks>
                                    <#list section.relatedLinks as link>
                                    <li>
                                        <a href="${link.linkUrl}" onClick="logGoogleAnalyticsEvent('Link click','CI landing page','${document.title} - ${link.linkUrl}');" onKeyUp="return vjsu.onKeyUp(event)" title="${link.linkText}">${link.linkText}</a>
                                    </li>
                                    </#list>
                                </#if>
                                </ul>
                            </div>
                        </#if>
                    </div>
                    </#list>
                    </#if>
                    <#-- [FTL-END] Optional Sub sections -->
                </div>
            </div>
        </div>
    </div>
</article>

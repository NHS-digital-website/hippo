<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "../common/macro/sectionNav.ftl">
<#include "../common/macro/fileMetaAppendix.ftl">

<@hst.setBundle basename="publicationsystem.labels,publicationsystem.headers"/>

<#-- Add meta tags -->
<#include "../common/macro/metaTags.ftl">
<@metaTags></@metaTags>

<#assign hasSubSections = document.subSections?has_content />
<#assign foundSubsectionTitle = countSectionTitles(document.subSections) gte 1 />
<#-- Only render the nav if there is at least 1 subsection with title -->
<#assign renderNav = hasSubSections && foundSubsectionTitle />

<@fmt.message key="headers.ci-landing-actions" var="actionsHeader" />
<#assign resourcesHeader = "Resources" />

<#function getSectionNavLinks>
    <#assign links = [{ "url": "#" + slugify(actionsHeader), "title": actionsHeader }] />

    <#if hasSubSections>
        <#list document.subSections as section>
            <#if section.title?has_content>
                <#assign links += [{ "url": "#" + slugify(section.title), "title": section.title }] />
            </#if>
        </#list>
    </#if>

    <#return links />
</#function>

<article class="article article--service">
    <div class="grid-wrapper grid-wrapper--article" aria-label="Document Content">
        <div class="grid-row">
            <div class="local-header article-header">
                <h1 class="local-header__title" data-uipath="document.title">${document.title}</h1>
            </div>
        </div>

        <div class="grid-row">
            <#if renderNav>
            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <div id="sticky-nav">
                    <@sectionNav getSectionNavLinks()></@sectionNav>
                </div>
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
                    <#if hasSubSections>
                    <#-- [FTL-BEGIN] Optional Sub sections -->
                    <#list document.subSections as section>
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

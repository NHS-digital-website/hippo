<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#assign formatFileSize="uk.nhs.digital.ps.directives.FileSizeFormatterDirective"?new() />
<@hst.setBundle basename="publicationsystem.labels,publicationsystem.headers"/>

<#-- Add meta tags -->
<#include "../common/macro/metaTags.ftl">
<@metaTags></@metaTags>

<#include "../common/macro/pubsBreadcrumb.ftl">
<@pubsBreadcrumb "Publications"></@pubsBreadcrumb>

<#assign hasSubSections = document.subSections?has_content />
<#assign foundSubsectionTitle = false />
<#list document.subSections as section>
    <#if section.title?has_content>
    <#assign foundSubsectionTitle = true />
    <#break>
    </#if>
</#list>
<#-- Only render the nav if there is at least 1 subsection with title -->
<#assign renderNav = hasSubSections && foundSubsectionTitle />

<article class="article article--service">
    <div class="grid-wrapper grid-wrapper--article" aria-label="Document Content">
        <div class="grid-row">
            <div class="local-header article-header">
                <h1 class="local-header__title" data-uipath="document.title">${document.title}</h1>
            </div>
        </div>

        <div class="grid-row">
            <#if renderNav>
            <div class="column column--one-third page-block page-block--sidebar sticky sticky--top">
                <div class="article-section-nav">
                    <h2 class="article-section-nav__title">Page contents</h2>
                    <hr>
                    <nav role="navigation">
                        <ol class="article-section-nav__list">
                            <li><a href="#section-actions"><@fmt.message key="headers.ci-landing-actions"/></a></li>
                            <#if hasSubSections>
                            <#list document.subSections as section>
                            <#if section.title?has_content>
                            <li><a href="#section-${section?index+1}">${section.title}</a></li>
                            </#if>
                            </#list>
                            </#if>
                        </ol>
                    </nav>
                </div>
            </div>
            </#if>

            <div class="column column--two-thirds page-block page-block--main">
                <#-- [FTL-BEGIN] Actions sections -->
                <div class="article-section article-section--highlighted" id="section-actions">
                    <div class="callout callout--attention">
                        <h2><@fmt.message key="headers.ci-landing-actions"/></h2>

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
                    <div id="section-${section?index+1}" class="article-section ${hasTitle?then("", "no-top-padding")}">
                        <#-- Render title -->
                        <#if hasTitle>
                        <h2 data-uipath="ps.cilanding.section-title">${section.title}</h2>
                        </#if>

                        <#-- Render content -->
                        <#if section.content?has_content>
                            <p data-uipath="ps.cilanding.content">
                                <@hst.html hippohtml=section.content />
                            </p>
                        </#if>

                        <#assign hasAttachments = section.attachments?has_content />
                        <#assign hasRelatedLinks = section.relatedLinks?has_content />

                        <#if hasAttachments || hasRelatedLinks>
                            <h3>Resources</h3>

                            <div data-uipath="ps.cilanding.resources">
                                <ul class="list">
                                <#if hasAttachments>
                                    <#list section.attachments as attachment>
                                        <li class="attachment">
                                            <@externalstorageLink attachment.resource; url>
                                            <a href="${url}" onClick="logGoogleAnalyticsEvent('Download attachment','CI landing page','${document.title} - ${attachment.resource.filename}');" title="${attachment.text}">${attachment.text}</a>; <span>[size: <@formatFileSize bytesCount=attachment.resource.length/>]</span>
                                            </@externalstorageLink>
                                        </li>
                                    </#list>
                                </#if>

                                <#if hasRelatedLinks>
                                    <#list section.relatedLinks as link>
                                    <li>
                                        <a href="${link.linkUrl}" onClick="logGoogleAnalyticsEvent('Link click','CI landing page','${document.title} - ${link.linkUrl}');" title="${link.linkText}">${link.linkText}</a>
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

<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/resource-image.ftl">
<@hst.setBundle basename="publicationsystem.headers"/>
<#assign formatFileSize="uk.nhs.digital.ps.directives.FileSizeFormatterDirective"?new() />

<section class="document-content">

    <#if document??>
        <h1 data-uipath="ps.document.title">${document.title}</h1>

        <div class="layout layout--large">
            <div class="layout__item layout-2-3" data-uipath="ps.cilanding.sections">

                <#if document.subSections?has_content>
                    <#list document.subSections as section>

                        <#if section.title?has_content>
                            <h2 data-uipath="ps.cilanding.section-title">
                                ${section.title}
                            </h2>
                        </#if>

                        <#if section.content?has_content>
                            <p data-uipath="ps.cilanding.content">
                                <@hst.html hippohtml=section.content />
                            </p>
                        </#if>

                        <#if section.attachments?has_content>
                            <section class="push-double--bottom">
                                <div class="panel panel--grey" data-uipath="ps.cilanding.attachments">
                                    <#list section.attachments as attachment>
                                        <div class="attachment media push-half--bottom">
                                            <@resourceImage file=attachment.resource.filename />
                                            <p class="media__body zeta">
                                                <a href="<@hst.link hippobean=attachment.resource/>" onClick="logGoogleAnalyticsEvent('Download attachment','CI landing page','${document.title} - ${attachment.resource.filename}');" title="${attachment.text}">${attachment.text}</a>;
                                                <span class="fileSize">size: <@formatFileSize bytesCount=attachment.resource.length/></span>
                                            </p>
                                        </div>
                                    </#list>
                                </div>
                            </section>
                        </#if>

                        <#if section.relatedLinks?has_content>
                            <ul data-uipath="ps.cilanding.links" class="push-double--bottom">
                                <#list section.relatedLinks as link>
                                    <@hst.link var="assetLink" hippobean=asset />
                                    <li><a href="${link.linkUrl}" onClick="logGoogleAnalyticsEvent('Link click','CI landing page','${document.title} - ${link.linkUrl}');" title="${link.linkText}">${link.linkText}</a></li>
                                </#list>
                            </ul>
                        </#if>

                    </#list>
                </#if>

            </div><!--
        --><div class="layout__item layout-1-3">
                <div class="panel panel--grey push-half--bottom push--top">
                    <h3><@fmt.message key="headers.ci-landing-actions"/></h3>
                    <ul>
                        <@hst.link var="homelink" path="/" />
                        <li><a href="${homelink}" title="Back to Clinical Indicators">Back to Clinical Indicators</a></li>
                        <#if document.actionLinkRelPath??>
                            <li><a href="${homelink}${document.actionLinkRelPath}" title="${document.actionLinkName}">${document.actionLinkName}</a></li>
                        </#if>
                    </ul>
                </div>
            </div>
        </div>
    </#if>
</section>

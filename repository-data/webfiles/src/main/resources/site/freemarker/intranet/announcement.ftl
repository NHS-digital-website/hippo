<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "../common/macro/documentHeader.ftl">
<#include "../common/macro/component/downloadBlock.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/googleTags.ftl">

<@googleTags documentBean=document pageSubject=''/>
<#-- Add meta tags -->
<@metaTags></@metaTags>

<article class="article article--intranet-home">

    <#assign metaData = [] />
    <#if document.priority?has_content>
        <#assign metaData += [{"key":"Priority", "value": document.priority?cap_first, "uipath": "priority", "schemaOrgTag": "", "type": ""}] />
    </#if>
    <#if document.expirydate?has_content>
        <#assign metaData += [{"key":"Expires", "value": document.expirydate.time, "uipath": "expires", "schemaOrgTag": "", "type": "date"}] />
    </#if>
    <@documentHeader document 'intranet-announcement' "" "" "" "" false metaData></@documentHeader>

    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--no-padding">

                <#if document.details?has_content>
                    <div class="article-section no-border article-section--introduction">
                        <div class="rich-text-content">
                            <@hst.html hippohtml=document.details />
                        </div>
                    </div>
                </#if>

                <#if document.relateddocument?has_content>
                    <div class="article-section">
                        <h2>Related Document</h2>
                        <div class="article-section-top-margin">
                            <@downloadBlock document.relateddocument />
                        </div>
                    </div>
                </#if>

                <#if document.link?has_content>
                    <div class="article-section">
                        <div class="article-section-top-margin">
                            <#if document.link.linkType == "internal">
                                <h2 class="cta__title"><a href="<@hst.link hippobean=document.link.link />">${document.link.link.title}</a></h2>
                                <p class="cta__text">${document.link.link.shortsummary}</p>
                            <#elseif document.link.linkType == "external">
                                <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, document.link.link) />
                                <h2 class="cta__title"><a href="${document.link.link}" onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)">${document.link.title}</a></h2>
                                <p class="cta__text">${document.link.shortsummary}</p>
                            </#if>
                        </div>
                    </div>
                </#if>
            </div>
        </div>
    </div>
</article>

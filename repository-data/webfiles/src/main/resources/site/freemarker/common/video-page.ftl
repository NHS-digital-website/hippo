<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/contentPixel.ftl">
<#include "macro/component/downloadBlock.ftl">

<#-- @ftlvariable name="video" type="uk.nhs.digital.website.beans.Video" -->

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<article itemscope itemtype="https://schema.org/VideoObject">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
        <div class="local-header article-header article-header--detailed">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                     <div class="grid-row">
                        <div class="column--three-quarters column--reset">
                            <span class="article-header__label">Video</span>
                            <h1 class="local-header__title" itemprop="headline name" data-uipath="document.title">${document.title}</h1>
                            <div class="article-header__subtitle" data-uipath="website.blog.shortsummary">${document.shortsummary}</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--two-thirds page-block page-block--main">
                <@hst.html hippohtml=document.introduction />
            </div>
        </div>

        <div class="article-section">
            <div class="grid-row">
                <div class="iframe-wrapper iframe-wrapper--16-9">
                    <iframe id="ytplayer" src="${document.videoUri}"></iframe>
                    <link itemprop="embedUrl" href="${document.videoUri}" />
                </div>
            </div>
        </div>

        <#if document.relatedDocuments?has_content>
            <div class="article-section">
                <h2>Related pages</h2>
                <#list document.relatedDocuments as child>
                    <div class="article-section-top-margin">
                        <@downloadBlock child />
                    </div>
                </#list>
            </div>
        </#if>
    </div>
</article>

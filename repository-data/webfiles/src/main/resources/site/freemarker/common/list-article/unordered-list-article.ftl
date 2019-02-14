<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.ComponentList" -->

<#include "../../include/imports.ftl">
<#include "../macro/fileMetaAppendix.ftl">
<#include "../macro/typeSpan.ftl">
<#include "../macro/component/lastModified.ftl">
<#include "../macro/fileIcon.ftl">

<article class="article article--list">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--reset">
                <div class="article-header article-header--secondary">
                    <div class="grid-row">
                        <div class="column column--two-thirds column--reset">
                            <h1 data-uipath="document.title">${document.title}</h1>
                        </div>
                    </div>
                </div>

                <#-- [FTL-BEGIN] 'Summary and optional Body' sections -->
                <div id="section-summary" class="article-section article-section--summary article-section--reset-top">
                    <div class="grid-row">
                        <div class="column column--two-thirds column--reset">
                            <div class="rich-text-content">
                                <div data-uipath="website.linkslist.summary"><@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/></div>
                                <#if document.body?has_content??>
                                <@hst.html hippohtml=document.body contentRewriter=gaContentRewriter/>
                                </#if>
                            </div>
                        </div>
                    </div>
                </div>
                <#-- [FTL-END] 'Summary and optional Body' sections -->

                <#-- [FTL-BEGIN] 'List' sections -->
                <#if document.blocks?has_content >
                <div class="article-section article-section--list">
                    <div class="grid-row">
                        <div class="column column--two-thirds column--reset">
                            <ul class="list list--reset cta-list cta-list--sections">
                                <#list document.blocks as block>
                                <li class="cta">
                                    <#if block.linkType??>
                                        <@typeSpan block.linkType />
                                        
                                        <#if block.linkType == "internal">
                                            <h2 class="cta__title"><a href="<@hst.link hippobean=block.link />">${block.link.title}</a></h2>
                                            <p class="cta__text">${block.link.shortsummary}</p>
                                        <#elseif block.linkType == "external">
                                            <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, block.link) />
                                            <h2 class="cta__title"><a href="${block.link}" onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)">${block.title}</a></h2>
                                            <p class="cta__text">${block.shortsummary}</p>
                                        <#elseif block.linkType == "asset">
                                            <a href="<@hst.link hippobean=block.link />"
                                               class="block-link"
                                               onClick="${onClickMethodCall}"
                                               onKeyUp="return vjsu.onKeyUp(event)">
                                                <div class="block-link__header">
                                                    <@fileIcon block.link.asset.mimeType></@fileIcon>
                                                </div>
                                                <div class="block-link__body">
                                                    <span class="block-link__title">${block.title}</span>
                                                    <@fileMetaAppendix block.link.asset.getLength()></@fileMetaAppendix>
                                                </div>
                                            </a>
                                        </#if>
                                    </#if>
                                </li>
                                </#list>
                            </ul>
                        </div>
                    </div>
                </div>
                </#if>
                <#-- [FTL-END] 'List' sections -->
                <div class="article-section muted">
                    <@lastModified document.lastModified false></@lastModified>
                </div>
            </div>
        </div>
    </div>
</article>

<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../macro/fileMetaAppendix.ftl">

<article class="article article--list">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--reset">
                <div class="article-header article-header--secondary">
                    <div class="grid-row">
                        <div class="column column--two-thirds column--reset">
                            <h1>${document.title}</h1>
                        </div>
                    </div>
                </div>

                <#-- [FTL-BEGIN] 'Summary and optional Body' sections -->
                <div id="section-summary" class="article-section article-section--summary article-section--reset-top">
                    <div class="grid-row">
                        <div class="column column--two-thirds column--reset">
                            <div class="rich-text-content">
                                <p>${document.summary}</p>
                                <#if document.body?has_content??>
                                <@hst.html hippohtml=document.body contentRewriter=gaContentRewriter/>
                                </#if>
                            </div>
                        </div>
                    </div>
                </div>
                <#-- [FTL-END] 'Summary and optional Body' sections -->

                <#-- [FTL-BEGIN] 'List' sections -->
                <#if document.blocks??>
                <div class="article-section article-section--list">
                    <div class="grid-row">
                        <div class="column column--two-thirds column--reset">
                            <div class="list list--reset cta-list cta-list--sections">
                                <#list document.blocks as block>
                                <div class="cta">  
                                    <#if block.getType() == "internal">
                                    <h2 class="cta__title"><a href="<@hst.link hippobean=block.link />">${block.link.title}</a></h2>
                                    <p class="cta__text">${block.link.shortsummary}</p>
                                    <#elseif block.getType() == "external">
                                    <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, block.link) />
                                    <h2 class="cta__title"><a href="${block.link}" onClick="${onClickMethodCall}">${block.title}</a></h2>
                                    <p class="cta__text">${block.shortsummary}</p>
                                    <#elseif block.getType() == "asset">
                                    <a href="<@hst.link hippobean=block.link />" onClick="${onClickMethodCall}">${block.title}</a><@fileMetaAppendix block.link.asset.getLength()></@fileMetaAppendix>
                                    </#if>
                                </div>
                                </#list>
                            </div>
                        </div>
                    </div>
                </div>
                </#if>
                <#-- [FTL-END] 'List' sections -->
            </div>
        </div>
    </div>
</article>

<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/narrowArticleSections.ftl">
<#include "macro/childPageGrid.ftl">
<#include "macro/hubPageComponents.ftl">

<article class="article article--hub">
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
                <div id="section-summary" class="article-section article-section--summary article-section--summary-with-border">
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
            </div>
        </div>

        <#-- [FTL-BEGIN] 'Child pages' section -->
        <#if childPages?? && childPages?size!=0>
        <div class="grid-row">
            <div class="column column--reset">
                <div class="article-section article-section--no-bottom-padding">
                    <div class="cta-list cta-list--grid">
                        <div class="grid-row">
                            <div class="column column--reset">
                                <h3 class="list-title">${document.listtitle}</h3>
                            </div>
                        </div>

                        <#-- [FTL-BEGIN] 'Child page' grid -->
                        <@childPageGrid childPages></@childPageGrid>
                        <#-- [FTL-END] 'Child page' grid -->
                    </div>
                </div>
            </div>
        </div>
        </#if>
        <#-- [FTL-END] 'Child pages' section -->

        <#if document.componentlist??>
        <div class="grid-row">
            <div class="column column--reset">
                <#-- [FTL-BEGIN] 'Components' section -->
                <@hubPageComponents components></@hubPageComponents>
                <#-- [FTL-END] 'Components' section -->
            </div>
        </div>
        </#if>
    </div>
</article>
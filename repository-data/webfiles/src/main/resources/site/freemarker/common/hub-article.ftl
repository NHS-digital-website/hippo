<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/childPageGrid.ftl">
<#include "macro/hubPageComponents.ftl">
<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<article class="article article--hub">
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
                                <div data-uipath="website.hub.summary"><@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/></div>
                                <#if document.body?has_content??>
                                <@hst.html hippohtml=document.body contentRewriter=gaContentRewriter/>
                                </#if>
                            </div>
                        </div>
                    </div>
                </div>
                <#-- [FTL-END] 'Summary and optional Body' sections -->

                <#-- [FTL-BEGIN] 'Child pages' section -->
                <#if childPages?? && childPages?size!=0>
                <div class="article-section">
                    <div class="grid-row">
                        <div class="column column--reset">
                            <div class="cta-list cta-list--grid">
                                <#if document.listtitle?? && document.listtitle?has_content>
                                <div class="grid-row">
                                    <div class="column column--reset">
                                        <h2 class="list-title">${document.listtitle}</h2>
                                    </div>
                                </div>
                                </#if>

                                <#-- [FTL-BEGIN] 'Child page' grid -->
                                <@childPageGrid childPages></@childPageGrid>
                                <#-- [FTL-END] 'Child page' grid -->
                            </div>
                        </div>
                    </div>
                </div>
                </#if>
                <#-- [FTL-END] 'Child pages' section -->

                <#-- [FTL-BEGIN] 'Components' section -->
                <#if document.componentlist?? && document.componentlist?has_content>
                <div class="article-section">
                    <div class="grid-row">
                        <div class="column column--reset">
                            <@hubPageComponents components></@hubPageComponents>
                        </div>
                    </div>
                </div>
                </#if>
                <#-- [FTL-END] 'Components' section -->
            </div>
        </div>
    </div>
</article>

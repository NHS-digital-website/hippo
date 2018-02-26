<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

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
                <div id="section-summary" class="article-section article-section--summary article-section--summary-with-border">
                    <div class="grid-row">
                        <div class="column column--two-thirds column--reset">
                            <div class="rtc">
                                <p>${document.summary}</p>
                                <#if document.body?has_content??>
                                <@hst.html hippohtml=document.body/>
                                </#if>
                            </div>
                        </div>
                    </div>
                </div>
                <#-- [FTL-END] 'Summary and optional Body' sections -->

                <#-- [FTL-BEGIN] 'List' sections -->
                <#if document.blocks??>
                <section class="article-section article-section--list article-section--last-one">
                    <div class="grid-row">
                        <div class="column column--two-thirds column--reset">
                            <div class="list list--reset cta-list cta-list--sections">
                                <#list document.blocks as block>
                                <div class="cta">  
                                    <#if block.getType() == "internal">
                                    <h2 class="cta__title"><a href="<@hst.link hippobean=block.link />">${block.link.title}</a></h2>
                                    <p class="cta__text">${block.link.shortsummary}</p>
                                    <#elseif block.getType() == "external">
                                    <h2 class="cta__title"><a href="${block.link}">${block.title}</a></h2>
                                    <p class="cta__text">${block.shortsummary}</p>
                                    </#if>
                                </div>
                                </#list>
                            </div>
                        </div>
                    </div>
                </section>
                </#if>
                <#-- [FTL-END] 'List' sections -->
            </div>
        </div>
    </div>
</article>

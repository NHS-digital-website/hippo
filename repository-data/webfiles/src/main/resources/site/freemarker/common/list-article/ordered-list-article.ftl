<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../macro/alphabeticalFilterNav.ftl">
<#include "../macro/alphabeticalGroupOfBlocks.ftl">

<article class="article article--filtered-list">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--two-thirds column--reset">
                <div class="article-header article-header--secondary">
                    <h1 data-uipath="document.title">${document.title}</h1>
                </div>
            </div>
        </div>

        <div class="grid-row">
            <div class="column column--two-thirds column--reset">
                <#-- [FTL-BEGIN] 'Summary and optional Body' sections -->
                <div id="section-summary" class="article-section article-section--summary  no-border">
                    <div class="grid-row">
                        <div class="column column--reset">
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
            </div>
        </div>

        <#assign alphabetical_hash = group_blocks(flat_blocks(document.blocks true))/>

        <#if alphabetical_hash??>
        <div class="grid-row">
            <div class="column column--one-third page-block page-block--sidebar sticky sticky--top">
                <@alphabeticalFilterNav alphabetical_hash></@alphabeticalFilterNav>
            </div>

            <div class="column column--two-thirds page-block page-block--main">
                <@alphabeticalGroupOfBlocks alphabetical_hash></@alphabeticalGroupOfBlocks>
            </div>
        </div>
        </#if>
    </div>
</article>

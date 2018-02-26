<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../macro/alphabeticalFilterNav.ftl">
<#include "../macro/alphabeticalGroupOfBlocks.ftl">

<article class="article article--service">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--two-thirds column--reset">
                <div class="article-header article-header--secondary">
                    <h1>${document.title}</h1>
                </div>
            </div>
        </div>

        <div class="grid-row">
            <div class="column column--two-thirds column--reset">
                <#-- [FTL-BEGIN] 'Summary and optional Body' sections -->
                <div id="section-summary" class="article-section article-section--summary">
                    <div class="grid-row">
                        <div class="columncolumn--reset">
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
            </div>
        </div>

        <#function flat_blocks blocks order>
            <#local flattened_blocks = [] />
            <#list blocks as block>
                <#if block.getType() == "internal">
                    <#local flattened_blocks = flattened_blocks + [ block.link ] />
                <#elseif block.getType() == "external">
                    <#local flattened_blocks = flattened_blocks + [ block ] />
                </#if>
            </#list>
            <#return order?then(flattened_blocks?sort_by("title"), flattened_blocks)/>
        </#function>

        <#function group_blocks blocks>
            <#local alphabetical_hash = {} />
            <#list blocks as block>
                <#local key = block.title?cap_first[0]/>
                <#local alphabetical_hash = alphabetical_hash + {  key : (alphabetical_hash[key]![]) + [ block ] } />
            </#list>
            <#return alphabetical_hash/>
        </#function>

        <#-- HOW TO USE the group_blocks function

            <#assign alphabetical_hash = group_blocks(flat_blocks(document.blocks true))/>
            <#list alphabetical_hash?keys as key>
                key: ${key} - value ${alphabetical_hash[key]?size}:
                <#list alphabetical_hash[key] as item>
                    ${item}
                </#list>
                <br/>
            </#list>

        -->

        <#-- TODO s
        1) when using the flat_blocks function, use the 'anchor' value as second parameter
        2) when printng the block field and using the 'flattened_list', remember that the type variable is only defined for the externallink

                <#if block.type?? && block.type == "external">
                <h2 class="cta__title"><a href="${block.link}">${block.title}</a></h2>
                <#else>
                <h2 class="cta__title"><a href="<@hst.link hippobean=block />">${block.title}</a></h2>
                </#if>
        -->

        <#assign alphabetical_hash = group_blocks(flat_blocks(document.blocks true))/>

        <div class="grid-row">
            <div class="column column--one-third page-block page-block--sidebar page-block--sticky-top">
                <#if alphabetical_hash??>
                <@alphabeticalFilterNav alphabetical_hash></@alphabeticalFilterNav>
                </#if>
            </div>

            <div class="column column--two-thirds page-block page-block--main">
                <#-- [FTL-BEGIN] 'Article group' section -->
                <#if alphabetical_hash??>
                <@alphabeticalGroupOfBlocks alphabetical_hash></@alphabeticalGroupOfBlocks>
                </#if>
                <#-- [FTL-END] 'Article group' section -->
            </div>
        </div>
    </div>
</article>

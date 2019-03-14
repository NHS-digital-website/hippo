<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.GlossaryList" -->

<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">
<#include "marco/stickyGroupBlockHeader.ftl">
<#include "macro/component/lastModified.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="rb.generic.headers,publicationsystem.headers"/>

<#assign hasSummaryContent = document.summary.content?has_content />
<#assign lettersOfTheAlphabet = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"]/>

<article class="article article--general">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--reset">
                <div class="local-header article-header">
                    <h1 class="local-header__title" data-uipath="document.title">${document.title}</h1>
                </div>

                <#if hasSummaryContent>
                    <div id="${slugify('Summary')}" class="article-section article-section--summary article-section--reset-top">
                        <h2><@fmt.message key="headers.summary" /></h2>

                        <div data-uipath="website.general.summary" class="article-section--summary">
                            <@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/>
                        </div>
                    </div>
                </#if>
            </div>
        </div>
        
        <#assign alphabetical_hash = group_blocks(flat_blocks(document.glossaryItems true))/>

        <div class="grid-row">
            <div class="column column--one-third page-block page-block--sidebar">
                <div id="sticky-nav">
                    <@alphabeticalFilterNav alphabetical_hash></@alphabeticalFilterNav>
                </div>
            </div>

            <div class="column column--two-thirds page-block page-block--main">
                <div class="article-section">
                    <dl>
                        <#list lettersOfTheAlphabet as letter>
                            <@stickyGroupBlockHeader letter></@stickyGroupBlockHeader>

                            <#list document.glossaryItems as glossaryitem>
                                <#if glossaryitem.heading?substring(0, 1) == letter>
                                    <#assign hasBlocks = glossaryitem.blocks?? && glossaryitem.blocks?size!=0 />

                                    <#if hasBlocks>
                                        <#list glossaryitem.blocks as block>
                                            <dt>
                                                <#if block.linkType == "external">
                                                    <a href="${block.link}">${glossaryitem.heading}</a>
                                                <#elseif block.linkType == "internal">
                                                    <a href="<@hst.link hippobean=block.link />">${glossaryitem.heading}</a>
                                                </#if>
                                            </dt>
                                        </#list>
                                    <#else>
                                        <dt>${glossaryitem.heading}</a></dt>
                                    </#if>

                                    <dd><@hst.html hippohtml=glossaryitem.definition contentRewriter=gaContentRewriter/></dd>
                                </#if>
                            </#list>
                        </#list>
                    </dl>
                </div>

                <div class="article-section muted">
                    <@lastModified document.lastModified false></@lastModified>
                </div>
            </div>
        </div>
    </div>
</article>

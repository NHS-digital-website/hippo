<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.ComponentList" -->

<#include "../../include/imports.ftl">
<#include "../macro/alphabeticalFilterNav.ftl">
<#include "../macro/alphabeticalGroupOfBlocks.ftl">
<#include "../macro/component/lastModified.ftl">

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

        <#if priorityTasks?? && priorityTasks?has_content>
            <div class="grid-row">
                <div class="column column--reset">
                    <div class="intra-box bottom-margin-20">
                        <h3>Common tasks</h3>
                        <#list priorityTasks as task>
                            <div class="intra-box__link">
                                <a href="<@hst.link hippobean=task/>">${task.title}</a>
                            </div>
                        </#list>
                    </div>
                </div>
            </div>
        </#if>

        <#if alternativeTasks?? && alternativeTasks?has_content >
            <#assign alphabetical_hash = group_blocks(flat_blocks(document.blocks true alternativeTasks))/>
        <#else>
            <#assign alphabetical_hash = group_blocks(flat_blocks(document.blocks true))/>
        </#if>

        <#if alphabetical_hash??>
        <div class="grid-row">
            <div class="column column--one-third page-block page-block--sidebar sticky sticky--top">
                <@alphabeticalFilterNav alphabetical_hash></@alphabeticalFilterNav>
            </div>

            <div class="column column--two-thirds page-block page-block--main">
                <@alphabeticalGroupOfBlocks alphabetical_hash></@alphabeticalGroupOfBlocks>
                <div class="article-section muted">
                    <@lastModified document.lastModified false></@lastModified>
                </div>
            </div>
        </div>
        </#if>
    </div>
</article>

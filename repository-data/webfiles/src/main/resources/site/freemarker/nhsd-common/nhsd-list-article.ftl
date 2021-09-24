<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.ComponentList" -->

<#include "../include/imports.ftl">
<#include "../common/macro/metaTags.ftl">
<#include "../common/macro/contentPixel.ftl">
<#include "macros/alphabetical-group-of-blocks.ftl">
<#include "macro/heroes/hero.ftl">
<#include "macro/heroes/hero-options.ftl">
<#include "macros/az-nav.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>
<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<#if document.blocks??>
    <#if alternativeTasks?? && alternativeTasks?has_content >
        <#assign alphabetical_hash = group_blocks(flat_blocks(document.blocks true alternativeTasks))/>
    <#else>
        <#assign alphabetical_hash = group_blocks(flat_blocks(document.blocks true))/>
    </#if>
<#else>
    <#assign alphabetical_hash = group_blocks(document.blocks)/>
</#if>

<@hero getHeroOptions(document) />

<div class="nhsd-t-grid nhsd-!t-margin-top-6">

        <div class="nhsd-t-row">
                <div class="rich-text-content nhsd-t-col-12">
                    <#if document.body?has_content??>
                        <@hst.html hippohtml=document.body contentRewriter=gaContentRewriter/>
                    </#if>
                </div>
        </div>

        <div class="nhsd-t-row">
        <div class="nhsd-t-col-3 nhsd-!t-display-hide nhsd-!t-display-l-show">
            <div class="nhsd-a-box nhsd-a-box--border-grey nhsd-!t-margin-right-3 nhsd-!t-display-sticky nhsd-!t-display-sticky--offset-2">
                <h2 id="side-az-nav-heading" class="nhsd-t-heading-xs">Search A-Z</h2>
                <@azList document "side-az-nav-heading"/>
            </div>
        </div>
        <div class="nhsd-t-col-l-9 nhsd-t-col-m-12">
            <div class="nhsd-!t-display-l-hide">
                <h2 id="top-az-nav-heading" class="nhsd-t-heading-xs">Search A-Z</h2>
                <div class="nhsd-!t-margin-bottom-4"><@azList document "top-az-nav-heading"/></div>
                <hr class="nhsd-a-horizontal-rule" />
            </div>
            <@alphabeticalGroupOfBlocks alphabetical_hash></@alphabeticalGroupOfBlocks>
        </div>
    </div>
</div>

<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<@hst.setBundle basename="rb.merger-banner"/>
<@fmt.message key="banner.colour" var="backgroundColor" />
<@fmt.message key="banner.enabled" var="bannerEnabled" />

<#assign bgColour = backgroundColor?upper_case />
<#if !(['#7C2855', '#425563', '#E8EDEE', '#A0DAF2'])?seq_contains(bgColour) >
    <#assign bgColour = '#7C2855' />
</#if>

<#assign textColor = '#FFFFFF' />
<#if (['#E8EDEE', '#A0DAF2'])?seq_contains(bgColour) >
    <#assign textColor = '#000000' />
</#if>

<style>
.merger-banner {
    padding: 1.6rem 0;
    background: ${bgColour};
}
.merger-banner, .merger-banner a {
    color: ${textColor};
}
</style>
<div class="merger-banner">
    <div class="grid-wrapper grid-wrapper--collapse">
        <div class="grid-row">
            <div class="column column--reset">
                <p>this is a test</p>
                <@hst.include ref="banner-selected"/>
            </div>
        </div>
    </div>
</div>

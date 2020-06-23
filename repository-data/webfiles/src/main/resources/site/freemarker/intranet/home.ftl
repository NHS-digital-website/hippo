<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#include "../common/macro/component/actionLink.ftl">
<#include "../common/macro/svgIcons.ftl">
<#include "../../src/js/utils/line-clamp-polyfill.js">

<#include "macro/metaTags.ftl">
<#include "macro/homePageHeader.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>
<@homePageHeader></@homePageHeader>

<#if username?has_content>
    <#assign userName = username/>
<#else>
    <#assign userName = "" />
</#if>

<div class="intra-home-header">
    <div class="intra-home-header__inner">
        <div class="intra-home-header__inner-contents">
            <div class="intra-home-header__greeting">
                <h1><span class="intra-home-header__greeting-welcome">Welcome</span> <span>${userName}</span></h1>
            </div>
            <div class="intra-home-header__weather">
                <span class="intra-home-header__weather-text">Exeter, 16°C</span>
                <@svgIcon id="weather" className="intra-home-header__weather-icon" />
            </div>
        </div>
    </div>
</div>

<div class="intra-home-grid">
    <div class="intra-home-grid__left-col">
        <@hst.include ref = "left"/>
    </div>

    <div class="intra-home-grid__right-col">
        <div class="intra-box intra-box--compact">
            <@hst.include ref = "right"/>
        </div>
    </div>
</div>

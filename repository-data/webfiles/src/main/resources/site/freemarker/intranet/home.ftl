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

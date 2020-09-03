<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#include "../common/macro/component/actionLink.ftl">
<#include "../common/macro/svgIcons.ftl">
<#include "../../src/js/line-clamp-polyfill.js.ftl">

<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<div class="intra-home-header"></div>
<div class="intra-home-grid">
    <div>
    <@hst.include ref ="notice"/>
    </div>
    <div>
        <@hst.include ref ="news"/>
    </div>
    <div>
        <@hst.include ref ="body"/>
    </div>
</div>


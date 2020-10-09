<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#include "../common/macro/component/actionLink.ftl">
<#include "../common/macro/svgIcons.ftl">

<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<div class="intranet">
    <div class="grid-wrapper">
        <@hst.include ref ="notice"/>
    </div>

    <div class="home-news-grid-wrapper">
        <@hst.include ref="news"/>
    </div>

    <div class="grid-wrapper">
        <@hst.include ref ="body"/>
    </div>
</div>


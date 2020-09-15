<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#include "../common/macro/component/actionLink.ftl">
<#include "../common/macro/svgIcons.ftl">
<#--<#include "../../src/js/line-clamp-polyfill.js.ftl">-->

<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<div class="intranet">
    <div class="grid-wrapper">
    <@hst.include ref ="notice"/>
    </div>

    <@hst.include ref ="news"/>

    <div class="home-news-grid-container-1">
        <div class="home-news-news-top-1">top</div>
        <div class="home-news-tasks-1">task</div>
        <div class="home-news-news-items-1">news items</div>
    </div>

    <div class="home-news-grid-container">
        <div class="home-news-news-top">top</div>
        <div class="home-news-tasks">task</div>
        <div class="home-news-news-items">news items</div>
    </div>

    <div class="grid-wrapper">
        <@hst.include ref ="body"/>
    </div>
</div>


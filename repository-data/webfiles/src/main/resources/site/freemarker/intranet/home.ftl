<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#include "../common/macro/component/actionLink.ftl">
<#include "../common/macro/svgIcons.ftl">

<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<div class="intra-home-header"></div>
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

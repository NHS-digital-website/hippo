<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "../../src/js/line-clamp-polyfill.js.ftl">

<section class="news-hub" aria-label="News hub">

    <@hst.include ref= "banner"/>

    <div class="grid-wrapper">
        <div class="site-main">
            <@hst.include ref="main"/>
        </div>
    </div>

</section>

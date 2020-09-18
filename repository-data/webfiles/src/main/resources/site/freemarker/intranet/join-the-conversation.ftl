<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<div class="homeland-blog">
    <article class="article article--intranet-home">
        <#if cparam.headline??>
            <h2>${cparam.headline}</h2>
            <div>
                <a href="${cparam.buttonUrl}">${cparam.buttonText}</a>
            </div>
        </#if>
    </article>
</div>

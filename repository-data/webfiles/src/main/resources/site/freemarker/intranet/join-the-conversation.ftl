<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<article class="article article--intranet-home">
    <#if cparam.headline??>
        <h2>${cparam.headline}</h2>
        <div>
            <a href="${cparam.buttonUrl}">${cparam.buttonText}</a>
        </div>
    </#if>
</article>


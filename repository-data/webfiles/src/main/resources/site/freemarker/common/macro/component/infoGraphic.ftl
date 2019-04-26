<#ftl output_format="HTML">
<#include "../../../include/imports.ftl">

<#macro infoGraphic graphic>
    <div class="infographic infographic--${slugify(graphic.colour)}">
        <@hst.link hippobean=graphic.icon var="imagePath" />
        <#if (imagePath)??>
            <div class="infographic__icon">
                <img src="${imagePath}" alt="Image for infographic ${graphic.headline}" width="100" height="100" class="infographic__img" />
            </div>
        </#if>
        <div class="infographic__body">
            <h4 class="infographic__title">${graphic.headline}</h4>
            <div class="infographic__explanatory"><@hst.html hippohtml=graphic.explanatoryLine contentRewriter=gaContentRewriter/></div>
            <div class="infographic__footer"><@hst.html hippohtml=graphic.qualifyingInformation contentRewriter=gaContentRewriter/></div>
        </div>
    </div>
</#macro>

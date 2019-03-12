<#ftl output_format="HTML">
<#include "../../../include/imports.ftl">

<#macro infoGraphic image="" color="white" headline="Infographic" explanatory="Explanatory line" qualifying="Qualifying information" imageAlt=explanatory>
    <div class="infographic infographic--${slugify(color)}">
        <#if image != "">
            <div class="infographic__icon">
                <img src="${image}" alt="${imageAlt}" width="100" height="100" class="infographic__img" />
            </div>
        </#if>
        <div class="infographic__body">
            <h4 class="infographic__title">${headline}</h4>
            <div class="infographic__explanatory">${explanatory}</div>
            <div class="infographic__footer">${qualifying}</div>
        </div>
    </div>
</#macro>

<#ftl output_format="HTML">
<#include "../../../include/imports.ftl">

<#macro infoGraphic image="" color="white" headline="Infographic" explanatory="Explanatory line" qualifying="Qualifying information" imageAlt=explanatory>
    <#assign defaultImage = "images/infographic/dinosaur-svgrepo-com.svg" />
    <div class="infographic infographic--${slugify(color)}">
        <div class="infographic__icon">
            <#if image == "">
                <img src="<@hst.webfile path="${defaultImage}"/>" alt="${imageAlt}" width="100" height="100" class="infographic__img" />
            <#else>
                <img src="${image}" alt="${imageAlt}" width="100" height="100" class="infographic__img" />
            </#if>
        </div>
        <div class="infographic__body">
            <h4 class="infographic__title">${headline}</h4>
            <div class="infographic__explanatory">${qualifying}</div>
            <div class="infographic__footer">${explanatory}</div>
        </div>
    </div>
</#macro>

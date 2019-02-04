<#ftl output_format="HTML">
<#include "../../../include/imports.ftl">

<#macro infoGraphic image="" color="white" headline="Infographic" explanatory="Explanatory line" qualifying="Qualifying information" imageAlt=explanatory>
    <#assign defaultImage = "images/infographic/dinosaur-svgrepo-com.svg" />
    <div class="infographic infographic--${slugify(color)}">
        <div class="infographic__body">
            <#if image == "">
                <img src="<@hst.webfile path="${defaultImage}"/>" alt="${imageAlt}" width="100" height="100" class="infographic__img" />
            <#else>
               <img src="${image}" alt="${imageAlt}" width="100" height="100" class="infographic__img" />
            </#if>
            <div class="infographic__heading">
                <p class="infographic__title">${headline} <span>${explanatory}</span></p>
            </div>
        </div>
        <div class="infographic__footer">
            <p>${qualifying}</p>
        </div>
    </div>
</#macro>

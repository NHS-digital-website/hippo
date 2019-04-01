<#ftl output_format="HTML">

<#macro visualhubBox link>

    <#assign title = link.title>
    <#assign summary = link.summary>
    <@hst.link var="icon" hippobean=link.icon.original fullyQualified=true />

    <#if link.linkType == "internal">
        <#assign title = link.link.title>
    <#elseif link.linkType == "external">
        <#assign summary = link.shortsummary>
    </#if>


    <div class="visualhubBox">

        <div class="visualhubBox-left">

            <h3 class="galleryItems__heading">
                <#if link.linkType == "internal">
                    <#-- Below does not work if declared in section above -->
                    <a href="<@hst.link var="link" hippobean=link.link />">${title}</a>
                <#elseif link.linkType == "external">
                    <a href="${link.link}" onKeyUp="return vjsu.onKeyUp(event)">${title}</a>
                <#elseif link.linkType == "asset">
                    <a href="<@hst.link hippobean=link.link onKeyUp="return vjsu.onKeyUp(event)" />">${title}</a>
                </#if>
            </h3>

            <div>
                ${summary}
            </div>

        </div>

        <div class="visualhubBox-right">
            <img src="${icon}" alt="${title}" />
        </div>

    </div>

</#macro>

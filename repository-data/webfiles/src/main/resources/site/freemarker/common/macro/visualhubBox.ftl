<#ftl output_format="HTML">

<#macro visualhubBox link>

    <#assign title = link.title>
    <#assign summary = link.summary>

    <#if link.linkType == "internal">
        <#assign title = link.link.title>
    <#elseif link.linkType == "external">
        <#assign summary = link.shortsummary>
    </#if>

    <@hst.link var="icon" hippobean=link.icon.original fullyQualified=true />

    <div class="visual-hub-box">
        <div class="visual-hub-box-content" >
            <#if link.linkType == "internal">
            <#-- Below does not work if declared in section above -->
                <a href="<@hst.link var="link" hippobean=link.link />">
            <#elseif link.linkType == "external">
                <a href="${link.link}" onKeyUp="return vjsu.onKeyUp(event)">
            <#elseif link.linkType == "asset">
                <a href="<@hst.link hippobean=link.link onKeyUp="return vjsu.onKeyUp(event)" />">
            </#if>
                <div class="visual-hub-box-content-text">
                    <h3>${title}</h3>
                    ${summary}
                </div>


                <img class="visual-hub-box-content-img inlineable-svg" src="${icon}" alt="${title}"/>
            </a>
        </div>
    </div>

</#macro>

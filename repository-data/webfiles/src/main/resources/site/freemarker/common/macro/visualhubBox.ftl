<#ftl output_format="HTML">

<#assign base64="uk.nhs.digital.freemarker.utils.StringToBase64"?new() />
<#assign colour="uk.nhs.digital.freemarker.svg.SvgChangeColour"?new() />

<#macro visualhubBox link>

    <#if link.title??>
        <#assign title = link.title>
    </#if>
    <#if link.summary??>
        <#assign summary = link.summary>
    </#if>
    <#if link.linkType == "internal">
        <#assign title = link.link.title>
    <#elseif link.linkType == "external">
        <#assign summary = link.shortsummary>
    </#if>

    <div class="visual-hub-box">
        <div class="visual-hub-box-content" >
            <#if link.linkType == "internal">
            <#-- Below does not work if declared in section above -->
                <a href="<@hst.link hippobean=link.link />">
            <#elseif link.linkType == "external">
                <a href="${link.link}" onKeyUp="return vjsu.onKeyUp(event)">
            <#elseif link.linkType == "asset">
                <a href="<@hst.link hippobean=link.link onKeyUp="return vjsu.onKeyUp(event)" />">
            </#if>
                <div class="visual-hub-box-content${((link.icon.original)??)?then('-text', '-full-text')}">
                    <h2>${title}</h2>
                    ${summary}
                </div>
                 <#if (link.icon.original)??>
                     <@hst.link var="icon" hippobean=link.icon.original fullyQualified=true />
                     <#if icon?ends_with("svg")>
                         <#if title?? && title?has_content>
                            <img src="data:image/svg+xml;base64,${base64(colour(link.svgXmlFromRepository, "005eb8"))}" alt="${title}" class="visual-hub-box-content-img" />
                         <#else>
                            <img src="data:image/svg+xml;base64,${base64(colour(link.svgXmlFromRepository, "005eb8"))}" class="visual-hub-box-content-img" />
                         </#if>
                    <#else>
                        <img src="${icon}" alt="${title}" class="visual-hub-box-content-img" />
                    </#if>
                </#if>
            </a>
        </div>
    </div>

</#macro>

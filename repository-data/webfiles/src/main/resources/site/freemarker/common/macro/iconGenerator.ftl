<#ftl output_format="HTML">

<#function buildIconPath id>
    <#local path = "" />

    <#if id = "down">
        <#local path = "<polygon points=\"15,8.5 13.5,7 9,11.2 9,1 7,1 7,11.2 2.5,7 1,8.5 8,15 \"/>" />
    <#elseif id = "up">
        <#local path = "<polygon points=\"1,7.5 2.5,9 7,4.8 7,15 9,15 9,4.8 13.5,9 15,7.5 8,1 \"/>" />
    <#elseif id = "right">
        <#local path = "<path d=\"M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z\"/>" />
    </#if>
    <#return path />
</#function>

<#macro buildInlineSvg id size="m" modClass="">
    <span class="nhsd-a-icon nhsd-a-icon--size-${size} ${modClass}">
        <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
            <#if buildIconPath(id)?has_content>
                <@hst.html formattedText="${buildIconPath(id)}" />
            </#if>
        </svg>
    </span>
</#macro>

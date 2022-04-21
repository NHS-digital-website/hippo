<#ftl output_format="HTML">
<#setting time_zone = "Europe/London">

<#macro lastModified datetime chrome=true label="Last edited: " >
    <#assign output>${label}${datetime?string["d MMMM yyyy h:mm a"]?replace("AM", "am")?replace("PM", "pm")}</#assign>
    <#if chrome>
        <p class="nhsd-t-body nhsd-!t-margin-top-0 nhsd-!t-margin-bottom-8" id="last-edited">
            ${output}
        </p>
    <#else>
        <p class="nhsd-t-body" id="last-edited">
            ${output}
        </p>
    </#if>
</#macro>

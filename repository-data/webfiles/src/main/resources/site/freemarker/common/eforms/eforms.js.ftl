<#ftl output_format="HTML" encoding="UTF-8">

<script data-eforms="setup">
    window.eformsInfo = {
        ajaxValidationUrl: "<@hst.resourceURL escapeXml=false resourceId="validation"/>",
        ajaxSubmissionUrl: "<@hst.actionURL escapeXml=false />",
        name: "${form.name}",
        conditions: ${form.conditionsAsJsonString?no_esc},
        <#if apiScriptServiceEnabled?? && apiScriptServiceEnabled>
        customApiConfig: 'scriptService'
        </#if>
    }
</script>

<#ftl output_format="HTML">

<#macro resourceImage file>
    <#if file?ends_with(".xlsx") || file?ends_with(".xls")>
        <span class="icon icon--chart">&nbsp;</span>
    <#elseif file?ends_with(".pdf")>
        <span class="icon icon--pdf">&nbsp;</span>
    <#elseif file?ends_with(".zip") || file?ends_with(".rar")>
        <span class="icon icon--zip">&nbsp;</span>
    <#else>
        <span class="icon icon-download">&nbsp;</span>
    </#if>
</#macro>

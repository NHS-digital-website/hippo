<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/results.ftl">

<#-- Add meta tags -->
<#include "macro/metaTags.ftl">
<@metaTags></@metaTags>
<#if odsResults?size gt 0>
    Code  --> Org Name</br>
    <#list odsResults as odsOrg>
        ${odsOrg.code} ---> ${odsOrg.orgName} </br>
    </#list>


</#if>
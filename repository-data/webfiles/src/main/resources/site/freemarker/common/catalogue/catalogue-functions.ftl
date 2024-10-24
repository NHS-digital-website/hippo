<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#function updateOrRemoveLinkWithTaxonomyPath link taxonomyPath>
    <#-- Split the link into base URL and query parameters -->
    <#assign parts = link?split("?", 2)>
    <#assign baseURL = parts[0]>
    <#assign queryParams = parts[1]?if_exists>

    <#-- Check if the base URL contains the taxonomy path -->
    <#if baseURL?contains(taxonomyPath)>
    <#-- Remove the taxonomy path from the base URL -->
        <#assign updatedBaseURL = baseURL?replace(taxonomyPath, "")>
    <#else>
    <#-- Append the taxonomy path to the base URL -->
        <#assign updatedBaseURL = baseURL + taxonomyPath>
    </#if>

    <#-- Reassemble the updated link with query parameters if they exist -->
    <#assign updatedLink = updatedBaseURL + (queryParams?has_content?then("?" + queryParams, ""))>

    <#-- Output the updated link without escaping HTML special characters -->
    <#return updatedLink?no_esc>
</#function>
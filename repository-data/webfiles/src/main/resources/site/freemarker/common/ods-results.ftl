<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/results.ftl">

<#-- Add meta tags -->
<#include "macro/metaTags.ftl">
<@metaTags></@metaTags>

<#if odsResults?? && odsResults.anyChildContained>
    <div data-uipath="ods.search-results" data-totalresults="${odsResults.valueMap['Organisations'].children.size()}">

        RESULTS: ${odsResults.valueMap['Organisations'].children.size()}

        <#assign organisations = odsResults.valueMap['Organisations'] />

        <table>
            <tr>
                <th>Type</th>
                <th>Code</th>
                <th>Name</th>
                <th>Postcode</th>
                <th>State</th>
            </tr>
            <#list organisations.children.collection as org>
            <tr>
                <td>${org.valueMap['PrimaryRoleDescription']}</td>
                <td>${org.valueMap['OrgId']}</td>
                <td>
                    <a class="cta__title cta__button" href="ods/detail?orgId=${org.valueMap['OrgId']}" title="${org.valueMap['Name']}" data-uipath="ps.search-results.result.title">
                        ${org.valueMap['Name']}
                    </a>
                </td>
                <td>${org.valueMap['PostCode']}</td>
                <td>${org.valueMap['Status']}</td>
            </tr>
            </#list>

        </table>

    </div>

</#if>

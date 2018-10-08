<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/results.ftl">

<#-- Add meta tags -->
<#include "macro/metaTags.ftl">
<@metaTags></@metaTags>

<#assign hasOdsResult = odsResults?? && odsResults.anyChildContained />

<article class="article article--service">
    <div class="grid-wrapper grid-wrapper--article">

        <#assign org = odsResults.valueMap['Organisation'] />

        <div class="grid-row">
            <div class="column column--reset">
                <div class="local-header article-header">
                    <h1 class="local-header__title">
                        <#if hasOdsResult>
                            ${org.valueMap['Name']}
                        <#else>
                            Not found
                        </#if>
                    </h1>
                </div>
            </div>
        </div>

        <div class="grid-row">

            <div class="column page-block page-block--main">

                <#if hasOdsResult>
                    <table>
                        <tr>
                            <th>Property</th>
                            <th>Value</th>
                        </tr>
                        <tr>
                            <td>Name</td>
                            <td>${org.valueMap['Name']}</td>
                        </tr>
                        <tr>
                            <td>Address</td>
                            <td>
                                ${org.getValue('GeoLoc/Location/AddrLn1')} <br />
                                ${org.getValue('GeoLoc/Location/AddrLn2')} <br />
                                ${org.getValue('GeoLoc/Location/Town')} <br />
                                ${org.getValue('GeoLoc/Location/County')} <br />
                                ${org.getValue('GeoLoc/Location/PostCode')} <br />
                                ${org.getValue('GeoLoc/Location/Country')} <br />
                            </td>
                        </tr>
                        <tr>
                            <td>Date</td>
                            <td>
                                <#assign dates = org.valueMap['Date'].children.collection />

                                <#list dates as date>
                                    TYPE: ${date.getValue('Type')}, START: ${date.getValue('Start')} <br />
                                </#list>
                            </td>
                        </tr>
                        <tr>
                            <td>Status</td>
                            <td>${org.valueMap['Status']}</td>
                        </tr>
                        <tr>
                            <td>LastChangeDate</td>
                            <td>${org.valueMap['LastChangeDate']}</td>
                        </tr>
                        <tr>
                            <td>orgRecordClass</td>
                            <td>${org.valueMap['orgRecordClass']}</td>
                        </tr>
                        <tr>
                            <td>OrgId/root</td>
                            <td>${org.getValue('OrgId/root')}</td>
                        </tr>
                        <tr>
                            <td>OrgId/assigningAuthorityName</td>
                            <td>${org.getValue('OrgId/assigningAuthorityName')}</td>
                        </tr>
                        <tr>
                            <td>OrgId/extension</td>
                            <td>${org.getValue('OrgId/extension')}</td>
                        </tr>

                        <tr>
                            <td>Roles</td>
                            <td>
                                <#assign roles = org.valueMap['Roles'].children.collection />

                                <#list roles as role>
                                    ID: ${role.children.get(0).getValue('id')}<br />
                                    Status: ${role.children.get(0).getValue('Status')}<br />
                                    uniqueRoleId: ${role.children.get(0).getValue('uniqueRoleId')}<br />
                                    primaryRole: ${role.children.get(0).getValue('primaryRole')?c}<br />

                                    <#assign dates = role.valueMap['Date'].children.collection />
                                    <#list dates as date>
                                        TYPE: ${date.getValue('Type')}, START: ${date.getValue('Start')} <br />
                                    </#list>

                                    <hr />

                                </#list>
                            </td>
                        </tr>

                        <tr>
                            <td>Rels</td>
                            <td>
                                <#assign rels = org.valueMap['Rels'].children.collection />

                                <#list rels as rel>
                                    ID: ${rel.children.get(0).getValue('id')}<br />
                                    Status: ${rel.children.get(0).getValue('Status')}<br />
                                    uniqueRelId: ${rel.children.get(0).getValue('uniqueRelId')}<br />

                                    <#assign dates = rel.valueMap['Date'].children.collection />
                                    <#list dates as date>
                                        TYPE: ${date.getValue('Type')}, START: ${date.getValue('Start')} <br />
                                    </#list>

                                    Target/OrgId/root: ${rel.children.get(0).getValue('Target/OrgId/root')}<br />
                                    Target/OrgId/assigningAuthorityName: ${rel.children.get(0).getValue('Target/OrgId/assigningAuthorityName')}<br />
                                    Target/OrgId/extension: ${rel.children.get(0).getValue('Target/OrgId/extension')}<br />

                                    Target/PrimaryRoleId/id: ${rel.children.get(0).getValue('Target/PrimaryRoleId/id')}<br />
                                    Target/PrimaryRoleId/uniqueRoleId: ${rel.children.get(0).getValue('Target/PrimaryRoleId/uniqueRoleId')}<br />

                                    <hr />

                                </#list>
                            </td>
                        </tr>


                    </table>
                </#if>

            </div>
        </div>

    </div>

</article>

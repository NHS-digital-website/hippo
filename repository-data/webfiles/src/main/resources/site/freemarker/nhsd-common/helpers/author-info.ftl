<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#--
A function to pull and standardise author info from documents
-->
<#function authorInfo doc>
    <#local authors = [] />

    <#if doc.authors?has_content>
        <#list doc.authors as author>
            <#local authorTitle = author.title />
            <@hst.link hippobean=author var="authorLink" />
            <#if author.roles?has_content && author.roles.primaryroles?has_content>
                <#local role = author.roles.firstprimaryrole />
            </#if>
            <#if author.roles?has_content && author.roles.primaryroleorg?has_content>
                <#local org = author.roles.primaryroleorg />
            </#if>

            <#local authorInfo = {
                'name': authorTitle,
                'initials': author.initials,
                'link': authorLink,
                'role': role?has_content?then(role, ''),
                'org': org?has_content?then(org, '')
            } />

            <#if author.personimages?has_content && author.personimages.picture?has_content>
                <#local authorInfo += {
                    'image': author.personimages.picture
                } />
            </#if>

            <#local authors += [authorInfo] />
        </#list>
    <#elseif doc.authorName?has_content>
        <#if doc.authorJobTitle?has_content>
            <#local role = doc.authorJobTitle />
        </#if>
        <#if doc.authororganisation?has_content>
            <#local org = doc.authororganisation />
        </#if>
        <#local authors += [{
            'name': doc.authorName,
            'role': role?has_content?then(role, ''),
            'org': org?has_content?then(org, '')
        }] />
    </#if>

    <#return authors />
</#function>

<#include "../include/imports.ftl">
<#assign dateFormat="h:mm a d/MM/yyyy"/>

<div id="searchResults" data-totalresults="${totalSize}">
    <#if result??>
        <h2>${totalSize} result<#if totalSize gt 1>s</#if> found</h2>
        <ul id="resultsList">
            <#list result as publication>
                <li>
                    <a href="<@hst.link hippobean=publication/>" data-document-title="${publication.name}">${publication.title}</a>
                        <@fmt.formatDate value=publication.nominalDate.time type="Date" pattern=dateFormat />
                    <br />
                    ${publication.summary}
                    <hr />
                </li>
            </#list>
        </ul>
    <#else >
         Please fill in a search term
    </#if>
</div>

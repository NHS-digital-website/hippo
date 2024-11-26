<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<nav class="nhsd-m-character-block-list ${ariaLabel}">
    <ul data-uipath="website.glossary.az-nav">
        <#if facets??>
            <#list facets as facetName, facetValues>
                    <#list facetValues as facetValue>
                            <#if facetValue.name == 'Alphabet'>
                                <#list facetValue.folders as value>
                                    <li>
                                        <#if value.count gt 0>
                                            <#if value.leaf>
                                                <@hst.facetnavigationlink var="link" remove=value current=facets />
                                                <a class="nhsd-a-character-block"
                                                   href="${link}"
                                                   aria-label="Jump to all articles '${value.name}'">${value.name}</a>
                                            <#else>
                                                <@hst.link var="link" hippobean=value navigationStateful=true />
                                                <a class="nhsd-a-character-block"
                                                   href="${link}"
                                                   aria-label="Jump to articles starting with the letter '${value.name}'">${value.name}</a>
                                            </#if>
                                        <#else>
                                            <span class="nhsd-a-character-block nhsd-a-character-block--disabled">${value.name}</span>
                                        </#if>
                                    </li>
                                </#list>
                            </#if>
                    </#list>
            </#list>
        </#if>
    </ul>
</nav>
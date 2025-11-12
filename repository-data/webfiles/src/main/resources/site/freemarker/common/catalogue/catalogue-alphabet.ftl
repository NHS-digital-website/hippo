<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<h2 id="side-az-nav-heading" class="nhsd-t-heading-xs">Quick navigation</h2>
<nav class="nhsd-m-character-block-list">
    <ul data-uipath="website.glossary.az-nav">
        <#if alphabetFacets??>
            <#assign rootFacets   = alphabetFacets.rootFacetNavigationBean!alphabetFacets>
            <#assign alphabetRoot = rootFacets.folders?filter(f -> f.name == 'Alphabet')?first>
            <#list alphabetRoot.folders as value>
                <#if value.count gt 0>
                    <#assign isSelected = hstRequestContext.resolvedSiteMapItem.pathInfo?contains("/Alphabet/${value.name}") />
                    <#if isSelected>
                        <@hst.facetnavigationlink var="link" remove=value current=facets />
                        <a class="nhsd-a-character-block nhsd-a-character-block--active"
                           href="${link}"
                           aria-label="Jump to all articles '${value.name}'">${value.name}</a>
                    <#else>
                        <@hst.link var="link" hippobean=value navigationStateful=false />
                        <a class="nhsd-a-character-block"
                           href="${link}"
                           aria-label="Jump to articles starting with the letter '${value.name}'">${value.name}</a>
                    </#if>
                <#else>
                    <span
                        class="nhsd-a-character-block nhsd-a-character-block--disabled">${value.name}</span>
                </#if>
            </#list>
        </#if>
    </ul>
</nav>
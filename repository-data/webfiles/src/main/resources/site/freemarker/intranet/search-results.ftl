<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#assign documentTitle = "Search results" />

<article class="article article--intranet-home">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="article-header">
            <h1 data-uipath="ps.document.title">${documentTitle}</h1>
        </div>

        <div class="grid-row">
            <div class="column column--two-thirds page-block page-block--main">
                <div class="article-section">
                    <p>This is where the search results will show up</p>
                    <#list pageable.items as result>
                        <p>${result.searchResultTitle}</p>
                        <p>${result.searchResultType}</p>
                    </#list>
                </div>
            </div>
        </div>
    </div>
</article>

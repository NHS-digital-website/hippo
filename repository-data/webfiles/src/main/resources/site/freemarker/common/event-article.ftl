<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/articleSections.ftl">
<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="site.website.labels"/>
<@fmt.message key="child-pages-section.title" var="childPagesSectionTitle"/>

<article class="article article--general">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">

            <div class="column column--two-thirds page-block page-block--main">
                <div class="article-header article-header--secondary">
                    <h1>${document.title}</h1>
                </div>
            </div>
        </div>
    </div>
</article>

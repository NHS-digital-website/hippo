<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<@hst.setBundle basename="nationalindicatorlibrary.headers"/>

<section class="document-content">
    <#if document??>
        <h1 data-uipath="ps.document.title">${document.title}</h1>
        <p>${document.summary}</p>      
    </#if>
</section>

<section class="document-content">
    <h2><@fmt.message key="headers.popularTopics"/></h2>
    <div class="nihubSection">
        <div class="document-content">
            <#list document.popularTopicLinks as link>
                <li>
                    <a href="${link.linkUrl}" target="_blank">${link.linkText}</a>
                </li>
            </#list>
        </div>
    </div>
</section>

<section class="document-content">
    <h2><@fmt.message key="headers.topicsAZ"/></h2>
    <div id="a-z" class="push-double--bottom">
        <#list document.azTaxonomySearchString as searchString>
                <a title="${searchString[0]}" class="niHubAlpha" href="${searchString[1]}" title="">${searchString[0]}</a>
        </#list>
    </div>
</section>


<section class="document-content">
    <div class="nihubIgbSection">
        <h2><@fmt.message key="headers.igbAssurance"/></h2>
        <div>
            <div class="layout__item layout-5-6">
                <p class="zeta" style="text-align:center;">${document.igbHubLink.summary}</p>
                <h3 style="text-align:center;"><a href="${document.igbHubLink.pageLink}" title="${document.igbHubLink.title}">${document.igbHubLink.title}</a></h3>  
            </div>
        </div>
    </div>
</section>

<section class="document-content">
    <h2><@fmt.message key="headers.indicatorsAndAssurance"/></h2>

    <div class="nihubSection">
        <div class="document-content">
            <#list document.niHubLink as link><!--
             --><div class="layout__item layout-1-3">
                    <h3><a href="${link.pageLink}" title="${link.title}">${link.title}</a></h3>
                    <p class="zeta">${link.summary}</p>
                </div><!--
         --></#list>
        </div>
    </div>
</section>

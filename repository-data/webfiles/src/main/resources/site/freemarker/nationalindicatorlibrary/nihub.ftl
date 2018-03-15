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
        <div class="document-content" style="text-align:center">
            <#list document.popularTopicLinks as link>
                <div class="layout__item layout-1-4">
                    <h3><a href="${link.linkUrl}" target="_blank">${link.linkText}</a></h3>
                </div>
            </#list>
        </div>
    </div>
</section>

<section class="document-content">
    <h2><@fmt.message key="headers.indicatorsAndAssurance"/></h2>
    <div class="nihubSection"><br>
    <div class="nihubIgbSection">
        <div>
            <p class="zeta" style="text-align:center;"><#outputformat "undefined">${document.gettingAssured.content.content}</#outputformat></p>
            <h3 style="text-align:center;"><a href="${document.gettingAssured.pageLink}" title="${document.gettingAssured.title}">${document.gettingAssured.title}</a></h3>  
            <br>
        </div>
    </div>
    </div>
</section>


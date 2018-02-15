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
    <h2><@fmt.message key="headers.popularTopics"/></h1>
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
    <h2><@fmt.message key="headers.topicsAZ"/></h1>
    <div class="nihubSection">
        <div class="document-content">
            <#list ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'] as x>
                <a class="niHubAlpha" href="/indicators/search?topic=a" title="">${x}</a>
            </#list>
        </div>
    </div>
</section>

<section class="document-content">
    <h2><@fmt.message key="headers.indicatorsAndAssurance"/></h1>

    <div class="nihubSection">
            <div class="document-content">
                <#list document.niHubLink as link><!--
                    --><div class="layout__item layout-1-3">
                        <@hst.link var="landingPageLink" hippobean=link.pageLink />
                        <h3><a href="${landingPageLink}" title="${link.title}">${link.title}</a></h3>
                        <p class="zeta">${link.summary}</p>
                    </div><!--
                --></#list>
            </div>
    </div>
</section>


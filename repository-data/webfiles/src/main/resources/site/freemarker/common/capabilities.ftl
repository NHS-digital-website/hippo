<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/documentHeader.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">
<#-- Add meta tags -->
<@metaTags></@metaTags>
<article class="article">
    <form role="search" method="get" action="" class="search-banner__form">
        <div class="grid-wrapper grid-wrapper--article">
            <div class="grid-row">
                <ul>
                    <div class="article-section">
                        <h2>${headingText}</h2>
                    </div>
                    <#list items as item>
                        <#assign itemId = 'None' />
                        <#if item.id??>
                            <#assign itemId = item.id />
                        </#if>
                        <li>
                            <a href="capabilities?id=${itemId}"/>${item.name}</a><br/>
                            ${item.description}
                            <br/>
                            <br/>
                        </li>
                    </#list>
                </ul>
            </div>
        </div>
    </form>
</article>

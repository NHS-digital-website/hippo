<#ftl output_format="HTML">

<#include "../../include/imports.ftl">

<#macro sectionNav links title="Page contents">
<div class="article-section-nav-wrapper">
    <div class="article-section-nav">
        <h2 class="article-section-nav__title">${title}</h2>
        <#if links??>
            <nav>
                <ol class="article-section-nav__list">
                    <#list links as link>
                        <#assign label = "Scroll to '${link.title}'" />
                        <li>
                            <a href="#${slugify(link.title)}" aria-label="${label}" title="${label}">${link.title}</a>
                        </li>
                    </#list>
                </ol>
            </nav>
        </#if>
    </div>
</div>
</#macro>

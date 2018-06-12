<#ftl output_format="HTML">

<#macro sectionNav links title="Page contents">
<div class="article-section-nav">
    <h2 class="article-section-nav__title">${title}</h2>
    <hr>
    <#if links??>
        <nav role="navigation" class="article-section-nav__contents">
            <ol class="article-section-nav__list">
            <#list links as link>
                <#assign label = "Scroll to '${link.title}'" />
                <#if link["aria-label"]??>
                    <#assign label = link["aria-label"] />
                </#if>
            
                <li>
                    <a href="${link.url}" aria-label=${label} title="${label}">${link.title}</a>
                </li>
            </#list>
            </ol>
        </nav>
    </#if>
</div>
</#macro>

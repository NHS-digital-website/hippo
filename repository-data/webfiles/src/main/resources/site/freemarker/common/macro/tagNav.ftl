<#ftl output_format="HTML">

<#macro tagNav links title="Filter by type">
<div class="article-section-nav-wrapper">
    <div class="article-section-nav">
        <h2 class="article-section-nav__title">${title}</h2>
        <#if links??>
            <nav role="navigation">
                <ol class="article-section-nav__list article-section-nav__list--tag-links">
                <#list links as link>
                    <li>
                        <a href="${link.url}" aria-label="Show '${link.title}' types only" title="Show '${link.title}' types only" class="tag-link">${link.title}</a>
                    </li>
                </#list>
                </ol>
            </nav>
        </#if>
    </div>
</div>
</#macro>

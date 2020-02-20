<#ftl output_format="HTML">

<#macro stickyNavYears links affix="" title="Filter by year">
<div class="article-section-nav-wrapper">
    <div class="article-section-nav">
        <h2 class="article-section-nav__title">${title}</h2>
        <#if links??>
            <nav>
                <ol class="article-section-nav__list article-section-nav__list--tag-links">
                <#list links as link>
                    <li>
                        <#if selectedYear == link.key>
                            <a href="${"?year=" + link.key + affix}" title="Show '${link.title}' only" class="tag-link selected" data-slugified-value="${slugify(link.title)}">${link.title}</a>
                        <#else>
                            <a href="?year=${link.key + affix}" title="Show '${link.title}' only" class="tag-link" data-slugified-value="${slugify(link.title)}">${link.title}</a>
                        </#if>
                    </li>
                </#list>
                </ol>
            </nav>
        </#if>
    </div>
</div>
</#macro>

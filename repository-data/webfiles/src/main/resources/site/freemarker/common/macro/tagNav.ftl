<#ftl output_format="HTML">

<#macro tagNav links title="Filter by type">
<div class="article-section-nav-wrapper">
    <div class="article-section-nav">
        <h2 class="article-section-nav__title">${title}</h2>
        <#if links??>
            <nav>
                <ol class="article-section-nav__list article-section-nav__list--tag-links">
                <#list links as link>
                    <li>
                        <#if selectedTypes?seq_contains(link.key)>
                            <#assign linkout = "&type=" + selectedTypes?join("&type=") />
                            <a href="${getDocumentUrl()}${linkout?replace("&type="+link.key, "")?replace("&", "?", "f")}" aria-label="Show '${link.title}' types only" title="Show '${link.title}' types only" class="tag-link selected">${link.title}</a>
                        <#else>
                            <a href="?type=${selectedTypes?join("&type=", "", "&type=")}${link.key}" aria-label="Show '${link.title}' types only" title="Show '${link.title}' types only" class="tag-link">${link.title}</a>
                        </#if>
                    </li>
                </#list>
                </ol>
            </nav>
        </#if>
    </div>
</div>
</#macro>

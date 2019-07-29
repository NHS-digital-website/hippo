<#ftl output_format="HTML">

<#macro tagNav links affix title filter selectedFilter>
<div class="article-section-nav-wrapper">
    <div class="article-section-nav">
        <h2 class="article-section-nav__title">${title}</h2>
        <#if links??>
            <nav>
                <ol class="article-section-nav__list article-section-nav__list--tag-links">
                    <#list links as link>
                    <li>
                        <#if selectedFilter?seq_contains(link.key)>
                        <#assign linkout = "&" + filter + "=" + selectedFilter?join("&" + filter + "=") + affix />
                          <a href="${getDocumentUrl()}${linkout?replace("&${filter}="+link.key, "")?replace("&", "?", "f")}" title="Show '${link.title}' ${filter} only" class="tag-link selected">${link.title}</a>
                        <#else>
                          <a href="?${filter}=${selectedFilter?join("&" + filter + "=", "", "&" + filter + "=")}${link.key + affix}" title="Show '${link.title}' ${filter} only" class="tag-link">${link.title}</a>
                        </#if>
                    </li>
                </#list>
                </ol>
            </nav>
        </#if>
    </div>
</div>
</#macro>

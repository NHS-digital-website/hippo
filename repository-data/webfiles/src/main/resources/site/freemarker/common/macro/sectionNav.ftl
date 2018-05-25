<#ftl output_format="HTML">

<#macro sectionNav links title="Page contents">
<div class="article-section-nav">
    <h2 class="article-section-nav__title">${title}</h2>
    <hr>
    <#if links??>
        <nav role="navigation">
            <ol class="article-section-nav__list">
            <#list links as link>
                <li>
                    <a href="${link.url}" aria-label="Scroll to '${link.title}'" title="Scroll to '${link.title}'">${link.title}</a>
                </li>
            </#list>
            </ol>
        </nav>
    </#if>
</div>
</#macro>

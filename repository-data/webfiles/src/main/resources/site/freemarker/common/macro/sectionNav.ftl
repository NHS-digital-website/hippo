<#ftl output_format="HTML">

<#macro sectionNav links title="Page contents">
<div class="article-section-nav-wrapper">
    <div class="article-section-nav">
        <h2 class="article-section-nav__title">${title}</h2>
        <#assign numberedListCount=0 />
        <#if links??>
            <nav>
                <ol class="article-section-nav__list">
                    <#list links as link>
                        <#assign label = "Scroll to '${link.title}'" />
                        <#if link["aria-label"]??>
                            <#assign label = link["aria-label"] />
                        </#if>
                        <#if link.isNumberedList?has_content && link.isNumberedList == "true">
                            <#assign numberedListCount++ />
                            <#assign numberedLinkTitle = numberedListCount + ". " + link.title>
                            <li class="section-numbered">
                                <a href="${link.url}" aria-label="${label}" title="${label}">${numberedLinkTitle}</a>
                            </li>
                        <#else>
                            <li>
                                <a href="${link.url}" aria-label="${label}" title="${label}">${link.title}</a>
                            </li>
                        </#if>
                    </#list>
                </ol>
            </nav>
        </#if>
    </div>
</div>
</#macro>

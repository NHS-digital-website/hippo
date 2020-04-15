<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.NavigationTile" -->

<#macro linkDestination link>
    <#if link.linkType == 'external'>
        ${link.link}
    <#else>
        <@hst.link hippobean=link.link />
    </#if>
</#macro>

<#macro navigationTile tile tileType="full" imageType="icon">

    <#assign hasLink = tile.link?? && tile.link?size gt 0 />

    <#if hasLink>
        <#local link = tile.link?first />
    </#if>

    <a
        class="navigation-tile navigation-tile--${tileType}"
        href="<@linkDestination link />"
    >
        <#if (tile.image.original)??>
            <@hst.link hippobean=tile.image.original fullyQualified=true var="image" />
            <#if (image?ends_with("svg") && imageType == 'icon')>
                <img src="${image?replace("/binaries", "/svg-magic/binaries")}?colour=005eb8"
                     alt="${title}"
                     class="navigation-tile__image navigation-tile__image--icon"/>
            <#else>
                <img src="${image}" alt="${title}"
                     class="navigation-tile__image"/>
            </#if>
        </#if>
        <div class="navigation-tile__content">
            <h3 class="navigation-tile__title">${tile.title}</h3>
            <p class="navigation-tile__description">
                ${tile.description}
            </p>
            <p class="navigation-tile__link">
                <span class="navigation-tile__link-text">${tile.actionDescription}</span>
                <span class="navigation-tile__link-arrow">
                    <img aria-hidden="true" alt="Right Arrow"
                         src="<@hst.webfile path="/images/chapter-navigation/right-arrow.svg"/>">
                </span>
            </p>
        </div>
    </a>
</#macro>

<#ftl output_format="HTML">

<#macro iconListItemWithoutLink iconListItem>
    <article class="nhsd-m-icon-list-item nhsd-!t-margin-bottom-6">
         <div class="nhsd-m-icon-list-item__box nhsd-m-icon-list-item__without-link" >
            <span class="nhsd-a-icon nhsd-a-icon--size-xl" style="min-width:36px;min-height:36px;">
                <@hst.link hippobean=iconListItem.image.original fullyQualified=true var="iconImage" />
                <#if iconImage?ends_with("svg")>
                    <img aria-hidden="true"  src="${iconImage?replace("/binaries", "/svg-magic/binaries")}?colour=231f20" alt="${section.title}" width="100" height="100" />
                <#else>
                    <img aria-hidden="true" src="${iconImage}" alt="${section.title}" width="100" height="100" />
                </#if>
            </span>

            <div>
                <#if iconListItem.heading?has_content >
                    <span class="nhsd-t-heading-s" data-uipath="website.contentblock.iconlistitem.heading">${iconListItem.heading}</span>
                </#if>
                <div class="nhsd-t-word-break" data-uipath="website.contentblock.iconlistitem.description" >
                    <@hst.html hippohtml=iconListItem.description contentRewriter=brContentRewriter />
                </div>
            </div>
        </div>
    </article>

</#macro>
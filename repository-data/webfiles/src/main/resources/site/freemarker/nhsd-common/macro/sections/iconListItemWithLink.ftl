<#ftl output_format="HTML">

<#assign base64="uk.nhs.digital.freemarker.utils.StringToBase64"?new() />
<#assign colour="uk.nhs.digital.freemarker.svg.SvgChangeColour"?new() />

<#macro iconListItemWithLink iconListItem>

    <article class="nhsd-m-icon-list-item nhsd-!t-margin-bottom-6">

        <#list iconListItem.itemlink as link>
            <#if link.linkType == "internal">
            <a href="<@hst.link hippobean=link.link />" onClick="logGoogleAnalyticsEvent('Link click','Person','${link.link.title}');" onKeyUp="return vjsu.onKeyUp(event)" title="${iconListItem.heading}" class="nhsd-m-icon-list-item__box nhsd-m-icon-list-item__with-link" aria-label="${iconListItem.heading}" >
            <#else>
            <a href="${link.link}" onClick="logGoogleAnalyticsEvent('Link click','Person','${link.link}');" onKeyUp="return vjsu.onKeyUp(event)" title="${iconListItem.heading}" class="nhsd-m-icon-list-item__box nhsd-m-icon-list-item__with-link" aria-label="${iconListItem.heading}" >
            </#if>
                <span class="nhsd-a-icon nhsd-a-icon--size-xl" style="min-width:36px;min-height:36px;">
                    <@hst.link hippobean=iconListItem.image.original fullyQualified=true var="iconImage" />
                    <#if iconImage?ends_with("svg")>
                        <#if iconListItem.heading?? && iconListItem.heading?has_content>
                            <img aria-hidden="true"  src="data:image/svg+xml;base64,${base64(colour(iconListItem.svgXmlFromRepository, "231f20"))}" alt="${iconListItem.heading}" width="100" height="100" />
                        <#else>
                            <img aria-hidden="true"  src="data:image/svg+xml;base64,${base64(colour(iconListItem.svgXmlFromRepository, "231f20"))}" width="100" height="100" />
                        </#if>
                    <#else>
                        <img aria-hidden="true" src="${iconImage}" alt="${iconListItem.heading}" width="100" height="100" />
                    </#if>
                </span>

                <div>
                    <#if iconListItem.heading?has_content >
                        <span class="nhsd-t-body nhsd-m-icon-list-item__with-link-text" data-uipath="website.contentblock.iconlistitem.heading">${iconListItem.heading}</span>
                    </#if>
                    <div class="nhsd-t-word-break" data-uipath="website.contentblock.iconlistitem.description" >
                        <@hst.html hippohtml=iconListItem.description contentRewriter=brContentRewriter />
                    </div>
                    <span class="nhsd-a-icon nhsd-a-arrow nhsd-a-icon--size-s nhsd-a-icon--col-black">
                        <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                            <path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/>
                        </svg>
                    </span>
                </div>
            </a>
        </#list>
    </article>

</#macro>
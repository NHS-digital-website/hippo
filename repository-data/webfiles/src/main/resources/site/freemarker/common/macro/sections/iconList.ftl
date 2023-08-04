<#ftl output_format="HTML">

<#assign base64="uk.nhs.digital.freemarker.utils.StringToBase64"?new() />
<#assign colour="uk.nhs.digital.freemarker.svg.SvgChangeColour"?new() />

<#macro iconList section mainHeadingLevel=2 >
    <div id="${slugify(section.title)}" class="${(section.headingLevel == 'Main heading')?then('article-section navigationMarker', 'article-header__detail-lines')}">
        <#if section.title?has_content>
            <#if section.headingLevel == 'Main heading'>
                <#assign mainHeadingTag = "h" + mainHeadingLevel />
                <${mainHeadingTag} data-uipath="website.contentblock.iconlist.title">${section.title}</${mainHeadingTag}>
            <#else>
                <#assign subHeadingLevel = mainHeadingLevel?int + 1 />
                <#assign subHeadingTag = "h" + subHeadingLevel />
                <${subHeadingTag} data-uipath="website.contentblock.iconlist.title">${section.title}</${subHeadingTag}>
            </#if>
        </#if>

        <div data-uipath="website.contentblock.iconlist.introduction">
            <@hst.html hippohtml=section.introduction contentRewriter=gaContentRewriter />
        </div>

        <div class="iconList">
            <#list section.iconListItems as iconListItem>

                <!-- iconListItem -->
                <div class="iconList__item">
                    <div class="iconList__icon">
                        <@hst.link hippobean=iconListItem.image.original fullyQualified=true var="iconImage" />
                        <#if iconImage?ends_with("svg")>
                            <#if section.title?? && section.title?has_content>
                                <img class="iconList__icon__img" aria-hidden="true"  src="data:image/svg+xml;base64,${base64(colour(iconListItem.svgXmlFromRepository, "005EB8"))}" alt="${section.title}" width="100" height="100" />
                            <#else>
                                <img class="iconList__icon__img" aria-hidden="true"  src="data:image/svg+xml;base64,${base64(colour(iconListItem.svgXmlFromRepository, "005EB8"))}" width="100" height="100" />
                            </#if>
                        <#else>
                            <img class="iconList__icon__img" aria-hidden="true" src="${iconImage}" alt="${section.title}" width="100" height="100" />
                        </#if>
                    </div>

                    <div class="iconList__content">
                        <#if iconListItem.itemlink?has_content>
                          <div class="iconList__title" data-uipath="website.contentblock.iconlistitem.heading">
                            <#list iconListItem.itemlink as link>
                              <#if link.linkType == "internal">
                                <a href="<@hst.link hippobean=link.link />" title="${iconListItem.heading}">${iconListItem.heading}</a>
                              <#else>
                                <a href="${link.link}" title="${iconListItem.heading}">${iconListItem.heading}</a>
                              </#if>
                            </#list>
                          </div>
                        <#else>
                              <div class="iconList__title" data-uipath="website.contentblock.iconlistitem.heading">${iconListItem.heading}</div>
                        </#if>

                        <div class="iconList__body" data-uipath="website.contentblock.iconlistitem.description">
                            <@hst.html hippohtml=iconListItem.description contentRewriter=gaContentRewriter />
                        </div>
                    </div>
                </div>

            </#list>
        </div>
    </div>
</#macro>

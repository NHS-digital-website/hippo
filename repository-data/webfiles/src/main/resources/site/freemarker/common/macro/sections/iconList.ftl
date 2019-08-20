<#ftl output_format="HTML">

<#macro iconList section>
    <div id="${slugify(section.title)}" class="${(section.headingLevel == 'Main heading')?then('article-section', 'article-header__detail-lines')}">
        <#if section.title?has_content>
            <#if section.headingLevel == 'Main heading'>
                <h2 data-uipath="website.contentblock.iconlist.title">${section.title}</h2>
            <#else>
                <h3 data-uipath="website.contentblock.iconlist.title">${section.title}</h3>
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
                            <img class="iconList__icon__img" aria-hidden="true"  src="${iconImage?replace("/binaries", "/svg-magic/binaries")}?colour=005EB8" alt="${section.title}" />
                        <#else>
                            <img class="iconList__icon__img" aria-hidden="true" src="${iconImage}" alt="${section.title}" />
                        </#if>
                    </div>

                    <div class="iconList__content">
                        <#if iconListItem.itemlink?has_content>
                          <div class="iconList__title" data-uipath="website.contentblock.iconlistitem.heading">
                            <#list iconListItem.itemlink as link>
                              <#if link.linkType == "internal">
                                <a href="<@hst.link hippobean=link.link />" onClick="logGoogleAnalyticsEvent('Link click','Person','${link.link.title}');" onKeyUp="return vjsu.onKeyUp(event)" title="${iconListItem.heading}">${iconListItem.heading}</a>
                              <#else>
                                <a href="${link.link}" onClick="logGoogleAnalyticsEvent('Link click','Person','${link.link}');" onKeyUp="return vjsu.onKeyUp(event)" title="${iconListItem.heading}">${iconListItem.heading}</a>
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

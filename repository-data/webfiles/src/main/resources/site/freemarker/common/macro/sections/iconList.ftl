<#ftl output_format="HTML">

<#macro iconList section>
    <div class="${(section.headingLevel == 'Main heading')?then('article-section', 'article-header__detail-lines')}">
        <#if section.title?has_content>
            <#if section.headingLevel == 'Main heading'>
                <h2 data-uipath="website.contentblock.iconlist.title" id="${slugify(section.title)}">${section.title}</h2>
            <#else>
                <h3 data-uipath="website.contentblock.iconlist.title">${section.title}</h3>
            </#if>
        </#if>

        <div data-uipath="website.contentblock.iconlist.introduction">
            <@hst.html hippohtml=section.introduction contentRewriter=gaContentRewriter />
        </div>

        <dl class="iconList">
            <#list section.iconListItems as iconListItem>

                <!-- iconListItem -->
                <div class="iconList__item">
                    <div class="iconList__icon">
                        <@hst.link hippobean=iconListItem.image.original fullyQualified=true var="iconImage" />
                        <img aria-hidden="true" src="${iconImage}" alt="" />
                    </div>

                    <div class="iconList__content">
                        <dt class="iconList__title" data-uipath="website.contentblock.iconlistitem.heading">${iconListItem.heading}</dt>

                        <dd class="iconList__body" data-uipath="website.contentblock.iconlistitem.description">
                            <@hst.html hippohtml=iconListItem.description contentRewriter=gaContentRewriter />
                        </dd>
                    </div>
                </div>

            </#list>
        </dl>
    </div>
</#macro>

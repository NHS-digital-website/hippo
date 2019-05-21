<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.Download" -->

<#include "../fileIconByMimeType.ftl">
<#include "../fileIconByFileType.ftl">
<#include "../typeSpan.ftl">

<#macro downloadSection section>

    <#assign hasLinks = section.items?? && section.items?size gt 0 />

    <div class="${(section.headingLevel == 'Main heading')?then('article-section', 'article-header__detail-lines')}">

        <#if section.headingLevel == 'Main heading'>
            <h2 data-uipath="website.contentblock.download.title" id="${slugify(section.heading)}">${section.heading}</h2>
        <#else>
            <h3 data-uipath="website.contentblock.download.title">${section.heading}</h3>
        </#if>

        <div data-uipath="website.contentblock.download.description"><@hst.html hippohtml=section.description contentRewriter=gaContentRewriter /></div>

        <#if hasLinks>
        <div class="article-section--list">
            <div class="grid-row">
                <div class="column column--reset">
                    <ul class="list list--reset cta-list cta-list--sections">
                        <#list section.items as block>
                        <li>
                            <#if block.linkType??>
                                <@typeSpan block.linkType />

                                <#if block.linkType == "internal">

                                    <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, block.link.title) />
                                    <a href="<@hst.link hippobean=block.link />"
                                       class="block-link"
                                       onClick="${onClickMethodCall}"
                                       onKeyUp="return vjsu.onKeyUp(event)">
                                        <div class="block-link__header">
                                            <span class="icon icon--2x icon--html" aria-hidden="true"></span>
                                        </div>
                                        <div class="block-link__body">
                                            <span class="block-link__title">${block.link.title}</span>
                                            <p class="cta__text">${block.link.shortsummary}</p>
                                        </div>
                                    </a>

                                <#elseif block.linkType == "external">
                                    <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, block.title) />

                                    <a href="${block.link}"
                                       class="block-link"
                                       onClick="${onClickMethodCall}"
                                       onKeyUp="return vjsu.onKeyUp(event)">
                                        <div class="block-link__header">
                                            <@fileIconByFileType block.link />
                                        </div>
                                        <div class="block-link__body">
                                            <span class="block-link__title">${block.title}</span>
                                            <p class="cta__text">${block.shortsummary}</p>
                                        </div>
                                    </a>

                                <#elseif block.linkType == "asset">
                                    <@hst.link hippobean=block.link var="link" />
                                    <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, link) />

                                    <a href="${link}"
                                       class="block-link"
                                       onClick="${onClickMethodCall}"
                                       onKeyUp="return vjsu.onKeyUp(event)">
                                    <div class="block-link__header">
                                        <@fileIconByMimeType block.link.asset.mimeType></@fileIconByMimeType>
                                    </div>
                                    <div class="block-link__body">
                                        <span class="block-link__title">${block.title}</span>
                                        <@fileMetaAppendix block.link.asset.getLength()></@fileMetaAppendix>
                                    </div>
                                    </a>
                                </#if>
                            </#if>
                        </li>
                        </#list>
                    </ul>
                    </div>
                </div>
            </div>
        </#if>


    </div>
</#macro>

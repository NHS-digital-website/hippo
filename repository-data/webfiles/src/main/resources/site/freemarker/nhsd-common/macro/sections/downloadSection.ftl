<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.Download" -->
<#include "../fileIconByMimeType.ftl">
<#include "../fileIconByFileType.ftl">
<#include "../fileMetaAppendix.ftl">
<#include "../typeSpan.ftl">
<#include "../component/downloadBlockInternal.ftl">
<#include "../component/downloadBlockAsset.ftl">
<#include "../component/downloadBlockExternal.ftl">

<#macro downloadSection section mainHeadingLevel=2 >

    <#assign hasLinks = section.items?? && section.items?size gt 0 />

    <div id="${slugify(section.heading)}" class="${(section.headingLevel == 'Main heading')?then('article-section navigationMarker', 'article-header__detail-lines navigationMarker-sub')}">

        <#if section.headingLevel == 'Main heading'>
            <p class="nhsd-t-heading-l" data-uipath="website.contentblock.download.title">${section.heading}</p>
        <#else>
            <p class="nhsd-t-heading-s" data-uipath="website.contentblock.download.title">${section.heading}</p>
        </#if>

        <div data-uipath="website.contentblock.download.description">
            <@hst.html hippohtml=section.description contentRewriter=brContentRewriter />
        </div>

        <#if hasLinks>
            <div class="nhsd-t-grid">
                <div class="nhsd-t-row">
                    <div class="nhsd-t-col">
                        <#list section.items as block>
                            <#if block.linkType??>
                                <#if block.linkType == "internal">
                                    <@downloadBlockInternal document.class.name block.link block.link.title block.link.shortsummary  />
                                <#elseif block.linkType == "external">
                                    <@downloadBlockExternal document.class.name block.link "${block.title}" "${block.shortsummary}" />
                                <#elseif block.linkType == "asset">
                                    <@downloadBlockAsset document.class.name block.link "${block.title}" "" block.link.asset.mimeType block.link.asset.getLength()  />
                                </#if>
                            </#if>
                        </#list>
                    </div>
                </div>
            </div>
        </#if>
    </div>
</#macro>

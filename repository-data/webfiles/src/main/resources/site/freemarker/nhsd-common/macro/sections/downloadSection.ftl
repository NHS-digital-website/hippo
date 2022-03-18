<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.Download" -->
<#include "../fileIconByMimeType.ftl">
<#include "../fileIconByFileType.ftl">
<#include "../fileMetaAppendix.ftl">
<#include "../typeSpan.ftl">
<#include "../component/downloadBlockInternal.ftl">
<#include "../component/downloadBlockAsset.ftl">
<#include "../component/downloadBlockExternal.ftl">

<#macro downloadSection section mainHeadingLevel=2 orgPrompt=false>
    <#assign hasLinks = section.items?? && section.items?size gt 0 />

    <div id="${slugify(section.heading)}" class="${(section.headingLevel == 'Main heading')?then('article-section navigationMarker', 'article-header__detail-lines navigationMarker-sub')} nhsd-!t-margin-bottom-6">

        <#if section.headingLevel == 'Main heading'>
            <h2 class="nhsd-t-heading-xl" data-uipath="website.contentblock.download.title">${section.heading}</h2>
        <#else>
            <h3 class="nhsd-t-heading-l" data-uipath="website.contentblock.download.title">${section.heading}</h3>
        </#if>

        <div data-uipath="website.contentblock.download.description">
            <@hst.html hippohtml=section.description contentRewriter=brContentRewriter />
        </div>

        <#if hasLinks>
            <div class="nhsd-t-grid nhsd-t-grid--nested">
                <div class="nhsd-t-row">
                    <div class="nhsd-t-col">
                        <#list section.items as block>
                            <#if block.linkType??>
                                <div ${block?is_last?then('', 'class=nhsd-!t-margin-bottom-4')}>
                                    <#if block.linkType == "internal">
                                        <@downloadBlockInternal document.class.name block.link block.link.title block.link.shortsummary getFileExtension(block.link.name) />
                                    <#elseif block.linkType == "external">
                                        <#if getMimeTypeByExtension(getFileExtension(block.link))?has_content>
                                            <@downloadBlockAsset document.class.name block.link "${block.title}" "${block.shortsummary}" getMimeTypeByExtension(getFileExtension(block.link)) "" true true orgPrompt />
                                        <#else>
                                            <@downloadBlockExternal document.class.name block.link "${block.title}" "${block.shortsummary}"/>
                                        </#if>
                                    <#elseif block.linkType == "asset">
                                        <@downloadBlockAsset document.class.name block.link "${block.title}" "" block.link.asset.mimeType block.link.asset.getLength() false false orgPrompt/>
                                    </#if>
                                </div>
                            </#if>
                        </#list>
                    </div>
                </div>
            </div>
        </#if>
    </div>
</#macro>

<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "fileMetaAppendix.ftl">
<#include "typeSpan.ftl">
<#include "fileIconByMimeType.ftl">
<#include "component/downloadBlockInternal.ftl">
<#include "component/downloadBlockAsset.ftl">
<#include "component/downloadBlockExternal.ftl">

<#macro childPageGrid childPages>
    <#if childPages?has_content>
        <div class="nhsd-t-grid nhsd-!t-no-gutters">
            <div class="nhsd-t-row">
                <#list childPages as childPage>
                    <div class="nhsd-t-col-12">
                        <article>
                            <#if childPage.linkType??>
                                <#-- Assign the link property of the externallink compound -->
                                <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, childPage.link) />

                                <@typeSpan childPage.linkType />

                                <#if childPage.linkType == "external">
                                    <#if getMimeTypeByExtension(getFileExtension(childPage.link))?has_content>
                                        <@downloadBlockAsset document.class.name childPage.link "${childPage.title}" "${childPage.shortsummary}" getMimeTypeByExtension(getFileExtension(childPage.link)) "" true />
                                    <#else>
                                        <@downloadBlockExternal document.class.name childPage.link "${childPage.title}" "${childPage.shortsummary}" />
                                    </#if>
                                <#elseif childPage.linkType == "asset">
                                    <@downloadBlockAsset document.class.name childPage.link childPage.title "" childPage.link.asset.mimeType childPage.link.asset.getLength() />
                                </#if>
                            <#elseif hst.isBeanType(childPage, 'org.hippoecm.hst.content.beans.standard.HippoBean')>
                                <@typeSpan "internal" />
                                <@downloadBlockInternal document.class.name childPage childPage.title childPage.shortsummary  />
                            </#if>
                        </article>
                    </div>
                </#list>
            </div>
        </div>
    </#if>
</#macro>

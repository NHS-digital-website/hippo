<#ftl output_format="HTML">

<#-- @ftlvariable name="childPages" type="java.util.List<uk.nhs.digital.common.components.DocumentChildComponent>" -->

<#include "fileMetaAppendix.ftl">
<#include "typeSpan.ftl">
<#include "fileIconByMimeType.ftl">
<#include "./component/downloadBlockAsset.ftl">

<#macro furtherInformationSection childPages orgPrompt=false>
    <@hst.setBundle basename="rb.generic.headers"/>
    <#-- BEGIN optional 'Further information section' -->
    <#if childPages?has_content>
    <div class="nhsd-!t-margin-bottom-8" id="further-information">
        <h2 class="nhsd-t-heading-xl"><@fmt.message key="headers.further-information" /></h2>
        <div class="nhsd-t-grid nhsd-!t-no-gutters">
            <#list childPages as childPage>
                <div class="nhsd-t-row">
                    <div class="nhsd-t-col-12 nhsd-!t-no-gutters">
                    <#-- If external link -->
                    <#if childPage.linkType??>

                        <@typeSpan childPage.linkType />

                        <#if childPage.linkType == "external">
                            <article class="nhsd-!t-margin-bottom-6">
                                <a class="nhsd-a-link" href="${childPage.link}"
                                   onClick="${getOnClickMethodCall(document.class.name, childPage.link)}" onKeyUp="return vjsu.onKeyUp(event)" rel="external">
                                    ${childPage.title}
                                    <span class="nhsd-t-sr-only">(external link, opens in a new tab)</span>
                                </a>
                                <#if childPage.shortsummary?? && childPage.shortsummary?has_content>
                                    <p class="nhsd-t-body-s nhsd-!t-margin-top-1">${childPage.shortsummary}</p>
                                </#if>
                            </article>
                        </#if>

                    <#-- If internal link -->
                    <#elseif hst.isBeanType(childPage, 'org.hippoecm.hst.content.beans.standard.HippoBean')>
                        <@hst.link hippobean=childPage var="relatedSubjectLink"/>
                        <article class="nhsd-!t-margin-bottom-6">
                            <@typeSpan "internal" />
                            <a class="nhsd-a-link" href="<@hst.link hippobean=childPage />" onClick="${getOnClickMethodCall(document.class.name, relatedSubjectLink)}">${childPage.title}</a>
                            <#if childPage.shortsummary?? && childPage.shortsummary?has_content>
                                <p class="nhsd-t-body-s nhsd-!t-margin-top-1">${childPage.shortsummary}</p>
                            </#if>
                        </article>
                    </#if>
                    </div>
                </div>
            </#list>
        </div>

        <div class="nhsd-t-grid nhsd-!t-no-gutters">
        <#list childPages as childPage>
            <div class="nhsd-t-row">
                <#if childPage.linkType??>
                    <#-- If asset link -->
                    <#if childPage.linkType == "asset">
                        <div class="nhsd-t-col-12 nhsd-!t-no-gutters nhsd-!t-margin-bottom-6">
                            <@downloadBlockAsset document.class.name childPage.link "${childPage.title}" "" childPage.link.asset.mimeType childPage.link.asset.getLength() false false orgPrompt childPage.link.archiveMaterial/>
                        </div>
                    </#if>
                </#if>
            </div>
        </#list>
        </div>
    </div>
    </#if>
    <#-- END optional 'Further information section' -->
</#macro>

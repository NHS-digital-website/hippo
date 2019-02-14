<#ftl output_format="HTML">
<#include "fileMetaAppendix.ftl">
<#include "typeSpan.ftl">
<#include "fileIcon.ftl">
<@hst.setBundle basename="rb.generic.headers"/>
<#-- @ftlvariable name="childPages" type="java.util.List<uk.nhs.digital.common.components.DocumentChildComponent>" -->

<#macro furtherInformationSection childPages>
    <#-- BEGIN optional 'Further information section' -->
    <#if childPages?has_content>
    <div class="article-section article-section--child-pages" id="further-information">
        <h2><@fmt.message key="headers.further-information" /></h2>

        <#-- Original tmpl -->
        <#--<ol class="list list--reset cta-list">-->
            <#--<#list childPages as childPage>-->
                <#--<li>-->
                    <#--<article class="cta cta--hf">-->
                        <#--<#if childPage.linkType??>-->
                            <#--<#assign onClickMethodCall = getOnClickMethodCall(document.class.name, childPage.link) />-->

                            <#--<@typeSpan childPage.linkType />-->

                            <#--<#if childPage.linkType == "external">-->
                                <#--<h2 class="cta__title"><a href="${childPage.link}" onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)">${childPage.title}</a></h2>-->
                            <#--<#elseif childPage.linkType == "asset">-->
                                <#--<h2 class="cta__title">-->
                                    <#--<a href="<@hst.link hippobean=childPage.link />" onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)">${childPage.title}</a><@fileMetaAppendix childPage.link.asset.getLength(), childPage.link.asset.mimeType></@fileMetaAppendix>-->
                                <#--</h2>-->
                            <#--</#if>-->
                        <#--<#elseif hst.isBeanType(childPage, 'org.hippoecm.hst.content.beans.standard.HippoBean')>-->
                            <#--<@typeSpan "internal" />-->

                            <#--<h2 class="cta__title"><a href="<@hst.link var=link hippobean=childPage />">${childPage.title}</a></h2>-->
                        <#--</#if>-->

                        <#--<#if childPage.shortsummary?? && childPage.shortsummary?has_content>-->
                            <#--<p class="cta__text">${childPage.shortsummary}</p>-->
                        <#--</#if>-->
                    <#--</article>-->
                <#--</li>-->
            <#--</#list>-->
        <#--</ol>-->
        <#-- End original list -->

        <ol class="list list--reset cta-list">
            <#list childPages as childPage>
                <li>
                    <#-- If external link -->
                    <#if childPage.linkType??>

                        <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, childPage.link) />
                        <@typeSpan childPage.linkType />

                        <#if childPage.linkType == "external">
                            <article class="cta cta--hf">
                                <h2 class="cta__title"><a href="${childPage.link}" onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)">${childPage.title}</a></h2>
                                <#if childPage.shortsummary?? && childPage.shortsummary?has_content>
                                    <p class="cta__text">${childPage.shortsummary}</p>
                                </#if>
                            </article>
                        </#if>

                    <#-- If internal link -->
                    <#elseif hst.isBeanType(childPage, 'org.hippoecm.hst.content.beans.standard.HippoBean')>
                        <article class="cta cta--hf">
                        <@typeSpan "internal" />
                        <h2 class="cta__title"><a href="<@hst.link var=link hippobean=childPage />">${childPage.title}</a></h2>
                            <#if childPage.shortsummary?? && childPage.shortsummary?has_content>
                                <p class="cta__text">${childPage.shortsummary}</p>
                            </#if>
                        </article>
                    </#if>
                </li>
            </#list>
        </ol>

        <ul class="list list--reset">
        <#list childPages as childPage>
            <li>
            <#if childPage.linkType??>
                <#-- If asset link -->
                <#if childPage.linkType == "asset">
                    <a href="<@hst.link hippobean=childPage.link />" class="block-link" onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)">
                        <div class="block-link__header">
                            <@fileIcon childPage.link.asset.mimeType></@fileIcon>
                        </div>
                        <div class="block-link__body">
                            <span class="block-link__title">${childPage.title}</span>
                            <@fileMetaAppendix childPage.link.asset.getLength(), childPage.link.asset.mimeType></@fileMetaAppendix>
                        </div>
                    </a>
                </#if>
            </#if>
            </li>
        </#list>
        </ul>
    </div>
    </#if>
    <#-- END optional 'Further information section' -->
</#macro>

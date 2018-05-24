<#ftl output_format="HTML">
<#include "fileMetaAppendix.ftl">

<@hst.setBundle basename="site.website.labels"/>
<@fmt.message key="child-pages-section.title" var="childPagesSectionTitle"/>

<#macro furtherInformationSection childPages>
    <#-- BEGIN optional 'Further information section' -->
    <#if childPages?has_content>
    <div class="article-section article-section--child-pages" id="further-information">
        <h2>${childPagesSectionTitle}</h2>
        <ol class="list list--reset cta-list">
            <#list childPages as childPage>
                <li>
                    <article class="cta">
                        <#if childPage.type??>
                            <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, childPage.link) />
                            <#if childPage.type == "external">
                                <#-- Assign the link property of the externallink compound -->
                                <h2 class="cta__title"><a href="${childPage.link}" onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)">${childPage.title}</a></h2>
                                <p class="cta__text">${childPage.shortsummary}</p>
                            <#elseif childPage.type == "asset">
                                <h2 class="cta__title">
                                    <a href="<@hst.link hippobean=childPage.link />" onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)">${childPage.title}</a><@fileMetaAppendix childPage.link.asset.getLength()></@fileMetaAppendix>
                                </h2>
                            </#if>
                        <#elseif hst.isBeanType(childPage, 'org.hippoecm.hst.content.beans.standard.HippoBean')>
                        <#-- In case the childPage is not a compound but still a document in the cms, then create a link to it-->
                        <h2 class="cta__title"><a href="<@hst.link var=link hippobean=childPage />">${childPage.title}</a></h2>
                        <p class="cta__text">${childPage.shortsummary}</p>
                        </#if>
                    </article>
                </li>
            </#list>
        </ol>
    </div>
    </#if>
    <#-- END optional 'Further information section' -->
</#macro>

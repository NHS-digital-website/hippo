<#ftl output_format="HTML">

<#assign hst=JspTaglibs["http://www.hippoecm.org/jsp/hst/core"] >

<#-- @ftlvariable name="menu" type="org.hippoecm.hst.core.sitemenu.HstSiteMenu" -->
<#-- @ftlvariable name="socialMediaLinks" type="java.util.Map<String, SocialMediaLink>" -->

<#assign getRemoteSvg="uk.nhs.digital.freemarker.svg.SvgFromUrl"?new() />

<#include "../include/imports.ftl">
<#include "macro/svgMacro.ftl">

<#if menu??>
    <footer class="nhsd-o-global-footer" id="footer">
        <div class="nhsd-t-grid">
            <div class="nhsd-t-row">
                <div class="nhsd-t-col-12">
                    <div class="nhsd-t-grid nhsd-t-grid--nested">
                        <div class="nhsd-t-row">
                            <#if menu.siteMenuItems??>
                                <#list menu.siteMenuItems as item>
                                    <#if !item.hstLink?? && !item.externalLink??>
                                        <#if !item?is_first>
                                            </ul></nav>
                                        </#if>
                                        <nav class="nhsd-t-col-xs-12 nhsd-t-col-s-6 nhsd-t-col-l-3" aria-labelledby="footer-heading-${item.name?lower_case?replace(" ", "-")}">
                                        <div class="nhsd-t-body-s nhsd-!t-font-weight-bold nhsd-!t-margin-bottom-1" id="footer-heading-${item.name?lower_case?replace(" ", "-")}">${item.name}</div>
                                        <ul class="nhsd-t-list nhsd-t-list--links">
                                    <#elseif item.hstLink?? && socialMediaLinks[item.hstLink.path]??>
                                        <#assign socialMediaLink = socialMediaLinks[item.hstLink.path] />
                                        <li class="nhsd-t-body-s">
                                            <span class="nhsd-a-icon nhsd-a-icon--size-l nhsd-a-icon--col-dark-grey">
                                                <#if socialMediaLink.icon == "other" >
                                                    <@hst.link hippobean=socialMediaLink.linkIcon var="iconLink" />
                                                    <#if iconLink?contains(".svg")>
                                                        <@svgWithoutAltText svgString=socialMediaLink.svgXmlFromRepository/>
                                                    <#else>
                                                        <img src="${iconLink}">
                                                    </#if>
                                                <#else>
                                                    <#assign svg = getRemoteSvg(socialMediaLink.icon) />
                                                    ${svg.data?no_esc}
                                                </#if>
                                            </span>
                                            <a target="_blank" class="nhsd-a-link nhsd-a-link--col-dark-grey" href="${socialMediaLink.nhsDigitalUrl}" rel="external">
                                                ${item.name}
                                                <span class="nhsd-t-sr-only">(external link, opens in a new tab)</span>
                                            </a>
                                        </li>
                                    <#else>
                                        <#if item.hstLink??>
                                            <#assign href><@hst.link link=item.hstLink/></#assign>
                                        <#elseif item.externalLink??>
                                            <#assign href>${item.externalLink?replace("\"", "")}</#assign>
                                        </#if>
                                        <#if  item.selected || item.expanded>
                                            <li class="nhsd-t-body-s active">
                                                <a class="nhsd-a-link nhsd-a-link--col-dark-grey" href="${href}">${item.name}</a>
                                            </li>
                                        <#else>
                                            <li class="nhsd-t-body-s">
                                                <a class="nhsd-a-link nhsd-a-link--col-dark-grey" href="${href}" rel="external">${item.name}</a>
                                            </li>
                                        </#if>
                                    </#if>
                                    <#if item?is_last>
                                        </ul></nav>
                                    </#if>
                                </#list>
                            </#if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <@hst.cmseditmenu menu=menu/>
    </footer>
</#if>

<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#-- @ftlvariable name="menu" type="org.hippoecm.hst.core.sitemenu.HstSiteMenu" -->
<#-- @ftlvariable name="editMode" type="java.lang.Boolean"-->

<#if menu??>
    <nav class="nhsd-u-text-centre">
        <#if menu.siteMenuItems??>
                <#list menu.siteMenuItems as item>
                    <#assign activeClass = (item.selected || item.expanded)?then("active", "") />

                    <div style="display: inline-block; margin: 10px;">
                        <#if !item.hstLink?? && !item.externalLink??>
                            <#if item.selected || item.expanded>
                                <a class="nhsd-a-link" href="#">${item.name}</a>
                            <#else>
                                <a class="nhsd-a-link" href="#">${item.name}</a>
                            </#if>
                        <#else>
                            <#if item.hstLink??>
                                <#assign href><@hst.link link=item.hstLink/></#assign>
                            <#elseif item.externalLink??>
                                <#assign href>${item.externalLink?replace("\"", "")}</#assign>
                            <#else>
                                <#assign href>#</#assign>
                            </#if>

                            <#if item.externalLink??>
                                <a class="nhsd-a-link ${activeClass}" href="${href}" target="_blank" rel="external">${item.name}</a>
                            <#else>
                                <a class="nhsd-a-link ${activeClass}" href="${href}">${item.name}</a>
                            </#if>
                        </#if>
                    </div>
                </#list>

        </#if>
        <@hst.cmseditmenu menu=menu/>
    </nav>
</#if>

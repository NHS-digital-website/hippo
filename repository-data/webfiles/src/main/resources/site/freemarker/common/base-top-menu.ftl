<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#-- @ftlvariable name="menu" type="org.hippoecm.hst.core.sitemenu.HstSiteMenu" -->
<#-- @ftlvariable name="editMode" type="java.lang.Boolean"-->
<#if menu??>
<nav>
    <#if menu.siteMenuItems??>
    <ol class="list list--reset list--inline">
        <#list menu.siteMenuItems as item>
        <#if !item.hstLink?? && !item.externalLink??>
        <#if item.selected || item.expanded>
        <li class="active"><div style="padding: 10px 15px;">${item.name}</div></li>
        <#else>
        <li><div style="padding: 10px 15px;">${item.name}</div></li>
        </#if>
        <#else>
        <#if item.hstLink??>
        <#assign href><@hst.link link=item.hstLink/></#assign>
        <#elseif item.externalLink??>
        <#assign href>${item.externalLink?replace("\"", "")}</#assign>
        </#if>
        <#if  item.selected || item.expanded>
        <li class="active"><a href="${href}">${item.name}</a></li>
        <#else>
        <li><a href="${href}">${item.name}</a></li>
        </#if>
        </#if>
        </#list>
    </ol>
    </#if>
    <@hst.cmseditmenu menu=menu/>
</nav>
</#if>

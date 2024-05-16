<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#-- @ftlvariable name="menu" type="org.hippoecm.hst.core.sitemenu.HstSiteMenu" -->
<#if menu??>
<nav class="menu">
    <ul class="list list--reset">
        <#if hstRequestContext.preview>
          <div style="position:relative">
            <@hst.cmseditmenu menu=menu/>
          </div>
        </#if>
        <#list menu.siteMenuItems as item>
            <#if item.hstLink??>
            <#assign href><@hst.link link=item.hstLink/></#assign>
            <#elseif item.externalLink??>
            <#assign href>${item.externalLink?replace("\"", "")}</#assign>
            <#else>
            <#assign href>#</#assign>
            </#if>
            
            <#if  item.selected || item.expanded>
                <li class="active"><a href="${href}">${item.name}</a></li>
            <#else>
                <li><a href="${href}">${item.name}</a></li>
            </#if>
        </#list>
    </ul>
</nav>
<#-- @ftlvariable id="editMode" type="java.lang.Boolean"-->
<#elseif editMode>
<img src="<@hst.link path="/images/essentials/catalog-component-icons/menu.png" />"> Click to edit Menu
</#if>

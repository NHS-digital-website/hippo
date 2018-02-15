<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#-- @ftlvariable name="menu" type="org.hippoecm.hst.core.sitemenu.HstSiteMenu" -->
<#if menu??>
<ul class="nav nav-pills">
    <#if hstRequestContext.preview>
      <div style="position:relative">
        <@hst.cmseditmenu menu=menu/>
      </div>
    </#if>
    <#list menu.siteMenuItems as item>
        <#if  item.selected || item.expanded>
            <li class="active"><a href="<@hst.link link=item.hstLink/>">${item.name}</a></li>
        <#else>
            <li><a href="<@hst.link link=item.hstLink/>">${item.name}</a></li>
        </#if>
    </#list>
</ul>
<#-- @ftlvariable id="editMode" type="java.lang.Boolean"-->
<#elseif editMode>
<img src="<@hst.link path="/images/essentials/catalog-component-icons/menu.png" />"> Click to edit Menu
</#if>

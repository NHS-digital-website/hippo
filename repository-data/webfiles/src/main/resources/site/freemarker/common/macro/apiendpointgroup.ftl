<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "hubBoxWithTitle.ftl">

<#macro apiendpointgroup endpoint>
<div>
   <h1 id="top" class="local-header__title-left" data-uipath="article.title" itemprop="name">${endpoint.title}</h1>

   <#if endpoint.apirequest?has_content>
       <p class="article-header__subtitle">
       <#list endpoint.apirequest as request>
         <@hst.link hippobean=request var="apirequest"/>
         <a temprop="url" href="${apirequest}" onClick="logGoogleAnalyticsEvent('document click','Event','${apirequest}');" onKeyUp="return vjsu.onKeyUp(event)" title="${request.requestname}"><span itemprop="name">${request.requestname}</span></a>
       </#list>
       </p>
   </#if>
</div>
</#macro>

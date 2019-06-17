<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "hubBoxWithTitle.ftl">

<#macro apiendpointgroup endpoint>
  <div id="apiendpoint-${slugify(endpoint.title)}" class="article-section-separator apiendpointgroup--div">
      <@hubBoxAndTitle doc=endpoint linksfield=endpoint.apirequest/>
  </div>
</#macro>

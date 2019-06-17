<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "hubBoxWithTitle.ftl">

<#macro apiinfobuilder builder>
  <div id="apiinfobuilder-${slugify(builder.title)}" class="article-section-separator apiinfobuilder--div">
      <@hubBoxAndTitle doc=builder linksfield=builder.content/>
  </div>
</#macro>

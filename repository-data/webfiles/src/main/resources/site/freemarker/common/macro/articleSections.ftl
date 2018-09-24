<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro articleSections sections>
<#if sections?has_content>
<#assign numberedListCount=0 />
<#list sections as section>
    <#if section.title?has_content>
        <div id="${slugify(section.title)}" class="article-section">
            <h2><#if section.isNumberedList><#assign numberedListCount++ />${numberedListCount}. </#if>${section.title}</h2>
            <div class="rich-text-content">
                <@hst.html hippohtml=section.html contentRewriter=gaContentRewriter/>
            </div>
        </div>
    <#else>
        <div class="article-section">
            <div class="rich-text-content">
                <@hst.html hippohtml=section.html contentRewriter=gaContentRewriter/>
            </div>
        </div>
    </#if>
</#list>
</#if>
</#macro>

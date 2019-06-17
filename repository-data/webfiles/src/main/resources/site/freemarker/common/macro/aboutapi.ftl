<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "sections/sections.ftl">


<#macro aboutapimacro aboutapi>
  <div id="aboutapi-${slugify(aboutapi.title)}" class="article-section aboutapi--div">

              <div class="article-section article-section--summary">
                <#if aboutapi.title?has_content>  
                  <h2>${aboutapi.title}</h2>
                </#if>
                <div itemprop="introduction">
                  <p data-uipath="article.aboutapi">
                    <@hst.html hippohtml=aboutapi.content contentRewriter=gaContentRewriter/>
                  </p>

                  <#if aboutapi.sections?has_content>
                    <@sections aboutapi.sections></@sections>
                  </#if>

                </div>
              </div>
  </div>
</#macro>

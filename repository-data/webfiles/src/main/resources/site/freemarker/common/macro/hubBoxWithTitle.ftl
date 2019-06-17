<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "hubBox.ftl">

<#macro hubBoxAndTitle doc linksfield>
            <div class="column">
              <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <h2>${doc.title}</h2>
              </div>
              <div class="page-block--main-alignment column column--two-thirds page-block page-block--main">
                <div class="column column--reset">
                  <div class="hub-box-list">
                    <#list linksfield as link>
                      <#if link.linkType == "internal">
                        <#assign boxdata = { "title": link.link.title, "text": link.link.shortsummary, "light": true } />
                        <@hst.link hippobean=link.link var="boxlink" />
                        <#assign boxdata += { "link": boxlink } />
                      <#elseif link.linkType == "external">
                        <#assign boxdata = { "title": link.title, "text": link.shortsummary, "light": true } />
                        <#assign boxdata += { "link": link.link } />
                      </#if>
                      <@hubBox boxdata></@hubBox>
                    </#list>
                  </div>
                </div>
              </div>
          </div>
</#macro>


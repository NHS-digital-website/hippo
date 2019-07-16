<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.ApiEndpoint" -->

<#include "../include/imports.ftl">

<#include "macro/sections/codeSection.ftl">
<#include "macro/furtherInformationSection.ftl">
<#include "../publicationsystem/macro/structured-text.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/sectionNav.ftl">
<#include "macro/fileMetaAppendix.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/fileIconByMimeType.ftl">


<#assign hasApiEndpointContent = document.requestname?? && document.uriaddress?? && document.summary?? />
<#assign hasAuthnAuthsContent = document.authnauths?? && document.authnauths.content?has_content />
<#assign hasParameters = document.apiendpointparams?? && document.apiendpointparams?has_content />
<#assign hasSampleRequest = document.samplerequest?? && document.samplerequest?has_content />
<#assign hasSampleResponse = document.sampleresponse?? && document.sampleresponse?has_content />
<#assign hasResponseDefinition = document.responsedefinitions?? && document.responsedefinitions?has_content />
<#assign hasStatusErrorCode = document.statuserrorcodes?? && document.statuserrorcodes?has_content />
<#assign sectionTitlesFound = countSectionTitles(document.sections) />
<#assign renderNav = sectionTitlesFound gte 1 || hasAuthnAuthsContent || hasApiEndpointContent />
<#assign hasSectionContent = document.sections?has_content />
<#assign hasShortsummary = document.shortsummary?? />

<#assign apimethodValue = document.apimethod?upper_case />
<#if apimethodValue == "HEAD">
  <#assign colourClass = "colour-class-head" />
<#elseif apimethodValue == "GET">
  <#assign colourClass = "colour-class-get" />
<#elseif apimethodValue == "POST">
  <#assign colourClass = "colour-class-post" />
<#elseif apimethodValue == "PUT">
  <#assign colourClass = "colour-class-put" />
<#elseif apimethodValue == "PATCH">
  <#assign colourClass = "colour-class-patch" />
<#elseif apimethodValue == "DELETE">
  <#assign colourClass = "colour-class-delete" />
<#elseif apimethodValue == "TRACE">
  <#assign colourClass = "colour-class-trace" />
<#elseif apimethodValue == "OPTIONS">
  <#assign colourClass = "colour-class-options" />
<#elseif apimethodValue == "CONNECT">
  <#assign colourClass = "colour-class-connect" />
</#if>

<article class="article article--service">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
                <div class="local-header article-header article-header--detailed">
                    <div class="grid-wrapper">
                        <div class="article-header__inner">
                          <div class="column column--two-thirds page-block page-block--main">
                              <div>
                                  <h1 class="local-header__title-left" data-uipath="document.title" itemprop="name" itemscope itemtype="https://schema.org/webapi">${document.requestname}</h1>
                                  <div class="local-header__tag">${releasestatuses[document.releasestatus]?upper_case}</div>
                                  <#if document.apiMasterParent?has_content && document.apiMasterParent.apiname??>
                                    <p class="article-header__subtitle">
                                    This is part of
                                    <a href="<@hst.link hippobean=document.apiMasterParent/>"
                                        title="${document.apiMasterParent.apiname}">
                                        <span itemprop="subjectof" itemscope itemtype="https://schema.org/webapi">${document.apiMasterParent.apiname}</span>
                                    </a>
                                    </p>
                                </#if>
                              </div>
                            <#if hasShortsummary>
                                <div class="article-header__subtitle" data-uipath="document.shortsummary">${document.shortsummary}</div>
                            </#if>
                          </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="grid-row">
            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <div id="sticky-nav">
                    <@sectionNav getSectionNavforApiEndpoint(document)></@sectionNav>
                </div>
            </div>

            <div class="column column--two-thirds page-block page-block--main">

              <#if hasApiEndpointContent>
                <div class="article-section">
                    <#if document.summary?has_content>
                      <div id="Endpoint" class="article-section article-section--highlighted">
                        <h2 data-uipath="website.contentblock.summary.title">Endpoint</h2>
                        <div data-uipath="website.contentblock.summary.content" class="rich-text-content">
                          <@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/>
                        </div>
                        <div>
                        <table class="api-endpoint-table">
                          <tbody>
                            <tr>
                              <td class="${colourClass}" >${document.apimethod?upper_case}</td>
                              <td itemprop="url" itemscope itemtype="https://schema.org/webapi">${document.uriaddress}</td>
                            </tr>
                          </tbody>
                        </table>
                        </div>
                      </div>
                    </#if>
                </div>
              </#if>

              <#if hasAuthnAuthsContent>
                <div class="article-section">
                    <div id="authns" class="article-section article-section--highlighted">
                         <h2 data-uipath="website.contentblock.auth.title">Authorisation and Authentication</h2>
                          <#if document.authnauths.title?has_content>
                            <h3 data-uipath="website.contentblock.auth.subtitle">${document.authnauths.title}</h3>
                          </#if>
                          <#if document.authnauths.content?has_content>
                            <div data-uipath="website.contentblock.auth.content" class="rich-text-content">
                                <@hst.html hippohtml=document.authnauths.content contentRewriter=gaContentRewriter/>
                            </div>
                          </#if>
                    </div>
                </div>
              </#if>

              <#if hasParameters>
                <div class="article-section">
                    <div id="Parameter" class="article-section article-section--highlighted">
                        <h2 data-uipath="website.contentblock.parameter.title">Parameters</h2>
                        <div>
                            <table data-disablesort="true">
                                <thead>
                                    <tr>
                                        <th scope="col">Name</th>
                                        <th scope="col">Parameter Type</th>
                                        <th scope="col">Mandatory/ Optional</th>
                                        <th scope="col">Path</th>
                                        <th scope="col">Description</th>
                                    </tr>
                                </thead>
                                <#list document.apiendpointparams as apiParam >
                                <tbody>
                                    <tr>
                                        <td itemprop="name" itemscope itemtype="https://schema.org/StructuredValue"><code class="parameter-codeinline">${apiParam.name}</code></td>
                                        <td itemprop="value" itemscope itemtype="https://schema.org/StructuredValue">${parametertypeList[apiParam.parametertype]}</td>
                                        <td>${apiParam.ismandatory?then('Mandatory','Optional')}</td>
                                        <td><code>${apiParam.path}</code></td>
                                        <td itemprop="description" itemscope itemtype="https://schema.org/StructuredValue"><@hst.html hippohtml=apiParam.description contentRewriter=gaContentRewriter/></td>
                                    </tr>
                                </tbody>
                                </#list>
                            </table>
                        </div>
                    </div>
                </div>
              </#if>

              <#if hasSampleRequest>
                <div class="article-section">
                    <div id="SampleRequest" class="article-section article-section--highlighted">
                        <h2 data-uipath="website.contentblock.samplerequest.title">Sample Request</h2>
                        <#list document.samplerequest as samReq >
                        <#if samReq.content?has_content>
                        <div data-uipath="website.contentblock.samplerequest.content" class="rich-text-content">
                            <@hst.html hippohtml=samReq.content contentRewriter=gaContentRewriter/>
                        </div>
                        </#if>
                        <#if samReq.webcode.codetext?has_content>
                        <@codeSection section=samReq.webcode/>
                        </#if>
                        </#list>
                    </div>
                </div>
              </#if>

              <#if hasSampleResponse>
                <div class="article-section">
                    <div id="SampleResponse" class="article-section article-section--highlighted">
                    <h2 data-uipath="website.contentblock.sampleresponse.title">Sample Response</h2>
                    <#list document.sampleresponse as samRes >
                    <#if samRes.content?has_content>
                    <div data-uipath="website.contentblock.sampleresponse.content" class="rich-text-content">
                        <@hst.html hippohtml=samRes.content contentRewriter=gaContentRewriter/>
                    </div>
                    </#if>
                    <#if samRes.webcode.codetext?has_content>
                    <@codeSection section=samRes.webcode/>
                    </#if>
                    </#list>
                    </div>
                </div>
              </#if>

              <#if hasResponseDefinition>
                <div class="article-section">
                    <div id="responseDefination" class="article-section article-section--highlighted">
                      <h2 data-uipath="website.contentblock.responsedefinition.responseitem">Response Definitions</h2>
                      <#if document.resdefcontent?has_content>
                      <p><@hst.html hippohtml=document.resdefcontent contentRewriter=gaContentRewriter/></p>
                      </#if>
                      <div>
                      <table data-disablesort="true">
                        <thead>
                          <tr>
                            <th>Name</th>
                            <th>Description</th>
                            <th>Data type</th>
                          </tr>
                        </thead>
                        <#list document.responsedefinitions as resDef >
                        <tbody>
                          <tr>
                            <td><code class="parameter-codeinline">${resDef.responseitem}</code></td>
                            <td><@hst.html hippohtml=resDef.description contentRewriter=gaContentRewriter/></td>
                            <td class="noword-wrap">${resDef.datatype}</td>
                          </tr>
                        </tbody>
                        </#list>
                      </table>
                      </div>
                    </div>
                </div>
              </#if>

              <#if hasStatusErrorCode>
                <div class="article-section">
                    <div id="statusErrorCode" class="article-section article-section--highlighted">
                      <h2 data-uipath="website.contentblock.statuserrorcode.title">Status and error codes</h2>
                      <div>
                      <table data-disablesort="true">
                        <thead>
                          <tr>
                            <th>HTTP Code</th>
                            <th>Meaning</th>
                            <th>Description</th>
                            <th>Diagnostics</th>
                          </tr>
                        </thead>
                        <#list document.statuserrorcodes as errorCode >
                        <tbody>
                          <tr>
                            <#if errorCode.httpcode?has_content>
                            <td><code>${errorCode.httpcode}</code></td>
                            <#else>
                            <td><code>N/A</code></td>
                            </#if>
                            <td><code>${errorCode.meaning}</code></td>
                            <td><@hst.html hippohtml=errorCode.description contentRewriter=gaContentRewriter/></td>
                            <td><@hst.html hippohtml=errorCode.howtofixthis contentRewriter=gaContentRewriter/></td>
                          </tr>
                        </tbody>
                        </#list>
                      </table>
                      </div>
                    </div>
                </div>
              </#if>

              <#if hasSectionContent>
                <div class="article-section">
                  <@sections document.sections></@sections>
                </div>
              </#if>

              <@lastModified document.lastModified></@lastModified>
            </div>
        </div>
    </div>
</article>

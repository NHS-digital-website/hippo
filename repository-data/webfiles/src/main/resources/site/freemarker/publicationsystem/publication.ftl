<#include "../include/imports.ftl">
<#assign dateFormat="dd/MM/yyyy"/>
<#assign formatFileSize="uk.nhs.digital.ps.directives.FileSizeFormatterDirective"?new() />
<#assign formatRestrictableDate="uk.nhs.digital.ps.directives.RestrictableDateFormatterDirective"?new() />

<@hst.setBundle basename="publicationsystem.headers"/>

<#-- @ftlvariable name="publication" type="uk.nhs.digital.ps.beans.Publication" -->

<#macro nominalPublicationDate>
  <dl class="media__body">
    <dt>Nominal Publication Date:</dt>
    <dd class="zeta" data-uipath="ps.publication.nominal-publication-date">
        <#if publication.nominalPublicationDate??>
            <@formatRestrictableDate value=publication.nominalPublicationDate/>
        <#else>
            (Not specified)
        </#if>
    </dd>
  </dl>
</#macro>

<#macro restrictedContentOfUpcomingPublication>
<section class="document-header">
  <h2 class="doc-title" data-uipath="ps.document.title">${publication.title?html}</h2>
  <dl class="doc-metadata">
    <p>
      <@nominalPublicationDate/>
    </p>
    <p data-uipath="ps.publication.upcoming-disclaimer">(Upcoming, not yet published)</p>
  </dl>
</section>
</#macro>

<#macro fullContentOfPubliclyAvailablePublication>
<section class="document-header">
  <div class="document-header__inner">

    <h3 data-uipath="ps.publication.information-types" class="flush--bottom push-half--top">
      <#list publication.informationType as type><#if type?index != 0>, </#if>${type?html}</#list>
    </h3>

    <h1 class="layout-5-6" data-uipath="ps.document.title">${publication.title?html}</h1>

    <#if publication.parentSeries??>
      <p class="flush--top">
        This is part of
        <a class="label label--parent-document" href="<@hst.link hippobean=publication.parentSeries.selfLinkBean/>"
            title="${publication.parentSeries.title}">
            ${publication.parentSeries.title}
        </a>
      </p>
    </#if>

    <div class="flex push--top push--bottom">
      <div class="flex__item">
        <div class="media">
          <img class="media__img" src="http://via.placeholder.com/52x52" alt="">
          <@nominalPublicationDate/>
        </div>
      </div><!--

      --><#if publication.geographicCoverage?has_content><div class="layout__item layout-1-4">
        <div class="media">
          <img class="media__img" src="http://via.placeholder.com/52x52" alt="">
          <dl class="media__body">
            <dt id="geographic-coverage">Geographic coverage</dt>
            <dd data-uipath="ps.publication.geographic-coverage">
            ${publication.geographicCoverage?html}
            </dd>
          </dl>
        </div>
      </div></#if><!--

      --><#if publication.granularity?has_content ><div class="flex__item">
        <div class="media">
          <img class="media__img" src="http://via.placeholder.com/52x52" alt="">
          <dl class="media__body">
            <dt>Geographical granularity</dt>
            <dd data-uipath="ps.publication.granularity">
                <#list publication.granularity as granularityItem><#if granularityItem?index != 0>, </#if>${granularityItem?html}</#list>
            </dd>
          </dl>
        </div>
      </div></#if><!--

      --><#if publication.coverageStart?? ><div class="flex__item">
          <div class="media">
            <img class="media__img" src="http://via.placeholder.com/52x52" alt="">
            <dl class="media__body">
              <dt>Date Range</dt>
              <dd data-uipath="ps.publication.date-range">
                  <#if publication.coverageStart?? >
                      <@fmt.formatDate value=publication.coverageStart.time type="Date" pattern=dateFormat />
                  <#else>
                      (Not specified)
                  </#if>
                  to
                  <#if publication.coverageEnd?? >
                      <@fmt.formatDate value=publication.coverageEnd.time type="Date" pattern=dateFormat />
                  <#else>
                      (Not specified)
                  </#if>
              </dd>
            </dl>
          </div>

      </div></#if><!--

      --><#if publication.taxonomyList??><div class="flex__item">
          <div class="media">
            <img class="media__img" src="http://via.placeholder.com/52x52" alt="">
            <dl class="media__body">
              <dt>Taxonomy</dt>
              <dd data-uipath="ps.publication.taxonomy">
                <#list publication.taxonomyList as taxonomyChain>
                    <div>${taxonomyChain?join(" => ")}</div>
                </#list>
              </dd>
            </dl>
          </div>
      </div></#if>

    </div>
  </div>
</section>


<section class="document-content">
  <div class="layout layout--large">
    <div class="layout__item layout-2-3">

      <h2><@fmt.message key="headers.summary"/></h2>
      <#list publication.summary.elements as element>
          <#if element.type == "Paragraph">
          <p data-uipath="ps.publication.summary">${element?html}</p>
          </#if>
      </#list>

      <#if publication.keyFacts??>
        <h2><@fmt.message key="headers.key-facts"/></h2>
        <p data-uipath="ps.publication.key-facts">${publication.keyFacts?html}</p>
      </#if>

    </div><!--

    --><div class="layout__item layout-1-3">

        <div class="panel panel--grey">
            <h3><@fmt.message key="headers.resources"/></h3>
            <ul data-uipath="ps.publication.resources">
                <#list publication.attachments as attachment>
                    <li class="attachment">
                        <a class="attachment-hyperlink" href="<@hst.link hippobean=attachment/>">${attachment.filename}</a>;
                        <span class="fileSize">size: <@formatFileSize bytesCount=attachment.length/></span>
                    </li>
                </#list>
                <#list publication.resourceLinks as link>
                    <li>
                        <a href="${link.linkUrl}">${link.linkText}</a>
                    </li>
                </#list>
                <#list publication.datasets as dataset>
                    <li class="dataset">
                        <a href="<@hst.link hippobean=dataset.selfLinkBean/>" title="${dataset.title}">${dataset.title}</a>
                    </li>
                </#list>
            </ul>
            <h3><@fmt.message key="headers.related-links"/></h3>
            <ul data-uipath="ps.publication.related-links">
              <#list publication.relatedLinks as link>
                <li>
                    <a href="${link.linkUrl}">${link.linkText}</a>
                </li>
              </#list>
            </ul>
            <#if publication.administrativeSources?has_content>
            <div class="content-section">
                <h4>Administrative Sources</h4>
                <p data-uipath="ps.publication.administrative-sources">
                ${publication.administrativeSources?html}
                </p>
            </div>
            </#if>

          <!--
          <h3>Example markup</h3>
          <div class="media">
            <img class="media__img" src="http://via.placeholder.com/28x28" alt="">
            <p class="media__body zeta">
              <a href="#">Sample template - Not linked</a>
              <br>Size: 211.66KB
            </p>
          </div>
          -->

      </div>
    </div>
  </div>
</section>


</#macro>


<#-- ACTUAL TEMPLATE -->
<#if publication?? >
    <#if publication.publiclyAccessible>
        <@fullContentOfPubliclyAvailablePublication/>
    <#else>
        <@restrictedContentOfUpcomingPublication/>
    </#if>
</#if>

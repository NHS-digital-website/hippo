<#ftl output_format="HTML">

<#-- @ftlvariable name="bloghub" type="uk.nhs.digital.website.beans.BlogHub" -->

<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/documentHeader.ftl">
<#include "macro/component/showAll.ftl">
<#include "macro/contentPixel.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<#assign itemsMaxCount=10/>

<#assign hasLatestBlogs = document.latestBlogs?? && document.latestBlogs?has_content />
<#assign hasSummaryContent = document.summary?? && document.summary?has_content />

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<article itemscope itemtype="http://schema.org/Blog">

    <!-- metadata schema.org data - START -->
    <#if document.publisher?has_content>
      <div class="is-hidden" itemprop="publisher" itemscope itemtype="http://schema.org/Organization"><span itemprop="name">${document.publisher.title}</span></div>
    </#if>
    <#if document.seosummary?has_content><div class="is-hidden" itemprop="description">${document.seosummary.content?replace('<[^>]+>','','r')}</div></#if>
    <#if document.taxonomyTags?has_content><span class="is-hidden" itemprop="keywords" ><#list document.taxonomyTags as tag>${tag} <#sep>, </#list></span></#if>

    <#if document.leadImage?has_content>
      <@hst.link hippobean=document.leadImage.original fullyQualified=true var="blogImage" />
      <div class="is-hidden" itemprop="image" itemscope itemtype="http://schema.org/ImageObject">
        <meta itemprop="url" content="${blogImage}">
        <span itemprop="contentUrl" >${blogImage}</span>
      </div>
    </#if>

    <#if document.subject?has_content><span class="is-hidden" itemprop="about" >${document.subject.title}</span></#if>
    <!-- metadata schema.org data - END   -->

    <@documentHeader document 'bloghub'></@documentHeader>

    <div class="grid-wrapper grid-wrapper--article" aria-label="document-content">
        <div class="grid-row">

                <#if hasLatestBlogs>
                        <div class="filter-parent" data-max-count="${itemsMaxCount}" data-state="short">
                        <#list document.latestBlogs as latest>

                            <#assign bloghubItemClass = "bloghub__item filter-list__item" />
                            <#assign bloghubItemIconClass = "bloghub__item__icon" />
                            <#assign bloghubItemContentClass = "bloghub__item__content" />
                            <#assign bloghubItemContentAuthorClass = "bloghub__item__content__author" />

                            <#if latest?is_first>
                              <#assign bloghubItemClass += " bloghub__item__first" />
                              <#assign bloghubItemIconClass += " bloghub__item__icon__first" />
                              <#assign bloghubItemContentClass += " bloghub__item__content__first" />
                              <#assign bloghubItemContentAuthorClass += " bloghub__item__content__author__first" />
                            </#if>

                            <div class="${bloghubItemClass}" itemprop="blogPost" itemscope itemtype="http://schema.org/BlogPosting">
                                <div class="${bloghubItemIconClass}"
                                     itemprop="image" itemscope
                                     itemtype="http://schema.org/ImageObject">
                                    <#if latest.leadImage??>
                                        <@hst.link hippobean=latest.leadImage.original fullyQualified=true var="leadImage" />

                                        <#if latest?is_first>
                                            <@hst.link hippobean=latest.leadImage.newsPostImage fullyQualified=true var="leadImage" />
                                            <@hst.link hippobean=latest.leadImage.newsPostImage2x fullyQualified=true var="leadImage2x" />
                                            <@hst.link hippobean=latest.leadImage.postImageSmall fullyQualified=true var="leadImageSmall" />
                                            <@hst.link hippobean=latest.leadImage.postImageSmall2x fullyQualified=true var="leadImageSmall2x" />
                                            <img itemprop="contentUrl"
                                                 class="bloghub__icon__img" srcset="
                                                ${leadImageSmall} 343w,
                                                ${leadImageSmall2x} 686w,
                                                ${leadImage} 640w,
                                                ${leadImage2x} 1280w"
                                                 sizes="(min-width: 990px) 640px, (min-width: 642px) 67vw, calc(100vw - 34px)"
                                                 src="${leadImage}"
                                                 alt="${latest.title}"/>
                                        <#else>
                                            <@hst.link hippobean=latest.leadImage.blogIndexThumb fullyQualified=true var="leadImage" />
                                            <@hst.link hippobean=latest.leadImage.blogIndexThumb2x fullyQualified=true var="leadImage2x" />
                                            <@hst.link hippobean=latest.leadImage.postImageSmall fullyQualified=true var="leadImageSmall" />
                                            <@hst.link hippobean=latest.leadImage.postImageSmall2x fullyQualified=true var="leadImageSmall2x" />
                                            <img itemprop="contentUrl"
                                                 class="bloghub__icon__img" srcset="
                                                ${leadImageSmall} 343w,
                                                ${leadImageSmall2x} 686w,
                                                ${leadImage} 468w,
                                                ${leadImage2x} 936w"
                                                 sizes="(min-width: 1000px) 468px, (min-width: 642px) 46vw, calc(100vw - 34px)"
                                                 src="${leadImage}"
                                                 alt="${latest.title}"/>
                                        </#if>
                                        <meta itemprop="url" content="${leadImage}">
                                    <#else>
                                        <meta itemprop="url"
                                              content="<@hst.webfile path="/images/fibre_57101102_med.jpg"/>"/>
                                        <img itemprop="contentUrl"
                                             src="<@hst.webfile path="/images/fibre_57101102_med.jpg"/>"
                                             alt="Default Blog Hub Image">
                                    </#if>
                                </div>

                              <div class="${bloghubItemContentClass}">
                                  <div itemprop="headline" class="bloghub__item__content__title"><a class="cta__title cta__button" href="<@hst.link hippobean=latest/>">${latest.title}</a></div>

                                  <div class="bloghub__item__content__date" itemprop="datePublished">
                                      <@fmt.formatDate value=latest.dateOfPublication.time type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" />
                                  </div>

                                  <div class="bloghub__item__content__body" data-uipath="website.blog.summary" itemprop="articleBody"><@hst.html hippohtml=latest.summary contentRewriter=gaContentRewriter/></div>

                                  <#if latest.taxonomyTags?has_content>
                                    <div class="bloghub__item__content__topics">
                                      <span class="bloghub__item__content__topics__title">Topics: </span><span itemtype="keywords" ><#list latest.taxonomyTags as tag>${tag}<#sep>, </#list></span>
                                    </div>
                                  </#if>

                                  <div itemprop="author" class="${bloghubItemContentAuthorClass}" itemscope itemtype="http://schema.org/Person">
                                      <#if latest.authors?has_content>
                                          <div class="bloghub__item__content__author__imgs">
                                            <#list latest.authors as author>
                                              <#if author.personimages.picture?has_content>
                                                <@hst.link hippobean=author.personimages.picture.authorPhoto fullyQualified=true var="authorImage" />
                                                <@hst.link hippobean=author.personimages.picture.authorPhoto2x fullyQualified=true var="authorImage2x" />
                                                  <img itemprop="image"
                                                       class="bloghub__item__content__author__img"
                                                       srcset="${authorImage} 1x, ${authorImage2x} 2x"
                                                       src="${authorImage}"
                                                       alt="${author.title}"/>
                                              </#if>
                                            </#list>
                                          </div>
                                          <div>
                                            <#list latest.authors as author>
                                              <div class = "bloghub__item__content__author__name">
                                                <a itemprop="name" class="cta__title cta__button" href="<@hst.link hippobean=author/>">${author.title}</a>
                                              </div>
                                              <#if latest.authors?size == 1 && author.roles?has_content>
                                              <div class="bloghub__item__content__author__name__role" >
                                                <#if author.roles.primaryroles?has_content><span itemprop="jobTitle">${author.roles.firstprimaryrole}</span ></#if><#if author.roles.primaryroleorg?has_content>, <span itemprop="worksFor">${author.roles.primaryroleorg}</span></#if>
                                              </div>
                                              </#if>
                                            </#list>
                                          </div>
                                      <#elseif latest.authorName?has_content>
                                            <div class = "bloghub__item__content__author__name">
                                              <span itemprop="name">${latest.authorName}</span>
                                            </div>
                                            <#if latest.authorRole?has_content>
                                              <div class="bloghub__item__content__author__name__role" >
                                                <span class="bloghub__item__content__author__name__org" itemprop="jobTitle">${latest.authorRole}</span><#if latest.authorOrganisation?has_content>, <span class="bloghub__item__content__author__name__org" itemprop="worksFor">${latest.authorOrganisation}</span></#if>
                                              </div>
                                            </#if>
                                      </#if>
                                  </div>
                              </div>
                            </div>
                        </#list>

                        <#assign noissnClass = "" />
                        <#if ! document.issn?has_content>
                          <#assign noissnClass = "bloghub__showall-no-issn" />
                        </#if>
                        <@showAll document.latestBlogs?has_content?then(document.latestBlogs?size, 0) itemsMaxCount "bloghub__showall ${noissnClass}"/>
                        </div>
                </#if>
        </div>
        <#if document.issn?has_content>
          <div class="bloghub__issn">
            ISSN number: <span itemprop="issn">${document.issn}</span>
          </div>
        </#if>
    </div>
</article>

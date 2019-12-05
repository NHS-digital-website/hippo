<#ftl output_format="HTML">
<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.News" -->
<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/relatedarticles.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/editorsnotes.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/component/downloadBlock.ftl">
<#include "macro/contactdetail.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="rb.doctype.news"/>

<#assign hasSectionContent = document.sections?has_content />
<#assign hasEditorsNotes = document.notesforeditors?has_content && document.notesforeditors.htmlnotes?has_content/>
<#assign hasLeadImage = document.leadimagesection?has_content && document.leadimagesection.leadImage?has_content/>
<#assign hasLeadImageAltText = hasLeadImage && document.leadimagesection.alttext?has_content/>
<#assign hasLeadImageCaption = hasLeadImage && document.leadimagesection.imagecaption.content?has_content />
<#assign hasBackstory = document.backstory?? && document.backstory.content?has_content />
<#assign hasPeople = document.peoplementioned?? && document.peoplementioned?has_content />
<#assign hasContactDetails = document.mediacontact?? && document.mediacontact?has_content />
<#assign hasLatestNews = document.latestNews?? && document.latestNews?has_content />

<#assign hasTopics = document.topics?? && document.topics?has_content />
<#assign hasNewsType = document.type?? && document.type?has_content />
<#assign hasTwitterHashtag = document.twitterHashtag?? && document.twitterHashtag?has_content />
<#assign idsuffix = slugify(document.title) />

<article class="article article--news" itemscope itemtype="http://schema.org/NewsArticle">

    <div class="grid-wrapper grid-wrapper--article grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
        <div class="local-header article-header--detailed">
            <div class="grid-wrapper">
                <div class="article-header__inner">

                     <div class="grid-row">
                        <div class="column--two-thirds column--reset">

                            <span class="article-header__label">News</span>

                            <h1 class="local-header__title" itemprop="headline" data-uipath="document.title">${document.title}</h1>
                            <meta itemprop="mainEntityOfPage" content="${document.title}">
                            <meta itemprop="author" content="NHS Digital">
                            <meta itemprop="copyrightHolder" content="NHS Digital">
                            <meta itemprop="license" content="https://digital.nhs.uk/about-nhs-digital/terms-and-conditions">

                            <div class="article-header__subtitle" data-uipath="website.news.summary" itemprop="description">${document.shortsummary}</div>
                        </div>

                        <div class="column--one-third column--reset local-header__icon">
                          <img src="<@hst.webfile path="images/penpaper.svg" fullyQualified=true/>" alt="news icon">
                        </div>

                     </div>

                     <div class="grid-row-no-margin">
                        <div class="column column--reset">
                            <div class="detail-list-grid">

                                <div class="grid-row">
                                    <div class="column column--reset">
                                        <dl class="detail-list">
                                            <dt class="detail-list__key">Date:</dt>
                                            <dd class="detail-list__value" itemprop="datePublished" data-uipath="website.news.dateofpublication">
                                                <@fmt.formatDate value=document.publisheddatetime.time type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" />
                                            </dd>
                                        </dl>
                                        <div class="is-hidden" itemprop="publisher" itemscope itemtype="https://schema.org/Organization"> 
                                            <meta itemprop="name" content="NHS Digital">
                                            <span itemprop="logo" itemscope itemtype="https://schema.org/ImageObject">
                                                <meta itemprop="url" content="<@hst.webfile path='/images/nhs-digital-logo.svg' />" />
                                            </span>
                                        </div>
                                    </div>
                                </div>

                                <#if hasTopics>
                                    <div class="grid-row">
                                        <div class="column column--reset">
                                            <dl class="detail-list">
                                                <dt class="detail-list__key">Topic:</dt>
                                                <dd class="detail-list__value" itemprop="keywords" data-uipath="website.news.topics">
                                                    <#list document.topics as tag>${tag}<#sep>, </#list>
                                                </dd>
                                            </dl>
                                        </div>
                                    </div>
                                 </#if>

                                <#if hasNewsType>
                                    <div class="grid-row">
                                        <div class="column column--reset">
                                            <dl class="detail-list">
                                                <dt class="detail-list__key">News type:</dt>
                                                <dd class="detail-list__value" data-uipath="website.news.type">
                                                    <span>${newstypes[document.type]}</span>
                                                </dd>
                                            </dl>
                                        </div>
                                    </div>
                                 </#if>

                                <#if hasTwitterHashtag>
                                    <div class="grid-row">
                                         <div class="column column--reset">
                                             <dl class="detail-list">
                                                 <dt class="detail-list__key">Twitter:</dt>
                                                 <dd class="detail-list__value" data-uipath="website.news.twitter">
                                                   <#list document.twitterHashtag as oldtag>
                                                     <#if ! oldtag?starts_with("#")>
                                                         <#assign tag = "#" + oldtag>
                                                     <#else>
                                                         <#assign tag = oldtag>
                                                     </#if>
                                                     <a href="https://twitter.com/search?q=${tag?replace('#','%23')}" target="blank_">${tag}</a><#sep>, 
                                                   </#list>
                                                 </dd>
                                             </dl>
                                         </div>
                                    </div>
                                </#if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--two-thirds page-block page-block--main">

                <#if document.creditBanner?has_content>
                <div class="article__warning" role="alert">
                    <div class="callout callout--warning callout--muted">
                        <div class="grid-row">
                            <div class="column column--reset">
                                ${creditbanner[document.creditBanner]}
                            </div>
                        </div>
                    </div>
                </div>
                </#if>

                <#if hasLeadImage>
                    <div class="lead-image-container" >
                        <div class="lead-image" itemprop="image" itemscope itemtype="http://schema.org/ImageObject">
                            <@hst.link hippobean=document.leadimagesection.leadImage.original fullyQualified=true var="leadImage" />
                            <meta itemprop="url" content="${leadImage}" />
                            <img itemprop="contentUrl" src="${leadImage}" alt="<#if hasLeadImageAltText>${document.leadimagesection.alttext}</#if>" />
                        </div>
                        <#if hasLeadImageCaption>
                          <div class="lead-image-caption" data-uipath="website.blog.leadimagecaption">
                              <@hst.html hippohtml=document.leadimagesection.imagecaption contentRewriter=gaContentRewriter/>
                          </div>
                        </#if>
                    </div>
                </#if>

                <#if hasSectionContent>
                  <div itemprop="articleBody">
                      <@sections document.sections></@sections>
                  </div>
                </#if>

                <#list document.relateddocuments as child>
                  <div class="article-header">
                    <@downloadBlock child />
                  </div>
                </#list>

                <#if hasEditorsNotes>
                      <div class="article-section-with-no-heading expander expander-some expander-notes-for-editors">
                        <details>
                          <summary><span>Notes for editors</span></summary>
                          <div class="details-body">
                            <ol>
                            <#list document.notesforeditors.htmlnotes as note>
                              <#if note.content?has_content>
                                <li class="notes-for-editors"><@hst.html hippohtml=note contentRewriter=gaContentRewriter/></li>
                              </#if>
                            </#list>
                            </ol>
                          </div>
                        </details>
                      </div>
                </#if>

                <#if hasBackstory>
                    <div class="article-header blog-backstory news-backstory" itemprop="backstory">
                        <h2>Back story</h2>
                        <div data-uipath="website.news.backstory">
                            <@hst.html hippohtml=document.backstory contentRewriter=gaContentRewriter />
                        </div>
                    </div>
                </#if>

                <div class="article-header" >
                    <h2>Share this page</h2>
                    <@hst.link var="link" hippobean=document />

                    <div class="blog-social">
                        <#-- Use UTF-8 charset for URL escaping from now: -->
                        <#setting url_escaping_charset="UTF-8">

                        <div class="blog-social-icon">
                            <a target="_blank" href="http://www.facebook.com/sharer.php?u=${currentUrl?url}">
                                <img src="<@hst.webfile path="/images/icon/Facebook.svg"/>" alt="Share on Facebook" class="blog-social-icon__img" />
                            </a>
                        </div>

                        <div class="blog-social-icon">
                            <#assign hashtags ='' />
                            <#if hasTwitterHashtag>
                                <#list document.twitterHashtag as tag>
                                    <#if tag?starts_with("#")>
                                        <#assign hashtags = hashtags + tag?keep_after('#') + ','>
                                    <#else>
                                        <#assign hashtags = hashtags + tag + ','>
                                    </#if>
                                </#list>
                            </#if>
                            <a target="_blank" href="https://twitter.com/intent/tweet?via=nhsdigital&url=${currentUrl?url}&text=${document.title?url}&hashtags=${hashtags?url}">
                                <img src="<@hst.webfile path="/images/icon/Twitter.svg"/>" alt="Share on Twitter" class="blog-social-icon__img" />
                            </a>
                        </div>

                        <div class="blog-social-icon">
                            <a target="_blank" href="http://www.linkedin.com/shareArticle?mini=true&url=${currentUrl?url}&title=${document.title?url}&summary=${document.shortsummary?url}">
                                <img src="<@hst.webfile path="/images/icon/LinkedIn.svg"/>" alt="Share on LinkedIn" class="blog-social-icon__img" />
                            </a>
                        </div>
                    </div>

                </div>
                
                <#if hasPeople>

                    <div class="latestBlog">
                        <#list document.peoplementioned as author>
                            <div class="latestBlog__item">

                                <div class="latestBlog__icon">
                                    <#if author.personimages?? && author.personimages?has_content && author.personimages.picture?has_content>
                                        <img class="latestBlog__icon__img" src="<@hst.link hippobean=author.personimages.picture.original fullyQualified=true />" alt="${author.title}" />
                                    <#else>
                                        <img class="latestBlog__icon__nhsimg" src="<@hst.webfile path="/images/fibre_57101102_med.jpg"/>" alt="NHS Digital blog">
                                    </#if>
                                </div>

                                <div class="latestBlog__content">
                                    <div class="latestBlog__title">
                                        <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, author.title) />
                                        <a class="cta__title cta__button" href="<@hst.link hippobean=author/>"  onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)">${author.title}</a>
                                    </div>
                                    <div class="blog-authors__body">
                                        <#if author.biographies?? && author.biographies?has_content && author.biographies.profbiography.content?has_content>
                                            <@hst.html hippohtml=author.biographies.profbiography contentRewriter=gaContentRewriter/>
                                        </#if>
                                    </div>
                                </div>

                            </div>
                        </#list>
                    </div>
                </#if>

                <#assign rendername="Media enquiries" /><#if hasContactDetails && document.mediacontact.name?has_content ><#assign rendername=document.mediacontact.name /></#if>
                <#assign renderemail="media@nhsdigital.nhs.net" /><#if hasContactDetails && document.mediacontact.emailaddress?has_content ><#assign renderemail=document.mediacontact.emailaddress /></#if>
                <#assign renderphone="0300 30 33 888" /><#if hasContactDetails && document.mediacontact.phonenumber?has_content ><#assign renderphone=document.mediacontact.phonenumber /></#if>
                <@contactdetail '' idsuffix rendername renderemail renderphone "Contact us" false></@contactdetail>

                <#if document.relatedsubjects?has_content>
                  <div class="article-section" itemprop="about">
                    <@relatedarticles document.relatedsubjects "News" false idsuffix "Related subjects" true/>
                  </div>
                </#if>

                <div class="article-section muted" itemprop="dateModified">
                    <@lastModified document.lastModified false></@lastModified>
                </div>
            </div>

            <#if hasLatestNews>
              <div class="column column--one-third">
                <div class="news-latest-news">
                  <div class="news-latest-news-title">Latest news</div>
                  <#list document.latestNews as news>
                    <div class="news-latest-news-item">
                      <div class="news-latest-news-item-headline">
                        <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, news.title) />
                        <a href="<@hst.link hippobean=news/>" onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)">${news.title}</a>
                      </div>
                      <div class="news-latest-news-item-date">
                        <@fmt.formatDate value=news.publisheddatetime.time type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" />
                      </div>
                    </div>
                  </#list>
                </div>
              </div>
            </#if>
        </div>
    </div>
</article>

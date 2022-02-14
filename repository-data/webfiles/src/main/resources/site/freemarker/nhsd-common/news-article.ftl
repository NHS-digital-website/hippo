<#ftl output_format="HTML">
<#setting url_escaping_charset="UTF-8">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.News" -->
<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/relatedarticles.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/editorsnotes.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/component/downloadBlockInternal.ftl">
<#include "macro/contactdetail.ftl">
<#include "macro/heroes/hero-options.ftl">
<#include "macro/heroes/hero.ftl">
<#include "macro/contentPixel.ftl">
<#include "macro/latestblogs.ftl">
<#include "macro/shareSection.ftl">

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
<#assign hasContactDetails = (document.mediacontact)?? && document.mediacontact?has_content />
<#assign hasLatestNews = document.latestNews?? && document.latestNews?has_content />

<#assign hasTopics = document.topics?? && document.topics?has_content />
<#assign hasNewsType = document.type?? && document.type?has_content />
<#assign hasTwitterHashtag = document.twitterHashtag?? && document.twitterHashtag?has_content />
<#assign idsuffix = slugify(document.title) />


<#assign heroOptions = getHeroOptions(document)/>
<#assign heroOptions += {
    "colour": "darkBlue",
    "introText": "News"
}/>

<@fmt.formatDate value=document.publisheddatetime.time type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" var="date" />
<#assign metadata = [{
   "title": "Date",
   "value": date,
   "schemaOrgTag": "datePublished"
  }] />
<#if hasTopics>
    <#assign metadata += [{
        "title": "Topics",
        "value": document.topics,
        "schemaOrgTag": "keywords"
    }] />
</#if>
<#if hasNewsType>
    <#assign metadata += [{
        "title": "News type",
        "value": newstypes[document.type]
    }] />
</#if>
<#if hasTwitterHashtag>
    <#assign hashTags = []/>
    <#list document.twitterHashtag as value>
        <#assign hashTag = value?starts_with("#")?then(value, "#${value}") />
        <#assign hashTags += [
            "<a href=\"https://twitter.com/search?q=${hashTag?url}\" class=\"nhsd-a-link nhsd-a-link--col-white\" target=\"blank_\">${hashTag}</a>"
        ]/>
    </#list>
    <#assign metadata += [{
        "title": "Twitter",
        "value": hashTags
    }] />
</#if>

<#assign heroOptions += {
    "metaData": metadata
}/>

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<article itemscope itemtype="http://schema.org/NewsArticle">
    <meta itemprop="mainEntityOfPage" content="${document.title}">
    <meta itemprop="author" content="NHS Digital">
    <meta itemprop="copyrightHolder" content="NHS Digital">
    <meta itemprop="license" content="https://digital.nhs.uk/about-nhs-digital/terms-and-conditions">
    <div class="is-hidden" itemprop="publisher" itemscope itemtype="https://schema.org/Organization">
        <meta itemprop="name" content="NHS Digital">
        <span itemprop="logo" itemscope itemtype="https://schema.org/ImageObject">
            <meta itemprop="url" content="<@hst.webfile path='/images/nhs-digital-logo-social.jpg' fullyQualified=true />" />
        </span>
    </div>

    <@hero heroOptions />

    <div class="nhsd-t-grid nhsd-!t-margin-top-8">
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-xs-12 nhsd-t-col-s-8">

                <#if document.creditBanner?has_content>
                    <div class="nhsd-m-emphasis-box nhsd-!t-margin-bottom-6" role="alert">
                        <div class="nhsd-a-box nhsd-a-box--border-blue">
                            <div class="nhsd-m-emphasis-box__content-box">
                                <p class="nhsd-t-body-s nhsd-t-word-break">${creditbanner[document.creditBanner]}</p>
                            </div>
                        </div>
                    </div>
                </#if>

                <#if hasLeadImage>
                    <div class="nhsd-t-col-12">
                        <div class="nhsd-o-gallery__card-container">
                            <div class="nhsd-m-card">
                                <div class="nhsd-a-box nhsd-a-box--border-grey">
                                    <figure class="nhsd-a-image nhsd-a-image--round-top-corners" itemprop="image" itemscope itemtype="http://schema.org/ImageObject">
                                        <@hst.link hippobean=document.leadimagesection.leadImage.newsPostImage2x fullyQualified=true var="leadImage" />
                                        <meta itemprop="url" content="${leadImage}" />
                                        <picture class="nhsd-a-image__picture ">
                                            <img itemprop="contentUrl" src="${leadImage}" alt="<#if hasLeadImageAltText>${document.leadimagesection.alttext}</#if>">
                                        </picture>
                                    </figure>
                                    <#if hasLeadImageCaption>
                                        <div class="nhsd-m-card__content_container">
                                            <div class="nhsd-m-card__content-box nhsd-!t-margin-bottom-4" data-uipath="website.blog.leadimagecaption">
                                                <@hst.html hippohtml=document.leadimagesection.imagecaption contentRewriter=brContentRewriter/>
                                            </div>
                                            <div class="nhsd-m-card__button-box"></div>
                                        </div>
                                    </#if>
                                </div>
                            </div>
                        </div>
                    </div>
                <#else>
                    <span itemprop="image" itemscope
                          itemtype="https://schema.org/ImageObject">
                        <meta itemprop="url" content="<@hst.webfile path='/images/nhs-digital-logo-social.jpg' fullyQualified=true />"/>
                    </span>
                </#if>

                <#if hasSectionContent>
                    <div itemprop="articleBody">
                        <@sections document.sections></@sections>
                    </div>
                </#if>

                <#if document.relateddocuments?has_content>
                    <#if hasSectionContent>
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>
                    <p class="nhsd-t-heading-xl">Related pages</p>
                    <div class="nhsd-t-grid">
                        <div class="nhsd-t-row">
                            <div class="nhsd-t-col">
                                <#list document.relateddocuments as child>
                                    <@downloadBlockInternal child.class.name child child.title child.shortsummary />
                                </#list>
                            </div>
                        </div>
                    </div>
                </#if>

                <#if hasEditorsNotes>
                    <p class="nhsd-t-heading-xl">Notes for editors</p>
                    <ol class="nhsd-t-list nhsd-t-list--number">
                        <#list document.notesforeditors.htmlnotes as note>
                            <#if note.content?has_content>
                                <li><@hst.html hippohtml=note contentRewriter=brContentRewriter/></li>
                            </#if>
                        </#list>
                    </ol>
                </#if>

                <#if hasBackstory>
                    <#if hasEditorsNotes || (hasSectionContent && !hasEditorsNotes && !document.relateddocuments?has_content)>
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>
                    <div itemprop="backstory">
                        <p class="nhsd-t-heading-xl">Back story</p>
                        <div class="nhsd-a-box nhsd-a-box--bg-light-blue nhsd-!t-margin-bottom-6" data-uipath="website.news.backstory">
                            <@hst.html hippohtml=document.backstory contentRewriter=brContentRewriter />
                        </div>
                    </div>
                </#if>

                <#--  Social media section  -->
                <#if (!hasBackstory && hasEditorsNotes) || (hasSectionContent && !hasBackstory && !hasEditorsNotes && !document.relateddocuments?has_content)>
                    <hr class="nhsd-a-horizontal-rule" />
                </#if>

                <div class="nhsd-!t-margin-bottom-6" itemprop="articleBody">
                    <@shareSection document />
                </div>

                <#if hasPeople>
                    <div class="nhsd-!t-margin-bottom-6">
                        <div class="nhsd-t-grid">
                            <div class="nhsd-t-row nhsd-o-gallery__items">
                                <#list document.peoplementioned as author>
                                    <div class="nhsd-t-col-xs-12 nhsd-t-col-s-6 nhsd-t-col-m-4 nhsd-!t-margin-bottom-6">
                                        <div class="nhsd-o-gallery__card-container">
                                            <div class="nhsd-m-card">
                                                <@hst.link hippobean=author var="authorLink"/>
                                                <a class="nhsd-a-box-link nhsd-a-box-link--focus-orange"
                                                   href="${authorLink}"
                                                   onClick="${getOnClickMethodCall(document.class.name, authorLink)}"
                                                   onKeyUp="return vjsu.onKeyUp(event)"
                                                   aria-label="${author.title}"
                                                >
                                                    <div class="nhsd-a-box nhsd-a-box--bg-light-grey">
                                                        <div class="nhsd-m-card__image_container">
                                                            <figure class="nhsd-a-image nhsd-a-image--maintain-ratio">
                                                                <picture class="nhsd-a-image__picture">
                                                                    <#if author.personimages?? && author.personimages?has_content && author.personimages.picture?has_content>
                                                                        <img src="<@hst.link hippobean=author.personimages.picture.original fullyQualified=true />" alt="${author.title}">
                                                                    <#else>
                                                                        <img src="<@hst.webfile path="/images/fibre_57101102_med.jpg"/>" alt="NHS Digital blog">
                                                                    </#if>
                                                                </picture>
                                                            </figure>
                                                        </div>
                                                        <div class="nhsd-m-card__content_container">
                                                            <div class="nhsd-m-card__content-box">
                                                                <p class="nhsd-t-heading-s">${author.title}</p>
                                                                <#if author.shortsummary?? && author.shortsummary?has_content>
                                                                    <p class="nhsd-t-body-s">${author.shortsummary}</p>
                                                                </#if>
                                                                <div class="nhsd-m-card__button-box">
                                                                    <span class="nhsd-a-icon nhsd-a-arrow nhsd-a-icon--size-s nhsd-a-icon--col-black">
                                                                        <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                                                            <path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/>
                                                                        </svg>
                                                                    </span>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </#list>
                            </div>
                        </div>
                    </div>
                </#if>


                <#assign rendername="Media enquiries" /><#if hasContactDetails && document.mediacontact.name?has_content ><#assign rendername=document.mediacontact.name /></#if>
                <#assign renderemail="media@nhsdigital.nhs.net" /><#if hasContactDetails && document.mediacontact.emailaddress?has_content ><#assign renderemail=document.mediacontact.emailaddress /></#if>
                <#assign renderphone="0300 30 33 888" /><#if hasContactDetails && document.mediacontact.phonenumber?has_content ><#assign renderphone=document.mediacontact.phonenumber /></#if>
                <@contactdetail '' idsuffix rendername renderemail renderphone "Contact us" false></@contactdetail>

                <#if hasLatestNews>
                    <@latestblogs document.latestNews 'News' 'news-' + idsuffix 'Latest news' false />
                    <hr class="nhsd-a-horizontal-rule nhsd-!t-margin-top-0" />
                </#if>

                <@lastModified document.lastModified false></@lastModified>
            </div>
        </div>
    </div>
</article>

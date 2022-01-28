<#ftl output_format="HTML">

<#-- @ftlvariable name="blog" type="uk.nhs.digital.website.beans.Blog" -->

<#include "../include/imports.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/latestblogs.ftl">
<#include "macro/personitem.ftl">
<#include "macro/contentPixel.ftl">
<#include "macro/heroes/hero.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>


<#assign hasAuthors = document.authors?? && document.authors?has_content />
<#assign hasAuthorManualEntry = document.authorRole?? || (document.authorDescription?? && document.authorDescription?has_content) ||
                                document.authorName?? || document.authorJobTitle?? || document.authorOrganisation?? />
<#assign hasBlogCategories = document.caseStudyCategories?? && document.caseStudyCategories?has_content />
<#assign hasTopics = document.topics?? && document.topics?has_content />

<#assign hasLeadImage = document.leadImage?has_content />
<#assign hasLeadImageAltText = document.leadImageAltText?has_content />
<#assign hasLeadImageCaption = document.leadImageCaption?? && document.leadImageCaption.content?has_content />
<#assign hasLeadParagraph = document.leadParagraph?? && document.leadParagraph.content?has_content />
<#assign hasSectionContent = document.sections?has_content />
<#assign hasBackstory = document.backstory?? && document.backstory.content?has_content />
<#assign hasTwitterHashtag = document.twitterHashtag?? && document.twitterHashtag?has_content />
<#assign hasContactDetails = document.contactDetails?? && document.contactDetails.content?has_content />
<#assign hasRelatedSubjects = document.relatedSubjects?? && document.relatedSubjects?has_content />
<#assign hasCubeHeader = document.headertype == "Cube header" >
<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<article itemscope itemtype="http://schema.org/BlogPosting">
	<#-- Use UTF-8 charset for URL escaping from now: -->
    <#setting url_escaping_charset="UTF-8">

    <#--  Facebook  -->
    <#assign facebookUrl = "http://www.facebook.com/sharer.php?u=${currentUrl?url}"/>
    <#assign facebookIconPath = "/images/icon/rebrand-facebook.svg" />

    <#--  Twitter  -->
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
    <#assign twitterUrl = "https://twitter.com/intent/tweet?via=nhsdigital&url=${currentUrl?url}&text=${document.title?url}&hashtags=${hashtags?url}"/>
    <#assign twitterIconPath = "/images/icon/rebrand-twitter.svg" />

    <#--  LinkedIn  -->
    <#assign linkedInUrl = "http://www.linkedin.com/shareArticle?mini=true&url=${currentUrl?url}&title=${document.title?url}&summary=${document.shortsummary?url}"/>
    <#assign linkedInIconPath = "/images/icon/rebrand-linkedin.svg" />

    <#--  YouTube  -->
    <#assign youTubeUrl = "http://www.youtube.com/watch?v=${currentUrl?url}"/>
    <#assign youTubeIconPath = "/images/icon/rebrand-youtube.svg" />

    <#assign metaData = [] />
    <#if hasAuthors>
        <#assign authorValue>
            <div class="nhsd-!t-margin-bottom-1">
                <#list document.authors as author>
                    <@hst.link hippobean=author var="authorLink" />
                    <div>
                        <a class="nhsd-a-link"
                           href="${authorLink}"
                           onClick="${getOnClickMethodCall(document.class.name, authorLink)}"
                        >
                            ${author.title}
                        </a>
                        <#if author.roles??>
                            <#if author.roles.primaryroles?has_content>, ${author.roles.firstprimaryrole}</#if><#if author.roles.primaryroleorg?has_content>, ${author.roles.primaryroleorg}</#if>
                        </#if>
                    </div>
                </#list>
            </div>
        </#assign>
        <#assign metaData += [{
            "title": "Author${(document.authors?size gt 1)?then('s', '')}",
            "value": authorValue
        }] />
    <#elseif document.authorName?has_content>
        <#assign authorValue>
            ${document.authorName}<#if document.authorJobTitle?has_content>, ${document.authorJobTitle}</#if><#if document.authororganisation?has_content>, ${document.authororganisation}</#if>
        </#assign>
        <#assign metaData += [{
            "title": "Author",
            "value": authorValue
        }] />
    </#if>
    <#if document.dateOfPublication.time?has_content>
        <@fmt.formatDate value=document.dateOfPublication.time type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" var="dateOfPublication" />
        <#assign metaData += [{
            "title": "Date",
            "value": dateOfPublication
        }] />
    </#if>
    <#if hasTopics>
        <#assign topics>
            <#list document.topics as tag>${tag}<#sep>, </#list>
        </#assign>
        <#assign metaData += [{
            "title": "Topic${(document.topics?size gt 1)?then('s','')}",
            "value": topics
        }] />
    </#if>
    <#if hasBlogCategories>
        <#assign categories>
            <#list document.caseStudyCategories as category>${category}<#sep>, </#list>
        </#assign>
        <#assign metaData += [{
            "title": "Categories",
            "value": categories
        }] />
    </#if>

    <@hst.link hippobean=document.leadImage.pageHeaderHeroModule2x fullyQualified=true var="bannerImage" />
    <#assign heroOptions = {
        "introTags": ["Blog"],
        "title": document.title,
        "summary": document.shortsummary,
        "metaData": metaData,
        "uiPath": "website.blog"
    } />

    <#if document.leadImage?has_content>
        <@hst.link hippobean=document.leadImage.pageHeaderHeroModule2x fullyQualified=true var="bannerImage" />
        <#assign heroOptions += {
            "image": {
                "src": bannerImage,
                "alt": document.leadImageAltText
            }
        }/>
    </#if>

    <#assign heroType = "default" />
    <#if document.headertype?has_content && document.headertype == "Image header" && document.leadImage?has_content>
        <#assign heroType = "backgroundImage" />
    </#if>
    <@hero heroOptions heroType/>

    <div itemprop="articleBody">
    <div class="nhsd-t-grid nhsd-!t-margin-top-8">
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-xs-12 nhsd-t-col-s-8">
                <#if hasLeadParagraph>
                    <div class="nhsd-t-heading-xs" data-uipath="website.blog.leadparagraph">
                        <@hst.html hippohtml=document.leadParagraph contentRewriter=brContentRewriter/>
                    </div>
                </#if>

                <#if hasLeadImage && hasCubeHeader>
                    <div class="nhsd-m-card nhsd-!t-margin-bottom-6">
                        <div class="nhsd-a-box nhsd-a-box--border-grey">
                            <div class="nhsd-m-card__image_container">
                                <figure class="nhsd-a-image nhsd-a-image--round-top-corners">
                                    <picture class="nhsd-a-image__picture ">
                                        <@hst.link hippobean=document.leadImage.newsPostImageLarge2x fullyQualified=true var="leadImageLarge2x" />
                                        <meta itemprop="url" content="${leadImage}">
                                        <img src="${leadImageLarge2x}" alt="<#if hasLeadImageAltText>${document.leadImageAltText}</#if>" />
                                    </picture>
                                </figure>
                            </div>
                            <#if hasLeadImageCaption>
                                <div class="nhsd-m-card__content_container">
                                    <div class="nhsd-m-card__content-box nhsd-!t-margin-bottom-4" data-uipath="website.blog.leadimagecaption">
                                        <@hst.html hippohtml=document.leadImageCaption contentRewriter=brContentRewriter/>
                                    </div>
                                </div>
                            </#if>
                        </div>
                    </div>
                </#if>

                <#if hasSectionContent>
                    <@sections document.sections></@sections>
                    <hr class="nhsd-a-horizontal-rule" />
                </#if>

                <#if hasBackstory>
                    <div class="nhsd-a-box nhsd-a-box--bg-light-blue nhsd-!t-margin-bottom-6">
                        <p class="nhsd-t-heading-m">Back story</p>
                        <div data-uipath="website.blog.backstory"><@hst.html hippohtml=document.backstory contentRewriter=brContentRewriter /></div>
                    </div>
                </#if>

                <#if hasContactDetails>
                    <#if hasSectionContent && !hasBackstory>
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>
                    <div class="nhsd-m-contact-us nhsd-!t-margin-bottom-6" aria-label="">
                        <div class="nhsd-a-box nhsd-a-box--bg-light-blue-10">
                            <div class="nhsd-m-contact-us__content">
                                <p class="nhsd-t-heading-m">Contact Us</p>
                                <div data-uipath="website.blog.contactus">
                                    <@hst.html hippohtml=document.contactDetails contentRewriter=brContentRewriter/>
                                </div>
                            </div>
                        </div>
                    </div>
                </#if>

                <#if hasRelatedSubjects>
                    <#if hasSectionContent && !hasBackstory && !hasContactDetails>
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>
                    <div class="nhsd-!t-margin-bottom-6">
                        <p class="nhsd-t-heading-xl">Related subjects</p>
                        <#list document.relatedSubjects as item>
                            <@hst.link hippobean=item var="relatedSubjectLink"/>
                            <div class="nhsd-!t-margin-bottom-1" itemprop="isBasedOn" itemscope itemtype="http://schema.org/Product">
                                <a itemprop="url"
                                   class="nhsd-a-link"
                                   href="${relatedSubjectLink}"
                                   onClick="${getOnClickMethodCall(document.class.name, relatedSubjectLink)}"
                                   onKeyUp="return vjsu.onKeyUp(event)"
                                >
                                    ${item.title}
                                </a>
                            </div>
                            <div class="nhsd-t-body">
                                ${item.shortsummary}
                            </div>
                        </#list>
                    </div>
                </#if>

                <div class="nhsd-!t-margin-bottom-6">
                    <@shareSection document />
                    <hr class="nhsd-a-horizontal-rule" />
                </div>

                <#if hasAuthors>
                    <hr class="nhsd-a-horizontal-rule" />
                    <p class="nhsd-t-heading-xl"> Author<#if document.authors?size gt 1 >s</#if> </p>
                    <div class="nhsd-o-gallery">
                        <div class="nhsd-t-grid nhsd-!t-no-gutters">
                            <div class="nhsd-t-row nhsd-o-gallery__items">
                                <#list document.authors as author>
                                    <div class="nhsd-t-col-xs-12 nhsd-t-col-s-6 nhsd-t-col-m-4">
                                        <div class="nhsd-o-gallery__card-container">
                                            <div class="nhsd-m-card">
                                                <@hst.link hippobean=author var="authorLink"/>
                                                <a href="${authorLink}"
                                                   onClick="${getOnClickMethodCall(document.class.name, authorLink)}"
                                                   onKeyUp="return vjsu.onKeyUp(event)"
                                                   class="nhsd-a-box-link nhsd-a-box-link--focus-orange"
                                                   aria-label="${author.title}"
                                                >
                                                    <div class="nhsd-a-box nhsd-a-box--bg-light-grey">
                                                    <#if author.personimages.picture.authorPhotoLarge2x?has_content>
                                                        <div class="nhsd-m-card__image_container">
                                                            <figure class="nhsd-a-image nhsd-a-image--maintain-ratio">
                                                                <picture class="nhsd-a-image__picture ">
                                                                    <@hst.link hippobean=author.personimages.picture.authorPhotoLarge2x fullyQualified=true var="authorPicture" />
                                                                    <img src="${authorPicture}" alt="${author.title}">
                                                                </picture>
                                                            </figure>
                                                        </div>
                                                    </#if>
                                                        <div class="nhsd-m-card__content_container">
                                                            <div class="nhsd-m-card__content-box">
                                                                <h1 class="nhsd-t-heading-s">${author.title}</h1>
                                                                <span class="nhsd-m-card__date">
                                                                    <#if author.roles?has_content >
                                                                        ${author.roles.firstprimaryrole}, ${author.roles.primaryroleorg}
                                                                    </#if>
                                                                </span>
                                                                <#assign profbiography><@hst.html hippohtml=author.biographies.profbiography contentRewriter=brContentRewriter/></#assign>
                                                                ${profbiography}
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

                <div class="grid-wrapper grid-wrapper--article" aria-label="document-content">
                    <div class="grid-row">
                        <@latestblogs document.latestBlogs></@latestblogs>
                    </div>
                </div>
                <div class="nhsd-!t-margin-bottom-6">
                    <@lastModified document.lastModified false></@lastModified>
                </div>
            </div>
        </div>
    </div>
    </div>
</article>

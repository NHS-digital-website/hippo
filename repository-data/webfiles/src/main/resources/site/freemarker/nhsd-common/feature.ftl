<#ftl output_format="HTML">

<#-- @ftlvariable name="feature" type="uk.nhs.digital.website.beans.Feature" -->

<#include "../include/imports.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/contentPixel.ftl">
<#include "macro/shareThisPage.ftl">
<#include "macro/heroes/hero-options.ftl">
<#include "macro/heroes/hero.ftl">
<#include "macro/card.ftl">
<#include "macro/personitem.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>


<#assign hasAuthors = document.authors?? && document.authors?has_content />
<#assign hasAuthorManualEntry = document.authorRole?? || (document.authorDescription?? && document.authorDescription?has_content) ||
                                document.authorName?? || document.authorJobTitle?? || document.authorOrganisation?? />
<#assign hasFeatureCategories = document.caseStudyCategories?? && document.caseStudyCategories?has_content />
<#assign hasTopics = document.topics?? && document.topics?has_content />

<#assign hasLeadImage = document.leadImage?has_content />
<#assign hasLeadImageAltText = document.leadImageAltText?has_content />
<#assign hasLeadImageCaption = document.leadImageCaption?? && document.leadImageCaption.content?has_content />
<#assign hasLeadParagraph = document.leadParagraph?? && document.leadParagraph.content?has_content />
<#assign hasSectionContent = document.sections?has_content />
<#assign hasBackstory = document.backstory?? && document.backstory.content?has_content />

<#assign hasContactDetails = document.contactDetails?? && document.contactDetails.content?has_content />
<#assign hasRelatedSubjects = document.relatedSubjects?? && document.relatedSubjects?has_content />
<#assign heroType = "default"/>
<#assign hasCubeHeader = document.headertype == "Cube header" />

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<article itemscope itemtype="http://schema.org/BlogPosting">
    <#assign heroOptions = getHeroOptions(document) />

    <#assign metaData = [] />

    <#if hasTopics>
        <#assign topics>
            <#list document.topics as tag>${tag}<#sep>, </#list>
        </#assign>
        <#assign metaData += [{
            "title": "Topic${(document.topics?size gt 1)?then('s','')}",
            "value": topics
        }] />
    </#if>
    <#if hasFeatureCategories>
        <#assign categories>
            <#list document.caseStudyCategories as category>${category}<#sep>, </#list>
        </#assign>
        <#assign metaData += [{
            "title": "Categories",
            "value": categories
        }] />
    </#if>

    <#assign heroOptions += {
        "introTags": ["Feature"],
        "metaData": metaData,
        "uiPath": "website.feature"
    } />

    <#if document.headertype?has_content && document.headertype == "Image header" && document.leadImage?has_content>
        <#assign heroType = "backgroundImage"/>
    <#elseif document.headertype?has_content && document.headertype == "Black hero" />
    	<#assign heroType = "blackHero"/>
    	<#assign heroOptions += {"colour": "Black"}>
    <#elseif document.headertype?has_content && document.headertype == "White hero" />
    	<#assign heroType = "whiteHero"/>
    	<#assign heroOptions += {"colour": "White"}>
    <#elseif document.headertype?has_content && document.headertype == "Black background" />
    	<#assign heroType = "blackBackground"/>
    	<#assign heroOptions += {"colour": "Black"}>
    	<#assign heroOptions += {"image": {"src": leadImageBlogIndexThumb@2x, "alt": document.leadImageAltText}}>
    </#if>
    <@hero heroOptions heroType />

    <div itemprop="articleBody">
    <div class="nhsd-t-grid nhsd-!t-margin-top-8" aria-label="document-content">
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-xs-12 nhsd-t-col-s-8">
                <#if hasLeadParagraph>
                    <div class="nhsd-t-heading-m" data-uipath="website.feature.leadparagraph">
                        <@hst.html hippohtml=document.leadParagraph contentRewriter=brContentRewriter/>
                    </div>
                </#if>

                <#if hasLeadImage && hasCubeHeader>
                    <div class="nhsd-m-card nhsd-!t-margin-bottom-6">
                        <div class="nhsd-a-box nhsd-a-box--border-grey">
                            <div class="nhsd-m-card__image_container">
                                <figure class="nhsd-a-image">
                                    <meta itemprop="url" content="${leadImage}" />
                                    <picture class="nhsd-a-image__picture " itemprop="image" itemscope itemtype="http://schema.org/ImageObject">
                                        <@hst.link hippobean=document.leadImage.newsPostImageLarge2x fullyQualified=true var="leadImageLarge2x" />
                                        <img src="${leadImageLarge2x}" alt="<#if hasLeadImageAltText>${document.leadImageAltText}</#if>" />
                                    </picture>
                                </figure>
                            </div>
                            <#if hasLeadImageCaption>
                                <div class="nhsd-m-card__content_container">
                                    <div class="nhsd-m-card__content-box nhsd-!t-margin-bottom-4" data-uipath="website.feature.leadimagecaption">
                                        <@hst.html hippohtml=document.leadImageCaption contentRewriter=brContentRewriter/>
                                    </div>
                                </div>
                            </#if>
                        </div>
                    </div>
                </#if>

                <#if hasLeadParagraph || (hasLeadImage && hasCubeHeader)>
                    <hr class="nhsd-a-horizontal-rule" />
                </#if>

                <#if hasSectionContent>
                    <@sections document.sections></@sections>
                </#if>

                 <#if hasBackstory>
                    <#if hasSectionContent>
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>
                    <div>
                        <div class="nhsd-a-box nhsd-a-box--bg-light-blue nhsd-!t-margin-bottom-6" data-uipath="website.feature.backstory">
                            <p class="nhsd-t-heading-m">Back story</p>
                            <@hst.html hippohtml=document.backstory contentRewriter=brContentRewriter />
                        </div>
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
                                <div data-uipath="website.feature.contactus">
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
                    <#if hasRelatedSubjects || hasSectionContent && !hasBackstory && !hasContactDetails && !hasRelatedSubjects>
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>

                    <#if hasAuthors>
	                    <p class="nhsd-t-heading-xl"> Author<#if document.authors?size gt 1 >s</#if> </p>
	                    <div class="nhsd-o-gallery">
	                        <div class="nhsd-t-grid nhsd-!t-no-gutters">
	                            <div class="nhsd-t-row nhsd-o-gallery__items">
	                                <#list document.authors as author>
	                                    <@personitem author/>
	                                </#list>
	                            </div>
	                        </div>
	                    </div>
	                <#elseif document.authorName?has_content>
	                	<p class="nhsd-t-heading-xl"> Author</p>
				        <#assign authorValue>
				            ${document.authorName}<#if document.authorJobTitle?has_content>, ${document.authorJobTitle}</#if><#if document.authororganisation?has_content>, ${document.authororganisation}</#if>
				        </#assign>

        				<#assign cardProperties = {
                    		"background": "light-grey",
							"title": authorValue
                    	}/>
	                    <@card cardProperties/>
                	</#if>
                    <#if !hasRelatedSubjects && !hasSectionContent >
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>
                    <p class="nhsd-t-heading-xl">Share this page</p>
                    <#-- Use UTF-8 charset for URL escaping from now: -->
                    <#setting url_escaping_charset="UTF-8">

                    <div class="nhsd-t-grid nhsd-!t-margin-bottom-4 nhsd-!t-no-gutters">
                        <#--  Facebook  -->
                        <#assign facebookUrl = "http://www.facebook.com/sharer.php?u=${currentUrl?url}"/>
                        <#assign facebookIconPath = "/images/icon/rebrand-facebook.svg" />
                        <@shareThisPage document "Facebook" facebookUrl facebookIconPath/>

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
                        <@shareThisPage document "Twitter" twitterUrl twitterIconPath/>

                        <#--  LinkedIn  -->
                        <#assign linkedInUrl = "http://www.linkedin.com/shareArticle?mini=true&url=${currentUrl?url}&title=${document.title?url}&summary=${document.shortsummary?url}"/>
                        <#assign linkedInIconPath = "/images/icon/rebrand-linkedin.svg" />
                        <@shareThisPage document "LinkedIn" linkedInUrl linkedInIconPath/>
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

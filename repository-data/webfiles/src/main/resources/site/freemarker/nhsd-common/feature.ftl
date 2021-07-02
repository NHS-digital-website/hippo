<#ftl output_format="HTML">

<#-- @ftlvariable name="feature" type="uk.nhs.digital.website.beans.feature" -->

<#include "../include/imports.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/personitem.ftl">
<#include "macro/contentPixel.ftl">
<#include "macro/shareThisPage.ftl">

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
<#assign hasCubeHeader = document.headertype == "Cube header" />

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<article itemscope itemtype="http://schema.org/BlogPosting">
    <#if !document.headertype?has_content || document.headertype == "Cube header"  >
        <div class="nhsd-o-hero nhsd-!t-bg-bright-blue-20-tint nhsd-!t-bg-pale-grey nhsd-!t-margin-bottom-6">
            <div class="nhsd-t-grid nhsd-!t-no-gutters ">
                <div class="nhsd-t-row ">
                    <div class="nhsd-t-col-xs-12 nhsd-t-col-s-8 nhsd-!t-no-gutters">
                        <div class="nhsd-o-hero__content-box  nhsd-o-hero__content-box--left-align">
                            <div class="nhsd-o-hero__content">

                                <span class="nhsd-a-tag nhsd-a-tag--phase">Feature</span>

                                <#if document.title?has_content> 
                                <span class="nhsd-t-heading-xl nhsd-!t-margin-top-3" itemprop="headline" data-uipath="document.title">${document.title}</span>
                                </#if>

                                <#if document.shortsummary?has_content>
                                <p class="nhsd-t-heading-s nhsd-!t-margin-bottom-6" data-uipath="website.feature.summary">${document.shortsummary}</p>
                                </#if> 

                                <div class="nhsd-o-hero__meta-data nhsd-!t-margin-bottom-6">
                                    <#if hasAuthors>
                                        <div class="nhsd-o-hero__meta-data-item">
                                            <div class="nhsd-o-hero__meta-data-item-title">Author<#if document.authors?size gt 1 >s</#if>: </div>

                                            <div class="nhsd-t-grid nhsd-!t-no-gutters">
                                                <#list document.authors as author>
                                                    <div class="nhsd-t-row">
                                                        <div class="nhsd-t-col-12 nhsd-!t-no-gutters">
                                                            <@hst.link hippobean=author var="authorLink"/> 
                                                            <div class="nhsd-o-hero__meta-data-item-description">
                                                                <a class="nhsd-a-link" 
                                                                   href="${authorLink}" 
                                                                   onClick="${getOnClickMethodCall(document.class.name, authorLink)}" 
                                                                   onKeyUp="return vjsu.onKeyUp(event)" 
                                                                   aria-label="${author.title}"
                                                                >
                                                                    ${author.title}
                                                                    <span class="nhsd-t-sr-only"></span>
                                                                </a>
                                                                <#if author.roles??><#if author.roles.primaryroles?has_content>, ${author.roles.firstprimaryrole}</#if></#if><#if author.roles??><#if author.roles.primaryroleorg?has_content>, ${author.roles.primaryroleorg}</#if></#if>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </#list>
                                            </div>
                                        </div>
                                    <#elseif document.authorName?has_content>
                                        <div class="nhsd-o-hero__meta-data-item">
                                            <div class="nhsd-o-hero__meta-data-item-title">Author: </div>
                                            <div class="nhsd-o-hero__meta-data-item-description">
                                            ${document.authorName}<#if document.authorJobTitle?has_content>, ${document.authorJobTitle}</#if><#if document.authororganisation?has_content>, ${document.authororganisation}</#if></div>
                                        </div>
                                    </#if> 

                                    <#if document.dateOfPublication.time?has_content >
                                        <div class="nhsd-o-hero__meta-data-item">
                                            <div class="nhsd-o-hero__meta-data-item-title">Date: </div>
                                            <div class="nhsd-o-hero__meta-data-item-description" itemprop="datePublished" data-uipath="website.feature.dateofpublication"><@fmt.formatDate value=document.dateOfPublication.time type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" /></div>
                                        </div>
                                    </#if>

                                    <#if hasTopics>
                                        <div class="nhsd-o-hero__meta-data-item">
                                            <div class="nhsd-o-hero__meta-data-item-title">Topic<#if document.topics?size gt 1 >s</#if>: </div>
                                            <div class="nhsd-o-hero__meta-data-item-description" itemprop="keywords" data-uipath="website.feature.topics"><#list document.topics as tag>${tag}<#sep>, </#list></div>
                                        </div>
                                    </#if> 

                                    <#if hasFeatureCategories>
                                        <div class="nhsd-o-hero__meta-data-item">
                                            <div class="nhsd-o-hero__meta-data-item-title">Categories: </div>
                                            <div class="nhsd-o-hero__meta-data-item-description" itemprop="keywords" data-uipath="website.feature.categories"><#list document.caseStudyCategories as category>${category}<#sep>, </#list></div>
                                        </div>
                                    </#if>   
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="nhsd-a-digiblocks nhsd-a-digiblocks--pos-tr">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 550 550"><g><g transform="translate(222, 224)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon></g><g transform="translate(328.5, 367.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon></g><g transform="translate(151, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#3C4D57"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#32434C"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#313D45"></polygon></g><g transform="translate(80, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon></g></g><g><g transform="translate(186.5, 203.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon></g><g transform="translate(186.5, 285.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(222, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(9, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(257.5, 449.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#F5D507"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F2CB0C"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EEC000"></polygon></g></g><g><g transform="translate(186.5, 203.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#6D7B86"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#62717A"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#5C6B75"></polygon></g><g transform="translate(399.5, 326.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon></g><g transform="translate(222, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon></g></g><g><g transform="translate(328.5, 162.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#F5D507"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F2CB0C"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EEC000"></polygon></g><g transform="translate(399.5, 244.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(115.5, 162.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#6D7B86"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#62717A"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#5C6B75"></polygon></g><g transform="translate(186.5, 244.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon></g><g transform="translate(328.5, 326.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon></g><g transform="translate(257.5, 326.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#3C4D57"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#32434C"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#313D45"></polygon></g></g><g><g transform="translate(328.5, 244.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#DADFDF"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#CDD5D6"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#C5CDCF"></polygon></g><g transform="translate(257.5, 285.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon></g><g transform="translate(44.5, 203.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#6D7B86"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#62717A"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#5C6B75"></polygon></g><g transform="translate(151, 265)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon></g></g><g><g transform="translate(435, 142)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon></g></g><g><g transform="translate(328.5, 39.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon></g><g transform="translate(222, 19)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(257.5, 80.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#DADFDF"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#CDD5D6"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#C5CDCF"></polygon></g></g></svg>
            </div>
        </div>
    <#else>
        <div class="nhsd-o-hero nhsd-!t-bg-pale-grey nhsd-!t-text-align-center nhsd-o-hero--background-image nhsd-!t-margin-bottom-6">
            <div class="nhsd-t-grid nhsd-!t-no-gutters  nhsd-t-grid--full-width">
                <div class="nhsd-t-row nhsd-t-row--centred">
                    <div class="nhsd-t-col-xs-12 nhsd-t-col-s-8 nhsd-t-col-l-6 nhsd-!t-no-gutters">
                        <div class="nhsd-o-hero__content-box ">
                            <div class="nhsd-o-hero__content">

                                <span class="nhsd-a-tag nhsd-a-tag--phase">Feature</span>

                                <#if document.title?has_content> 
                                    <span class="nhsd-t-heading-xl nhsd-!t-margin-up-3" itemprop="headline" data-uipath="document.title">${document.title}</span>
                                </#if>

                                <#if document.summary?has_content> 
                                    <p class="nhsd-t-heading-s nhsd-!t-margin-bottom-6" data-uipath="website.feature.summary">${document.shortsummary}</p>
                                </#if>

                                <div class="nhsd-o-hero__meta-data nhsd-!t-margin-bottom-6">
                                    <#if hasAuthors>
                                        <div class="nhsd-o-hero__meta-data-item">
                                            <div class="nhsd-o-hero__meta-data-item-title">Author<#if document.authors?size gt 1 >s</#if>: </div>
                                            <div class="nhsd-t-grid nhsd-!t-no-gutters">
                                                <#list document.authors as author>
                                                    <div class="nhsd-t-row">
                                                        <div class="nhsd-t-col-12 nhsd-!t-no-gutters">
                                                            <@hst.link hippobean=author var="authorLink"/>
                                                            <div class="nhsd-o-hero__meta-data-item-description">
                                                                <a class="nhsd-a-link" 
                                                                   href="${authorLink}" 
                                                                   onClick="${getOnClickMethodCall(document.class.name, authorLink)}" 
                                                                   onKeyUp="return vjsu.onKeyUp(event)"
                                                                   aria-label="${author.title}" 
                                                                >
                                                                    ${author.title}
                                                                    <span class="nhsd-t-sr-only"></span>
                                                                </a>
                                                                <#if author.roles??><#if author.roles.primaryroles?has_content>, ${author.roles.firstprimaryrole}</#if></#if><#if author.roles??><#if author.roles.primaryroleorg?has_content>, ${author.roles.primaryroleorg}</#if></#if>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </#list>
                                            </div>
                                        </div>
                                    <#elseif document.authorName?has_content>
                                        <div class="nhsd-o-hero__meta-data-item">
                                            <div class="nhsd-o-hero__meta-data-item-title">Author: </div>
                                            <div class="nhsd-o-hero__meta-data-item-description">
                                                ${document.authorName}<span class="nhsd-t-sr-only"></span></a><#if document.authorJobTitle?has_content>, ${document.authorJobTitle}</#if><#if document.authororganisation?has_content>, ${document.authororganisation}</#if>
                                            </div>
                                        </div>
                                    </#if>

                                    <#if document.dateOfPublication.time?has_content >
                                        <div class="nhsd-o-hero__meta-data-item">
                                            <div class="nhsd-o-hero__meta-data-item-title">Date: </div>
                                            <div class="nhsd-o-hero__meta-data-item-description" itemprop="datePublished" data-uipath="website.feature.dateofpublication"><@fmt.formatDate value=document.dateOfPublication.time type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" /></div>
                                        </div>
                                    </#if>

                                    <#if hasTopics>
                                        <div class="nhsd-o-hero__meta-data-item">
                                            <div class="nhsd-o-hero__meta-data-item-title">Topic<#if document.topics?size gt 1 >s</#if>: </div>
                                            <div class="nhsd-o-hero__meta-data-item-description" itemprop="keywords" data-uipath="website.feature.topics"><#list document.topics as topic>${topic}<#sep>, </#list></div>
                                        </div>
                                    </#if> 
                                    
                                    <#if hasFeatureCategories>
                                        <div class="nhsd-o-hero__meta-data-item">
                                            <div class="nhsd-o-hero__meta-data-item-title">Categories: </div>
                                            <div class="nhsd-o-hero__meta-data-item-description" itemprop="keywords" data-uipath="website.feature.categories"><#list document.caseStudyCategories as category>${category}<#sep>, </#list></div>
                                        </div>
                                    </#if> 
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="nhsd-o-hero__background-image">
                <figure class="nhsd-a-image nhsd-a-image--cover" aria-hidden="true">
                    <picture class="nhsd-a-image__picture ">
                        <@hst.link hippobean=document.leadImage.pageHeaderHeroModule2x fullyQualified=true var="bannerImage" />
                        <img src="${bannerImage}" alt="<#if hasLeadImageAltText>${document.leadImageAltText}</#if>">
                    </picture>
                </figure>
            </div>
        </div>
    </#if>  

    <div class="nhsd-t-grid " aria-label="document-content">
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-12">
                <#if hasLeadParagraph>
                    <div class="nhsd-t-heading-xs" itemprop="articleBody" data-uipath="website.feature.leadparagraph">
                        <@hst.html hippohtml=document.leadParagraph contentRewriter=brContentRewriter/>
                    </div>
                </#if>

                <#if hasLeadImage && hasCubeHeader>
                    <div class="nhsd-m-card nhsd-!t-margin-bottom-6">
                        <div class="nhsd-a-box nhsd-a-box--border-grey">
                            <div class="nhsd-m-card__image_container">
                                <figure class="nhsd-a-image nhsd-a-image--round-top-corners">
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

                <#if hasSectionContent>
                    <#if !hasLeadImage>
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>
                    <div itemprop="articleBody">
                        <@sections document.sections></@sections>
                    </div>
                </#if>

                 <#if hasBackstory>
                    <#if hasSectionContent>
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>
                    <div itemprop="articleBody">
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
                            <div class="nhsd-m-contact-us__content" itemprop="articleBody">
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
                
                <div class="nhsd-!t-margin-bottom-6" itemprop="articleBody">
                    <#if hasRelatedSubjects || hasSectionContent && !hasBackstory && !hasContactDetails && !hasRelatedSubjects>
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

                <div class="nhsd-!t-margin-bottom-6" aria-label="document-content">
                    <@lastModified document.lastModified false></@lastModified>
                </div>
                
            </div>
        </div>
    </div>
</article>

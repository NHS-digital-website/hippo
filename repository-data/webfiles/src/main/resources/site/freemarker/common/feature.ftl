<#ftl output_format="HTML">

<#-- @ftlvariable name="feature" type="uk.nhs.digital.website.beans.feature" -->

<#include "../include/imports.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/personitem.ftl">
<#include "macro/contentPixel.ftl">

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
<#assign hasTwitterHashtag = document.twitterHashtag?? && document.twitterHashtag?has_content />
<#assign hasContactDetails = document.contactDetails?? && document.contactDetails.content?has_content />
<#assign hasRelatedSubjects = document.relatedSubjects?? && document.relatedSubjects?has_content />

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<article itemscope itemtype="http://schema.org/BlogPosting">
    <div class="grid-wrapper grid-wrapper--article grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
        <div class="local-header article-header--detailed">
            <div class="grid-wrapper">
                <div class="article-header__inner">

                     <div class="grid-row">
                        <div class="column--three-quarters column--reset">

                            <span class="article-header__label">Feature</span>

                            <h1 class="local-header__title" itemprop="headline" data-uipath="document.title">${document.title}</h1>

                            <div class="article-header__subtitle" data-uipath="website.feature.summary"><@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/></div>

                            <div class="detail-list-grid">

                                <#if hasAuthors || hasAuthorManualEntry>
                                    <div class="grid-row">
                                        <div class="column column--reset">
                                            <dl class="detail-list">
                                                <dt class="detail-list__key">Author:</dt>
                                                <dd class="detail-list__value">
                                                    <#if hasAuthors>
                                                        <#list document.authors as author>
                                                            <div class="blog-author" itemprop="author" itemscope itemtype="https://schema.org/Person">
                                                                <span itemprop="name">${author.title}</span><#if author.roles??><#if author.roles.primaryroles?has_content>, <span itemprop="jobtitle">${author.roles.firstprimaryrole}</span></#if><#if author.roles.primaryroleorg?has_content>, <span itemprop="worksfor" itemscope itemtype="https://schema.org/Organization"><span itemprop="name">${author.roles.primaryroleorg}</span></span></#if></#if>
                                                            </div>
                                                        </#list>
                                                    <#else>
                                                        <div itemprop="author" itemscope itemtype="https://schema.org/Person">
                                                            <#if document.authorName?has_content><span itemprop="name">${document.authorName}</span></#if><#if document.authorJobTitle?has_content>, <span itemprop="jobtitle">${document.authorJobTitle}</span></#if><#if document.authorRole?has_content>, <span itemprop="jobtitle">${document.authorRole}</span></#if><#if document.authorOrganisation?has_content>, <span itemprop="worksfor" itemscope itemtype="https://schema.org/Organization"><span itemprop="name">${document.authorOrganisation}</span></span></#if>
                                                        </div>
                                                    </#if>
                                                </dd>
                                            </dl>
                                        </div>
                                    </div>
                                </#if>


                                <div class="grid-row">
                                    <div class="column column--reset">
                                        <dl class="detail-list">
                                            <dt class="detail-list__key">Date:</dt>
                                            <dd class="detail-list__value" itemprop="datePublished" data-uipath="website.feature.dateofpublication">
                                                <@fmt.formatDate value=document.dateOfPublication.time type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" />

                                                <div itemprop="publisher" itemscope itemtype="https://schema.org/Organization">
                                                    <meta itemprop="name" content="NHS Digital">
                                                    <span itemprop="logo" itemscope itemtype="https://schema.org/ImageObject">
                                                        <meta itemprop="url" content="<@hst.webfile path='/images/nhs-digital-logo.svg' />" />
                                                    </span>
                                                </div>
                                            </dd>
                                        </dl>
                                    </div>
                                </div>

                                <#if hasTopics>
                                    <div class="grid-row">
                                        <div class="column column--reset">
                                            <dl class="detail-list">
                                                <dt class="detail-list__key">Topic:</dt>
                                                <dd class="detail-list__value" itemprop="keywords" data-uipath="website.feature.topics">
                                                    <#list document.topics as tag>${tag}<#sep>, </#list>
                                                </dd>
                                            </dl>
                                        </div>
                                    </div>
                                 </#if>

                                <#if hasFeatureCategories>
                                    <div class="grid-row">
                                         <div class="column column--reset">
                                             <dl class="detail-list">
                                                 <dt class="detail-list__key">Categories:</dt>
                                                 <dd class="detail-list__value" data-uipath="website.feature.categories">
                                                    <#list document.caseStudyCategories as category>${category}<#sep>, </#list>
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

    <div class="grid-wrapper grid-wrapper--article" aria-label="document-content">
        <div class="grid-row">

            <div class="column column--two-thirds page-block page-block--main">

                <#if hasLeadParagraph>
                    <div class="article-header" itemprop="articleBody" data-uipath="website.feature.leadparagraph">
                        <div class="lead-paragraph">
                            <@hst.html hippohtml=document.leadParagraph contentRewriter=gaContentRewriter/>
                        </div>
                    </div>
                </#if>

                <#if hasLeadImage>
                    <div class="lead-image-container" >
                        <div class="lead-image" itemprop="image" itemscope itemtype="http://schema.org/ImageObject">
                            <@hst.link hippobean=document.leadImage.original fullyQualified=true var="leadImage" />
                            <meta itemprop="url" content="${leadImage}">
                            <img itemprop="contentUrl" src="${leadImage}" alt="<#if hasLeadImageAltText>${document.leadImageAltText}</#if>" />
                        </div>
                        <#if hasLeadImageCaption>
                          <div class="lead-image-caption" data-uipath="website.feature.leadimagecaption">
                              <@hst.html hippohtml=document.leadImageCaption contentRewriter=gaContentRewriter/>
                          </div>
                        </#if>
                    </div>
                </#if>

                <#if hasSectionContent>
                    <div class="article-header" itemprop="articleBody">
                        <@sections document.sections></@sections>
                    </div>
                </#if>


                <#if hasBackstory>
                    <div class="article-header blog-backstory" itemprop="articleBody">
                        <h2>Back story</h2>
                        <div data-uipath="website.feature.backstory">
                            <@hst.html hippohtml=document.backstory contentRewriter=gaContentRewriter />
                        </div>
                    </div>
                </#if>


                <#if hasContactDetails>
                    <div class="article-header blog-contactus" itemprop="articleBody">
                        <h3>Contact us</h3>
                        <div data-uipath="website.feature.contactus">
                            <@hst.html hippohtml=document.contactDetails contentRewriter=gaContentRewriter/>
                        </div>
                    </div>
                </#if>


                <#if hasRelatedSubjects>
                    <div class="article-header">
                        <h2>Related subjects</h2>
                        <ul>
                            <#list document.relatedSubjects as item>
                                <li>
                                    <div itemprop="isBasedOn" itemscope itemtype="http://schema.org/Product">
                                        <a itemprop="url" class="cta__title cta__button" href="<@hst.link hippobean=item/>">${item.title}</a>
                                    </div>
                                    <div>
                                        ${item.shortsummary}
                                    </div>
                                </li>
                            </#list>
                        </ul>
                    </div>
                </#if>


                <div class="article-header" itemprop="articleBody">
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


                <#if hasAuthors>
                    <div class="blog-authors">
                        <#list document.authors as author>
                            <@personitem author />
                        </#list>
                    </div>
                </#if>

            </div>
        </div>
    </div>

    <div class="grid-wrapper grid-wrapper--article" aria-label="document-content">
        <div class="grid-row">
            <div class="article-section muted">
                <@lastModified document.lastModified false></@lastModified>
            </div>
        </div>
    </div>


</article>

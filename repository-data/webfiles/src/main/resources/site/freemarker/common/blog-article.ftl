<#ftl output_format="HTML">

<#-- @ftlvariable name="blog" type="uk.nhs.digital.website.beans.Blog" -->

<#include "../include/imports.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/sectionNav.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>


<#assign hasAuthors = document.authors?? && document.authors?has_content />
<#assign hasAuthorManualEntry = document.authorRole?? || (document.authorDescription?? && document.authorDescription?has_content) ||
                                document.authorName?? || document.authorJobTitle?? || document.authorOrganisation?? />
<#assign hasBlogCategories = document.blogCategories?? && document.blogCategories?has_content />
<#assign hasTaxonomyTags = document.taxonomyTags?? && document.taxonomyTags?has_content />

<#assign hasLeadImage = document.leadImage?has_content />
<#assign hasLeadImageAltText = document.leadImageAltText?has_content />
<#assign hasLeadImageCaption = document.leadImageCaption?? && document.leadImageCaption.content?has_content />
<#assign hasLeadParagraph = document.leadParagraph?? && document.leadParagraph.content?has_content />
<#assign hasSectionContent = document.sections?has_content />
<#assign hasBackstory = document.backstory?? && document.backstory.content?has_content />
<#assign hasTwitterHashtag = document.twitterHashtag?? && document.twitterHashtag?has_content />
<#assign hasContactDetails = document.contactDetails?? && document.contactDetails.content?has_content />
<#assign hasRelatedSubjects = document.relatedSubjects?? && document.relatedSubjects?has_content />
<#assign hasLatestBlogs = document.latestBlogs?? && document.latestBlogs?has_content />


<article itemscope itemtype="http://schema.org/BlogPosting">
    <div class="grid-wrapper grid-wrapper--article grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
        <div class="local-header article-header--detailed">
            <div class="grid-wrapper">
                <div class="article-header__inner">

                     <div class="grid-row">
                        <div class="column--two-thirds column--reset">

                            <span class="article-header__label">Blog</span>

                            <h1 class="local-header__title" itemprop="headline" data-uipath="document.title">${document.title}</h1>

                            <div class="article-header__subtitle" data-uipath="website.blog.summary"><@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/></div>

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
                                                                <span itemprop="name">${author.title}</span><#if author.roles??><#if author.roles.primaryrole?has_content>, <span itemprop="jobtitle">${author.roles.primaryrole}</span></#if><#if author.roles.primaryroleorg?has_content>, <span itemprop="worksfor" itemscope itemtype="https://schema.org/Organization"><span itemprop="name">${author.roles.primaryroleorg}</span></span></#if></#if>
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
                                            <dd class="detail-list__value" itemprop="datePublished" data-uipath="website.blog.dateofpublication">
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

                                <#if hasTaxonomyTags>
                                    <div class="grid-row">
                                        <div class="column column--reset">
                                            <dl class="detail-list">
                                                <dt class="detail-list__key">Topic:</dt>
                                                <dd class="detail-list__value" itemprop="keywords" data-uipath="website.blog.taxonomytags">
                                                    <#list document.taxonomyTags as tag>${tag}<#sep>, </#list>
                                                </dd>
                                            </dl>
                                        </div>
                                    </div>
                                 </#if>

                                <#if hasBlogCategories>
                                    <div class="grid-row">
                                         <div class="column column--reset">
                                             <dl class="detail-list">
                                                 <dt class="detail-list__key">Categories:</dt>
                                                 <dd class="detail-list__value" data-uipath="website.blog.categories">
                                                    <#list document.blogCategories as category>${category}<#sep>, </#list>
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
                    <div class="article-header" itemprop="articleBody" data-uipath="website.blog.leadparagraph">
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
                        <div class="lead-image-caption" data-uipath="website.blog.leadimagecaption">
                            <@hst.html hippohtml=document.leadImageCaption contentRewriter=gaContentRewriter/>
                        </div>
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
                        <div data-uipath="website.blog.backstory">
                            <@hst.html hippohtml=document.backstory contentRewriter=gaContentRewriter />
                        </div>
                    </div>
                </#if>


                <#if hasContactDetails>
                    <div class="article-header blog-contactus" itemprop="articleBody">
                        <h3>Contact us</h3>
                        <div data-uipath="website.blog.contactus">
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
                            <div class="blog-authors__item">

                                <div class="blog-authors__icon">
                                    <#if author.personimages?? && author.personimages?has_content>
                                        <@hst.link hippobean=author.personimages.picture.original fullyQualified=true var="authorPicture" />
                                        <img class="blog-authors__icon__img" src="${authorPicture}" alt="${author.title}" />
                                    <#else>
                                        <img class="blog-authors__icon__nhsimg" src="<@hst.webfile path="/images/fibre_57101102_med.jpg"/>" alt="NHS Digital blog">
                                    </#if>
                                </div>

                                <div class="blog-authors__content">
                                    <div class="blog-authors__title">
                                        <a class="cta__title cta__button" href="<@hst.link hippobean=author/>">${author.title}</a>
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

            </div>
        </div>
    </div>

    <div class="grid-wrapper grid-wrapper--article" aria-label="document-content">
        <div class="grid-row">

                <#if hasLatestBlogs>
                    <div class="latestBlog">
                        <h2>Latest blogs</h2>

                        <#list document.latestBlogs as latest>
                            <div class="latestBlog__item">
                                <div class="latestBlog__icon">
                                    <#if latest.leadImage??>
                                        <@hst.link hippobean=latest.leadImage.original fullyQualified=true var="leadImage" />
                                        <img class="latestBlog__icon__img" src="${leadImage}" alt="<#if hasLeadImageAltText>${latest.leadImageAltText}</#if>" />
                                    <#else>
                                        <img class="latestBlog__icon__nhsimg" src="<@hst.webfile path="/images/fibre_57101102_med.jpg"/>" alt="NHS Digital blog" >
                                    </#if>
                                </div>

                                <div class="latestBlog__content">
                                    <div class="latestBlog__title"><a class="cta__title cta__button" title="${latest.title}" href="<@hst.link hippobean=latest/>">${latest.title}</a></div>
                                        <div class="latestBlog__author">
                                            <#if latest.authors?? && latest.authors?has_content>
                                                By <#list latest.authors as author>${author.title}<#sep>, </#list>.
                                            </#if>
                                            <@fmt.formatDate value=latest.dateOfPublication.time type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" />
                                        </div>
                                    <div class="latestBlog__body">${latest.shortsummary}</div>
                                </div>
                            </div>
                        </#list>
                    </div>
                </#if>

                <div class="article-section muted">
                    <@lastModified document.lastModified false></@lastModified>
                </div>
        </div>
    </div>


</article>

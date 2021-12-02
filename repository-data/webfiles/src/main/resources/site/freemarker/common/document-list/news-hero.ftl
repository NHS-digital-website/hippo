<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../macro/newsHeroItem.ftl">

<div class="grid-row news-hero">
    <div class="column column--reset">
        <#if pageable?? && pageable.items?has_content>
            <#list pageable.items as item>

                <#assign hasVideo = false />
                <#if item.class.name == 'uk.nhs.digital.website.beans.Video'>
                    <#assign hasVideo = true />
                </#if>

                <#assign hasLeadImage = item.leadimagesection?has_content && item.leadimagesection.leadImage?has_content/>
                <#if hasLeadImage>
                    <#assign image = item.leadimagesection.leadImage />
                    <#assign alttext = item.leadimagesection.alttext />
                </#if>

                <#assign pubtime = '' />
                <#if item.dateOfPublication?has_content>
                    <#assign pubtime = item.dateOfPublication.time />
                <#elseif item.publisheddatetime?has_content>
                    <#assign pubtime = item.publisheddatetime.time />
                </#if>

                <div class="news-hero__item">
                    <a href="#"
                       class="media-text-hero media-text-hero--post media-text-hero--image">
                        <figure class="media-text-hero__media">
                            <#if hasVideo == true && item.videoUri?has_content>
                                <div class="iframe-wrapper iframe-wrapper--16-9">
                                    <iframe id="ytplayer" src="${item.videoUri}" allow="autoplay; encrypted-media; picture-in-picture" allowfullscreen></iframe>
                                    <link itemprop="embedUrl" href="${item.videoUri}" />
                                </div>
                            <#elseif image?? && image?has_content>
                                <@hst.link hippobean=image.original fullyQualified=true var="leadImage" />
                                <img src="${leadImage}" alt="${alttext}">
                            <#else>
                                <img src="<@hst.webfile path="/images/fibre_57101102_med.jpg"/>" alt="NHS Digital article" />
                            </#if>
                        </figure>
                        <div class="media-text-hero__content media-text-hero--post__content">
                            <div class="media-text-hero__content-items">
                                <#if pubtime?? && pubtime?has_content>
                                <#--shema:datePublished-->
                                    <@fmt.formatDate value=pubtime type="Date" pattern="d MMMM yyyy" var="publishedDateTimeString" timeZone="${getTimeZone()}" />
                                    <@fmt.formatDate value=pubtime type="Date" pattern="yyyy-MM-dd HH:mm:ss" var="publishedDateTime" timeZone="${getTimeZone()}" />
                                    <time class="media-text-hero--post__date" datetime="${publishedDateTime}">${publishedDateTimeString}</time>
                                </#if>
                                <h2 class="media-text-hero__title">${item.title}</h2>
                                <#if item.shortsummary??>
                                    <p class="media-text-hero__body">${item.shortsummary}</p>
                                </#if>
                                <div class="nhsd-icon-right-arrow"
                                     role="presentation"></div>
                            </div>
                        </div>
                    </a>
                </div>
            </#list>
        </#if>
    </div>
</div>

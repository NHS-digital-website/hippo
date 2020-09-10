<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<@hst.setBundle basename="homepage.intranet.labels"/>

<#if pageable?? && pageable.items?has_content>
    <div class="homeland-blog">
        <div class="article-section__header">
            <h2>Latest Blogs</h2>
        </div>

        <div class="grid-row">

            <div class="column column--reset">
                <#list pageable.items as item>

                    <div class="homeland-blog__item filter-list__item" itemprop="blogPost" itemscope itemtype="http://schema.org/BlogPosting">
                        <div class="homeland-blog__item__icon" itemprop="image" itemscope itemtype="http://schema.org/ImageObject">
                            <#if item.leadImage??>
                                <@hst.link hippobean=item.leadImage.original fullyQualified=true var="leadImage" />
                                <meta itemprop="url" content="${leadImage}">
                                <img itemprop="contentUrl" class="homeland-blog__icon__img" src="${leadImage}" alt="${item.title}" />
                            <#else>
                                <meta itemprop="url" content="<@hst.webfile path="/images/fibre_57101102_med.jpg"/>" />
                                <img itemprop="contentUrl" src="<@hst.webfile path="/images/fibre_57101102_med.jpg"/>" alt="Default Blog Hub Image" class="latestBlog__icon__img">
                            </#if>
                        </div>

                        <div class="homeland-blog__item__content">
                            <div itemprop="headline" class="homeland-blog__item__content__title"><a class="cta__title cta__button" href="<@hst.link hippobean=item/>">${item.title}</a>
                            </div>

                            <div class="homeland-blog__item__content__date" itemprop="datePublished">
                                <@fmt.formatDate value=item.dateOfPublication.time type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" />
                            </div>
                        </div>
                    </div>
                </#list>
            </div>
        </div>
    </div>
</#if>

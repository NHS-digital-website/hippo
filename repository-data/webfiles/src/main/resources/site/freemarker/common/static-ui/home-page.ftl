<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#include "shared/search-banner.ftl"/>

<div class="grid-wrapper grid-wrapper--article">
    <#-- [FTL-BEGIN] Data and information section -->
    <div class="article-section article-section--data-and-information article-section--padded">
        <div class="article-section__header">
            <h2>Data and information</h2>
        </div>

        <div class="article-section__contents">
            <#include "home-page/large-cta-box.ftl"/>
            <#include "home-page/latest-publications-box.ftl"/>
        </div>
    </div>
    <#-- [FTL-END] Data and information section -->

    <#-- [FTL-BEGIN] Systems & Services section -->
    <div class="article-section">
        <div class="article-section__header">
            <h2>Systems and services </h2>
            <p>We are the national provider of information, data and IT systems for commissioners, analysts and clinicians in health and social care.</p>
        </div>

        <div class="article-section__contents">
            <#include "home-page/popular-services-box.ftl"/>

            <div class="grid-row">
                <a href="#" class="button">View all services</a>
            </div>
        </div>

    </div>
    <#-- [FTL-END] Systems & Services section -->

    <#-- [FTL-BEGIN] About us section -->
    <div class="article-section article-section--about-us">
        <div class="article-section__header">
            <h2>About us</h2>
            <p>We're the national information and technology partner to the health and social care system. We're using digital technology to transform the NHS and social care.</p>
        </div>

        <div class="article-section__contents">
            <#include "home-page/about-us-section-contents.ftl"/>
        </div>
    </div>
    <#-- [FTL-END] About us section -->

    <div class="article-section article-section--padded no-border">
        <#include "home-page/information-box.ftl"/>
    </div>
</div>

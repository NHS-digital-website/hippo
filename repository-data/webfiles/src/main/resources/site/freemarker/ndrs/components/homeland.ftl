<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="homepage.website.labels"/>
<@fmt.message key="data-and-information.title" var="dataAndInformationSectionTitle"/>

<div>
  <@hst.include ref="top"/>
</div>

<div class="page-block">
    <div class="grid-wrapper grid-wrapper--article">

        <#-- [FTL-BEGIN] 'Blog latest' section -->
            <@hst.include ref="blog"/>
        <#-- [FTL-END] 'Blog latest' section -->


        <#-- [FTL-BEGIN] 'Data and information' section -->
        <div class="article-section--data-and-information">
            <div class="article-section__contents">
                <@hst.include ref="center"/>
            </div>
        </div>
        <#-- [FTL-END] 'Data and information' section -->


        <#-- [FTL-BEGIN] 'Systems and Services' section -->
        <div class="article-section">
            <div class="article-section__contents">
                <@hst.include ref="center-2"/>
            </div>
        </div>
        <#-- [FTL-END] 'Systems and Services' section -->


        <#-- [FTL-BEGIN] 'About us' section -->
        <div class="article-section article-section--about-us">
            <div class="article-section__header">
                <@hst.include ref="bottom-intro"/>
            </div>

            <div class="article-section__contents">
                <div class="grid-row">
                    <div class="column column--one-third column--left">
                        <@hst.include ref="bottom-left"/>
                    </div>
                    <div class="column column--one-third column--center">
                        <@hst.include ref="bottom-center"/>
                    </div>
                    <div class="column column--one-third column--right">
                        <@hst.include ref="bottom-right"/>
                    </div>
                </div>
            </div>
        </div>
        <#-- [FTL-END] 'About us' section -->


        <#-- [FTL-BEGIN] 'Information box' section -->
        <div class="article-section article-section--reset-top">
            <div class="article-section__contents">
                <@hst.include ref="information"/>
            </div>
        </div>
        <#-- [FTL-END] 'Information box' section -->

    </div>
</div>

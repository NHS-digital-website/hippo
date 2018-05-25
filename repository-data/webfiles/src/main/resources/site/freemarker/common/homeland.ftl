<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="homepage.website.labels"/>
<@fmt.message key="data-and-information.title" var="dataAndInformationSectionTitle"/>
<@fmt.message key="about-us.title" var="aboutUsSectionTitle"/>
<@fmt.message key="about-us.text" var="aboutUsSectionText"/>
<@fmt.message key="systems-and-services.title" var="systemsAndServicesTitle"/>
<@fmt.message key="systems-and-services.text" var="systemsAndServicesText"/>

<div>
  <@hst.include ref="top"/>
</div>

<div class="page-block">
    <div class="grid-wrapper grid-wrapper--article">
        <#-- [FTL-BEGIN] 'Data and information' section -->
        <div class="article-section article-section--data-and-information article-section--padded">
            <div class="article-section__header">
                <h2>${dataAndInformationSectionTitle}</h2>
            </div>
            
            <div class="article-section__contents">
                <@hst.include ref="center"/>
            </div>
        </div>
        <#-- [FTL-END] 'Data and information' section -->

        <#-- [FTL-BEGIN] 'Systems and Services' section -->
        <div class="article-section">
            <div class="article-section__header">
                <h2>${systemsAndServicesTitle}</h2>
                <p>${systemsAndServicesText}</p>
            </div>

            <div class="article-section__contents">
                <@hst.include ref="center-2"/>
            </div>
        </div>
        <#-- [FTL-END] 'Systems and Services' section -->

        <#-- [FTL-BEGIN] 'About us' section -->
        <div class="article-section article-section--about-us">
            <div class="article-section__header">
                <h2>${aboutUsSectionTitle}</h2>
                <p>${aboutUsSectionText}</p>
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

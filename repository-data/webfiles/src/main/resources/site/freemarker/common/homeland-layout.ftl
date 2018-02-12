<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<@hst.setBundle basename="homepage.website.labels"/>
<@fmt.message key="data-and-information.title" var="dataAndInformationSectionTitle"/>
<@fmt.message key="about-us.title" var="aboutUsSectionTitle"/>
<@fmt.message key="about-us.text" var="aboutUsSectionText"/>
<@fmt.message key="systems-and-services.title" var="systemsAndServicesTitle"/>
<@fmt.message key="systems-and-services.text" var="systemsAndServicesText"/>

<header role="banner">
  <@hst.include ref="top"/>
</header>

<main>
    <div class="page-block">
        <div class="grid-wrapper grid-wrapper--article">
            <#-- Data and information section -->
            <div class="article-section article-section--data-and-information">
                <div class="article-section__header">
                    <h2>${dataAndInformationSectionTitle}</h2>
                </div>
                <div class="article-section__contents">
                    <@hst.include ref="center"/>
                </div>
            </div>

            <#-- Systems and Services section -->
            <div class="article-section article-section--systems-and-services">
                <div class="article-section__header">
                    <h2>${systemsAndServicesTitle}</h2>
                    <p>${systemsAndServicesText}</p>
                </div>
                <@hst.include ref="center-2"/>
            </div>

            <#-- About us section -->
            <div class="article-section article-section--about-us">
                <div class="article-section__header">
                    <h2>${aboutUsSectionTitle}</h2>
                    <p>${aboutUsSectionText}</p>
                </div>

                <div class="article-section__contents">
                    <#-- <@hst.include ref="bottom-header"/> -->
                    <div class="grid-row list-of-articles list-of-articles--3-columns">
                        <div class="column column--one-third article-section__column article-section__column--left">
                            <@hst.include ref="bottom-left"/>
                        </div>
                        <div class="column column--one-third article-section__column article-section__column--center">
                            <@hst.include ref="bottom-center"/>
                        </div>
                        <div class="column column--one-third article-section__column article-section__column--right">
                            <@hst.include ref="bottom-right"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

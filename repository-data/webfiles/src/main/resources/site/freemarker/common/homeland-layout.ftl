<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<@hst.setBundle basename="homepage.website.labels"/>
<@fmt.message key="data-and-information.title" var="dataAndInformationSectionTitle"/>

<header role="banner">
  <@hst.include ref="top"/>
</header>

<main>
    <div class="grid-wrapper grid-wrapper--article">
        <div class="article-section article-section--data-and-information">
            <h2 class="article-section__title">${dataAndInformationSectionTitle}</h2>
            <@hst.include ref="center"/>
        </div>

        <div class="article-section">
            <@hst.include ref="bottom-header"/>
            <div class="column column--one-third">
                <@hst.include ref="bottom-left"/>
            </div>
            <div class="column column--one-third">
                <@hst.include ref="bottom-center"/>
            </div>
            <div class="column column--one-third">
                <@hst.include ref="bottom-right"/>
            </div>
        </div>

    </div>
</main>

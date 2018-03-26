<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/structured-text.ftl">
<@hst.setBundle basename="publicationsystem.headers"/>

<h2>template: cihub.ftl</h2>

<main>
    <div class="page-block">
        <div class="grid-wrapper grid-wrapper--article">

            <section class="document-content">
                <#if document??>
                    <h1 data-uipath="ps.document.title">${document.title}</h1>
                    <p data-uipath="ps.document.content">
                        <@structuredText item=document.summary uipath="ps.document.summary" />
                    </p>
                    <h2><@fmt.message key="headers.ci-hub-overview"/></h2>


                    <!-- MARKUP -->
                    <!--
                    <div class="grid-row">
                        <div class="column column--one-half column--left">
                            <div class="cta cta--large">
                                <h3 class="cta__title">Col 1</h3>
                                <p class="cta__text">Col 1 Text</p>
                                <a href="#" class="cta__button button">Col 1 button</a>
                            </div>
                        </div>
                        <div class="column column--one-half column--right">
                            <div class="cta cta--large">
                                <h3 class="cta__title">Col 2</h3>
                                <p class="cta__text">Col 2 TextCol 2 TextCol 2 TextCol 2 TextCol 2 TextCol 2 TextCol 2 TextCol 2 TextCol 2 TextCol 2 TextCol 2 TextTextCol 2 TextCol 2 TextCol 2 TextCol 2 TextCol 2 TextCol 2 TextCol 2 TextCol 2 Text</p>
                                <a href="#" class="cta__button button">Col 2 button</a>
                            </div>
                        </div>

                        <div class="column column--one-half column--left">
                            <div class="cta cta--large">
                                <h3 class="cta__title">Col 1</h3>
                                <p class="cta__text">Col 1 Text</p>
                                <a href="#" class="cta__button button">Col 1 button</a>
                            </div>
                        </div>

                        <div class="column column--one-half column--right">
                            <div class="cta cta--large">
                                <h3 class="cta__title">Col 2</h3>
                                <p class="cta__text">Col 2 Text</p>
                                <a href="#" class="cta__button button">Col 2 button</a>
                            </div>
                        </div>
                    </div>
                    -->

                    <!-- TEMPLATE -->


                    <div class="grid-row">

                        <#function zebra index>
                          <#if (index % 2) == 0>
                            <#return "column--left" />

                          <#else>
                            <#return "column--right" />
                          </#if>
                        </#function>

                        <#list document.ciHubLink as link>

                            <div class="column column--one-half ${zebra(link_index)}">
                                <div class="cta cta--large">
                                    <@hst.link var="landingPageLink" hippobean=link.pageLink />
                                    <h4>${link.title}</h4>
                                    <p>${link.summary}</p>
                                    <a href="${landingPageLink}" title="${link.title}" class="button">See all data and publications</a>
                                </div>
                            </div>
                        </#list>
                    </div>
                </#if>
            </section>

        </div>
    </div>
</main>

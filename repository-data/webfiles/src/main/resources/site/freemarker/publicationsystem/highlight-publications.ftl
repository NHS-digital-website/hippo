<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "../common/macro/sectionNav.ftl">
<@hst.setBundle basename="publicationsystem.labels,publicationsystem.headers"/>
<#-- Add meta tags -->
<#include "../common/macro/metaTags.ftl">
<@metaTags></@metaTags>


<div class="grid-wrapper grid-wrapper--article <#if document??>article-section</#if>" aria-label="Document Content">
    <div class="grid-row">
        <div class="column column--one-third page-block page-block--sidebar sticky sticky--top">
            <#assign links = [{ "url": "#recently-published", "title": "Recently published" }, { "url": "#future-publications", "title": "Future publications" },{ "url": "#related-areas", "title": "Related Areas" },{ "url": "#freedom-of-information", "title": "Freedom of Information" }] />
            <@sectionNav links></@sectionNav>
        </div>

        <div class="column column--two-thirds page-block page-block--main">

            <#if document??>

                <div class="article-section" id="recently-published">

                    <#list document.highlightSections as section>

                        <h2>Published <@formatDate date=section.publishedDate.time/></h2>

                        <#list section.highlightTiles as tile>

                            <div style="float:left;width:25%; margin-right: 40px">
                                <#if tile.image?has_content>
                                    <img src="<@hst.link hippobean=tile.image/>"
                                         alt=""
                                         data-uipath="ps.publication.image-section.image"
                                         style="width:100%"
                                    />
                                </#if>

                                <#if tile.publicationLink?has_content>
                                    <a href="<@hst.link hippobean=tile.publicationLink />">
                                        ${tile.publicationLink.referencedBean.displayName}
                                    </a>
                                </#if>

                                <br />

                                <#if tile.summary?has_content>
                                    ${tile.summary}
                                </#if>
                            </div>

                        </#list>

                    </#list>

                </div>

                <br style="clear:both" />

                <#if document.relatedAreas?has_content>
                    <div class="article-section" id="related-areas">
                        <h2>Related Areas</h2>
                        <p itemprop="isBasedOn" data-uipath="ps.publication.administrative-sources">
                            <@hst.html hippohtml=document.relatedAreas />
                        </p>
                    </div>
                </#if>

                <#if document.freedomOfInformation?has_content>
                    <div class="article-section" id="freedom-of-information">
                        <h2>Freedom of information</h2>
                        <p itemprop="isBasedOn" data-uipath="ps.publication.administrative-sources">
                            <@hst.html hippohtml=document.freedomOfInformation />
                        </p>
                    </div>
                </#if>

            </#if>

        </div>
    </div>
</div>

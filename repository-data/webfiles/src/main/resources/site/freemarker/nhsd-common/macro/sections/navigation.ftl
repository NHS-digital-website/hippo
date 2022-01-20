<#ftl output_format="HTML">
<#include "navigationTile.ftl">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.Navigation" -->
<#macro navigation section>
    <#local imageType = section.imageType />
    <#local tileType = section.columnAlignment />

    <#local headingClass = (section.headingLevel?has_content && section.headingLevel == 'Main heading')?then('article-section', 'article-header__detail-lines') />

    <div ${section.heading?has_content?then('id="' + slugify(section.heading) + '"', '')}>
        <#if section.heading?has_content>
            <#if section.headingLevel?has_content && section.headingLevel == 'Main heading'>
                <p class="nhsd-t-heading-l">${section.title}</p>
            <#else>
                <p class="nhsd-t-heading-s">${section.title}</p>
            </#if>
        </#if>

        <#if section.sectionIntroduction?has_content>
            <@hst.html hippohtml=section.sectionIntroduction contentRewriter=brContentRewriter />
        </#if>

        <div class="nhsd-o-nav-block-list">
            <div class="nhsd-t-grid nhsd-t-grid--nested">
                <div class="nhsd-t-row nhsd-o-nav-block-list__items nhsd-t-row--centred">
                    <#list section.navigationTiles as tile>
                        <div class="nhsd-t-col-xs-12
                            <#if section.navigationTiles?size gt 1>
                                nhsd-t-col-l-6
                            </#if>">
                            <@navigationTile tile tileType imageType />
                        </div>
                    </#list>
                </div>
            </div>
        </div>
    </div>
</#macro>

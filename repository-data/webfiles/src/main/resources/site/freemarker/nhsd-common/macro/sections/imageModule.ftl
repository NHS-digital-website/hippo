<#ftl output_format="HTML">

<#include "../../../include/imports.ftl">
<#include "../heroes/hero-options.ftl">
<#include "../heroes/hero.ftl">

<#macro imageModule section>
    <@hst.link var="imgName" hippobean=section.image/>

    <#if section.imageType?contains("hero image")>
        <#assign heroOptions = getHeroOptions(document) />
        <#assign heroOptions += {
        "image": {"src": imgName, "alt": "${section.altText}"},
        "introText": "",
        "title": "",
        "summary": section.text,
        "inline": "yes",
        "colourBar": 1 == 0,
        "caption": section.caption
        }/>

        <#if section.imageType?contains("left")>
            <#assign heroType = "imageMirrored"/>
        <#else>
            <#assign heroType = "image"/>
        </#if>

        <#if section.imageType?contains("Blue hero image")>
            <#assign heroOptions += {
            "colour": "Dark blue"
            }/>
        <#elseif section.imageType?contains("Black hero image")>
            <#assign heroOptions += {
            "colour": "Black"
            }/>
        </#if>

        </div></div></div>
        <@hero heroOptions heroType />
        <div class="nhsd-t-grid">
        <div class="nhsd-t-row">
        <div class="nhsd-t-col-xs-12 nhsd-t-col-s-8 nhsd-t-off-s-2 nhsd-t-coll-m-8 nhs-t-off-m-2 nhsd-t-col-l-8 nhsd-t-off-l-2 nhsd-t-col-xl-6 nhsd-t-off-xl-3">
    <#else>
        <#assign figureStyle = "width: 100%;"/>
        <#assign imageStyle = "width: 100%"/>

        <#if section.imageType == 'Half width in line'>
            <#assign figureStyle = "float: left; width: 50%"/>
            <#assign imageStyle = ""/>
        <#elseif section.imageType == 'Full width'>
            </div></div></div>
        </#if>

        <div style="margin-top: 12px;">
        <#if section.imageType == 'Right column' && (document.docType == "Feature" || document.docType == "Blog")>
            <div style = "width: 50%; float: right; padding-right: 10px;">
        <#elseif section.imageType == 'Left column' && (document.docType == "Feature" || document.docType == "Blog")>
            <div style = "width: 50%; float: left; padding-left: 10px;">
        </#if>

        <figure class="nhsd-a-image nhsd-a-image--no-scale nhsd-a-image--round-corners nhsd-!t-margin-bottom-2" aria-hidden="true" style="${figureStyle}">
            <img style="${imageStyle}" src="${imgName}" alt="${section.altText}" data-uipath="website.image-module.image"/>
            <#if section.caption?has_content>
                <figcaption style="text-align:center">
                    <div class="nhsd-t-heading-s">
                        <@hst.html hippohtml=section.caption contentRewriter=brContentRewriter/>
                    </div>
                </figcaption>
            </#if>
        </figure>

    <#if (section.imageType == 'Right column' || section.imageType == 'Left column') && (document.docType == "Feature" || document.docType == "Blog")>
        </div>
        <div style="width: 50%; float: left; padding-left: 10px;">
            <@hst.html hippohtml=section.text contentRewriter=brContentRewriter/>
        </div>
    </#if>
        </div>

        <div style="clear: both; width: 100%">&nbsp;</div> <#-- clear float so the next section does not wrap around the image -->
        <#if section.imageType == 'Full width'>
            <div class="nhsd-t-grid">
            <div class="nhsd-t-row">
            <div class="nhsd-t-col-xs-12 nhsd-t-col-s-8 nhsd-t-off-s-2 nhsd-t-coll-m-8 nhs-t-off-m-2 nhsd-t-col-l-8 nhsd-t-off-l-2 nhsd-t-col-xl-6 nhsd-t-off-xl-3">
        </#if>
    </#if>
</#macro>
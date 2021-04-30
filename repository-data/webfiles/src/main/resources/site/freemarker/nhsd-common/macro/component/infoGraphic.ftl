<#ftl output_format="HTML">
<#include "../../../include/imports.ftl">

<#macro infoGraphic graphic>

    <#assign selectedColour = "" />
    <#assign selectedColour = graphic.colour?trim />

    <#--  Handle colours from old brand  -->
    <#if selectedColour == "White">
        <#assign selectedColour = "Grey" />
    <#elseif selectedColour == "Light blue">
        <#assign selectedColour = "Yellow" />
    </#if>


    <div class="nhsd-m-infographic nhsd-!t-margin-bottom-6">
        <div class="nhsd-a-box nhsd-a-box--bg-light-${slugify(selectedColour)}">

            <div class="nhsd-m-infographic__icon-box">
                <span class="nhsd-a-icon nhsd-a-icon--size-xxl">
                    <@hst.link hippobean=graphic.icon var="imagePath" />
                    <#if (imagePath)??>
                    <div class="infographic__icon">
                        <#if imagePath?ends_with("svg")>
                        <img src="${imagePath?replace("/binaries", "/svg-magic/binaries")}?colour=231f20" alt="Image for infographic ${graphic.headline}" width="100" height="100" />
                        <#else>
                        <img aria-hidden="true" src="${imagePath}" alt="Image for infographic ${graphic.headline}" width="100" height="100" />
                        </#if>
                    </div>
                    </#if>
                </span>
            </div>

            <div class="nhsd-m-infographic__headline-box">
                <p class="nhsd-t-heading-l nhsd-t-word-break">
                    ${graphic.headline}
                </p>
            </div>
            <#if graphic.explanatoryLine?has_content>
                <div class="nhsd-m-infographic__explanatory-line-box">
                    <p class="nhsd-t-heading-m nhsd-t-word-break">
                        <@hst.html hippohtml=graphic.explanatoryLine contentRewriter=stripTagsWithLinksContentRewriter/>
                    </p>
                </div>
            </#if>
            <#if graphic.qualifyingInformation?has_content>
                <div class="nhsd-m-infographic__qualifying-info-box">
                    <div class="nhsd-t-word-break">
                        <@hst.html hippohtml=graphic.qualifyingInformation contentRewriter=brContentRewriter/>
                    </div>
                </div>
            </#if>
        </div>
    </div>
</#macro>

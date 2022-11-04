<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/gridColumnGenerator.ftl">
<#include "macro/navigationBlockSmall.ftl">
<#include "macro/navigationBlockLarge.ftl">
<#include "macro/digiBlock.ftl">

<@hst.setBundle basename="rb.generic.texts"/>
<@fmt.message key="text.sr-only-link" var="srOnlyLinkText" />

<#-- Document -->
<#assign hasDocuments = documents?has_content />

<#-- Theme -->
<#assign lightGrey = "light-grey", darkGrey = "dark-grey",
black = "black", yellow = "yellow", blue = "blue", darkBlue = "dark-blue" />

<#assign darkVariants = [darkGrey, black, blue, darkBlue] />

<#if theme == "colourful">
    <#assign themeColourVariants = [blue, yellow, black, darkBlue, blue, yellow] />
<#elseif theme == "colourless">
    <#assign themeColourVariants = [lightGrey, lightGrey, lightGrey, lightGrey, lightGrey, lightGrey] />
<#else>
    <#assign themeColourVariants = [lightGrey, darkGrey, black, lightGrey, darkGrey, black] />
</#if>

<#-- Size -->
<#assign isSmallBlock = size == "small" />

<#-- Position -->
<#assign positions = { "bottom-left":"bl", "top-left":"tl", "top-right":"tr", "bottom-right":"br" } />

<#-- Heading -->
<#assign hasHeading = heading?has_content />

<#if hasDocuments>
    <div class="nhsd-!t-margin-bottom-9">
        <div class="nhsd-t-grid">
            <#if hasHeading>
                <div class="nhsd-t-row">
                    <div class="nhsd-t-col">
                        <h2 class="nhsd-t-heading-xl nhsd-t-text-align-center nhsd-!t-margin-bottom-7">${heading}</h2>
                    </div>
                </div>
            </#if>

            <div class="nhsd-t-row nhsd-t-row--centred">
                <#list documents as item>
                    <#assign hasTitle = item.title?has_content />
                    <#assign hasContent = item.content?has_content />
                    <#assign hasImage = item.icon?has_content />
                    <#assign hasLink = item.external?has_content || item.internal?has_content />
                    <#assign hasLabel = item.label?has_content />
                    <#assign label = hasLabel?then(item.label, item.title) />

                    <#-- Theme -->
                    <#assign colourVariant = themeColourVariants[item?index] />
                    <#assign isYellowLink = (colourVariant == yellow)?then("nhsd-a-box-link--focus-orange", "") />
                    <#assign isDarkMolecule = darkVariants?seq_contains(colourVariant)?then(isSmallBlock?then("nhsd-m-card--light-text", "nhsd-m-nav-block--light-text"), "") />
                    <#assign isDarkButton = darkVariants?seq_contains(colourVariant)?then("nhsd-a-button--invert", "") />

                    <div class="nhsd-t-col-xs-12 ${getGridCol(documents?size, "large")} nhsd-!t-margin-bottom-6">
                        <#if isSmallBlock>
                            <#assign id = "navigation-small-block-${item?index + 1}" />
                            <@navigationBlockSmall
                                item
                                id
                                colourVariant
                                isDarkMolecule
                                isYellowLink
                                hasHeading/>
                        <#else>
                            <#assign id = "navigation-large-block-${item?index + 1}" />
                            <@navigationBlockLarge
                                item
                                id
                                colourVariant
                                isDarkMolecule
                                isYellowLink
                                isDarkButton
                                positions[position]
                                hasHeading
                                { "fullHeight": true }/>
                        </#if>
                    </div>
                </#list>
            </div>
        </div>
    </div>
</#if>

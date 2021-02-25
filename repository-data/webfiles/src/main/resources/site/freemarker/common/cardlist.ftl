<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include './macro/cardItem.ftl'>
<#include './macro/docGetImage.ftl'>
<#include 'macro/gridColumnGenerator.ftl'>
<#setting date_format="dd MMMM yyyy">

<#assign cards = 0 />
<#list cardList as card>
    <#assign cards = card?has_content?then(cards + 1, cards)>
</#list>

<#assign templateClass = "" />
<#assign cardColour = "pale-grey" />
<#if template == "Grey">
    <#assign templateClass = "nhsd-!t-bg-pale-grey-40-tint" />
    <#assign cardColour = "" />
</#if>

<#if template?has_content>
    <div class=" ${templateClass} nhsd-!t-padding-4 nhsd-!t-margin-bottom-9"><div class="nhsd-t-grid">
<#else>
    <div class="nhsd-t-grid nhsd-!t-margin-bottom-9">
</#if>
        <div class="nhsd-t-row nhsd-t-row--centred">
            <div class="nhsd-t-col-8 nhsd-t-text-align-center">
                <#if headerText?has_content>
                    <h2 class="nhsd-t-heading-xl">${headerText}</h2>
                </#if>
                <#if introText?has_content>
                    <p class="nhsd-t-heading-s nhsd-!t-margin-bottom-4">${introText}</p>
                </#if>
            </div>
        </div>
        <div class="nhsd-t-row">
            <#list cardList as card>
                <#assign imageData = getImageData(card) />

                <#assign itemShortSummary = "" />
                <#if card.shortsummary?has_content>
                    <#assign itemShortSummary = card.shortsummary />
                <#elseif card.content?has_content>
                    <#assign itemShortSummary = card.content />
                </#if>

                <#assign linkDestination = "#" />
                <#if card.internal?has_content>
                    <@hst.link hippobean=card.internal var="itemLinkDestination"/>
                    <#assign linkDestination = itemLinkDestination />
                <#elseif card.external?has_content>
                    <#assign linkDestination = card.external/>
                <#else>
                    <@hst.link hippobean=card var="itemLinkDestination"/>
                    <#assign linkDestination = itemLinkDestination />
                </#if>

                <#assign cardProps = card/>
                <#assign cardProps += {
                    "image": imageData[0],
                    "alttext": imageData[1],
                    "shortsummary": itemShortSummary,
                    "link": linkDestination,
                    "background": cardColour,
                    "cardClass": "nhsd-m-card--full-height"
                }/>

                <div class="${getGridCol(cards)} nhsd-!t-margin-bottom-4">
                    <@cardItem cardProps />
                </div>
            </#list>
        </div>
        <#if button1Title?has_content && button2Title?has_content>
            <nav class="nhsd-m-button-nav">
                <div class="nhsd-t-grid">
                    <div class="nhsd-t-row">
                        <#if button1Title?has_content>
                            <div class="nhsd-t-col-6 nhsd-!t-text-align-s-right nhsd-!t-text-align-m-right nhsd-!t-text-align-l-right nhsd-!t-text-align-xl-right">
                                <a class="nhsd-a-button"
                                   href="${button1Url}">
                                    <span class="nhsd-a-button__label">${button1Title}</span>
                                </a>
                            </div>
                        </#if>
                        <#if button2Title?has_content>
                            <div class="nhsd-t-col-6 nhsd-!t-text-align-s-left nhsd-!t-text-align-m-left nhsd-!t-text-align-l-left nhsd-!t-text-align-xl-left">
                                <a class="nhsd-a-button nhsd-a-button--outline"
                                   href="${button2Url}">
                                    <span class="nhsd-a-button__label">${button2Title}</span>
                                </a>
                            </div>
                        </#if>
                    </div>
                </div>
            </nav>
        <#elseif button1Title?has_content || button2Title?has_content>
            <nav class="nhsd-m-button-nav">
                <div class="nhsd-t-grid">
                    <div class="nhsd-t-row">
                        <div class="nhsd-t-col nhsd-!t-text-align-m-center nhsd-!t-text-align-l-center">
                            <a class="nhsd-a-button" href="${button1Title?has_content?then(button1Url,button2Url)}">
                                <span class="nhsd-a-button__label">${button1Title?has_content?then(button1Title,button2Title)}</span>
                            </a>
                        </div>
                    </div>
                </div>
            </nav>
        </#if>
    </div>
<#if template?has_content>
    </div>
</#if>

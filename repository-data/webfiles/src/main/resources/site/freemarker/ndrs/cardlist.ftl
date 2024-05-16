<#ftl output_format="HTML">
<#include "./include/imports.ftl">
<#include './macro/cardItem.ftl'>
<#include './macro/docGetImage.ftl'>
<#include './macro/gridColumnGenerator.ftl'>
<#setting date_format="dd MMMM yyyy">

<#assign cards = 0 />
<#list cardList as card>
    <#assign cards = card?has_content?then(cards + 1, cards)>
</#list>

<#assign templateClass = "" />
<#assign rowClass = "" />

<#assign cardColour = "pale-grey" />
<#if template == "Grey">
    <#assign templateClass = "nhsd-!t-bg-pale-grey-40-tint gray-color-cart-list" />
    <#assign cardColour = "" />
</#if>
<#if template == "MultiColor">
    <#assign rowClass = "multi-color-cart-list" />
</#if>
<#if template == "REd">
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
        <div class="nhsd-t-row ${rowClass}">
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

                <div class="${getGridCol(cards)} nhsd-!t-margin-bottom-4 ndrs-card-item-col">
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
                                        <#if buttonIconLink2 == "mail">
                                            <span class="email-icon">
                                            <svg width="20" height="16" viewBox="0 0 20 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M17 0H3C2.20435 0 1.44129 0.316071 0.87868 0.87868C0.316071 1.44129 0 2.20435 0 3V13C0 13.7956 0.316071 14.5587 0.87868 15.1213C1.44129 15.6839 2.20435 16 3 16H17C17.7956 16 18.5587 15.6839 19.1213 15.1213C19.6839 14.5587 20 13.7956 20 13V3C20 2.20435 19.6839 1.44129 19.1213 0.87868C18.5587 0.316071 17.7956 0 17 0ZM16.33 2L10 6.75L3.67 2H16.33ZM17 14H3C2.73478 14 2.48043 13.8946 2.29289 13.7071C2.10536 13.5196 2 13.2652 2 13V3.25L9.4 8.8C9.5731 8.92982 9.78363 9 10 9C10.2164 9 10.4269 8.92982 10.6 8.8L18 3.25V13C18 13.2652 17.8946 13.5196 17.7071 13.7071C17.5196 13.8946 17.2652 14 17 14Z" fill="#218183"/>
                                            </svg>
                                        </span>
                                        </#if>
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

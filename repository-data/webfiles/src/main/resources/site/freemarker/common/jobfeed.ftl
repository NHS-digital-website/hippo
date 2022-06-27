<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include 'macro/gridColumnGenerator.ftl'>
<#setting date_format="dd MMMM yyyy">

<div class="content-box content-box--secondary content-box--visible-sections content-box--latest-publications">
    <div class="nhsd-o-card-list">
        <div class="nhsd-t-grid">
            <div class="nhsd-t-row">
                <div class="nhsd-t-col">
                    <h2 class="nhsd-t-heading-xl nhsd-t-text-align-center nhsd-!t-margin-bottom-7">
                        ${feedHeader}</h2>
                </div>
            </div>

            <#if jobList?has_content>
                <div class="nhsd-t-row nhsd-t-row--centred nhsd-o-card-list__items">
                    <#list jobList as item>
                        <div class="nhsd-t-col-xs-12 ${getGridCol(jobList?size)}">
                            <article class="nhsd-m-card">
                                <a href="${item.link}"
                                   class="nhsd-a-box-link "
                                   aria-label="Potential Coronavirus (COVID-19) symptoms reported through NHS Pathways and 111 online">
                                    <#assign boxModifier = "bg-dark-grey"/>
                                    <#if item?index%2 == 1 && (jobList?size lte 2 || jobList?size%4 == 0 || jobList?size%3 == 0)>
                                        <#assign boxModifier = "bg-blue"/>
                                    </#if>
                                    <div class="nhsd-a-box nhsd-a-box--${boxModifier}">
                                        <div class="nhsd-m-card__content_container">
                                            <div class="nhsd-m-card__content-box">
                                                <h1 class="nhsd-t-heading-s">${item.title}</h1>
                                                <p class="nhsd-t-body">${item.location}</p>
                                                <p class="nhsd-t-body">${item.salary}</p>
                                            </div>

                                            <div class="nhsd-m-card__button-box">
                                            <span class="nhsd-a-icon nhsd-a-icon--size-s nhsd-a-icon--col-white nhsd-a-arrow">
                                                <svg xmlns="http://www.w3.org/2000/svg"
                                                     preserveAspectRatio="xMidYMid meet"
                                                     aria-hidden="true"
                                                     focusable="false"
                                                     viewBox="0 0 16 16" width="100%"
                                                     height="100%">
                                                    <path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/>
                                                </svg>
                                            </span>
                                            </div>
                                        </div>
                                    </div>
                                </a>
                            </article>
                        </div>
                    </#list>
                </div>
            </#if>
            <#if button1Text?has_content && button2Text?has_content>
                <nav class="nhsd-m-button-nav">
                    <div class="nhsd-t-grid">
                        <div class="nhsd-t-row">
                            <#if button1Text?has_content>
                                <div class="nhsd-t-col-6 nhsd-!t-text-align-m-right nhsd-!t-text-align-l-right nhsd-!t-text-align-xl-right">
                                    <a class="nhsd-a-button"
                                       href="${button1Url}">
                                        <span class="nhsd-a-button__label">${button1Text}</span>
                                    </a>
                                </div>
                            </#if>
                            <#if button2Text?has_content>
                                <div class="nhsd-t-col-6 nhsd-!t-text-align-m-left nhsd-!t-text-align-l-left nhsd-!t-text-align-xl-left">
                                    <a class="nhsd-a-button nhsd-a-button--outline"
                                       href="${button2Url}">
                                        <span class="nhsd-a-button__label">${button2Text}</span>
                                    </a>
                                </div>
                            </#if>
                        </div>
                    </div>
                </nav>
            <#elseif button1Text?has_content || button2Text?has_content>
                <nav class="nhsd-m-button-nav">
                    <div class="nhsd-t-grid">
                        <div class="nhsd-t-row">
                            <div class="nhsd-t-col nhsd-!t-text-align-m-center nhsd-!t-text-align-l-center">
                                <a class="nhsd-a-button"
                                   href="${button1Text?has_content?then(button1Url,button2Url)}">
                                    <span class="nhsd-a-button__label">${button1Text?has_content?then(button1Text,button2Text)}</span>
                                </a>
                            </div>
                        </div>
                    </div>
                </nav>
            </#if>
        </div>
    </div>
</div>

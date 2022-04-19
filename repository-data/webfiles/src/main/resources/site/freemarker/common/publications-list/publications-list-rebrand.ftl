<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include '../macro/gridColumnGenerator.ftl'>
<#setting date_format="dd MMMM yyyy">

<#-- @ftlvariable name="item" type="uk.nhs.digital.ps.beans.Publication" -->
<#if pageable?? && pageable.items?has_content>
    <div class="nhsd-o-card-list">
        <div class="nhsd-t-grid">
            <div class="nhsd-t-row">
                <div class="nhsd-t-col">
                    <h2 class="nhsd-t-heading-xl nhsd-t-text-align-center nhsd-!t-margin-bottom-7">Latest publications</h2>
                </div>
            </div>

            <div class="nhsd-t-row nhsd-t-row--centred nhsd-o-card-list__items">
                <#list pageable.items as item>
                    <div class="nhsd-t-col-xs-12 ${getGridCol(pageable.items?size)}">
                        <article class="nhsd-m-card">
                            <a href="<@hst.link hippobean=item/>" class="nhsd-a-box-link " aria-label="Potential Coronavirus (COVID-19) symptoms reported through NHS Pathways and 111 online" >
                                <#assign boxModifier = "bg-dark-grey"/>
                                <#if item?index%2 == 1 && (size lte 2 || size%4 == 0 || size%3 == 0)>
                                    <#assign boxModifier = "bg-blue"/>
                                </#if>
                                <div class="nhsd-latest-publications-a-box nhsd-a-box nhsd-a-box--${boxModifier}">
                                    <div class="nhsd-m-card__content_container">
                                        <div class="nhsd-m-card__content-box">
                                            <#assign nomimaldate = item.getNominalPublicationDate()/>
                                            <#if !nomimaldate.isRestricted()>
                                                <#assign pubdate = nomimaldate.dayOfMonth + " " + nomimaldate.month +" "+ nomimaldate.year?c/>
                                            </#if>
                                            <span class="nhsd-m-card__date"><time class="cta__meta" datetime="${pubdate?date?iso_utc}">${pubdate?date}</time></span>
                                            <h1 class="nhsd-t-heading-s">${item.title}</h1>
                                        </div>

                                        <div class="nhsd-m-card__button-box">
                                            <span class="nhsd-a-icon nhsd-a-icon--size-s nhsd-a-icon--col-white nhsd-a-arrow">
                                                <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
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

            <nav class="nhsd-m-button-nav">
                <div class="nhsd-t-grid">
                    <div class="nhsd-t-row">
                            <div class="nhsd-t-col-6 nhsd-!t-text-align-m-right nhsd-!t-text-align-l-right nhsd-!t-text-align-xl-right">
                                <a class="nhsd-a-button" href="${viewAllUrl}">
                                    <span class="nhsd-a-button__label">View all</span>
                                </a>
                            </div>
                            <div class="nhsd-t-col-6 nhsd-!t-text-align-m-left nhsd-!t-text-align-l-left nhsd-!t-text-align-xl-left">
                                <a class="nhsd-a-button nhsd-a-button--outline" href="${viewUpcomingUrl}">
                                    <span class="nhsd-a-button__label">View upcoming</span>
                                </a>
                            </div>
                    </div>
                </div>
            </nav>
        </div>
    </div>
</#if>

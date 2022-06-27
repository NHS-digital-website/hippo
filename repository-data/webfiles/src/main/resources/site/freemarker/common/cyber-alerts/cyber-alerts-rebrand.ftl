<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../macro/gridColumnGenerator.ftl">

<#-- @ftlvariable name="cyberAlert" type="uk.nhs.digital.website.beans.CyberAlert" -->

<@hst.setBundle basename="rb.doctype.data-security-homepage"/>
<@fmt.message key="labels.alert-severity" var="alertsSeverityLabel" />

<div class="nhsd-o-card-list nhsd-!t-margin-bottom-2">
    <div class="nhsd-t-grid">
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-12">
                <h2 id="nhsd-az-nav-heading" class="nhsd-t-heading-m">${title}</h2>
            </div>
        </div>
        <#list cyberAlertList?chunk(3) as row>
            <div class="nhsd-t-row nhsd-o-card-list__items nhsd-!t-margin-bottom-0">
                <#list row as cyberAlert>
                    <@hst.link hippobean=cyberAlert var="itemLink" />
                    <@fmt.formatDate value=cyberAlert.publishedDate.time type="Date" pattern="EEEE d MMMM yyyy" timeZone="${getTimeZone()}" var="publishedDate" />
                    <@fmt.formatDate value=cyberAlert.lastModified type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" var="lastModifiedDate" />
                    <div class="nhsd-t-col-xs-12 ${getGridCol(3)} nhsd-!t-padding-bottom-0 nhsd-!t-margin-bottom-4">
                        <article class="nhsd-m-card">
                            <a href="${itemLink}" class="nhsd-a-box-link nhsd-a-box-link--focus-orange">
                                <div class="nhsd-a-box nhsd-a-box--bg-white nhsd-a-box--border-grey">
                                    <div class="nhsd-m-card__content_container">
                                        <div class="nhsd-m-card__content-box">
                                            <div class="nhsd-m-card__tag-list">
                                                <span class="nhsd-a-tag nhsd-a-tag--bg-light-${(cyberAlert.severity?lower_case == "high")?then("red", "blue")}">${cyberAlert.severity}</span>
                                            </div>
                                            <span class="nhsd-m-card__date">${publishedDate} (updated ${lastModifiedDate})</span>

                                            <h3 class="nhsd-t-heading-s">${cyberAlert.title}</h3>
                                        </div>
                                        <div class="nhsd-m-card__button-box">
                                            <span class="nhsd-a-icon nhsd-a-arrow nhsd-a-icon--size-s nhsd-a-icon--col-black">
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
        </#list>
    </div>
</div>

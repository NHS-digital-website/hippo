<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/cyberAlertBox.ftl">

<#-- @ftlvariable name="cyberAlert" type="uk.nhs.digital.website.beans.CyberAlert" -->

<@hst.setBundle basename="rb.doctype.data-security-homepage"/>
<@fmt.message key="headers.alerts" var="cyberAlerts" />
<@fmt.message key="labels.all-alerts" var="cyberAlertsBtn" />
<@fmt.message key="url.all-alerts" var="cyberAlertsBtnUrl" />
<@fmt.message key="labels.alert-date" var="alertsDateLabel" />
<@fmt.message key="labels.alert-severity" var="alertsSeverityLabel" />

<div class="grid-row cyber-grid-row">
    <div class="column column--reset" id="${slugify(cyberAlerts)}">
        <div class="cyber-header">
            <div class="cyber-header__group">
                <h2 class="cyber-header__title" data-uipath="document.header.alerts">${cyberAlerts}</h2>
            </div>

            <#if cyberAlertsBtnUrl?has_content>
                <div class="cyber-header__cta ctabtn-right"
                     aria-labelledby="ctabtn-${slugify(cyberAlertsBtn)}" data-uipath="document.cta.alerts">
                    <a href="${cyberAlertsBtnUrl}" class="ctabtn--nhs-digital-button"
                       id="ctabtn-${slugify(cyberAlertsBtn)}">${cyberAlertsBtn}</a>
                </div>
            </#if>
        </div>

        <#if cyberAlertList??>
            <div class="hub-box-list hub-box-list--grid">
                <#assign index=0/>
                <#list cyberAlertList?chunk(3) as row>
                    <div class="hub-box-list--grid-row">
                        <#list row as cyberAlert>
                            <#assign item = cyberAlert />
                            <@hst.link hippobean=item var="itemLink" />
                            <@fmt.formatDate value=item.publishedDate.time type="Date" pattern="dd/MM/yyyy" timeZone="${getTimeZone()}" var="publishedDate" />
                            <#assign item += {"link": itemLink, "publishedDate": publishedDate} />

                            <#assign item += {"severityLabel": alertsSeverityLabel, "dateLabel": alertsDateLabel} />

                            <#assign item += {"grid": true} />
                            <#assign item += {"newStyle": true} />
                            <#assign item += {"colSize": row?size }/>

                            <#assign key = "alert-" + index + "-" + cyberAlert?index />
                            <#assign item += {"key":  key}/>

                            <@cyberAlertBox item></@cyberAlertBox>
                        </#list>
                    </div>
                    <#assign index += 1/>
                </#list>
            </div>
        </#if>
    </div>
</div>

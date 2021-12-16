<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../macro/visualhubBox.ftl">
<#include "../macro/cyberAlertBox.ftl">
<#include "../macro/hubBox.ftl">
<#include "../macro/sections/quoteSection.ftl">

<#assign cyberAlerts="Alerts"/>
<#assign cyberAlertsBtn="See all alerts"/>
<#assign mostUsedServices="Most used services"/>
<#assign mostUsedServicesBtn="See all services"/>
<#assign usefulInfo="Useful information"/>
<#assign usefulInfoBtn="See all information"/>
<#assign caseStudies="Case studies"/>
<#assign caseStudiesBtn="View case studies"/>


<article class="article article--news-hub">
    <div class="banner-image banner-image--short article-header" style="background-image: url(https://i.ibb.co/0QMQgzT/Artboard.jpg);">
        <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
            <div class="article-header--with-icon">
                <div class="grid-wrapper">
                    <div class="article-header__inner">
                        <div class="grid-row">
                            <div class="column--one-half column--reset">
                                <h1 class="local-header__title local-header__title--dark" data-uipath="document.title">Cyber security for the NHS</h1>
                                <p class="article-header__subtitle article-header__subtitle--dark">We help NHS organisations to manage and improve their cyber security.</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="grid-wrapper">
        <div class="grid-row cyber-grid-row">
            <div class="column column--reset" id="${slugify(cyberAlerts)}">
                <div class="cyber-header">
                    <div class="cyber-header__group">
                        <h2 class="cyber-header__title">${cyberAlerts}</h2>
                    </div>

                    <div class="cyber-header__cta ctabtn-right" aria-labelledby="ctabtn-${slugify(cyberAlertsBtn)}">
                        <a href="#" class="ctabtn--nhs-digital-button" id="ctabtn-${slugify(cyberAlertsBtn)}">${cyberAlertsBtn}</a>
                    </div>
                </div>
                <#assign cyberAlertsList = [{
                "title": "Citrix RCE Vulnerability in Multiple Products",
                "severity": "high",
                "publishedDate": "21/01/20202",
                "link": "#"
                }, {
                "title": "HPE Enterprise Storage Abnormal Failure Rate",
                "severity": "medium",
                "publishedDate": "21/01/20202",
                "link": "#"
                }, {
                "title": "Google Releases Security Updates for Chrome",
                "severity": "low",
                "publishedDate": "21/01/20202",
                "link": "#"
                }] />

                <div class="hub-box-list hub-box-list--grid">
                    <div class="hub-box-list--grid-row">
                        <#list cyberAlertsList as alertData >
                            <#assign item = alertData />
                            <#assign item += {"severityLabel": "Severity", "dateLabel": "Date"} />

                            <#assign item += {"grid": true} />
                            <#assign item += {"newStyle": true} />

                            <#assign item += {"colSize": cyberAlertsList?size} />
                            <@cyberAlertBox item></@cyberAlertBox>
                        </#list>
                    </div>
                </div>
            </div>
        </div>

        <div class="grid-row cyber-grid-row">
            <div class="column column--reset" id="${slugify(mostUsedServices)}">
                <div class="cyber-header">
                    <div class="cyber-header__group">
                        <h2 class="cyber-header__title">${mostUsedServices}</h2>
                    </div>

                    <div class="cyber-header__cta ctabtn-right" aria-labelledby="ctabtn-${slugify(mostUsedServicesBtn)}">
                        <a href="#" class="ctabtn--nhs-digital-button" id="ctabtn-${slugify(mostUsedServicesBtn)}">${mostUsedServicesBtn}</a>
                    </div>
                </div>

                <#assign serviceDataList = [{
                "title": "NHS Secure Boundary",
                "link": "#"
                }, {
                "title": "Specialist training for SIROs",
                "link": "#"
                }, {
                "title": "GCHQ-certified board training",
                "link": "#"
                }] />

                <div class="hub-box-list hub-box-list--grid">
                    <div class="hub-box-list--grid-row">
                        <#list serviceDataList as item >
                            <#assign serviceData = item />
                            <#assign serviceData += {"grid": true} />
                            <#assign serviceData += {"newStyle": true} />

                            <#assign serviceData += {"colSize": serviceDataList?size} />
                            <@cyberAlertBox serviceData></@cyberAlertBox>
                        </#list>
                    </div>
                </div>
            </div>
        </div>

        <div class="grid-row cyber-grid-row">
            <div class="column column--reset" id="${slugify(usefulInfo)}">
                <div class="cyber-header">
                    <div class="cyber-header__group">
                        <h2 class="cyber-header__title">${usefulInfo}</h2>
                    </div>

                    <div class="cyber-header__cta ctabtn-right" aria-labelledby="ctabtn-${slugify(usefulInfoBtn)}">
                        <a href="#" class="ctabtn--nhs-digital-button" id="ctabtn-${slugify(usefulInfoBtn)}">${usefulInfoBtn}</a>
                    </div>
                </div>

                <#assign usefulInfoDataList = [{
                "title": "NHS Cyber security",
                "link": "#"
                }, {
                "title": "Risk framework",
                "link": "#"
                }, {
                "title": "Policies and knowledge",
                "link": "#"
                }] />

                <div class="hub-box-list hub-box-list--grid">
                    <div class="hub-box-list--grid-row">
                        <#list usefulInfoDataList as item >
                            <#assign serviceData = item />
                            <#assign serviceData += {"grid": true} />
                            <#assign serviceData += {"newStyle": true} />

                            <#assign serviceData += {"colSize": usefulInfoDataList?size} />
                            <@cyberAlertBox serviceData></@cyberAlertBox>
                        </#list>
                    </div>
                </div>
            </div>
        </div>

        <#assign serviceDataList = [{
        "title":"Join our cyber security network",
        "content":"Get peer-to-peer support and information by joining our Cyber Associates Network (CAN)",
        "label":"Join the network",
        "url":"#"
        }] />
        <#list serviceDataList as item>
            <div class="grid-row cyber-grid-row">
                <div class="column column--reset" id="${slugify(item.title)}">
                    <div class="cyber-header">
                        <div class="cyber-header__group">
                            <h2 class="cyber-header__title">${item.title}</h2>
                            <p class="cyber-header__content">${item.content}</p>
                        </div>

                        <div class="cyber-header__cta ctabtn-right" aria-labelledby="ctabtn-${slugify(item.label)}">
                            <a href="${item.url}" class="ctabtn--nhs-digital-button" id="ctabtn-${slugify(item.label)}">${item.label}</a>
                        </div>
                    </div>
                </div>
            </div>
        </#list>

        <div class="grid-row cyber-grid-row">
            <div class="column column--reset" id="${slugify(caseStudies)}">
                <div class="cyber-header">
                    <div class="cyber-header__group">
                        <h2 class="cyber-header__title">${caseStudies}</h2>
                    </div>

                    <div class="cyber-header__cta ctabtn-right" aria-labelledby="ctabtn-${slugify(caseStudiesBtn)}">
                        <a href="#" class="ctabtn--nhs-digital-button" id="ctabtn-${slugify(caseStudiesBtn)}">${caseStudiesBtn}</a>
                    </div>
                </div>
                <#assign caseStudiesList = [{
                "leadParagraph": "This is an example quote. You can also include internal (pages on the NHS Digital website) or external links within a quote.",
                "authorName": "Example person",
                "authorJobTitle": "Example role",
                "authorOrganisation": "Example organisation"
                }, {
                "leadParagraph": "This is an example quote. You can also include internal (pages on the NHS Digital website) or external links within a quote.",
                "authorName": "Example person",
                "authorJobTitle": "Example role",
                "authorOrganisation": "Example organisation"
                }] />
                <div class="quote-box-list--grid">
                    <div class="quote-box-list--grid-row">
                        <#list caseStudiesList as item >
                            <#assign caseStudyData = item />
                            <#assign caseStudyData += {"quote": item.leadParagraph, "person":item.authorName, "role":item.authorJobTitle, "organisation":item.authorOrganisation} />
                            <#assign caseStudyData += {"grid": true} />
                            <#assign caseStudyData += {"newStyle": true} />
                            <#assign caseStudyData += {"colSize": caseStudiesList?size} />
                            <div class="quote-box__container quote-box--col-${caseStudiesList?size}">
                                <@quoteSection caseStudyData/>
                            </div>
                        </#list>
                    </div>
                </div>
            </div>
        </div>

    </div>

</article>

<#ftl output_format="HTML">
<#include "../../macro/component/icon.ftl">
<#include "../../macro/cyberAlertBox.ftl">
<#include "../../macro/hubBox.ftl">
<#include "../../../include/imports.ftl">

<#assign cyberAlertData = {
    "title": "Title",
    "text": "Proactively enhance high standards in deliverables.",
    "severity": "critical",
    "threatType": "critical",
    "threatId": "Worm",
    "types": ["Type A", "Type B"],
    "link": "http://link.link",
    "publishedDate": "Monday 27 January 2020",
    "lastModifiedDate": "Thursday 30 January 2020"
} />

<div class="grid-row">
    <div class="column column--two-thirds">
        <div class="article-section article-section--summary">
            <h1>Hub Box</h1>
            <div class="article-section--summary">
                <p>Use the hub box to display a list of components with title, summary and icon</p>
            </div>
        </div>
        <div class="article-section article-section--summary">
            <h2>Supplementary Information</h2>

            <#assign supplementaryInfoData = {
            "title": "Joiners to the NHS by nationality group",
            "text": "NHS Hospital and Community Health Services (HCHS): Joiners to the NHS in England partitioned to nationality group and customer specified organisations, in NHS Trusts and CCGs, 30 June 2010 to 31 August 2018, headcount.",
            "link": "https://digital.nhs.uk/news-and-events/latest-news/nhs-digital-appoints-new-ciso",
            "relatedLinks": [{"title": "NHS Digital appoints Pete Rose", "url": "http://valtech.com"}, {"title": "Chief Information Security Officer (CISO) for the health and care system and Deputy CEO", "url": "http://valtech.com"}, {"title": "Community provider engagement event, Leeds", "url": "http://valtech.com"}],
            "date": "3 January 2019"
            } />

            <div class="hub-box-list">
                <@hubBox supplementaryInfoData></@hubBox>
                <@hubBox supplementaryInfoData></@hubBox>
                <@hubBox supplementaryInfoData></@hubBox>
            </div>
        </div>
        <div class="article-section article-section--summary">
            <h2>Cyber alert box</h2>
            <div class="hub-box-list">
                <@cyberAlertBox cyberAlertData></@cyberAlertBox>
            </div>
        </div>

        <div class="article-section article-section--summary">
            <h2>Cyber alert hub</h2>
            <div class="hub-box-list">
                <@cyberAlertBox cyberAlertData></@cyberAlertBox>
                <@cyberAlertBox cyberAlertData></@cyberAlertBox>
            </div>
        </div>

        <div class="article-section article-section--summary">
            <h2>Event hub article</h2>

            <#assign eventArticleData = {
                "title": "Community provider engagement event, Leeds",
                "text": "These events aim to support providers of community services to submit the new community dataset (V1.5) via the new submission platform (SDCS Cloud).",
                "types": ["Networking", "Event", "Leeds", "Very great"],
                "link": "http://link.link",
                "date": "Thursday 30 January 2020"
            } />

            <div class="hub-box-list">
                <@hubBox eventArticleData></@hubBox>
                <@hubBox eventArticleData></@hubBox>
            </div>
        </div>

        <div class="article-section article-section--summary">
            <h2>News hub article</h2>

            <#assign newsArticleData = {
            "title": "NHS Digital appoints Pete Rose as new Chief Information Security Officer (CISO) for the health and care system and Deputy CEO",
            "text": "NHS Digital has today announced the appointment of Pete Rose as the new Chief Information Security Officer (CISO) for the health and care system and its new Deputy CEO.",
            "imagesection": "EMPTY",
            "link": "https://digital.nhs.uk/news-and-events/latest-news/nhs-digital-appoints-new-ciso",
            "date": "Thursday 30 January 2020"
            } />

            <div class="hub-box-list">
                <@hubBox newsArticleData></@hubBox>
                <@hubBox newsArticleData></@hubBox>
            </div>
        </div>
    </div>
</div>



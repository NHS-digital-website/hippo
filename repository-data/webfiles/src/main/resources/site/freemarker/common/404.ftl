<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">

<#assign documentTitle = "We can't seem to find the page you're looking for" />

<#-- Add meta tags -->
<#assign document = { "title": documentTitle } />
<@metaTags></@metaTags>

<div class="grid-wrapper">
    <div class="grid-row">
        <div class="column column--reset">
            <ol class="breadcrumb">
                <li class="breadcrumb__crumb">
                    <a href="<@hst.link siteMapItemRefId='root'/>" class="breadcrumb__link" title="NHS Digital homepage">NHS Digital</a>
                </li>
                <li class="breadcrumb__crumb">
                    <img src="<@hst.webfile  path="images/icon-arrow.png"/>" alt="Right arrow icon" class="breadcrumb__sep"/>
                    <span class="breadcrumb__link breadcrumb__link--secondary" data-uipath="document.title">${documentTitle}</span>
                </li>
            </ol>
        </div>
    </div>
</div>

<article class="article article--404">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="article-header">
            <h1 data-uipath="ps.document.title">${documentTitle}</h1>
        </div>

        <div class="grid-row">
            <div class="column column--two-thirds page-block page-block--main">
                <div class="article-section">
                    <h2>Error code 404</h2>

                    <p>If you typed in the web address, please check it is correct and try again.</p>
                    <p>We recently moved to a new website. Our old website and content is still available on the <a href="http://webarchive.nationalarchives.gov.uk/search/result/?q=NHS+Digital" title="Visit National Archives website">National archives</a>.</p>
                </div>

                <div class="article-section no-border">
                    <p class="strong">You could try our search:</p>

                    <#include "search-strip.ftl"/>
                </div>

                <div class="article-section article-section--contact no-border">
                    <p>If you still can’t find the information you want, try contacting our <a href="mailto:enquiries@nhsdigital.nhs.uk" title="Get in touch with our contact centre">helpdesk</a>.</p>
                    <div class="rich-text-content">
                        <p>Telephone:<br /><a href="tel:03003035678" title="Contact us by telephone">0300 303 5678</a></p>
                        <p>Email:<br /><a href="mailto:enquiries@nhsdigital.nhs.uk" title="Get in touch with our contact centre">enquiries@nhsdigital.nhs.uk</a></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</article>

<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<div class="grid-wrapper">
    <div class="grid-row">
        <div class="column column--reset">
            <nav aria-label="Breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb__crumb">
                        <a href="/" class="breadcrumb__link" data-text="NHS Digital">NHS Digital</a>
                    </li>
                    <li class="breadcrumb__crumb">
                        <img src="<@hst.webfile  path="images/icon-arrow.svg"/>" alt="Right arrow icon" class="breadcrumb__sep" aria-hidden="true" />
                        <a href="/about-nhs-digital" class="breadcrumb__link" data-text="About NHS Digital">About NHS Digital</a>
                    </li>
                    <li class="breadcrumb__crumb">
                        <img src="<@hst.webfile  path="images/icon-arrow.svg"/>" alt="Right arrow icon" class="breadcrumb__sep" aria-hidden="true" />
                        <a href="/about-nhs-digital/corporate-information-and-documents" class="breadcrumb__link" data-text="Corporate information and documents">Corporate information and documents</a>
                    </li>
                    <li class="breadcrumb__crumb">
                        <img src="<@hst.webfile  path="images/icon-arrow.svg"/>" alt="Right arrow icon" class="breadcrumb__sep" aria-hidden="true" />
                        <a href="/about-nhs-digital/corporate-information-and-documents/nhs-digital-style-guidelines" class="breadcrumb__link" data-text="NHS Digital style guidelines">NHS Digital style guidelines</a>
                    </li>
                    <li class="breadcrumb__crumb">
                        <img src="<@hst.webfile  path="images/icon-arrow.svg"/>" alt="Right arrow icon" class="breadcrumb__sep" aria-hidden="true" />
                        <a href="/about-nhs-digital/corporate-information-and-documents/nhs-digital-style-guidelines/how-we-look" class="breadcrumb__link" data-text="How we look">How we look</a>
                    </li>
                    <li class="breadcrumb__crumb">
                        <img src="<@hst.webfile  path="images/icon-arrow.svg"/>" alt="Right arrow icon" class="breadcrumb__sep" aria-hidden="true" />
                        <a href="/about-nhs-digital/corporate-information-and-documents/nhs-digital-style-guidelines/how-we-look" class="breadcrumb__link" data-text="Icons" aria-current="page">Icons</a>
                    </li>
                    <li class="breadcrumb__crumb">
                        <img src="<@hst.webfile  path="images/icon-arrow.svg"/>" alt="Right arrow icon" class="breadcrumb__sep" aria-hidden="true" />
                        <span class="breadcrumb__link breadcrumb__link--secondary" data-text="Library" aria-current="page">Library</span>
                    </li>
                </ol>
            </nav>
        </div>
    </div>
</div>

<article class="article article--general icon-library">

    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
        <div class="local-header article-header article-header--with-icon">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <div class="grid-row">
                        <div class="column--two-thirds column--reset">
                            <h1 id="top" class="local-header__title" data-uipath="document.title">Icon Library</h1>
                            <div class="article-header__subtitle" data-uipath="website.general.summary">
                                <p>The icon library has been built as a resource for all NHS Digital staff. It contains
                                    a set of icons that are free to use in NHS Digital communications and are in line
                                    with our visual identity.</p>
                            </div>
                        </div>
                        <div class="column--one-third column--reset local-header__icon">
                            <img src="https://digital.nhs.uk/svg-magic/binaries/content/gallery/website/icons/universal/like.svg?colour=ffcd60"
                                 alt="Working at NHS Digital">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="page-block">
        <div class="grid-wrapper grid-wrapper--article">
            <div class="article-section article-section--data-and-information article-section--padded no-thanks">
                <div class="grid-row">
                    <div class="column--two-thirds column--reset">
                        <div class="emphasis-box-gutter">
                            <div class="emphasis-box emphasis-box-important">
                                <div class="emphasis-box__content">
                                    <div>
                                        <p>
                                            <strong>
                                              These icons are no longer part of the NHS Digital brand
                                            </strong>
                                        </p>
                                        <br />
                                        <p>
                                            If you are a member of the staff at NHS Digital, we now have a new set of icons that you can find in your PowerPoint app or on the
                                          <a href="https://hscic365.sharepoint.com/MarketingComms/Pages/Templates-and-Branding-Guidelines.aspx"> communications pages of our intranet.</a></p>
                                        <br />
                                        <p>If you work for a different NHS organisation, the icon library remains available for you to use.</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="grid-row">
                    <div class="column column--reset">
                        <div class="search-form">
                            <span class="screen-label">Find an icon</span>
                            <form role="search" method="get" class="search-strip" aria-label="Filter icon results"
                                  id="filter">
                                <div class="search-strip__contents">
                                    <div class="search-strip__table-cell">
                                        <input type="text" name="icon-search" id="icon-search" class="search-strip__input"
                                               placeholder="e.g. chart" value="${requestContext.getServletRequest().getParameter("icon-search")}" aria-label="Search">
                                        <label for="icon-search" class="visually-hidden">Search</label>
                                    </div>
                                    <div class="search-strip__table-cell search-strip__table-cell--button">
                                        <button data-uipath="search.button" class="search-strip__button"
                                                aria-label="Filter">
                                            <img src="/webfiles/1566301554675/images/icon-search-white.png"
                                                 alt="Magnifying glass">
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="grid-row">
                    <#if icons?size != 0>
                        <div class="column column--reset icon-library-container">
                            <#list icons as icon>
                                <#assign image>/svg-magic/${icon.path}?colour=005eb8</#assign>
                                <#assign name>${icon.name?substring(0, icon.name?length-4)?replace('-', ' ')}</#assign>
                                <#assign link>${requestContext.servletRequest.pathTranslated}${requestContext.servletRequest.pathTranslated?ends_with("/")?then("", "/")}${icon.id}</#assign>
                                <div class="icon-box-margin">
                                    <a class="icon-box" href="${link}">
                                        <img loading="lazy" src="${image}" alt="${name}"/>
                                        <p class="icon-name">${name}</p>
                                    </a>
                                </div>
                            </#list>
                        </div>
                    <#else>
                        <div class="column column--reset">
                            <p>No results.</p>
                        </div>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</article>


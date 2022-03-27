<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<#assign image>/svg-magic/${icon.path}?colour=005eb8</#assign>
<#assign name>${icon.name?substring(0, icon.name?length-4)?replace('-', ' ')}</#assign>

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
                        <a href="/icon-library" class="breadcrumb__link" aria-current="page">Library</a>
                    </li>
                    <li class="breadcrumb__crumb">
                        <img src="<@hst.webfile  path="images/icon-arrow.svg"/>" alt="Right arrow icon" class="breadcrumb__sep" aria-hidden="true" />
                        <span class="breadcrumb__link breadcrumb__link--secondary" data-text="Library" aria-current="page">${name}</span>
                    </li>
                </ol>
            </nav>
        </div>
    </div>
</div>

<article class="article article--general icon-library-item">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
        <div class="local-header article-header article-header--detailed">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <span class="article-header__label">Icon Library</span>
                    <h1 class="local-header__title"
                        data-uipath="document.title">${name}</h1>
                    <div class="detail-list-grid">
                        <div class="grid-row">
                            <div class="column column--reset">
                                <dl class="detail-list">
                                    <dt class="detail-list__key">Keywords:</dt>
                                    <dd class="detail-list__value">${icon.keywords?join(', ')}</dd>
                                </dl>
                            </div>
                        </div>
                        <div class="grid-row">
                            <div class="column column--reset">
                                <dl class="detail-list">
                                    <dt class="detail-list__key">Icon category:</dt>
                                    <dd class="detail-list__value">${icon.category}</dd>
                                </dl>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--one-quarter">
                <div class="base-icon">
                    <img src="${image}" alt="${name} ">
                </div>
            </div>
            <div class="column column--three-quarters download-image-form">
                <h2>Download icon</h2>
                <form action="/download-icon/${icon.path}" method="get">
                    <div class="form-group">
                        <fieldset aria-describedby="type-legend">
                            <legend id="type-legend">
                                Select the file type
                            </legend>
                            <div class="radio-item">
                                <input class="radio-input"
                                       id="type-svg" name="type" type="radio"
                                       value="svg" checked>
                                <label for="type-svg">SVG</label>
                            </div>
                            <div class="radio-item">
                                <input class="radio-input"
                                       id="type-png" name="type" type="radio"
                                       value="png">
                                <label for="type-png">PNG</label>
                            </div>
                        </fieldset>
                    </div>
                    <div class="form-group">
                        <fieldset aria-describedby="colour-legend">
                            <legend id="colour-legend">
                                Select the colour
                            </legend>
                            <div class="radio-item">
                                <input class="radio-input"
                                       id="colour-blue" name="colour" type="radio"
                                       value="blue" checked>
                                <label for="colour-blue">Blue</label>
                            </div>
                            <div class="radio-item">
                                <input class="radio-input"
                                       id="colour-yellow" name="colour" type="radio"
                                       value="yellow">
                                <label for="colour-yellow">Yellow</label>
                            </div>
                            <div class="radio-item">
                                <input class="radio-input"
                                       id="colour-white" name="colour" type="radio"
                                       value="white">
                                <label for="colour-white">White</label>
                            </div>
                            <div class="radio-item">
                                <input class="radio-input"
                                       id="colour-black" name="colour" type="radio"
                                       value="black">
                                <label for="colour-black">
                                    Black (Only use for black and white print)
                                </label>
                            </div>
                        </fieldset>
                    </div>
                    <input name="name" value="${name}" type="hidden">
                    <input type="submit" class="cta__button button"
                           value="Download">
                </form>
            </div>
        </div>
        <div class="grid-row">
            <div class="column--three-quarters column--reset">
                <div class="emphasis-box emphasis-box-important">
                    <div class="emphasis-box__content">
                        <div>
                            <p>Do not try to create your own icons or amend existing
                                ones. Any requirement for new icons should be
                                directed
                                to the communications team at:
                                <br/>
                                <a href="mailto:brandteam@nhs.net">brandteam@nhs.net</a>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</article>

<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#assign documentTitle = "Error!" />

<article class="article article--intranet-home">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="article-header">
            <h1 data-uipath="ps.document.title">${documentTitle}</h1>
        </div>

        <div class="grid-row">
            <div class="column column--two-thirds page-block page-block--main">
                <div class="article-section">
                    <h2>Heading</h2>
                    <p>There was a problem logging you in!</p>
                    <p>Please logout of your SSO provider and try again!</p>
                </div>
            </div>
        </div>
    </div>
</article>

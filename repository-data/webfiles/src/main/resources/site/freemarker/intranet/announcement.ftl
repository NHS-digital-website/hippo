<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#if document??>
    <#assign documentTitle = document.title />
<#else>
    <#assign documentTitle = "Announcement" />
</#if>

<article class="article article--intranet-home">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="article-header">
            <h1 data-uipath="ps.document.title">${documentTitle}</h1>
        </div>

        <div class="grid-row">
            <div class="column column--two-thirds page-block page-block--main">
                <div class="article-section">
                    <#if document??>
                        <@hst.html hippohtml=document.details />
                        <br />
                        <p><strong>Priority: </strong>${document.priority}</p>
                        <p><strong>Expires: </strong><@fmt.formatDate value=document.expirydate.time type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" /></p>
                        <p><strong>Related document: </strong>${document.relateddocument.title}</p>
                    <#else>
                        <p>No announcement.</p>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</article>

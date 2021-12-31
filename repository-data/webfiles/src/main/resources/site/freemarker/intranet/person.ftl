<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#if person??>
    <#assign documentTitle = "${document.displayName}" />
<#else>
    <#assign documentTitle = "Profile page" />
</#if>

<article class="article article--intranet-home">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="article-header">
            <h1 data-uipath="ps.document.title">${documentTitle}</h1>
        </div>
        <#if accessTokenRequired??>
            <div>
                <h3>Your session has expired due to inactivity. Please login again.</h3>
                <a class="button" href="<@hst.link siteMapItemRefId='root'/>">Home</a>
            </div>
        <#else>
            <#if document??>
                <img src="data:image/png;base64,${document.photo}" alt="photo"/>

                <div class="grid-row">
                    <div class="column column--two-thirds page-block page-block--main">
                        <div class="article-section">
                            <p><strong>Email: </strong>${document.email}</p>
                            <p><strong>Phone number: </strong>${document.phoneNumber}</p>
                            <p><strong>Job role: </strong>${document.jobRole}</p>
                            <p><strong>Department: </strong>${document.department}</p>
                        </div>
                    </div>
                </div>
            <#else>
                <div>
                    <h3>Cannot find user.</h3>
                </div>
            </#if>
        </#if>
    </div>
</article>

<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "../common/macro/fileMetaAppendix.ftl">

<@hst.setBundle basename="nationalindicatorlibrary.headers"/>

<#-- Add meta tags -->
<#include "../common/macro/metaTags.ftl">
<@metaTags></@metaTags>

<article class="article article--nilanding">
    <#if document??>
    <div class="grid-wrapper grid-wrapper--article" aria-label="Document Content">
        <div class="grid-row">
            <div class="local-header article-header">
                <h1 class="local-header__title" data-uipath="document.title">${document.title}</h1>
            </div>
        </div>

        <div class="grid-row">
            <div class="column column--two-thirds page-block page-block--main">
                <div class="article-section">
                    <#outputformat "undefined">${document.mainContent.content}</#outputformat>
                </div>

                <div class="article-section" data-uipath="nil.landing.section.advice">
                    <h2>${document.adviceTitle}</h2>

                    <div class="rich-text-content">
                        <#outputformat "undefined">${document.adviceContent.content}</#outputformat>
                    </div>
                    
                    <br>
                    <@externalstorageLink document.adviceForm.resource; url>
                    <a class="niFormBtn" title="${document.adviceForm.text}" href="${url}" onClick="logGoogleAnalyticsEvent('Download advice form','Indicator','${document.adviceForm.resource.filename}');" onKeyUp="return vjsu.onKeyUp(event)"><@fmt.message key="headers.adviceForm"/></a>
                    </@externalstorageLink>
                    <@fileMetaAppendix document.adviceForm.resource.length, document.adviceForm.resource.mimeType></@fileMetaAppendix>
                </div>

                <div class="article-section" data-uipath="nil.landing.section.add">
                    <h2>${document.addTitle}</h2>

                    <div class="rich-text-content">
                        <#outputformat "undefined">${document.addContent.content}</#outputformat>
                    </div>

                    <br>
                    <@externalstorageLink document.addForm.resource; url>
                    <a class="niFormBtn" title="${document.addForm.text}" href="${url}" onClick="logGoogleAnalyticsEvent('Download indicator template form','Indicator','${document.addForm.resource.filename}');" onKeyUp="return vjsu.onKeyUp(event)"><@fmt.message key="headers.addForm"/></a>
                    </@externalstorageLink>
                    <@fileMetaAppendix document.addForm.resource.length, document.addForm.resource.mimeType ></@fileMetaAppendix>
                </div>

                <div class="article-section" data-uipath="nil.landing.section.apply">
                    <h2>${document.applyTitle}</h2>

                    <div class="rich-text-content">
                        <#outputformat "undefined">${document.applyContent.content}</#outputformat>
                    </div>
                    
                    <br>
                    
                    <@externalstorageLink document.applyForm.resource; url>
                    <a class="niFormBtn" title="${document.applyForm.text}" href="${url}" onClick="logGoogleAnalyticsEvent('Download assurance application form','Indicator','${document.applyForm.resource.filename}');" onKeyUp="return vjsu.onKeyUp(event)"><@fmt.message key="headers.applyForm"/></a>
                    </@externalstorageLink>
                    <@fileMetaAppendix document.applyForm.resource.length, document.applyForm.resource.mimeType ></@fileMetaAppendix>
                    
                    <br>
                    
                    <@externalstorageLink document.applyGuidanceForm.resource; url>
                    <a class="niFormBtn" title="${document.applyGuidanceForm.text}" href="${url}" onClick="logGoogleAnalyticsEvent('Download application guidance form','Indicator','${document.applyGuidanceForm.resource.filename}');" onKeyUp="return vjsu.onKeyUp(event)"><@fmt.message key="headers.applyGuidanceForm"/></a></@externalstorageLink>
                    <@fileMetaAppendix document.applyGuidanceForm.resource.length, document.applyGuidanceForm.resource.mimeType ></@fileMetaAppendix>
                </div>
            </div>
        </div>
    </div>
    </#if>
</article>

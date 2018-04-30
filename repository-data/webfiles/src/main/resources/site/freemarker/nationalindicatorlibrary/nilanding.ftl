<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#assign formatFileSize="uk.nhs.digital.ps.directives.FileSizeFormatterDirective"?new() />
<@hst.setBundle basename="nationalindicatorlibrary.headers"/>

<section class="document-content nilanding">
    <#if document??>
        <h1 data-uipath='document.title' style="margin: 0px 0px 25px">${document.title}</h1>
        <#outputformat "undefined">${document.mainContent.content}</#outputformat>

        <section class="push-double--bottom" style="margin: 40px 0px 40px">
            <h2>${document.adviceTitle}</h2>
            <#outputformat "undefined">${document.adviceContent.content}</#outputformat>
            <@externalstorageLink document.adviceForm.resource; url>
            <a class="niFormBtn" title="${document.adviceForm.text}" href="${url}" onClick="logGoogleAnalyticsEvent('Download advice form','Indicator','${document.adviceForm.resource.filename}');"><@fmt.message key="headers.adviceForm"/></a>
            </@externalstorageLink>
            <span class="fileSize">(<@formatFileSize bytesCount=document.adviceForm.resource.length/>)</span>
        </section>

        <section class="push-double--bottom">
            <h2>${document.addTitle}</h2>
            <#outputformat "undefined">${document.addContent.content}</#outputformat>
            <@externalstorageLink document.addForm.resource; url>
            <a class="niFormBtn" title="${document.addForm.text}" href="${url}" onClick="logGoogleAnalyticsEvent('Download indicator template form','Indicator','${document.addForm.resource.filename}');"><@fmt.message key="headers.addForm"/></a>
            </@externalstorageLink>
            <span class="fileSize">(<@formatFileSize bytesCount=document.addForm.resource.length/>)</span>
        </section>

        <section class="push-double--bottom">
            <h2>${document.applyTitle}</h2>
            <#outputformat "undefined">${document.applyContent.content}</#outputformat>
            <@externalstorageLink document.applyForm.resource; url>
            <a class="niFormBtn" title="${document.applyForm.text}" href="${url}" onClick="logGoogleAnalyticsEvent('Download assurance application form','Indicator','${document.applyForm.resource.filename}');"><@fmt.message key="headers.applyForm"/></a>
            </@externalstorageLink>
            <span class="fileSize">(<@formatFileSize bytesCount=document.applyForm.resource.length/>)</span>
            <br>
            <@externalstorageLink document.applyGuidanceForm.resource; url>
            <a class="niFormBtn" title="${document.applyGuidanceForm.text}" href="${url}" onClick="logGoogleAnalyticsEvent('Download application guidance form','Indicator','${document.applyGuidanceForm.resource.filename}');"><@fmt.message key="headers.applyGuidanceForm"/></a>
            </@externalstorageLink>
            <span class="fileSize">(<@formatFileSize bytesCount=document.applyGuidanceForm.resource.length/>)</span>
        </section>
    </#if>
</section>


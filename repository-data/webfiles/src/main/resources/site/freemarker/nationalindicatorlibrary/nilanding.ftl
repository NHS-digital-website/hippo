<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<@hst.setBundle basename="nationalindicatorlibrary.headers"/>

<section class="document-content">
    <#if document??>
        <h1 data-uipath='ps.document.title'>${document.title}</h1>
        <#outputformat "undefined">${document.mainContent.content}</#outputformat>

        <section class="push-double--bottom">
            <h2>${document.adviceTitle}</h2>
            <#outputformat "undefined">${document.adviceContent.content}</#outputformat>
            <a class="niFormBtn" title="${document.adviceForm.text}" href="<@hst.link hippobean=document.adviceForm.resource/>" onClick="logGoogleAnalyticsEvent('Download advice form','Indicator','${document.adviceForm.resource.filename}');"><@fmt.message key="headers.adviceForm"/></a>
        </section>

        <section class="push-double--bottom">
            <h2>${document.addTitle}</h2>
            <#outputformat "undefined">${document.addContent.content}</#outputformat>
            <a class="niFormBtn" title="${document.addForm.text}" href="<@hst.link hippobean=document.addForm.resource/>" onClick="logGoogleAnalyticsEvent('Download indicator template form','Indicator','${document.addForm.resource.filename}');"><@fmt.message key="headers.addForm"/></a>
        </section>

        <section class="push-double--bottom">        
            <h2>${document.applyTitle}</h2>
            <#outputformat "undefined">${document.applyContent.content}</#outputformat>       
            <a class="niFormBtn" title="${document.applyForm.text}" href="<@hst.link hippobean=document.applyForm.resource/>" onClick="logGoogleAnalyticsEvent('Download assurance application form','Indicator','${document.applyForm.resource.filename}');"><@fmt.message key="headers.applyForm"/></a>
            <a class="niFormBtn" title="${document.applyGuidanceForm.text}" href="<@hst.link hippobean=document.applyGuidanceForm.resource/>" onClick="logGoogleAnalyticsEvent('Download application guidance form','Indicator','${document.applyGuidanceForm.resource.filename}');"><@fmt.message key="headers.applyGuidanceForm"/></a>            
        </section>         
    </#if>
</section>


<#ftl output_format="HTML" encoding="UTF-8">

<#include "../../include/imports.ftl">

<@hst.setBundle basename="rb.doctype.form"/>

<#assign core=JspTaglibs ["http://java.sun.com/jsp/jstl/core"] >
<#assign fmt=JspTaglibs ["http://java.sun.com/jsp/jstl/fmt"] >
<#assign hst=JspTaglibs["http://www.hippoecm.org/jsp/hst/core"] >
<#import "eforms-custom-field-renderer.ftl" as fieldRenderer>

<#-- @ftlvariable name="form" type="com.onehippo.cms7.eforms.hst.model.Form" -->

<@hst.defineObjects />

<#assign hasFieldErrors = eforms_errors?? && eforms_errors?size gt 0 />

<#function getFieldError fieldName>
    <#if hasFieldErrors && eforms_errors[fieldName]??>
        <#return eforms_errors[fieldName].localizedMessage />
    <#else>
        <#return "">
    </#if>
</#function>


<#assign style="display:none;">
<#if hasFieldErrors>
    <#assign style="">
</#if>

<div id="feedbackPanel" class="nojs-error callout callout--alert eform-errors" style="${style}">
    <p><@fmt.message key="texts.errors-found"/></p>
</div>


<#if form.title?has_content>
    <h2>${form.title}</h2>
</#if>
<#if formIntro?has_content && processDone! != "true">
    <p class="eforms-intro">${formIntro}</p>
</#if>

<h1> Form id: ${formId} </h1>

<#if apiEnabled?? && apiEnabled>
    <h1> api enabled is true</h1>
    <#else>
    <h1> api enabled is false</h1>
</#if>

<#assign className = (processDone! == "true")?then("eforms-success-box", "eforms-success-box visually-hidden") />
<div class="${className}">
    <span class="eforms-success-box__title">Form submitted successfully</span>

    <#if afterProcessSuccessText?has_content>
        <p>${afterProcessSuccessText?replace("<br />", " ")}</p>
    </#if>
</div>

<#if maxFormSubmissionsReached?has_content>
    <#if maxFormSubmissionsReachedText?has_content>
        <p>${maxFormSubmissionsReachedText}</p>
    <#else>
        <p>The maximum number of submission for this form has been reached</p>
    </#if>
<#else>
    <#if "${processDone!}" != "true">
        <form class="eforms form" action="<@hst.actionURL escapeXml=false />" method="post" name="${form.name!}"
              <#if form.multipart>enctype="multipart/form-data"</#if>>

            <#assign formPages = form.pages>
            <#if formPages?? && formPages?size gte 1>
                <#list formPages as page>
                    <div id="page${page_index}" class="eforms-page conditionally-visible">
                        <#if formPages?size gt 1>
                            <p>${page.label}</p>
                        </#if>

                        <#list page.fields as fieldItem>
                            <#if fieldItem.type == "fieldgroup">
                                <#assign groupCssClassName = "eforms-fieldgroup">
                                <#if fieldItem.oneline>
                                    <#assign groupCssClassName = "eforms-fieldgroup oneline">
                                </#if>

                                <fieldset name="${fieldItem.fieldNamePrefix!}" class="${groupCssClassName!}">
                                    <#if fieldItem.label?has_content>
                                        <legend class="eforms-fieldgroupname">${fieldItem.label}</legend>
                                    </#if>
                                    <#list fieldItem.fields as fieldItemInGroup>
                                        <#assign fieldError = getFieldError(fieldItemInGroup.formRelativeUniqueName) />
                                        <@fieldRenderer.renderField field=fieldItemInGroup error=fieldError />
                                    </#list>
                                    <#if fieldItem.hint??>
                                        <span class="eforms-hint">${fieldItem.hint}</span>
                                    </#if>
                                </fieldset>
                            <#else>
                                <#assign fieldError = getFieldError(fieldItem.formRelativeUniqueName) />
                                <@fieldRenderer.renderField field=fieldItem error=fieldError />
                            </#if>
                        </#list>
                    </div>
                </#list>
            </#if>

            <div class="recaptcha">
                <div class="g-recaptcha" data-sitekey="6LdAieMZAAAAAB4_mxatkiPqkCG3a4mmT9-ZoaHH" data-callback="enableSubmit" data-expired-callback="displayRecaptcha"></div>
            </div>

            <div class="eforms-buttons">
                <#list form.buttons as button>
                    <#if button.type == "nextbutton">
                        <input id="nextPageButton" type="button" name="nextPageButton" class="button button--quaternary ${button.styleClass!}" style="display: none" value="<#if button.value?has_content>${button.value}<#else>${button.name}</#if>" />
                    <#elseif button.type == "previousbutton">
                        <input id="previousPageButton" type="button" name="previousPageButton" class="button button--quaternary ${button.styleClass!}" style="display: none" value="<#if button.value?has_content>${button.value}<#else>${button.name}</#if>" />
                    <#elseif button.type == "resetbutton">
                        <input type="reset" name="${button.formRelativeUniqueName}" class="button button--quaternary ${button.styleClass!}" value="<#if button.value?has_content>${button.value}<#else>${button.name}</#if>" />

                    <#elseif button.type == "submitbutton">
                        <input type="submit" name="${button.formRelativeUniqueName}" class="button" value="<#if button.value?has_content>${button.value}<#else>${button.name}</#if>" />
                    <#else>
                        <input type="button" name="${button.formRelativeUniqueName}" class="button ${button.styleClass!}" value="<#if button.value?has_content>${button.value}<#else>${button.name}</#if>" />
                    </#if>
                </#list>
            </div>
        </form>
    </#if>
</#if>


<#include "eforms-head-contributions.ftl"/>

<#include "eforms.js.ftl"/>

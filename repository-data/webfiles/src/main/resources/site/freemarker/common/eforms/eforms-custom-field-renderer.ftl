<#ftl output_format="HTML" encoding="UTF-8">
<#include "../../include/imports.ftl">

<#assign core=JspTaglibs ["http://java.sun.com/jsp/jstl/core"] >
<#assign fmt=JspTaglibs ["http://java.sun.com/jsp/jstl/fmt"] >
<#assign hst=JspTaglibs["http://www.hippoecm.org/jsp/hst/core"] >

<#assign formatDate="uk.nhs.digital.ps.directives.DateFormatterDirective"?new() >

<#macro renderError errorMessage = "">    
    <#if errorMessage?length gt 0>
    <div class="eforms-field__error-message">${errorMessage}</div>
    <#else>
    <div class="eforms-field__error-message visually-hidden"></div>
    </#if>
</#macro>

<#function removeCommas integerWithCommas>
    <#return integerWithCommas?replace("\\,+", "", "r") />
</#function>

<#macro renderField field error="">
    <#assign hasError = (error != "")?then(true, false) />
    <#assign fieldClassName = hasError?then("eforms-field eforms-field--invalid", "eforms-field") />

    <#if field.type == "simpletextfield">
         <div class="eforms-text" name="${field.formRelativeUniqueName}">
            <#if field.label?has_content>
            <div class="eforms-label ${field.styleClass!}">${field.label!}</div>
            </#if>
            <p>${field.hint!}</p>
        </div>
    <#elseif field.type == "textfield">
        <div class="${fieldClassName}">
            <label class="eforms-label">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></label>
            <input type="text" name="${field.formRelativeUniqueName}" class="${field.styleClass!}" autocomplete="nope" value="${field.value!}"
            <#if (field.length > 0)>size="${field.length}"</#if> <#if (field.minLength > 0)>minlength="${removeCommas(field.minLength)}"</#if> <#if (field.maxLength > 0)>maxlength="${removeCommas(field.maxLength)}"</#if> />
            <@renderError errorMessage = error />
            <span class="eforms-hint">${field.hint!}</span>
        </div>
    <#elseif field.type == "passwordfield">
        <div class="${fieldClassName}">
            <label class="eforms-label">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></label>
            <input type="password" name="${field.formRelativeUniqueName}" autocomplete="nope" class="${field.styleClass!}"
            <#if (field.length > 0)>size="${field.length}"</#if> <#if (field.minLength > 0)>minlength="${removeCommas(field.minLength)}"</#if> <#if (field.maxLength > 0)>maxlength="${removeCommas(field.maxLength)}"</#if> />
            <@renderError errorMessage = error />
            <span class="eforms-hint">${field.hint!}</span>
        </div>
    <#elseif field.type == "textarea">
        <div class="${fieldClassName}">
            <label class="eforms-label">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></label>
            <textarea name="${field.formRelativeUniqueName}" autocomplete="nope" class="${field.styleClass!}"
            cols="${field.cols}" rows="${field.rows}"
            <#if (field.minLength > 0)>minlength="${removeCommas(field.minLength)}"</#if> <#if (field.maxLength > 0)>maxlength="${removeCommas(field.maxLength)}"</#if>>${field.value!}</textarea>
            <@renderError errorMessage = error />
            <span class="eforms-hint">${field.hint!}</span>
        </div>
    <#elseif field.type == "dropdown">
        <div class="${fieldClassName}">
            <label class="eforms-label">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></label>
            <select name="${field.formRelativeUniqueName}" class="${field.styleClass!}">
                <#list field.options as option>
                <option value="${option.value!}" <#if option.selected>selected="selected"</#if>>${option.text!}</option>
                </#list>
            </select>
            <@renderError errorMessage = error />
            <span class="eforms-hint">${field.hint!}</span>
        </div>
    <#elseif field.type == "fileuploadfield">
        <div class="${fieldClassName}">
            <label class="eforms-label">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></label>
            <input type="file" name="${field.formRelativeUniqueName}" class="${field.styleClass!}"
            data-validate="fileSizeAndExtension" data-max-size="${field.maxUploadSize}"
            data-allowed-extensions="<#if field.fileExtensions?? && (field.fileExtensions?size > 0)>${field.fileExtensions?join(",")}</#if>" />
            <@renderError errorMessage = error />
            <span class="eforms-hint">${field.hint!}</span>
        </div>

    <#elseif field.type == "datefield">
        <div class="${fieldClassName}">
            <#if field.value?? && field.value?has_content>
            <@fmt.formatDate value=field.getInitialValue()?datetime type="Date" pattern="${field.dateFormat}" var="value" timeZone="${getTimeZone()}" />
            </#if>

            <label class="eforms-label">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></label>
            <input type="text" name="${field.formRelativeUniqueName}" class="date ${field.styleClass!}" value="${value!}" autocomplete="nope" />
            <@renderError errorMessage = error />
            <span class="eforms-hint">${field.hint!}</span>
        </div>
        <script>
            $(document).ready(function() {
                $(function() {
                    $('input[class*="date"][name="${field.formRelativeUniqueName}"]').datepicker({
                        dateFormat:'${field.dateFormat!}'
                        .replace(/d+/g,'dd')
                        .replace(/M+/g,'mm')
                        .replace(/y+/g,'yy'),
                        step: 10,
                        timepicker: ('${field.dateFormat!}'.indexOf('HH:mm') >= 0)
                    });
                });
            });
        </script>

    <#elseif field.type == "radiogroup">
        <div class="${fieldClassName}">
            <label class="eforms-label">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></label>
            <ul class="radiogroup">
                <#list field.fields as radio>
                <li>
                    <label>
                        <input type="radio" name="${field.formRelativeUniqueName}" class="${radio.styleClass!}" value="${radio.value!}" <#if radio.checked>checked="true"</#if> />
                        ${radio.label!}
                    </label>
                </li>
                </#list>
                <#if field.allowOther>
                <li>
                    <label>
                        <input type="radio" name="${field.formRelativeUniqueName}" class="${field.styleClass!}" value="${field.renderOtherValue!}" <#if field.otherValue>checked="true"</#if> /> Other:
                    </label>
                    <input type="text" value="<#if field.otherValue>${field.value!}</#if>" name="${field.otherFieldName!}" autocomplete="nope" class="textfield-other" <#if (field.length > 0)>size="${field.length}"</#if> <#if (field.minLength > 0)>minlength="${removeCommas(field.minLength)}"</#if> <#if (field.maxLength > 0)>maxlength="${removeCommas(field.maxLength)}"</#if> />
                </li>
                </#if>
            </ul>
            <@renderError errorMessage = error />
            <span class="eforms-hint">${field.hint!}</span>
        </div>
    <#elseif field.type == "checkboxgroup">
        <div class="${fieldClassName}">
            <label class="eforms-label">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></label>
            <ul class="checkboxgroup">
                <#list field.fields as checkbox>
                <li>
                    <label>
                        <input type="checkbox" name="${checkbox.formRelativeUniqueName}" class="${checkbox.styleClass!}" value="${checkbox.value!}"
                        <#if checkbox.checked>checked="true"</#if> />${checkbox.label!}
                    </label>
                </li>
                </#list>
                <#if field.allowOther>
                <li>
                    <label>
                        <input type="checkbox" name="${field.formRelativeUniqueName}" class="${field.styleClass!}" value="${field.renderOtherValue!}"
                        <#if field.otherValue>checked="true"</#if> />Other:
                    </label>
                    <input type="text" value="<#if field.otherValue>${field.value!}</#if>" name="${field.otherFieldName!}" autocomplete="nope" class="textfield-other"
                    <#if (field.length > 0)>size="${field.length}"</#if> <#if (field.minLength > 0)>minlength="${removeCommas(field.minLength)}"</#if> <#if (field.maxLength > 0)>maxlength="${removeCommas(field.maxLength)}"</#if> />
                </li>
                </#if>
            </ul>
            <@renderError errorMessage = error />
            <span class="eforms-hint">${field.hint!}</span>
        </div>
    <#elseif field.type == "likert">
        <div class="${fieldClassName}">
            <input type="hidden" name="${field.formRelativeUniqueName}"/>
            <label class="eforms-label">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></label>
            <table class="eforms-likert-table">
                <tr>
                    <td>&nbsp;</td>
                    <#list field.options as option>
                    <td>
                        ${option!}
                    </td>
                    </#list>
                </tr>
                <#list field.optionsKeyValuePairs as pair>
                <tr>
                    <td>${pair.key.label!}</td>
                    <#list pair.value as radio>
                    <td>
                        <input type="radio" name="${radio.formRelativeUniqueName}" class="${radio.styleClass!}" value="${radio.value!}"
                        <#if radio.checked>checked="true"</#if> />
                    </td>
                    </#list>
                </tr>
                </#list>
            </table>
            <@renderError errorMessage = error />
            <span class="eforms-hint">${field.hint!}</span>
        </div>
    <#elseif field.type == "antispam">

    <#assign cssClass="eforms-field">
    <#if field.extraCssClass?has_content>
    <#assign cssClass="eforms-field ${field.extraCssClass}">
    </#if>

    <#if field.honeyPot>
    <div class="${cssClass}" <#if !field.extraCssClass?has_content>style="display:none"</#if>>
        <label class="eforms-label">${field.label}<span class="eforms-req">${field.requiredMarker}</span></label>
        <input type="text" name="${field.formRelativeUniqueName}" class="${field.styleClass}" value="${field.value!}"/>
    </div>
    <#elseif field.slider>
    <div class="${cssClass}">
        <div id="slider"></div>
        <input type="hidden" id="sliderInput" name="${field.formRelativeUniqueName}" value=""/>
        <div id="notSlided" style="display:block">
            <p>Slide to be able to submit</p>
        </div>
        <div id="slided" style="display:none">
            <p>You may submit the form</p>
        </div>
    </div>
    <#else>
    ${field.antiSpamType}
    </#if>
    </#if>
</#macro>

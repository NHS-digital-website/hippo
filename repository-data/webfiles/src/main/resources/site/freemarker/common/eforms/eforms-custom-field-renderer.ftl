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
         <div class="eforms-text">
            <#if field.label?has_content>
            <div class="eforms-label ${field.styleClass!}" id="${field.formRelativeUniqueName}">${field.label!}</div>
            </#if>
            <p>${field.hint!}</p>
        </div>
    <#elseif field.type == "textfield">
        <div class="${fieldClassName}">
            <label for="${field.formRelativeUniqueName}" class="eforms-label">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></label>
            <#assign autocompleteValue = field.autocomplete?default('off')>
            <input type="${((field.autocomplete?default(""))?lower_case == "email")?then("email", "text")}" name="${field.formRelativeUniqueName}" id="${field.formRelativeUniqueName}" class="${field.styleClass!}" autocomplete="${autocompleteValue}" value="${field.value!}"
            <#if (field.length > 0)>size="${field.length}"</#if> <#if (field.minLength > 0)>minlength="${removeCommas(field.minLength)}"</#if> <#if (field.maxLength > 0)>maxlength="${removeCommas(field.maxLength)}"</#if> aria-describedby="${field.formRelativeUniqueName}-hint" />
            <@renderError errorMessage = error />
            <span class="eforms-hint" id="${field.formRelativeUniqueName}-hint">${field.hint!}</span>
        </div>
    <#elseif field.type == "custompostcodefield">
        <div class="${fieldClassName}">
            <label for="${field.formRelativeUniqueName}" class="eforms-label">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></label>
            <#assign autocompleteValue = field.autocomplete?default('off')>
            <input type="text" name="${field.formRelativeUniqueName}" id="${field.formRelativeUniqueName}" class="${field.styleClass!}" autocomplete="${autocompleteValue}" value="${field.value!}" aria-describedby="${field.formRelativeUniqueName}-hint"/>
            <@renderError errorMessage = error />
            <span class="eforms-hint" id="${field.formRelativeUniqueName}-hint">${field.hint!}</span>
        </div>

    <#elseif field.type == "customdatefield">
        <div style="white-space: nowrap;" class="${fieldClassName}">
            <label for="${field.formRelativeUniqueName}"
                   class="eforms-label">${field.label!}<span
                        class="eforms-req">${field.requiredMarker!}</span></label>

            <div style="display: flex">

                <div style="margin-right: 12px;margin-top: 0px">
                    <label class="nhsd-m-date-input--day-label" for="day">
                        Day
                    </label>

                    <input type="text" name="${field.formRelativeUniqueName}"
                           id="date__DD" class="${field.styleClass!}"
                           autocomplete="off" value="${field.value!}" size="1"
                           aria-describedby="${field.formRelativeUniqueName}-hint"/>
                </div>

                <div style="margin-right: 12px;margin-top: 0px">
                    <label class="nhsd-m-date-input--month-label" for="month">
                        Month
                    </label>
                    <input type="text" name="${field.formRelativeUniqueName}"
                           id="date__MM"
                           class="${field.styleClass!}"
                           autocomplete="off" value="${field.value!}"
                           size="1"
                           aria-describedby="${field.formRelativeUniqueName}-hint"/>
                </div>

                <div class="nhsd-m-date-input-year" style="margin-top: 0px">
                    <label class="nhsd-m-date-input--year-label" for="year">
                        Year
                    </label>
                    <input type="text" name="${field.formRelativeUniqueName}"
                           id="date__YYYY"
                           class="${field.styleClass!}"
                           autocomplete="off" value="${field.value!}" size="3"
                           aria-describedby="${field.formRelativeUniqueName}-hint"/>
                </div>
                <input type="hidden" name="${field.formRelativeUniqueName}"
                       class="visually-hidden"
                       id="${field.formRelativeUniqueName}"/>

                <@renderError errorMessage = error />

            </div>
            <span class="eforms-hint" id="${field.formRelativeUniqueName}-hint">${field.hint!}</span>
        </div>




    <#elseif field.type == "passwordfield">
        <div class="${fieldClassName}">
            <label for="${field.formRelativeUniqueName}" class="eforms-label">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></label>
            <input type="password" name="${field.formRelativeUniqueName}" id="${field.formRelativeUniqueName}" autocomplete="off" class="${field.styleClass!}"
            <#if (field.length > 0)>size="${field.length}"</#if> <#if (field.minLength > 0)>minlength="${removeCommas(field.minLength)}"</#if> <#if (field.maxLength > 0)>maxlength="${removeCommas(field.maxLength)}"</#if> aria-describedby="${field.formRelativeUniqueName}-hint" />
            <@renderError errorMessage = error />
            <span class="eforms-hint" id="${field.formRelativeUniqueName}-hint">${field.hint!}</span>
        </div>
    <#elseif field.type == "textarea">
        <div class="${fieldClassName}">
            <label for="${field.formRelativeUniqueName}" class="eforms-label">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></label>
            <#assign autocompleteValue = field.autocomplete?default('off')>
            <textarea name="${field.formRelativeUniqueName}" id="${field.formRelativeUniqueName}" autocomplete="${autocompleteValue}" class="${field.styleClass!}"
            cols="${field.cols}" rows="${field.rows}"
            <#if (field.minLength > 0)>minlength="${removeCommas(field.minLength)}"</#if> <#if (field.maxLength > 0)>maxlength="${removeCommas(field.maxLength)}"</#if>
            aria-describedby="${field.formRelativeUniqueName}-hint">${field.value!}</textarea>
            <@renderError errorMessage = error />
            <span class="eforms-hint" id="${field.formRelativeUniqueName}-hint">${field.hint!}</span>
        </div>
    <#elseif field.type == "dropdown">
        <div class="${fieldClassName}">
            <label for="${field.formRelativeUniqueName}" class="eforms-label">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></label>
            <#assign autocompleteValue = field.autocomplete?default('off')>
            <select name="${field.formRelativeUniqueName}" id="${field.formRelativeUniqueName}" class="${field.styleClass!}" autocomplete="${autocompleteValue}" aria-describedby="${field.formRelativeUniqueName}-hint">
                <#list field.options as option>
                <option value="${option.value!}" <#if option.selected>selected="selected"</#if>>${option.text!}</option>
                </#list>
            </select>
            <@renderError errorMessage = error />
            <span class="eforms-hint" id="${field.formRelativeUniqueName}-hint">${field.hint!}</span>
        </div>
    <#elseif field.type == "fileuploadfield">
        <div class="${fieldClassName}">
            <label for="${field.formRelativeUniqueName}" class="eforms-label">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></label>
            <input type="file" name="${field.formRelativeUniqueName}" id="${field.formRelativeUniqueName}" class="${field.styleClass!}"
            data-validate="fileSizeAndExtension" data-max-size="${field.maxUploadSize}"
            data-allowed-extensions="<#if field.fileExtensions?? && (field.fileExtensions?size > 0)>${field.fileExtensions?join(",")}</#if>"
            aria-describedby="${field.formRelativeUniqueName}-hint" />
            <@renderError errorMessage = error />
            <span class="eforms-hint" id="${field.formRelativeUniqueName}-hint">${field.hint!}</span>
        </div>

    <#elseif field.type == "datefield">
        <div class="${fieldClassName}" data-eforms-field="date" data-eforms-dateformat="${field.dateFormat!}" data-eforms-formname="${field.formRelativeUniqueName}">
            <#if field.value?? && field.value?has_content>
            <@fmt.formatDate value=field.getInitialValue()?datetime type="Date" pattern="${field.dateFormat}" var="value" timeZone="${getTimeZone()}" />
            </#if>

            <label for="${field.formRelativeUniqueName}" class="eforms-label">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></label>
            <#assign autocompleteValue = field.autocomplete?default('off')>
            <input type="text" name="${field.formRelativeUniqueName}" id="${field.formRelativeUniqueName}" class="date ${field.styleClass!}" value="${value!}" autocomplete="${autocompleteValue}" aria-describedby="${field.formRelativeUniqueName}-hint" />
            <@renderError errorMessage = error />
            <span class="eforms-hint" id="${field.formRelativeUniqueName}-hint">${field.hint!}</span>
        </div>
    <#elseif field.type == "radiogroup">
        <fieldset class="${fieldClassName}">
            <legend class="eforms-label">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></legend>
            <ul class="radiogroup">
                <#list field.fields as radio>
                <li>
                    <label for="${slugify(field.formRelativeUniqueName)}-${slugify(radio.value!)}">
                        <input type="radio" name="${field.formRelativeUniqueName}" id="${slugify(field.formRelativeUniqueName)}-${slugify(radio.value!)}" class="${radio.styleClass!}" value="${radio.value!}" <#if radio.checked>checked="true"</#if> aria-describedby="${field.formRelativeUniqueName}-hint" />
                        ${radio.label!}
                    </label>
                </li>
                </#list>
                <#if field.allowOther>
                <li>
                    <label for="${slugify(field.formRelativeUniqueName)}-${slugify(field.renderOtherValue!)}">
                        <input type="radio" name="${field.formRelativeUniqueName}" id="${slugify(field.formRelativeUniqueName)}-${slugify(field.renderOtherValue!)}" class="${field.styleClass!}" value="${field.renderOtherValue!}" <#if field.otherValue>checked="true"</#if> aria-describedby="${field.formRelativeUniqueName}-hint" /> Other:
                    </label>
                    <input type="text" value="<#if field.otherValue>${field.value!}</#if>" name="${field.otherFieldName!}" id="${slugify(field.formRelativeUniqueName)}-${slugify(field.otherFieldName!)}" autocomplete="off" class="textfield-other" <#if (field.length > 0)>size="${field.length}"</#if> <#if (field.minLength > 0)>minlength="${removeCommas(field.minLength)}"</#if> <#if (field.maxLength > 0)>maxlength="${removeCommas(field.maxLength)}"</#if> />
                    <label for="${slugify(field.formRelativeUniqueName)}-${slugify(field.otherFieldName!)}" class="visually-hidden">Other:</label>
                </li>
                </#if>
            </ul>
            <@renderError errorMessage = error />
            <span class="eforms-hint" id="${field.formRelativeUniqueName}-hint">${field.hint!}</span>
        </fieldset>
    <#elseif field.type == "checkboxgroup">
        <fieldset class="${fieldClassName}">
            <legend class="eforms-label">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></legend>
            <ul class="checkboxgroup">
                <#list field.fields as checkbox>
                <li>
                    <label for="${slugify(checkbox.formRelativeUniqueName)}-${slugify(checkbox.value)}">
                        <input type="checkbox" name="${checkbox.formRelativeUniqueName}" id="${slugify(checkbox.formRelativeUniqueName)}-${slugify(checkbox.value)}" class="${checkbox.styleClass!}" value="${checkbox.value!}"
                        <#if checkbox.checked>checked="true"</#if> aria-describedby="${field.formRelativeUniqueName}-hint" />${checkbox.label!}
                    </label>
                </li>
                </#list>
                <#if field.allowOther>
                <li>
                    <label for="${slugify(field.formRelativeUniqueName)}-${slugify(field.renderOtherValue!)}" >
                        <input type="checkbox" name="${field.formRelativeUniqueName}" id="${slugify(field.formRelativeUniqueName)}-${slugify(field.renderOtherValue!)}" class="${field.styleClass!}" value="${field.renderOtherValue!}"
                        <#if field.otherValue>checked="true"</#if> aria-describedby="${field.formRelativeUniqueName}-hint" />Other:
                    </label>
                    <input type="text" value="<#if field.otherValue>${field.value!}</#if>" name="${field.otherFieldName!}" id="${slugify(field.formRelativeUniqueName)}-${slugify(field.otherFieldName!)}" autocomplete="off" class="textfield-other"
                    <#if (field.length > 0)>size="${field.length}"</#if> <#if (field.minLength > 0)>minlength="${removeCommas(field.minLength)}"</#if> <#if (field.maxLength > 0)>maxlength="${removeCommas(field.maxLength)}"</#if> />
                    <label for="${slugify(field.formRelativeUniqueName)}-${slugify(field.otherFieldName!)}" class="visually-hidden">Other:</label>
                </li>
                </#if>
            </ul>
            <@renderError errorMessage = error />
            <span class="eforms-hint" id="${field.formRelativeUniqueName}-hint">${field.hint!}</span>
        </fieldset>
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
                        <input type="radio" name="${radio.formRelativeUniqueName}" id="${slugify(radio.formRelativeUniqueName)}-${slugify(radio.value)}" class="${radio.styleClass!}" value="${radio.value!}"
                        <#if radio.checked>checked="true"</#if> aria-describedby="${field.formRelativeUniqueName}-hint" />
                        <label for="${slugify(radio.formRelativeUniqueName)}-${slugify(radio.value)}" class="visually-hidden">${radio.value!}</label>
                    </td>
                    </#list>
                </tr>
                </#list>
            </table>
            <@renderError errorMessage = error />
            <span class="eforms-hint" id="${field.formRelativeUniqueName}-hint">${field.hint!}</span>
        </div>

    </#if>
</#macro>

<#ftl output_format="HTML" encoding="UTF-8">
<#--
  Copyright 2015-2016 Hippo B.V. (http://www.onehippo.com)
-->

<#assign core=JspTaglibs ["http://java.sun.com/jsp/jstl/core"] >
<#assign fmt=JspTaglibs ["http://java.sun.com/jsp/jstl/fmt"] >
<#assign hst=JspTaglibs["http://www.hippoecm.org/jsp/hst/core"] >

<#macro renderField field>

    <#if field.type == "simpletextfield">

    <div class="eforms-text" name="${field.formRelativeUniqueName}">
        <div class="${field.styleClass!}">${field.label!}</div>
        <span class="eforms-hint">${field.hint!}</span>
    </div>

    <#elseif field.type == "textfield">

    <div class="eforms-field">
        <label for="${field.formRelativeUniqueName}">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></label>
        <input type="text" name="${field.formRelativeUniqueName}" id="${field.formRelativeUniqueName}" class="${field.styleClass!}" value="${field.value!}"
             <#if (field.length > 0)>size="${field.length}"</#if> <#if (field.maxLength > 0)>maxlength="${field.maxLength}"</#if> aria-describedby="${field.formRelativeUniqueName}-hint" />
        <span class="eforms-hint" id="${field.formRelativeUniqueName}-hint">${field.hint!}</span>
    </div>

    <#elseif field.type == "passwordfield">

    <div class="eforms-field">
        <label for="${field.formRelativeUniqueName}">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></label>
        <input type="password" name="${field.formRelativeUniqueName}" id="${field.formRelativeUniqueName}" class="${field.styleClass!}"
             <#if (field.length > 0)>size="${field.length}"</#if> <#if (field.maxLength > 0)>maxlength="${field.maxLength}"</#if> aria-describedby="${field.formRelativeUniqueName}-hint" />
        <span class="eforms-hint" id="${field.formRelativeUniqueName}-hint">${field.hint!}</span>
    </div>

    <#elseif field.type == "textarea">

    <div class="eforms-field">
        <label for="${field.formRelativeUniqueName}">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></label>
        <textarea name="${field.formRelativeUniqueName}" id="${field.formRelativeUniqueName}" class="${field.styleClass!}"
                  cols="${field.cols}" rows="${field.rows}"
                <#if (field.minLength > 0)>minlength="${field.minLength}"</#if> <#if (field.maxLength > 0)>maxlength="${field.maxLength}"</#if>
                aria-describedby="${field.formRelativeUniqueName}-hint">${field.value!}</textarea>
        <span class="eforms-hint" id="${field.formRelativeUniqueName}-hint">${field.hint!}</span>
    </div>

    <#elseif field.type == "dropdown">

    <div class="eforms-field">
        <label for="${field.formRelativeUniqueName}">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></label>
        <select name="${field.formRelativeUniqueName}" id="${field.formRelativeUniqueName}" class="${field.styleClass!}" aria-describedby="${field.formRelativeUniqueName}-hint">
        <#list field.options as option>
            <option value="${option.value!}" <#if option.selected>selected="selected"</#if>>${option.text!}</option>
        </#list>
        </select>
        <span class="eforms-hint" id="${field.formRelativeUniqueName}-hint">${field.hint!}</span>
    </div>

    <#elseif field.type == "fileuploadfield">

    <div class="eforms-field">
        <label for="${field.formRelativeUniqueName}">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></label>
        <input type="file" name="${field.formRelativeUniqueName}" id="${field.formRelativeUniqueName}" class="${field.styleClass!}"
               data-validate="fileSizeAndExtension" data-max-size="${field.maxUploadSize}"
               data-allowed-extensions="<#if field.fileExtensions?? && (field.fileExtensions?size > 0)>${field.fileExtensions?join(",")}</#if>"
               aria-describedby="${field.formRelativeUniqueName}-hint" />
        <span class="eforms-hint" id="${field.formRelativeUniqueName}-hint">${field.hint!}</span>
    </div>

    <#elseif field.type == "datefield">

    <div class="eforms-field">
        <label for="${field.formRelativeUniqueName}">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></label>
        <input type="text" name="${field.formRelativeUniqueName}" id="${field.formRelativeUniqueName}" class="date ${field.styleClass!}" value="${field.value!}" aria-describedby="${field.formRelativeUniqueName}-hint" />
        <span class="eforms-hint" id="${field.formRelativeUniqueName}-hint">${field.hint!}</span>
    </div>
    <script>
        $(document).ready(function() {
            $(function() {
                $('input[class*="date"][name="${field.formRelativeUniqueName}"]').datetimepicker({
                    format:'${field.dateFormat!}'.replace(/m+/g,'i')
                        .replace(/H+/g,'H')
                        .replace(/d+/g,'d')
                        .replace(/M+/g,'m')
                        .replace(/y+/g,'Y'),
                    step:10,
                    timepicker:('${field.dateFormat!}'.indexOf('HH:mm') >= 0)
                });
            });
        });
    </script>

    <#elseif field.type == "radiogroup">

    <div class="eforms-field">
        <label id="${field.formRelativeUniqueName}-group-label">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></label>
        <ul class="radiogroup" aria-labelledby="${field.formRelativeUniqueName}-group-label" role="radiogroup">
        <#list field.fields as radio>
          <li>
              <input type="radio" name="${field.formRelativeUniqueName}" id="${field.formRelativeUniqueName}-${radio.value!}" class="${radio.styleClass!}" value="${radio.value!}"
                   <#if radio.checked>checked="true"</#if> aria-describedby="${field.formRelativeUniqueName}-hint" />
              <label for="${field.formRelativeUniqueName}-${radio.value!}">${radio.label!}</label>
          </li>
        </#list>
        <#if field.allowOther>
          <li>
              <input type="radio" name="${field.formRelativeUniqueName}" id="${field.formRelativeUniqueName}-other" class="${field.styleClass!}" value="${field.renderOtherValue!}"
              <#if field.otherValue>checked="true"</#if> aria-describedby="${field.formRelativeUniqueName}-hint" />
              <label for="${field.formRelativeUniqueName}-other">Other:</label>
              <span>
                  <input type="text" value="<#if field.otherValue>${field.value!}</#if>" name="${field.otherFieldName!}" id="${field.otherFieldName!}" class="textfield-other"
                     <#if (field.length > 0)>size="${field.length}"</#if> <#if (field.maxLength > 0)>maxlength="${field.maxLength}"</#if> />
              </span>
          </li>
        </#if>
        </ul>
        <span class="eforms-hint" id="${field.formRelativeUniqueName}-hint">${field.hint!}</span>
    </div>

    <#elseif field.type == "checkboxgroup">

    <div class="eforms-field">
        <label id="${field.formRelativeUniqueName}-group-label">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></label>
        <div role="group" aria-labelledby="${field.formRelativeUniqueName}-group-label">
      <#list field.fields as checkbox>
        <p>
            <input type="checkbox" name="${checkbox.formRelativeUniqueName}" id="${checkbox.formRelativeUniqueName}" class="${checkbox.styleClass!}" value="${checkbox.value!}"
                 <#if checkbox.checked>checked="true"</#if> aria-describedby="${field.formRelativeUniqueName}-hint" />
            <label for="${checkbox.formRelativeUniqueName}">${checkbox.label!}</label>
        </p>
      </#list>
      <#if field.allowOther>
        <p>
            <input type="checkbox" name="${field.formRelativeUniqueName}" id="${field.formRelativeUniqueName}-other" class="${field.styleClass!}" value="${field.renderOtherValue!}"
              <#if field.otherValue>checked="true"</#if> aria-describedby="${field.formRelativeUniqueName}-hint" />
            <label for="${field.formRelativeUniqueName}-other">Other:</label>
            <span>
                <input type="text" value="<#if field.otherValue>${field.value!}</#if>" name="${field.otherFieldName!}" id="${field.otherFieldName!}" class="textfield-other"
                     <#if (field.length > 0)>size="${field.length}"</#if> <#if (field.maxLength > 0)>maxlength="${field.maxLength}"</#if> />
            </span>
        </p>
      </#if>
        </div>
        <span class="eforms-hint" id="${field.formRelativeUniqueName}-hint">${field.hint!}</span>
    </div>

    <#elseif field.type == "likert">

    <div class="eforms-field">
        <input type="hidden" name="${field.formRelativeUniqueName}"/>
        <label id="${field.formRelativeUniqueName}-group-label">${field.label!}<span class="eforms-req">${field.requiredMarker!}</span></label>
        <table class="eforms-likert-table" aria-labelledby="${field.formRelativeUniqueName}-group-label">
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
                  <input type="radio" name="${radio.formRelativeUniqueName}" id="${radio.formRelativeUniqueName}-${radio.value!}" class="${radio.styleClass!}" value="${radio.value!}"
                       <#if radio.checked>checked="true"</#if> aria-describedby="${field.formRelativeUniqueName}-hint" />
                  <label for="${radio.formRelativeUniqueName}-${radio.value!}" class="visually-hidden">${radio.value!}</label>
              </td>
            </#list>
          </tr>
        </#list>
        </table>
        <span class="eforms-hint" id="${field.formRelativeUniqueName}-hint">${field.hint!}</span>
    </div>

    <#elseif field.type == "antispam">

        <#assign cssClass="eforms-field">
        <#if field.extraCssClass?has_content>
            <#assign cssClass="eforms-field ${field.extraCssClass}">
        </#if>

        <#if field.honeyPot>

      <div class="${cssClass}" <#if !field.extraCssClass?has_content>style="display:none"</#if>>
          <label>${field.label}<span class="eforms-req">${field.requiredMarker}</span></label>
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

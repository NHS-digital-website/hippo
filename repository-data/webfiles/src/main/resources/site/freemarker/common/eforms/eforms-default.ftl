<#ftl output_format="HTML">
<#ftl encoding="UTF-8">
<#--
  Copyright 2015-2016 Hippo B.V. (http://www.onehippo.com)
-->
<#assign core=JspTaglibs ["http://java.sun.com/jsp/jstl/core"] >
<#assign fmt=JspTaglibs ["http://java.sun.com/jsp/jstl/fmt"] >
<#assign hst=JspTaglibs["http://www.hippoecm.org/jsp/hst/core"] >
<#import "../lib/eforms-field-renderer.ftl" as fieldRenderer>

<#-- @ftlvariable name="form" type="com.onehippo.cms7.eforms.hst.model.Form" -->

<@hst.defineObjects />

<#if "${processDone!}" == "true" && afterProcessSuccessText?has_content>
  <p>${afterProcessSuccessText!}</p>
</#if>
<#if form.title?has_content>
  <h2>${form.title}</h2>
</#if>

<#if formIntro?has_content>
  <p>${formIntro}</p>
</#if>

<#assign style="display:none;">
<#if eforms_error?? && (eforms_error?size > 0)>
    <#assign style="">
</#if>

<div id="feedbackPanel" class="nojs-error" style="${style}">
    <ul>
    <#if eforms_errors?? && (eforms_errors?size > 0)>
      <#list eforms_errors?keys as key>
        <li>${eforms_errors[key].localizedMessage}</li>
      </#list>
    </#if>
    </ul>
</div>

<#if maxFormSubmissionsReached?has_content>

    <#if maxFormSubmissionsReachedText?has_content>
    <p>${maxFormSubmissionsReachedText}</p>
    <#else>
    <p>The maximum number of submission for this form has been reached</p>
    </#if>

<#else>

  <form class="form" action="<@hst.actionURL escapeXml=false />" method="post" name="${form.name!}"
        <#if form.multipart>enctype="multipart/form-data"</#if>>

    <#assign formPages = form.pages>

    <#if formPages?? && (formPages?size > 1)>

      <ul id="pagesTab" class="eforms-pagetab" style="DISPLAY: none">
        <#list formPages as page>
          <#if page_index == 0>
            <li class="conditionally-visible selected">${page.label}</li>
          <#else>
            <li class="conditionally-visible">${page.label}</li>
          </#if>
        </#list>
      </ul>
    </#if>

    <#if formPages?? && (formPages?size > 0)>

        <#list formPages as page>

        <div id="page${page_index}" class="eforms-page conditionally-visible">

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
                    <@fieldRenderer.renderField field=fieldItemInGroup />
                </#list>
                <#if fieldItem.hint??>
                    <span class="eforms-hint">${fieldItem.hint}</span>
                </#if>
              </fieldset>

            <#else>
                <@fieldRenderer.renderField field=fieldItem />
            </#if>

          </#list>

        </div>

        </#list>

    </#if>

      <div class="eforms-buttons">
      <#list form.buttons as button>
        <#if button.type == "nextbutton">
          <input id="nextPageButton" type="button" name="nextPageButton" class="${button.styleClass!}" style="display: none"
                 value="<#if button.value?has_content>${button.value}<#else>${button.name}</#if>" />
        <#elseif button.type == "previousbutton">
          <input id="previousPageButton" type="button" name="previousPageButton" class="${button.styleClass!}" style="display: none"
                 value="<#if button.value?has_content>${button.value}<#else>${button.name}</#if>" />
        <#elseif button.type == "resetbutton">
          <input type="reset" name="${button.formRelativeUniqueName}" class="${button.styleClass!}"
                 value="<#if button.value?has_content>${button.value}<#else>${button.name}</#if>" />
        <#elseif button.type == "submitbutton">

          <button type="submit" name="${button.formRelativeUniqueName}">
                 <#if button.value?has_content>${button.value}<#else>${button.name}</#if>
          </button>
        <#else>
          <input type="button" name="${button.formRelativeUniqueName}" class="${button.styleClass!}"
                 value="<#if button.value?has_content>${button.value}<#else>${button.name}</#if>" />
        </#if>
      </#list>
      </div>

  </form>

</#if>

<#--
    //########################################################################
    //  HEADER CONTRIBUTIONS
    //########################################################################
-->

<@hst.headContribution keyHint="formValidationCss">
  <link rel="stylesheet" href="<@hst.webfile path="/js/eforms/formcheck/theme/blue/formcheck.css"/>" type="text/css" />
</@hst.headContribution>

<@hst.headContribution keyHint="jqueryUICss">
  <link rel="stylesheet" href="<@hst.webfile path="/css/eforms/jquery-ui-1.10.2.custom.min.css"/>" type="text/css" />
</@hst.headContribution>

<@hst.headContribution keyHint="jqueryDatetimePickerCss">
  <link rel="stylesheet" href="<@hst.webfile path="/css/eforms/jquery.datetimepicker-2.3.7.min.css"/>" type="text/css" />
</@hst.headContribution>

<@hst.headContribution keyHint="jquery">
  <script type="text/javascript" src="<@hst.webfile path="/js/eforms/jquery-1.9.1.min.js"/>"></script>
</@hst.headContribution>

<@hst.headContribution keyHint="jquery-datepicker">
  <script type="text/javascript" src="<@hst.webfile path="/js/eforms/jquery-ui-1.10.2.custom.min.js"/>"></script>
</@hst.headContribution>

<@hst.headContribution keyHint="jquery-datetimepicker">
  <script type="text/javascript" src="<@hst.webfile path="/js/eforms/jquery.datetimepicker.full-2.3.7.min.js"/>"></script>
</@hst.headContribution>

<@hst.headContribution keyHint="formJsValidation">
  <script type="text/javascript" src="<@hst.webfile path="/js/eforms/jquery-validate-1.1.2.min.js"/>"></script>
</@hst.headContribution>

<@hst.headContribution keyHint="formJsValidationHippo">
  <script type="text/javascript" src="<@hst.webfile path="/js/eforms/jquery-hippo-validate.js"/>"></script>
</@hst.headContribution>

<@hst.headContribution keyHint="formCss">
  <link rel="stylesheet" href="<@hst.webfile path="/css/eforms/eforms.css"/>" type="text/css" />
</@hst.headContribution>

<script type="text/javascript">
    <#if hstRequestContext.preferredLocale??>
  $.datetimepicker.setLocale('${hstRequestContext.preferredLocale.language}');
    </#if>

    $(document).ready(function() {

        $(function() {
            if ($( "#slider" )) {
                $("#submitButton").prop('disabled', true);
                $("#slider").slider({
                    stop: function (event, ui) {
                        var selection = $("#slider").slider("value");
                        var max = $("#slider").slider("option", "max");
                        if (selection >= 70) {
                            $("#slider").slider("value", max);
                            $('#slided').css({'display': 'block'});
                            $('#notSlided').css({'display': 'none'});
                            $("#sliderInput").prop("value", max);
                            $("#submitButton").prop('disabled', false);
                        }
                        else {
                            $("#slider").slider("value", 0);
                            $('#slided').css({'display': 'none'});
                            $('#notSlided').css({'display': 'block'});
                            $("#sliderInput").prop("value", "");
                            $("#submitButton").prop('disabled', true);
                        }
                    }
                });
            };
        });

        $('form[name="${form.name}"]').validate({errorElement:'div'});

        var resetPagesVisible = function() {
            var allPages = $('.eforms-page.conditionally-visible');
            var curPage = $('.eforms-page.conditionally-visible:visible:first');
            var curIndex = -1;

            for (var i = 0; i < allPages.length; i++) {
                if (allPages[i].id == curPage.attr('id')) {
                    curIndex = i;
                }
            }

            if (curIndex > 0) {
                $('#previousPageButton').show();
            }

            if (curIndex < allPages.length - 1) {
                $('#nextPageButton').show();
                $('.eforms-buttons input[type="submit"]').each(function() {
                    $(this).hide();
                });
            } else if (curIndex == allPages.length - 1) {
                $('#nextPageButton').hide();
                $('.eforms-buttons input[type="submit"]').each(function() {
                    $(this).show();
                });
            }

            $('#pagesTab li').hide();
            $('#pagesTab li.conditionally-visible').show();
        };


    <#--
      Function used to create parameters object containing form fields name/value pairs.
      The params object can be posted through ajax for validation.
    -->
        function addFormFieldsToParameters(fields, params) {
            fields.each(function() {
                var field = $(this);
                var fieldType = field.attr('type');
                var fieldName = field.attr('name');
                var checked = [];
                var checkedSelector = '.eforms-page.conditionally-visible:visible .eforms-field *:input[name="' + fieldName + '"]:checked';

                if (fieldType == 'checkbox') {
                    if (!params[fieldName]) {
                        checked = $(checkedSelector);
                        if (checked.length > 0) {
                            var values = [];
                            checked.each(function(index) {
                                values[index] = $(this).val();
                            });
                            params[fieldName] = values;
                        } else {
                            params[fieldName] = '';
                        }
                    }
                } else if (fieldType == 'radio') {
                    if (!params[fieldName]) {
                        checked = $(checkedSelector);
                        if (checked.length > 0) {
                            params[fieldName] = checked.val();
                        } else {
                            params[fieldName] = '';
                        }
                    }
                } else {
                    params[fieldName] = $(this).val();
                }
            });
        }

        function endsWith(subject, search) {
            var position = subject.length - search.length;
            var lastIndex = subject.indexOf(search, position);
            return lastIndex !== -1 && lastIndex === position;
        }

    <#-- real-time ajax-based single field validation -->
        var fields = $('.eforms-field *:input');
        var ajaxValidationUrl = '<@hst.resourceURL escapeXml=false resourceId="validation"/>';

        fields.blur(function() {
            // on leaving form field, post field name/value for ajax validation
            var params = {};
            var field = $(this);
            var fieldName = field.attr('name');
            var fieldType = field.attr('type');
            var fieldValue = field.val();
            var otherSuffix = '-other';

            // Radio groups and checkboxes have an option for showing a textfield if users wish to provide a different value
            // than is provided through the checkboxes/radio buttons. This value is always named <fieldName>-other and will only
            // be validated correctly over Ajax if both the field that enables/disables the '-other' textfield as well as the
            // '-other' textfield itself is submitted.
            if (endsWith(fieldName, otherSuffix)) {
                var prevFieldName = fieldName.substring(0, fieldName.length - otherSuffix.length);
                // check if the radio/checkbox that enables this '-other' field is checked
                var prevField = $('.eforms-field *:input[name=' + prevFieldName + '][value=-other]:checked');
                if (prevField.length) {
                    params[fieldName] = fieldValue;
                    params[prevFieldName] = otherSuffix;
                }
            } else if (fieldType === 'checkbox') {
                var checked = $('.eforms-field *:input[name=' + fieldName + ']:checked');
                if (checked.length > 0) {
                    var values = [];
                    checked.each(function (index) {
                        values[index] = $(this).val();
                    });
                    params[fieldName] = values;
                } else {
                    params[fieldName] = '';
                }
            } else {
                params[fieldName] = fieldValue;
            }

            if (params.length === 0) {
                // No fields to validate
                return;
            }

            $.post(ajaxValidationUrl, params,
                function(data) {
                    var feedbackPanel = $('#feedbackPanel');
                    var count = 0;
                    if (data) {
                        var messagesList = $('#feedbackPanel > ul');
                        for (var errorKey in data) {
                            if (data.hasOwnProperty(errorKey)) {
                                // get the error message
                                var errorMessage = data[errorKey];
                                // remove previous error messages from feedback panel
                                messagesList.empty();
                                if (errorMessage) {
                                    // add error message to feedback panel
                                    messagesList.append('<li>' + errorMessage.localizedMessage + '</li>');
                                    count++;
                                }
                            }
                        }
                    }
                    if (count > 0) {
                        // make feedback panel visible
                        feedbackPanel.show();
                    } else {
                        // make feedback panel invisible
                        feedbackPanel.hide();
                    }
                }, "json");
        });


    <#-- Write JSON of field condition infos -->
        var conditions = ${form.conditionsAsJsonString};
        var condFieldNames = {};

        if (conditions) {
            var items = [];
            if (conditions['fields']) {
                items = items.concat(conditions['fields']);
            }
            if (conditions['pages']) {
                items = items.concat(conditions['pages']);
            }
            for (var i = 0; i < items.length; i++) {
                var item = items[i];
                var condFieldName = item['condname'];
                if (!condFieldNames[condFieldName]) {
                    condFieldNames[condFieldName] = true;
                }
            }
        }

        for (var condFieldName in condFieldNames) {
            var condField = $('.eforms-field *[name="' + condFieldName + '"]');
            if (condField.length == 0) continue;
            var eventType = 'change';

            condField.bind(eventType, function() {
                if (conditions && conditions['fields']) {
                    var fields = conditions['fields'];

                    for (var i = 0; i < fields.length; i++) {
                        var field = fields[i];
                        var condFieldName = field['condname'];
                        if ($(this).attr('name') != condFieldName) continue;

                        var name = field['name'];
                        var targetField = $('.eforms-field *[name="' + name + '"]');
                        if (targetField.length == 0) {
                            targetField = $('.eforms-fieldgroup[name="' + name + '"]');
                        }
                        if (targetField.length == 0) {
                            targetField = $('.eforms-text[name="' + name + '"]');
                        }
                        if (targetField.length == 0) continue;

                        var targetContainer = targetField.parents('.eforms-field');
                        if (targetContainer.length == 0) {
                            targetContainer = targetField;
                        }

                        var type = field['condtype'];
                        var condFieldValue = field['condvalue'];
                        var condNegated = field['condnegated'];
                        var curSelectedValue = $(this).val();
                        if ($(this).is('input') && $(this).attr('type') == 'radio') {
                            curSelectedValue = $('.eforms-field *[name="' + condFieldName + '"]:radio:checked').val();
                        }

                        if (type == 'visibility') {
                            if ((!condNegated && condFieldValue == curSelectedValue)||(condNegated && condFieldValue != curSelectedValue)) {
                                targetContainer.show();
                            } else {
                                targetContainer.hide();
                            }
                        }
                    }

                    var pages = conditions['pages'];
                    for (var i = 0; i < pages.length; i++) {
                        var page = pages[i];
                        var condFieldName = page['condname'];
                        if ($(this).attr('name') != condFieldName) continue;

                        var pageIndex = page['index'];
                        var targetPage = $('#page' + pageIndex);
                        var type = page['condtype'];
                        var condFieldValue = page['condvalue'];
                        var condNegated = page['condnegated'];
                        var curSelectedValue = $(this).val();
                        if ($(this).is('input') && $(this).attr('type') == 'radio') {
                            curSelectedValue = $('.eforms-field *[name="' + condFieldName + '"]:radio:checked').val();
                        }

                        if (type == 'visibility') {
                            if ((!condNegated && condFieldValue == curSelectedValue)||(condNegated && condFieldValue != curSelectedValue)) {
                                targetPage.addClass('conditionally-visible');
                                $('#pagesTab li:nth-child(' + (pageIndex + 1) + ')').addClass('conditionally-visible');
                            } else {
                                targetPage.removeClass('conditionally-visible');
                                $('#pagesTab li:nth-child(' + (pageIndex + 1) + ')').removeClass('conditionally-visible');
                            }
                            resetPagesVisible();
                        }
                    }
                }
            });

            condField.trigger(eventType);
        }

    <#-- In order not to show page tab for script-disabled clients, show the tabs by script if exits. -->
        if ($('#pagesTab')) {
            $('#pagesTab').show();
        }
    <#-- Hide all the pages except of the first page -->
        $('.eforms-page').each(function() {
            $(this).hide();
        });
        if ($('.eforms-page.conditionally-visible').length) {
            $('.eforms-page.conditionally-visible:first').show();
        }

        resetPagesVisible();

        $('#previousPageButton').click(function() {
            var curPage = $('.eforms-page.conditionally-visible:visible');
            var prevPage = curPage.prevAll('.eforms-page.conditionally-visible:first');
            prevPage.show();
            curPage.hide();

            var curIndex = parseInt(curPage.attr('id').replace(/^page/, ''));
            var prevIndex = parseInt(prevPage.attr('id').replace(/^page/, ''));
            $('#pagesTab li:nth-child(' + (curIndex + 1) + ')').removeClass('selected');
            $('#pagesTab li:nth-child(' + (prevIndex + 1) + ')').addClass('selected');

            if (prevPage.prevAll('.eforms-page.conditionally-visible:first').length == 0) {
                $('#previousPageButton').hide();
            }
            $('#nextPageButton').show();
            $('.eforms-buttons input[type="submit"]').each(function() {
                $(this).hide();
            });

            // remove error messages from feedback panel
            var messagesList = $('#feedbackPanel > ul');
            messagesList.empty();

            // hide feedbackPanel
            var feedbackPanel = $('#feedbackPanel');
            feedbackPanel.hide();

        });

        $('#nextPageButton').click(function() {
            var curPage = $('.eforms-page.conditionally-visible:visible');

            // ajax based validation
            // validate all fields on current page before going to the next
            var params = {};
            var fieldsOnPage = $('.eforms-page.conditionally-visible:visible .eforms-field:visible *:input');
            addFormFieldsToParameters(fieldsOnPage, params);

            // add an empty parameter for any group on the current page
            var groupsOnPage = $('.eforms-page.conditionally-visible:visible .eforms-fieldgroup:visible');
            groupsOnPage.each(function() {
                params[$(this).attr('name')] = '';
            });

            // add current page index to parameters
            params['currentPage'] = curPage.attr('id').replace(/^page/, '');

            $.post(ajaxValidationUrl, params,
                function(data){

                    // remove previous error messages from feedback panel
                    var messagesList = $('#feedbackPanel > ul');
                    messagesList.empty();

                    var count = 0;
                    if (data) {
                        for (var fieldName in data) {
                            // get the error message
                            var errorMessage = data[fieldName];
                            if (errorMessage) {
                                // add error message to feedback panel
                                messagesList.append('<li>' + errorMessage.localizedMessage + '</li>');
                                count++;
                            }
                        }
                    }
                    var feedbackPanel = $('#feedbackPanel');
                    if (count > 0) {
                        // there are validation errors
                        // make feedback panel visible
                        feedbackPanel.show();
                    } else {
                        // no error messages
                        // make feedback panel invisible
                        feedbackPanel.hide();

                        // go to the next page
                        var nextPage = curPage.nextAll('.eforms-page.conditionally-visible:first');
                        nextPage.show();
                        curPage.hide();

                        var curIndex = parseInt(curPage.attr('id').replace(/^page/, ''));
                        var nextIndex = parseInt(nextPage.attr('id').replace(/^page/, ''));
                        $('#pagesTab li:nth-child(' + (curIndex + 1) + ')').removeClass('selected');
                        $('#pagesTab li:nth-child(' + (nextIndex + 1) + ')').addClass('selected');

                        $('#previousPageButton').show();
                        if (nextPage.nextAll('.eforms-page.conditionally-visible:first').length == 0) {
                            $('#nextPageButton').hide();
                            $('.eforms-buttons input[type="submit"]').each(function() {
                                $(this).show();
                            });
                        }

                    }

                }, "json");


        });


        var valid = false;

        // ajax page validation in case of last (or only) page
        $('form[name="${form.name}"]').submit(function(event) {

            var curPage = $('.eforms-page.conditionally-visible:visible');

            // if valid flag is set, page was validated and form can be submitted
            if (valid) {
                return true;
            }

            var params = {};
            var fieldsOnPage = $('.eforms-page.conditionally-visible:visible .eforms-field:visible *:input');
            addFormFieldsToParameters(fieldsOnPage, params);

            // add an empty parameter for any visible group on the current page
            var groupsOnPage = $('.eforms-page.conditionally-visible:visible .eforms-fieldgroup:visible');
            groupsOnPage.each(function() {
                params[$(this).attr('name')] = '';
            });

            // add current page index to parameters
            params['currentPage'] = curPage.attr('id').replace(/^page/, '');

            // prevent form submission as we want to do ajax validation first
            event.preventDefault();

            $.post(ajaxValidationUrl, params,
                function(data){

                    // remove previous error messages from feedback panel
                    var messagesList = $('#feedbackPanel > ul');
                    messagesList.empty();

                    var count = 0;
                    if (data) {
                        for (var fieldName in data) {
                            // get the error message
                            var errorMessage = data[fieldName];
                            if (errorMessage) {
                                // add error message to feedback panel
                                messagesList.append('<li>' + errorMessage.localizedMessage + '</li>');
                                count++;
                            }
                        }
                    }
                    var feedbackPanel = $('#feedbackPanel');
                    if (count > 0) {
                        // there are validation errors
                        // make feedback panel visible
                        feedbackPanel.show();

                    } else {
                        // no error messages
                        // make feedback panel invisible
                        feedbackPanel.hide();

                        // set valid flag and resubmit form
                        valid = true;
                        $('form[name="${form.name}"] input:submit').click();
                    }

                }, "json");

        });

    });
</script>

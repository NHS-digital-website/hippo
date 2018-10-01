<#ftl output_format="HTML" encoding="UTF-8">

<script>
$(document).ready(function() {

    // Cache DOM elements
    var $form = $('form[name="${form.name}"]');
    var $formEl = document.forms[name="${form.name}"];
    var $submitButton = $form.find('input:submit');
    var $previousButton = $('#previousPageButton');
    var $nextButton = $('#nextPageButton');
    var $errorWarning = $('#feedbackPanel');
    var $successMessage = $('.eforms-success-box');

    var ajaxValidationUrl = '<@hst.resourceURL escapeXml=false resourceId="validation"/>';
    var ajaxSubmissionUrl = '<@hst.actionURL escapeXml=false />';
    var valid = false;
    var userHitSubmit = false;

    $form.validate({errorElement:'div'});

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
          $previousButton.show();
        }

        if (curIndex < allPages.length - 1) {
          $nextButton.show();
          $('.recaptcha').hide();
          $('.eforms-buttons input[type="submit"]').each(function() {
            $(this).hide();
          });
        } else if (curIndex == allPages.length - 1) {
          $nextButton.hide();
          $('.eforms-buttons input[type="submit"]').each(function() {
            $(this).show();
          });

          // Show ReCaptcha
          displayRecaptcha();
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
            var checkedSelector = '*:input[name="' + fieldName + '"]:checked';

            if (fieldType == 'checkbox') {
                if (!params[fieldName]) {
                    checked = $(checkedSelector);

                    if (checked.length > 0) {
                        var values = [];
                        checked.each(function(index) {
                            values[index] = $(this).val();
                        });
                        params[fieldName] = "" + values + "";
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


    <#-- Write JSON of field condition infos -->
    var conditions = ${form.conditionsAsJsonString?no_esc};
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
    if ($('#pagesTab').length) {
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

    // Handle previous button click
    $previousButton.click(function() {
        $previousButton.blur();
        hideRecaptcha();

        var curPage = $('.eforms-page.conditionally-visible:visible');
        var prevPage = curPage.prevAll('.eforms-page.conditionally-visible:first');
        
        prevPage.show();
        curPage.hide();

        var curIndex = parseInt(curPage.attr('id').replace(/^page/, ''));
        var prevIndex = parseInt(prevPage.attr('id').replace(/^page/, ''));
        $('#pagesTab li:nth-child(' + (curIndex + 1) + ')').removeClass('selected');
        $('#pagesTab li:nth-child(' + (prevIndex + 1) + ')').addClass('selected');

        if (prevPage.prevAll('.eforms-page.conditionally-visible:first').length == 0) {
            $previousButton.hide();
        }
        
        $nextButton.show();
        $('.eforms-buttons input[type="submit"]').each(function() {
            $(this).hide();
        });

        hideErrors();
    });

    // Handle next button click
    $nextButton.click(function() {
        $nextButton.blur();

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

        validateUserInput(params, function() {
            // go to the next page
            var nextPage = curPage.nextAll('.eforms-page.conditionally-visible:first');
            nextPage.show();
            curPage.hide();

            var curIndex = parseInt(curPage.attr('id').replace(/^page/, ''));
            var nextIndex = parseInt(nextPage.attr('id').replace(/^page/, ''));
            $('#pagesTab li:nth-child(' + (curIndex + 1) + ')').removeClass('selected');
            $('#pagesTab li:nth-child(' + (nextIndex + 1) + ')').addClass('selected');

            $previousButton.show();
            if (nextPage.nextAll('.eforms-page.conditionally-visible:first').length == 0) {
                $nextButton.hide();
                $('.eforms-buttons input[type="submit"]').each(function() {
                    $(this).show();
                });
                // Show ReCaptcha
                displayRecaptcha();
                grecaptcha.reset();
            }
        });
        return;
    });

    function validateUserInput(params, successCallback, errorCallback) {
        hideErrors();

        $.ajax({
            type: 'POST',
            url: ajaxValidationUrl,
            data: params,
            success: function(response) {
                if(typeof successCallback === 'function') {
                    successCallback();
                }
            },
            error: function(response) {
                var errorObject = JSON.parse(response.responseText.replace(/&quot;/g, '"').replace(/&#39;/g, '\''));

                showErrors(errorObject);
                $(window).scrollTop($errorWarning.offset().top);

                if(typeof errorCallback === 'function') {
                    errorCallback();
                }
            },
            dataType: 'JSON'
        });
    }

    function submitForm(params) {
        $form.addClass('disabled');

        $.post({
            url: ajaxSubmissionUrl,
            data: params,
            success: function(response) {
                $form.attr('disabled', 'disabled');
                
                userHitSubmit = false;
                reset();
                showSuccessMessage();
            },
            error: function(response) {
                // do something with the response
                $(window).scrollTop($errorWarning.offset().top);
                $errorWarning.show();
                $form.removeClass('disabled');

                grecaptcha.reset();
                displayRecaptcha();
            }
        });
    }

    /* Show success message and hide everything else*/
    function showSuccessMessage() {
        $successMessage.removeClass('visually-hidden');

        $form.remove();
    }

    /* Show the error messages */
    function showErrors(errors) {
        if (errors) {
            $errorWarning.show();

            for (var r in errors) {
                var input = $form.find('[name="' + r + '"]');
                var isMultiField = (input.attr('type') == 'checkbox' || input.attr('type') == 'radio');

                if (input.length > 0) {
                    var eformsField = input.parent();
                    
                    if (isMultiField) {
                        eformsField = input.parent().parent().parent().parent();
                    }

                    var errorField = eformsField.find('.eforms-field__error-message');

                    eformsField.addClass('eforms-field--invalid');
                    errorField.html(errors[r].localizedMessage);
                    errorField.removeClass('visually-hidden');

                    if (isMultiField) {
                        continue;
                    }
                }
            }
        }
    }

    /* Hide the error messages */
    function hideErrors() {
        $invalidFields = $form.find('.eforms-field--invalid');
        
        $invalidFields.each(function(index, el) {
            $(el).removeClass('eforms-field--invalid');
            var $errorField = $(el).find('.eforms-field__error-message');
            $errorField.addClass('visually-hidden');
            $errorField.html('');
        });

        $errorWarning.hide();
    }

    /* Reset the form */
    function reset() {
        $formEl.reset();
        grecaptcha.reset();
    }

    function enableSubmit() {
        $submitButton.prop('disabled', false);
    }

    function displayRecaptcha() {
        $('.recaptcha').show();
        $submitButton.prop('disabled', true);
    }

    function hideRecaptcha() {
        $('.recaptcha').hide();
        $submitButton.prop('disabled', false);
    }

    // directly assign into the global space
    window.enableSubmit = enableSubmit;
    window.hideRecaptcha = hideRecaptcha;
    window.displayRecaptcha = displayRecaptcha;

    /* Handle the form submit click */
    $submitButton.click(function(event) {
        userHitSubmit = true;

        event.preventDefault();
        $submitButton.blur();

        var curPage = $('.eforms-page.conditionally-visible:visible');

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

        validateUserInput(params, function() {
            if (curPage.attr('id') != 'page0') {
                // Collate input from all pages since above params just includes last page
                params = {};
                var allFields = $('.eforms-field *:input');
                addFormFieldsToParameters(allFields, params);
            }
            params['gRecaptchaResponse'] = $("#g-recaptcha-response").val();
            submitForm(params);
        });
    });

    // Are you a Robot?
    $(function() {
        var $slider = $("#slider");
        if ($slider.length) {
            $submitButton.prop('disabled', true);
            
            $slider.slider({
                stop: function (event, ui) {
                    var selection = $slider.slider("value");
                    var max = $slider.slider("option", "max");
                    
                    if (selection >= 70) {
                        $slider.slider("value", max);
                        $('#slided').css({'display': 'block'});
                        $('#notSlided').css({'display': 'none'});
                        $("#sliderInput").prop("value", max);
                        $submitButton.prop('disabled', false);
                    } else {
                        $slider.slider("value", 0);
                        $('#slided').css({'display': 'none'});
                        $('#notSlided').css({'display': 'block'});
                        $("#sliderInput").prop("value", "");
                        $submitButton.prop('disabled', true);
                    }
                }
            });
        };
    });

});
</script>

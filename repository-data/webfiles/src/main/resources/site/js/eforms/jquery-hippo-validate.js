/*
 * Copyright 2014 Hippo B.V. (http://www.onehippo.com)
 */

jQuery.validateExtend({
    fileSizeAndExtension: {

        conditional: function (value, options) {
            // check support:
            if (!window.File) {
                console.log('No front end file validation possible for this browser');
                return;
            }

            var messages = [];

            var field = $(this);
            var allowedExtensions = field.attr('data-allowed-extensions');
            var maxSize = field.attr('data-max-size');
            console.log('Validating against allowed extensions "' + allowedExtensions + '" and max size ' + maxSize);
            if (field && field[0]) {
                var files = field[0].files;
                for (var i = 0; i < files.length; i++) {
                    var file = files[i];
                    var name = file.name;
                    if (maxSize) {
                        var size = file.size/1048576;
                        size = Math.round(size * 100) / 100;
                        if (size > maxSize) {
                            console.log('Invalidated: file ' + name + ' with size ' + size + ' is bigger than max size ' + maxSize);
                            messages.push('Size ' + size + ' of file "' + name + '" is greater than ' + maxSize + ' MB');
                        }
                        else {
                            console.log('Validated OK: file ' + name + ' with size ' + size + ' confirms to max size ' + maxSize);
                        }
                    }

                    // check allowed extensions
                    if (allowedExtensions && allowedExtensions.length > 0) {

                        var nameParts = name.split('.');
                        if (nameParts.length > 1) {
                            var extension = nameParts[nameParts.length - 1].toLowerCase();
                            if (allowedExtensions.indexOf(extension) < 0) {
                                console.log('Invalidated: extension ' + extension + ' of file + ' + name + ' is not part of allowed list ' + allowedExtensions);
                                messages.push('Extension "' + extension + '" of file "' + name + '" is not allowed.');
                            }
                            else {
                                console.log('Validated OK: extension ' + extension + ' of file + ' + name + ' is part of allowed list ' + allowedExtensions);
                            }
                        }
                    }
                }
            }

            // add messages to feedback panel and show it
            if (messages.length > 0) {
                var feedbackPanel = $('#feedbackPanel');
                if (!feedbackPanel) {
                    console.error('Cannot show validation messages because there is no element with id "feedbackPanel"');
                }
                else {
                    var messagesList = $('#feedbackPanel > ul');
                    if (messagesList) {
                        messagesList.empty();

                        for (var j = 0; j < messages.length; j++) {
                            messagesList.append('<li>' + messages[j] + '</li>');
                        }

                        feedbackPanel.show();
                    }
                }

                return false;
            }

            return true;
        }
    }
});

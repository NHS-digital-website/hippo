/*
 * Copyright 2014 Hippo B.V. (http://www.onehippo.com)
 */

/**
 * CUSTOMISED by Jodi Warren
 *
 * If you update, please make sure you export the function
 */
export default function(jQuery) {
    jQuery.validateExtend({
        fileSizeAndExtension: {

            conditional (value, options) {
                // check support:
                if (!window.File) {
                    console.log('No front end file validation possible for this browser');
                    return;
                }

                const messages = [];

                const field = $(this);
                const allowedExtensions = field.attr('data-allowed-extensions');
                const maxSize = field.attr('data-max-size');
                console.log(`Validating against allowed extensions "${  allowedExtensions  }" and max size ${  maxSize}`);
                if (field && field[0]) {
                    const {files} = field[0];
                    for (let i = 0; i < files.length; i++) {
                        const file = files[i];
                        const {name} = file;
                        if (maxSize) {
                            let size = file.size / 1048576;
                            size = Math.round(size * 100) / 100;
                            if (size > maxSize) {
                                console.log(`Invalidated: file ${  name  } with size ${  size  } is bigger than max size ${  maxSize}`);
                                messages.push(`Size ${  size  } of file "${  name  }" is greater than ${  maxSize  } MB`);
                            } else {
                                console.log(`Validated OK: file ${  name  } with size ${  size  } confirms to max size ${  maxSize}`);
                            }
                        }

                        // check allowed extensions
                        if (allowedExtensions && allowedExtensions.length > 0) {

                            const nameParts = name.split('.');
                            if (nameParts.length > 1) {
                                const extension = nameParts[nameParts.length - 1].toLowerCase();
                                if (allowedExtensions.indexOf(extension) < 0) {
                                    console.log(`Invalidated: extension ${  extension  } of file + ${  name  } is not part of allowed list ${  allowedExtensions}`);
                                    messages.push(`Extension "${  extension  }" of file "${  name  }" is not allowed.`);
                                } else {
                                    console.log(`Validated OK: extension ${  extension  } of file + ${  name  } is part of allowed list ${  allowedExtensions}`);
                                }
                            }
                        }
                    }
                }

                // add messages to feedback panel and show it
                if (messages.length > 0) {
                    const feedbackPanel = $('#feedbackPanel');
                    if (!feedbackPanel) {
                        console.error('Cannot show validation messages because there is no element with id "feedbackPanel"');
                    } else {
                        const messagesList = $('#feedbackPanel > ul');
                        if (messagesList) {
                            messagesList.empty();

                            for (let j = 0; j < messages.length; j++) {
                                messagesList.append(`<li>${  messages[j]  }</li>`);
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
}

jQuery(function () {
    jQuery('.code-block').each(function (index, elm) {
        if (!navigator.clipboard) {
            // No browser support.
            jQuery(elm).find('.button-code-block').remove()
        } else {
            jQuery(elm).find('.button-code-block').click(function (e) {
                var btn = $(this);
                navigator.clipboard.writeText(jQuery(elm).find('pre').text()).then(function() {
                    btn.addClass("button-code-block-done");
                    setTimeout(function() {
                        btn.removeClass("button-code-block-done");
                    }, 1200)
                }, function(err) {
                    btn.text(' Could not copy code snip-it! ');
                });
            });
        }
    })
});

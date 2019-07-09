/**
 * Suppress long breadcrumbs
 *
 * @author Geoff Hayward
 */
jQuery(function(){
    let ellipseLabel = "Suppressed breadcrumbs, click to expand breadcrumbs";
    let html;

    function expandBreadcrumbs(breadcrumbs) {
        breadcrumbs.closest('ol').html(html);
    }
    function reset(breadcrumbs) {
        breadcrumbs.find("li").show();
        breadcrumbs.find(".breadcrumb__link").text(function () {
            return jQuery(this).data("text");
        });
    }
    function suppress(breadcrumbs, crumb, upperBound, canTryShortingMore) {
        if (breadcrumbs.find("li:first").height() != breadcrumbs.height()) {
            if (crumb < upperBound - 2) {
                if (crumb == 1) {
                    breadcrumbs.find("li:nth-child(" + (++crumb) + ")").find("a").html("&hellip;").attr('aria-label', ellipseLabel).attr('title', ellipseLabel).on('click', function (e){
                        e.preventDefault();
                        expandBreadcrumbs(breadcrumbs);
                    });
                } else {
                    breadcrumbs.find("li:nth-child(" + (++crumb) + ")").hide();
                }
                suppress(breadcrumbs, crumb, upperBound, canTryShortingMore);
            } else {
                if (canTryShortingMore) {
                    reset(breadcrumbs)
                    breadcrumbs.find("li:nth-child(" + (upperBound) + ")").find("span").html("This page");
                    suppress(breadcrumbs, 1, upperBound, false);
                }
            }
        }
    }
    jQuery(".breadcrumb").each(function (index, elm) {
        html = elm.innerHTML;
        let breadcrumbs = jQuery(this);
        let size = jQuery(this).find("li").length;
        if(size > 3) {
            jQuery(window).resize(function () {
                reset(breadcrumbs);
                suppress(breadcrumbs, 1, size, true);
            });
            suppress(breadcrumbs, 1, size, true);
        }
    });
});

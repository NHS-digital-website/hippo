/**
 * Suppress long breadcrumbs
 *
 * @author Geoff Hayward
 */
jQuery(function(){
    function reset(breadcrumbs) {
        breadcrumbs.find("li").show();
        breadcrumbs.find(".breadcrumb__link").text(function () {
            return jQuery(this).data("text");
        });
    }
    function suppress(breadcrumbs, crumb, upperBound, canTryShortingMore) {
        if (breadcrumbs.find("li:first").height() != breadcrumbs.height()) {
            if(crumb < upperBound - 2) {
                if (crumb == 1) {
                    breadcrumbs.find("li:nth-child(" + (++crumb) + ")").find("a").html("&hellip;");
                } else {
                    breadcrumbs.find("li:nth-child(" + (++crumb) + ")").hide();
                }
                suppress(breadcrumbs, crumb, upperBound, canTryShortingMore);
            } else {
                if(canTryShortingMore){
                    reset(breadcrumbs)
                    breadcrumbs.find("li:nth-child(" + (upperBound) + ")").find("span").html("This page");
                    suppress(breadcrumbs, 1, upperBound, false);
                }
            }
        }
    }
    jQuery(".breadcrumb").each(function () {
        var breadcrumbs = jQuery(this);
        var size = jQuery(this).find("li").length;
        if(size > 3) {
            jQuery(window).resize(function () {
                reset(breadcrumbs);
                suppress(breadcrumbs, 1, size, true);
            });
            suppress(breadcrumbs, 1, size, true);
        }
    });
});

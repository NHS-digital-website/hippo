<#ftl output_format="HTML">

<#include "resize-sensor.js.ftl"/>
<#include "scroll-spy.js.ftl"/>
<#include "sticky-sidebar.js.ftl"/>

<script>
    var stickyNav = document.querySelector('#sticky-nav');
    var chapterNav = document.querySelector('.chapter-pagination-wrapper');

    function getStickyOffset() {
        var CHAPTER_NAV_SPACING = 24;
        var SIDEBAR_SPACING = 20;

        return chapterNav ? (chapterNav.offsetHeight + (CHAPTER_NAV_SPACING + SIDEBAR_SPACING)) : SIDEBAR_SPACING;
    }

    if (stickyNav) {
        var sidebar = new StickySidebar('#sticky-nav', {
            topSpacing: getStickyOffset(),
            bottomSpacing: 40,
            resizeSensor: true,
            containerSelector: '.grid-row'
        });

        $(window).on('load resize', function () {
            if (window.matchMedia('(max-width: 924px)').matches) {
                sidebar.destroy();
            } else {
                sidebar.initialize();
            }
        });

        $('.article-section-nav__list a').click(function () {
            var match = jQuery(this).attr('href').match(/#\S+/);
            var url = match ? location.pathname + match[0] : location.pathname;
            logGoogleAnalyticsEvent('Link click', 'Section Nav', url);
        });
    }
</script>

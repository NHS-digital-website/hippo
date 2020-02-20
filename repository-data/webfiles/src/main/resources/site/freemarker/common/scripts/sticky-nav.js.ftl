<#ftl output_format="HTML">

<#include "resize-sensor.js.ftl"/>
<#include "scroll-spy.js.ftl"/>
<#include "sticky-sidebar.js.ftl"/>

<script>
    var DOMElement = $('#sticky-nav');
    if(DOMElement.length) {
        var sidebar = new StickySidebar('#sticky-nav', {
            topSpacing: 20,
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

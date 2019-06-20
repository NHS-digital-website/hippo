<#ftl output_format="HTML">

<#include "resize-sensor.js.ftl"/>
<#include "scroll-spy.js.ftl"/>
<#include "sticky-sidebar.js.ftl"/>

<script>
    var DOMElement = $('#sticky-nav');
    var sidebar = new StickySidebar('#sticky-nav', {
        topSpacing: 20,
        bottomSpacing: 40,
        resizeSensor: true,
        containerSelector: '.grid-row'
    });

    $(window).on('load resize', function() {
        if (window.matchMedia('(max-width: 924px)').matches) {
            sidebar.destroy();

            DOMElement.removeAttr('id');
        } else {
            sidebar.initialize();
            DOMElement.attr('id', 'sticky-nav');
        }
    });

    $('.article-section-nav__list a').click(function() {
        var match = jQuery(this).attr('href').match(/#\S+/);
        logGoogleAnalyticsEvent('Link click','Section Nav',location.pathname + match[0]);
    });
</script>

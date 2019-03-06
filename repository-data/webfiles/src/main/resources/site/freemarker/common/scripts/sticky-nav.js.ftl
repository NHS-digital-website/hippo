<#ftl output_format="HTML">

<#include "sticky-sidebar.js.ftl"/>
<#include "scrollspy.js.ftl"/>

<script>
    (function(){
        if (window.matchMedia('(max-width: 924px)').matches) {
            return;
        }

        var sidebar = new StickySidebar('#sticky-nav', {
            topSpacing: 20,
            bottomSpacing: 40,
            containerSelector: '.grid-row'
        });

        var spy = new ScrollSpy('.article-section-nav nav', {
            nav: '.article-section-nav__list > li > a',
            className: 'active',
        });
    }());

    $('.article-section-nav__list a').click(function() {
        var match = jQuery(this).attr('href').match(/#\S+/);
        logGoogleAnalyticsEvent('Link click','Section Nav',location.pathname + match[0]);
    });
</script>
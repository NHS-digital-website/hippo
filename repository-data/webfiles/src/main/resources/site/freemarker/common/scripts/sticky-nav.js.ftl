<script src="https://cdn.jsdelivr.net/npm/sticky-sidebar@3.3.1/dist/sticky-sidebar.min.js"></script>
<script src="https://cdn.rawgit.com/makotot/scrollspy/master/build/scrollspy.js"></script>

<script>
    var sidebar = new StickySidebar('#sticky-nav', {
        topSpacing: 20,
        bottomSpacing: 40,
        containerSelector: '.grid-row'
    });

    var spy = new ScrollSpy('.article-section-nav nav', {
        nav: '.article-section-nav__list > li > a',
        className: 'active',
    });

    $('.article-section-nav__list a').click(function() {
        var match = jQuery(this).attr('href').match(/#\S+/);
        logGoogleAnalyticsEvent('Link click','Section Nav',location.pathname + match[0]);
    });
</script>
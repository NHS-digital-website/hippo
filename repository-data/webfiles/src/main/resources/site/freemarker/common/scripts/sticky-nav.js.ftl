<#ftl output_format="HTML">

<script>
    (function() {
        var vjsUtils = window.vanillaJSUtils;
        var oldBrowser = window.addEventListener ? false : true;
        var $stickyNav = document.querySelectorAll('#sticky-nav')[0];

        var $sidebarSection = document.querySelectorAll('.page-block--sidebar')[0];
        var $mainSection = document.querySelectorAll('.page-block--main')[0];

        if (typeof $stickyNav === 'undefined' || typeof $mainSection === 'undefined' || typeof $sidebarSection === 'undefined') {
            return false;
        }
        var $sectionWrapper = $mainSection.parentNode;
        var $navTitle = $stickyNav.querySelectorAll('.article-section-nav__title')[0];

        var windowHeight = wrapperHeight = mainHeight = wrapperOffsetY = cachedScrollY = posY = navOffsetY = wrapperWidth = layoutWidth = 0;
        var ticking = contentTallerThanNav = false;

        var MODES = {
            CONTAIN: 'CONTAIN',
            OVERFLOW: 'OVERFLOW'
        };
        var mode = MODES.SMALL;

        var BREAKPOINTS = {
            DESKTOP_MIN: 924
        };

        var POSITIONS = {
            STICKY_WINDOW_TOP: 'STICKY WINDOW TOP',
            STICKY_CONTENT_BOTTOM: 'STICKY CONTENT BOTTOM',
            STICKY_WINDOW_BOTTOM: 'STICKY WINDOW BOTTOM',
            NATURAL_SCROLL: 'NATURAL SCROLL'
        };
        var position = POSITIONS.NATURAL_SCROLL;

        function init() {
            if (!oldBrowser) {
                window.addEventListener('resize', handleResize, { passive: true });
                window.addEventListener('scroll', handleScroll, { passive: true });
            } else {
                window.attachEvent('onresize', handleResize);
                window.attachEvent('onscroll', handleScroll);
            }

            handleScroll();
            handleResize();
        }

        function updateNav(scrollY) {
            mode = windowHeight > wrapperHeight ? MODES.CONTAIN : MODES.OVERFLOW;

            // Screnario 1 - Naturally scroll with the content until the top of the nav reaches the top of window
            $stickyNav.style.position = 'relative';
            posY = 0;
            position = POSITIONS.NATURAL_SCROLL;

            if (mode === MODES.CONTAIN) {
                if (scrollY >= wrapperOffsetY + mainHeight - wrapperHeight) {
                    // When reach the bottom of the content => stick to bottom of the content

                    $stickyNav.style.position = 'relative';
                    position = POSITIONS.STICKY_CONTENT_BOTTOM;

                    if (contentTallerThanNav) {
                        posY = mainHeight - wrapperHeight;
                    }
                } else if (scrollY >= wrapperOffsetY) {
                    // Stick to top once reached the window top until

                    $stickyNav.style.position = 'fixed';
                    posY = -1 * wrapperOffsetY;
                    position = POSITIONS.STICKY_WINDOW_TOP;
                }
            } else {
                if (scrollY >= wrapperOffsetY + mainHeight - windowHeight) {
                    // When reach the bottom of the content => stick to bottom of the content

                    $stickyNav.style.position = 'relative';
                    position = POSITIONS.STICKY_CONTENT_BOTTOM;

                    if (contentTallerThanNav) {
                        posY = mainHeight - wrapperHeight;
                    }
                } else if (scrollY >= wrapperOffsetY + wrapperHeight - windowHeight) {
                    // When reach the bottom of the nav => stick to bottom of the window

                    $stickyNav.style.position = 'fixed';
                    position = POSITIONS.STICKY_WINDOW_BOTTOM;
                    posY = -1 * (wrapperOffsetY + wrapperHeight - windowHeight);
                }
            }

            applyPosY(posY);
        }

        function applyPosY(posY) {
            var transform = 'translateY(' + posY + 'px)';
            $stickyNav.style['-webkit-transform'] = transform;
            $stickyNav.style['-moz-transform'] = transform;
            $stickyNav.style['-o-transform'] = transform;
            $stickyNav.style['-ms-transform'] = transform;
            $stickyNav.style['transform'] = transform;
        }

        function scrollTop() {
            return document.body.scrollTop || document.documentElement.scrollTop || window.pageYOffset || 0;
        }

        function handleResize() {
            windowHeight = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
            navOffsetY = typeof $navTitle !== 'undefined' ? $navTitle.offsetTop : 0;
            wrapperHeight = $stickyNav.offsetHeight;
            mainHeight = $mainSection.offsetHeight;
            wrapperOffsetY = $sectionWrapper.offsetTop;
            contentTallerThanNav = mainHeight > wrapperHeight;
            wrapperWidth = 'auto';
            layoutWidth = document.body.clientWidth;

            if (layoutWidth > BREAKPOINTS.DESKTOP_MIN) {
                wrapperWidth = Math.round($stickyNav.parentNode.parentNode.offsetWidth * 0.333) + 'px';
                cachedScrollY = scrollTop();
                updateNav(cachedScrollY);
            } else {
                $stickyNav.style.position = 'relative';
                applyPosY(0);
            }

            $stickyNav.style.width = wrapperWidth;
        }

        function handleScroll() {
            if (layoutWidth > BREAKPOINTS.DESKTOP_MIN) {
                cachedScrollY = scrollTop();

                if (typeof window.requestAnimationFrame !== 'undefined') {
                    if (!ticking) {
                        window.requestAnimationFrame(function() {
                            updateNav(cachedScrollY);
                            ticking = false;
                        });

                        ticking = true;
                    }
                } else {
                    updateNav(cachedScrollY);
                }
            } else {
                $stickyNav.style.position = 'relative';
                cachedScrollY = 0;
                updateNav(0);
            }
        }

        // Change Active menu item on page scroll
        // Cache selectors
        var lastId,
            stickyNav = $("#sticky-nav"),
            // All list items
            menuItems = stickyNav.find('a[href^="#"]'),

            // Anchors corresponding to menu items
            scrollItems = menuItems.map(function(){
                var item = $($(this).attr("href"));
                if (item.length) { return item;}
            });

        // Bind to scroll
        $(window).scroll(function(){

            // Get container scroll position
            var fromTop = $(this).scrollTop()+10;

            // Get id of current scroll item
            var cur = scrollItems.map(function(){
                if ($(this).offset().top < fromTop)
                    return this;
            });

            // get id of the current element
            cur = cur[cur.length-1];
            var id = cur && cur.length ? cur[0].id : "";

            if (lastId !== id) {
                lastId = id;

                // Set/remove active class
                menuItems
                    .parent().removeClass("active")
                    .end().filter("[href='#" + id + "']").parent().addClass("active");
            }
        });

        $('.article-section-nav__list a').click(function() {
            var match = jQuery(this).attr('href').match(/#\S+/);
            logGoogleAnalyticsEvent('Link click','Section Nav',location.pathname + match[0]);
        });

        init();
    }());
</script>

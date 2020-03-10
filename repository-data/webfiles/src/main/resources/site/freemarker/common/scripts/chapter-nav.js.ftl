<#ftl output_format="HTML">

<script>

    var stickChapterNav = (function ($) {
        'use strict';

        var chapterNavElement;
        var chapterNavDimensions;
        var chapterNavTop;
        var chapterPlaceholder;

        function testStickySupported() {
            var prefix = ['', '-o-', '-webkit-', '-moz-', '-ms-'];
            var test = document.head.style;
            for (var i = 0; i < prefix.length; i += 1) {
                test.position = prefix[i] + 'sticky';
            }
            var stickySupported = test.position ? true : false;
            test.position = '';
            return stickySupported;
        }

        function createPlaceholder() {
            if (!chapterPlaceholder) {
                chapterPlaceholder = document.createElement('div');
                chapterPlaceholder.style.position = 'relative';
                chapterPlaceholder.style.display = 'none';
                chapterPlaceholder.classList.add('chapter-nav-placeholder');
                chapterNavElement.insertAdjacentElement('beforebegin', chapterPlaceholder);
            }
            chapterPlaceholder.style.height = chapterNavDimensions.height + 'px';
        }

        function stick() {
            chapterNavElement.classList.add('sticky');
            chapterPlaceholder.style.display = 'block';
        }

        function unstickTop() {
            chapterNavElement.classList.remove('sticky');
            chapterPlaceholder.style.display = 'none';
        }

        function unstickBottom() {

        }

        function handleScroll() {
            if (window.pageYOffset > chapterNavTop) {
                requestAnimationFrame(stick)
            } else {
                requestAnimationFrame(unstickTop)
            }
        }

        function stopWatching() {
            document.removeEventListener('scroll', handleScroll);
            if (chapterPlaceholder) {
                chapterPlaceholder.destroy();
                chapterPlaceholder = null;
            }
        }

        function watchAndStick() {
            console.log('WATCH AND STICK');
            chapterNavDimensions = chapterNavElement.getBoundingClientRect();
            chapterNavTop = chapterNavDimensions.top + window.pageYOffset;
            console.log('chapterNavTopGap', chapterNavDimensions.top);
            console.log('chapterNavTop', chapterNavTop);
            createPlaceholder();
            document.addEventListener('scroll', handleScroll);
        }

        function init() {
            chapterNavElement = document.querySelector('.chapter-pagination-wrapper');
            if (!chapterNavElement) {
                return;
            }

            document.documentElement.style.scrollPaddingTop = chapterNavElement.offsetHeight + 'px';
            if (!testStickySupported()) {
                watchAndStick();
            }
        }

        return {
            init: init,
            watch: watchAndStick,
            stop: stopWatching
        };
    }());


    var desktopQuery = window.matchMedia('(min-width: 924px)');
    var wideQuery = window.matchMedia('(min-width: 1520px)');

    function handleDesktopQuery(event) {
        if (event.matches) {
            stickChapterNav.init();
        } else {
            stickChapterNav.stop();
        }
    }

    function handleWideQuery(event) {
        if (event.matches) {
            stickChapterNav.init();
        }
    }

    desktopQuery.addListener(handleDesktopQuery);
    wideQuery.addListener(handleWideQuery);
    handleDesktopQuery(desktopQuery);

</script>

<#ftl output_format="HTML">

<script>
    /**
     * Simple-ish implementation of a scroll spy for NHS Digital's Sticky Nav's section tracking status.
     * @author Geoff Hayward
     *
     * Attributions
     *  - The solution is based on https://codepen.io/zchee/pen/ogzvZZ.
     *  - The method getPosition() came from https://stackoverflow.com/a/53351648.
     */
    (function() {
        'use strict';
        var section = document.querySelectorAll(".article-section, .article-section-separator, .article-section--summary-separator");
        var sections = { top : 0 };

        const MARGIN_OF_ERROR = 2; //used to 'adjust/falsify' element position for marking while click

        function getPosition(element) {
            var distance = -MARGIN_OF_ERROR;
            do {
                distance += element.offsetTop;
                element = element.offsetParent;
            } while (element);
            return distance;
        }
        function calculate() {
            Array.prototype.forEach.call(section, function(e) {
                if (e.id != ""){
                    sections[e.id] = getPosition(e);
                }
            });
        }

        function markStickyNavElem(navElem){
            if (document.querySelector('.active') != null) document.querySelector('.active').setAttribute('class', ' ');
            if (document.querySelector('a[href*=' + navElem + ']') != null) document.querySelector('a[href*=' + navElem + ']').setAttribute('class', 'active');
        }

        function getBottomStickyNavElement(sections) {
            //if no sections in sticky nav return immediatelly
            if (sections == null || sections.length == 0) {
              return null;
            }

            //sort sections descending by value / second element

            //create temp array to sort dict by second element
            var sectionsArray = Object.keys(sections).map(function(key) {
              return [key, sections[key]];
            });

            // Sort the array descending based on the second element
            sectionsArray.sort(function(first, second) {
              return second[1] - first[1];
            });

            //return first element after sorting by position / which is bottom 
            // element of the the sticky nav
            return sectionsArray[0][0];
        }

        function marker() {
            var scrollPosition = document.documentElement.scrollTop || document.body.scrollTop;
            for (var navElem in sections) {
                if (navElem != null && navElem != "" && sections[navElem] <= scrollPosition) {
                  markStickyNavElem(navElem);
                }
            }

            //if scrolled to the bottom (with margin of error) => mark last section
            if($(window).scrollTop() + $(window).height() > $(document).height() - MARGIN_OF_ERROR) {

                //mark last element on the sticky nav
                navElem = getBottomStickyNavElement(sections);
                if (navElem != null && navElem != "") {
                  markStickyNavElem(navElem);
                }
            }

        }
        $(window).on('load resize scroll', function(e) {
            if(e.type == 'resize' || e.type == 'load') {
                calculate();
            }
            marker();
        });
        new ResizeSensor(document.getElementsByClassName("page-block--main")[0], function(){
            calculate();
        });
    })();
</script>

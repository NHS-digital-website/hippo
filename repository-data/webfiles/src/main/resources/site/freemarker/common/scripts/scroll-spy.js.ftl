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
        var section = document.querySelectorAll(".article-section");
        var sections = { top : 0 };
        function getPosition(element) {
            var distance = 0;
            do {
                distance += element.offsetTop;
                element = element.offsetParent;
            } while (element);
            return distance < 0 ? 0 : distance;
        }
        function calculate() {
            Array.prototype.forEach.call(section, function(e) {
                if (e.id != ""){
                    sections[e.id] = getPosition(e);
                }
            });
        }
        function marker() {
            var scrollPosition = document.documentElement.scrollTop || document.body.scrollTop;
            for (var i in sections) {
                if (sections[i] <= scrollPosition && i != null && i != "") {
                    if (document.querySelector('.active') != null) document.querySelector('.active').setAttribute('class', ' ');
                    if (document.querySelector('a[href*=' + i + ']') != null) document.querySelector('a[href*=' + i + ']').setAttribute('class', 'active');
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

<#ftl output_format="HTML">

<#-- based on https://stackoverflow.com/a/29919212  -->

<script>
    $(function () {
        $('img[src$=".svg"].inlineable-svg').each(function () {
            var $e = $(this);
            var imgURL = $e.prop('src');
            $.get(imgURL, function (data) {
                // Get the SVG tag, ignore the rest
                var $svg = $(data).find('svg');
                // change the color
                $svg.find('path').attr('style', 'fill:none;stroke:#ffcd60;stroke-width:5') ;
                $e.prop('src', "data:image/svg+xml;base64," + window.btoa($svg.prop('outerHTML')));
                $e.removeClass('inlineable-svg');
            });

        });
    });
</script>

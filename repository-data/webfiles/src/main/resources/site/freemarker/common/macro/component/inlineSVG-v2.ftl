<#ftl output_format="HTML">

<#-- based on https://stackoverflow.com/a/29919212  -->
<#macro svg src style alt classes="">
    <#assign id>id-${.now?string["ssSSS"]}</#assign>
    <img id="${id}" class="${classes} inlineable-svg" src="${src}" alt="${alt}"/>
    <script>
        $(function () {
            var img = $("#${id}");
            var imgURL = img.prop('src');
            $.get(imgURL, function (data) {
                // Get the SVG tag, ignore the rest
                var $svg = $(data).find('svg');
                // change the color
                $svg.find('path').attr('style', "${style}");
                img.prop('src', "data:image/svg+xml;base64," + window.btoa($svg.prop('outerHTML')));
                img.removeClass('inlineable-svg');
            });
        });
    </script>
</#macro>


// Based on https://github.com/highlightjs/highlight.js/issues/424#issuecomment-50075236
function paint(lines, lang) {
    lines = lines.split("\n");
    var output = "";
    var state = null;
    lines.forEach(function (value, i) {
        var result = hljs.highlight(lang, value, true, state);
        state = result.top;
        output +=  '<span class="tr hljs ' + (i%2 != 0 ? 'even' : 'odd') + '"><span class="th"></span><code>' + result.value + '</code></span>\n';
    });
    return output;
}

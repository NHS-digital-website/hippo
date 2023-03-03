<#ftl output_format="HTML">

<#include "../../../include/imports.ftl">
<#include "../../macro/toolkit/organisms/codeViewer.ftl">
<#import "./code-viewer/code.ftl" as codeSamples>

<div class="nhsd-t-grid nhsd-!t-margin-top-6 nhsd-!t-margin-bottom-6">
    <div class="nhsd-t-row nhsd-!t-margin-top-6">
        <div class="nhsd-t-col" data-variant="basic">
            <@nhsdCodeViewer "/site/automated-test-pages/macros?macro=code-viewer">
                <@nhsdCodeViewerExampleContent 'Example'>${codeSamples.html?no_esc}</@nhsdCodeViewerExampleContent>
                <@nhsdCodeViewerCodeContent 'HTML'>${codeSamples.html}</@nhsdCodeViewerCodeContent>
                <@nhsdCodeViewerCodeContent 'Nunjucks'>${codeSamples.nunjucks}</@nhsdCodeViewerCodeContent>
            </@nhsdCodeViewer>
        </div>
    </div>
    <div class="nhsd-t-row nhsd-!t-margin-top-6">
        <div class="nhsd-t-col" data-variant="no tabs or header">
            <@nhsdCodeViewer>
                <@nhsdCodeViewerCodeContent 'HTML'>${codeSamples.html}</@nhsdCodeViewerCodeContent>
            </@nhsdCodeViewer>
        </div>
    </div>
    <div class="nhsd-t-row nhsd-!t-margin-top-6">
        <div id="CodeHighlight" class="nhsd-t-col" data-variant="syntax highlights">
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/highlight.js@9.12.0/styles/dark.css">
            <style>.hljs { background: none; }</style>
            <script src="//cdnjs.cloudflare.com/ajax/libs/highlight.js/11.7.0/highlight.min.js"></script>

            <@nhsdCodeViewer>
                <@nhsdCodeViewerCodeContent 'HTML'>${codeSamples.html}</@nhsdCodeViewerCodeContent>
                <@nhsdCodeViewerCodeContent 'Nunjucks'>${codeSamples.nunjucks}</@nhsdCodeViewerCodeContent>
            </@nhsdCodeViewer>
            <script>
            document.querySelectorAll('#CodeHighlight code').forEach((el) => hljs.highlightElement(el));
            </script>
        </div>
    </div>
</div>
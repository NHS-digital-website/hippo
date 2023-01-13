<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.ps.beans.VisualisationSection" -->
<#macro visualisationSection section>
    <div class="nhsd-!t-margin-bottom-6">
        <div id="chart-${section.uniqueId}"></div>
    </div>

    <script data-chartsource="nhsd-data-viz" data-sectionid="${section.uniqueId}">
    window.datavizData = window.datavizData || {};
    window.datavizData[${section.uniqueId}] = <#outputformat "plainText">${section.chartConfig}</#outputformat>;
    </script>
</#macro>

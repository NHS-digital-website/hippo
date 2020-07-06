<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.DynamicChartSection" -->

<#macro dynamicChartSection section size>
    <@fmt.message key="headers.download-chart-data" var="downloadDataFileHeader" />
    <#local linkText>${downloadDataFileHeader} ${section.title}</#local>
    <#local getData="uk.nhs.digital.freemarker.highcharts.RemoteChartDataFromUrl"?new() />
    <#local chartData =  getData(section.url) />
    <#if chartData??>
        <figure data-chart="highchart">
            <div id="chart-${section.uniqueId}"
                 style="width:100%; height:${size}px;"></div>
            <span class="attachment">
                <a data-uipath="ps.publication.chart-section.data-file"
                   title="${linkText}"
                   download="${slugify(section.title)}.csv"
                   href="data:text/plain;base64,${chartData.data}"
                   onClick="logGoogleAnalyticsEvent(
                           'Download chart data','Publication','${slugify(section.title)}'
                           );"
                   onKeyUp="logGoogleAnalyticsEvent(
                           'Download chart data','Publication','${slugify(section.title)}'
                           );">${linkText}</a>
            </span>
        </figure>
        <script type="text/javascript" data-chartsource="highchart" data-charttype="chart" data-sectionid="${section.uniqueId}">
            window.highchartData${section.uniqueId?remove_beginning("-")} = {
                chart: {
                    type: '${section.type?lower_case}',
                    alignTicks: false,
                },
                <#if section.YTitle?has_content>
                yAxis: {
                    title: {
                        text: "${section.YTitle}"
                    },
                    gridLineDashStyle: 'shortdash',
                    gridLineWidth: 1
                },
                </#if>
                <#if section.XTitle?has_content>
                xAxis: {
                    title: {
                        text: "${section.XTitle}"
                    },
                    gridLineDashStyle: 'shortdash',
                    gridLineWidth: 1
                },
                </#if>
                title: {
                    text: "${section.title}"
                },
                series: [<#list section.seriesItem as item>{
                    type: '${item.type}',
                    name: '${item.name}'
                }<#if item?is_last><#else>, </#if></#list>],
                data: {
                    csv: atob("${chartData.data}"),
                    firstRowAsNames: true
                }
            };
        </script>
    <#else>
        <p>The dynamic chart is unavailable at this time. Please try again soon.</p>
    </#if>
</#macro>

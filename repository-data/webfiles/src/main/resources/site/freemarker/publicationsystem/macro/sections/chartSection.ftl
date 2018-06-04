<#ftl output_format="HTML">

<#macro chartSection section>
<#-- @ftlvariable name="section" type="uk.nhs.digital.ps.beans.ChartSection" -->

    <div id="${section.uniqueId}" data-uipath="ps.publication.chart-section" style="width:100%; height:400px;"></div>

    <#if section.dataFile?has_content>
        <#local dataFile=section.dataFile>
        <span class="attachment" itemprop="hasPart" itemscope itemtype="http://schema.org/MediaObject">
            <@externalstorageLink dataFile; url>
            <a data-uipath="ps.publication.chart-section.data-file"
               itemprop="contentUrl"
               title="${dataFile.filename}"
               href="${url}"
               onClick="logGoogleAnalyticsEvent(
                   'Download chart data','Publication','${dataFile.filename}'
               );">
                <span itemprop="name">${dataFile.filename}</span>
            </a>;
            </@externalstorageLink>
            <span class="fileSize">[size: <span itemprop="contentSize"><@formatFileSize bytesCount=dataFile.length/></span>]</span>
        </span>
    </#if>

    <script>
        $(function () {
            var myChart = Highcharts.chart('${section.uniqueId}', <#outputformat "plainText">${section.chartConfig}</#outputformat>);
        });
    </script>
</#macro>

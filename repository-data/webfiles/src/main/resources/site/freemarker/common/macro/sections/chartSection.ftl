<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.ps.beans.ChartSection" -->

<#macro chartSection section type size>
    <@hst.setBundle basename="publicationsystem.headers"/>
    <@fmt.message key="headers.download-chart-data" var="downloadDataFileHeader" />

    <div id="${section.uniqueId}" data-uipath="ps.publication.chart-section" style="width:100%; height:${size}px;"></div>

    <#if section.dataFile?has_content>
        <#local dataFile=section.dataFile>
        <span class="attachment">
            <@externalstorageLink dataFile; url>
            <a data-uipath="ps.publication.chart-section.data-file"
               title="${downloadDataFileHeader}"
               href="${url}"
               onClick="logGoogleAnalyticsEvent(
                   'Download chart data','Publication','${dataFile.filename}'
               );"
               onKeyUp="logGoogleAnalyticsEvent(
                   'Download chart data','Publication','${dataFile.filename}'
               );">
                ${downloadDataFileHeader}
            </a>
            </@externalstorageLink>
        </span>
    </#if>

    <script>
        $(function () {
            Highcharts.
            ${type}
            ('${section.uniqueId}', <#outputformat "plainText">${section.chartConfig}</#outputformat>);
        });
    </script>
</#macro>

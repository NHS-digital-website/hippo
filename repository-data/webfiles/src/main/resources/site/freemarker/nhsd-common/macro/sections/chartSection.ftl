<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.ps.beans.ChartSection" -->
<#macro chartSection section type size>

    <@hst.setBundle basename="publicationsystem.headers"/>
    <@fmt.message key="headers.download-chart-data" var="downloadDataFileHeader" />
    <#local linkText>${downloadDataFileHeader} ${section.title}</#local>

    <div class="nhsd-!t-margin-bottom-6" data-uipath="ps.publication.chart-section">
        <div class="nhsd-!t-margin-bottom-2 nhsd-!t-display-hide" id="chart-${section.uniqueId}" style="width:100%; height:${size}px;">
            <span class="css-loader"></span>
        </div>
        <div class="nhsd-!t-margin-bottom-2 js-no-script-chart"><img src="${section.imageUrl}" alt="${section.title}" class="nhsd-a-image" /></div>

        <#if section.dataFile?has_content>
            <#local dataFile=section.dataFile>
            <span class="attachment">
                <@externalstorageLink dataFile; url>
                <a class="nhsd-a-link"
                data-uipath="ps.publication.chart-section.data-file"
                title="${linkText}"
                href="${url}"
                onClick="logGoogleAnalyticsEvent(
                    'Download chart data','Publication','${dataFile.filename}'
                );"
                onKeyUp="logGoogleAnalyticsEvent(
                    'Download chart data','Publication','${dataFile.filename}'
                );">
                    ${linkText}
                </a>
                </@externalstorageLink>
            </span>
            <span>
        <#if section.htmlCode?has_content>
            ${section.htmlCode?no_esc}
        </#if>
    </span>
        </#if>
    </div>

    <script data-chartsource="highchart" data-charttype="${type}" data-sectionid="${section.uniqueId}">
        window.highchartData${section.uniqueId?remove_beginning("-")} = <#outputformat "plainText">${section.chartConfig}</#outputformat>;
    </script>
</#macro>

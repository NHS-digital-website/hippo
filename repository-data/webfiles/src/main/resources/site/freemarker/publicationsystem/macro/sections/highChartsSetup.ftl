<#ftl output_format="HTML">

<script src="<@hst.webfile path="/js/jquery/jquery-2.1.3.min.js"/>"></script>

<script src="<@hst.webfile path="/js/highcharts/highcharts.js"/>"></script>
<!--[if lt IE 9]>
<script src="<@hst.webfile path="/js/highcharts/oldie.js"/>"></script>
<![endif]-->
<script src="<@hst.webfile path="/js/highcharts/exporting.js"/>"></script>
<script src="<@hst.webfile path="/js/highcharts/offline-exporting.js"/>"></script>

<script>
    Highcharts.setOptions({
        colors: ['#005EB8', '#84919C', '#003087', '#71CCEF', '#D0D5D6'],
        chart: {
            backgroundColor: '#F4F5F6',
            style: {
                fontFamily: 'Frutiger W01'
            },
            zoomType: 'xy'
        },
        xAxis: {
            title: {
                style: {"color":"#000000","fontWeight":"bold"},
            },
            gridLineDashStyle: 'shortdash',
            gridLineWidth: 1
        },
        yAxis: {
            title: {
                style: {"color":"#000000","fontWeight":"bold"},
            },
            gridLineDashStyle: 'shortdash',
            gridLineWidth: 1
        },
        credits: {
            enabled: false
        },
        plotOptions: {
            scatter: {
                marker: {
                    radius: 5,
                }
            },
            line: {
                marker: {
                    enabled: false
                }
            }
        }
    });
</script>

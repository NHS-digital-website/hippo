import Highcharts from "highcharts";
import {highchartPlugins} from './highchart-plugins';
import {qsa} from "../utils/utils";

export default function initHighcharts() {
    window.Highcharts = Highcharts;
    highchartPlugins(Highcharts);

    Highcharts.setOptions({
        accessibility: {
            keyboardNavigation: {
                enabled : true
            }
        },
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
                style: {"color": "#000000", "fontWeight": "bold","fontSize":"16px"},
            },
            gridLineDashStyle: 'shortdash',
            gridLineWidth: 1
        },
        yAxis: {
            title: {
                style: {"color": "#000000", "fontWeight": "bold"},
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

    const charts = qsa('[data-chartsource="highchart"]');
    charts.forEach(chart => {
        // extract chart data from DOM
        const sectionId = chart.getAttribute('data-sectionid');
        const type = chart.getAttribute('data-charttype');

        // get dynamic name for this chart's settings
        // Remove a leading "-", as that's not a valid JS variable name
        const thisChartSettings = `highchartData${sectionId.replace(/^-/g, "")}`;

        // Early return if the settings aren't there.
        if (!window[thisChartSettings]) {
            return;
        }

        if (type !== 'mapChart') {
            Highcharts[type](`chart-${sectionId}`, window[thisChartSettings]);
        } else {
            // Dynamically import map module
            import(/* webpackChunkName: "highcharts-map" */ 'highcharts/modules/map').then(map => {
                map.default(Highcharts);
                if (!window[thisChartSettings].chart || !window[thisChartSettings].chart.map) {
                    return;
                }

                // Only fetch the map data that we need
                if (window[thisChartSettings].chart.map === "custom/british-isles") {
                    import(/* webpackChunkName: "british-isles" */ "./maps/mapdata/british-isles.json").then(mapdata => {
                        Highcharts.maps["custom/british-isles"] = mapdata.default;
                        Highcharts.mapChart(`chart-${sectionId}`, window[thisChartSettings]);
                    });
                }

                if (window[thisChartSettings].chart.map === "custom/british-isles-all") {
                    import(/* webpackChunkName: "british-isles-all" */ "./maps/mapdata/british-isles-all.json").then(mapdata => {
                        Highcharts.maps["custom/british-isles-all"] = mapdata.default;
                        Highcharts.mapChart(`chart-${sectionId}`, window[thisChartSettings]);
                    });
                }

            });
        }
    })

}

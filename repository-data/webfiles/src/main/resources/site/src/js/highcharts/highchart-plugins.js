import Data from 'highcharts/modules/data';
import Exporting from 'highcharts/modules/exporting';
import OfflineExporting from 'highcharts/modules/offline-exporting';

export function highchartPlugins(highcharts) {
    Data(highcharts);
    Exporting(highcharts);
    OfflineExporting(highcharts);
}

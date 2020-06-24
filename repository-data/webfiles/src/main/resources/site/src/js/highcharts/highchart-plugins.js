import Data from 'highcharts/modules/data';
import Accessibility from 'highcharts/modules/accessibility';
import Exporting from 'highcharts/modules/exporting';
import OfflineExporting from 'highcharts/modules/offline-exporting';

export function highchartPlugins(highcharts) {
    Data(highcharts);
    Accessibility(highcharts);
    Exporting(highcharts);
    OfflineExporting(highcharts);
}

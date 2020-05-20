import Tablesort from "tablesort";
import "./table-sort-date";

export function tableSort(tables) {
        tables.forEach(function (table) {

            const disableSort = table.getAttribute("data-disablesort");
            if (disableSort === 'true')
                return;

            const tableHeadings = table.querySelectorAll('th');

            tableHeadings.forEach(function (tableHead, index) {
                if (!index) {
                    tableHead.setAttribute('data-sort-default', '');
                }

                // if we don't manually specify a single sort method, the plugin
                // can potentially add multiple methods at once, giving more
                // than two possible sort orders, which is not wha we want
                const sortMethod = 'string';

                tableHead.setAttribute('data-sort-method', sortMethod);
            })

            // eslint-disable-next-line no-new
            new Tablesort(table);
        });
}

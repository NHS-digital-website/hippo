import Tablesort from "tablesort";

export function tableSort(tables) {
    tables.forEach(function (table) {

        //const disableSort = table.getAttribute("data-disablesort");
        const disableSort = table.getAttribute("id");
        if (disableSort === 'cannotsort')
            return;

        new Tablesort(table);
    });
}

<#ftl output_format="HTML">

<script>
    // @TODO - rather than targeting *all* tables, we should
    // target specific tables with a class e.g. "sortable"
    // i.e `document.querySelectorAll('table.sortable');`
    var tables = document.querySelectorAll('table');

    if (tables.length) {
        tables.forEach(function(table) {
            var tableHeadings = table.querySelectorAll('th');

            tableHeadings.forEach(function(tableHead, index) {
                if (!index) {
                    tableHead.setAttribute('data-sort-default', '');
                }

                // if we don't manually specify a single sort method, the plugin
                // can potentially add multiple methods at once, giving more
                // than two possible sort orders, which is not wha we want
                var sortMethod = 'string';

                tableHead.setAttribute('data-sort-method', sortMethod);
            })

            new Tablesort(table);
        });
    }
</script>

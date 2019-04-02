<#ftl output_format="HTML">

<script>
    var tables = document.querySelectorAll('table');

    if (tables.length) {
        tables.forEach(function(table) {

            var disableSort = table.getAttribute("data-disablesort");
            if (disableSort == 'true')
                return;

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

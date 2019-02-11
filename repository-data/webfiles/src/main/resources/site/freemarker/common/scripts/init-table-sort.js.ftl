<#ftl output_format="HTML">

<script>
    var tableHead = document.querySelector('thead');

    if(tableHead) {
        var table = tableHead.parentNode;
        var firstTh = tableHead.firstElementChild.firstElementChild;
        firstTh.setAttribute('data-sort-default', '');
        new Tablesort(table);
    }
</script>

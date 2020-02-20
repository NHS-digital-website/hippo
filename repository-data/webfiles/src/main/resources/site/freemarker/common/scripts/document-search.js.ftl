<#ftl output_format="HTML">

<script>
    $(document).ready(function() {
        // Caching elements
        $searchForm = document.querySelector('#hub-search-form');
        $searchInput = document.querySelector('#hub-search-input');
        $searchResultsContainer = document.querySelector('#hub-search-results');
        $searchPaginationContainer = document.querySelector('#hub-search-pagination');

        var cachedQueryString = '';
        var timer;

        if ($searchInput && $searchResultsContainer) {
            cachedQueryString = $searchInput.value;

            if (!$searchPaginationContainer.querySelectorAll('.pagination').length) {
                window.vjsu.addClass($searchPaginationContainer, 'is-hidden');
            }

            function delay(callback, ms) {
                if (typeof timer !== 'undefined') {
                    clearTimeout(timer);
                    timer = undefined;
                }

                return function() {
                    var context = this, args = arguments;
                    timer = setTimeout(function () {
                        callback.apply(context, args);
                    }, ms || 0);
                };
            }

            function replaceAll(str, find, replace) {
                return str.replace(new RegExp(find, 'gi'), replace);
            }

            function replaceFoundString(inputText, newString) {
                var queryString = $searchInput.value;
                var innerHTML = inputText.innerHTML;
                innerHTML = replaceAll(innerHTML, queryString, '<span class=\'highlight\'>'+queryString+'</span>');
                inputText.innerHTML = innerHTML;
            }

            function highlightSearchTerm() {
                return function() {
                    var shortSummaries = document.getElementsByClassName('hub-box__text');
                    Array.prototype.forEach.call(shortSummaries, replaceFoundString);
                    
                    var titles = document.getElementsByClassName('hub-box__title-a');
                    Array.prototype.forEach.call(titles, replaceFoundString);
                }
            }

            // Update the results DOM
            function renderResults(data) {    
                var resultsContainerID = $searchResultsContainer.getAttribute('id');
                var $dataResultsContainer = $(data).find('#' + resultsContainerID);
                $($searchResultsContainer).html(
                    $dataResultsContainer.length > 0 
                    ? $dataResultsContainer
                    : '<div>No results to display</div>');
            }

            // Update the pagination DOM
            function renderPagination(data) {
                var paginationContainerID = $searchPaginationContainer.getAttribute('id');
                var $dataPaginationEl = $(data).find('#' + paginationContainerID).find('.pagination');
                
                if ($dataPaginationEl.length) {
                    window.vjsu.removeClass($searchPaginationContainer, 'is-hidden');
                    $($searchPaginationContainer).html($dataPaginationEl);
                } else {
                    window.vjsu.addClass($searchPaginationContainer, 'is-hidden');
                    $($searchPaginationContainer).html('');
                }
            }

            function showProgress(complete) {                
                if (complete) {
                    window.vjsu.removeClass($searchForm, 'is-loading');
                } else {
                    window.vjsu.addClass($searchForm, 'is-loading');
                }
            }

            function onType() {
                return function() {
                    var queryString = $searchInput.value;
                    
                    if (cachedQueryString !== queryString) {
                        showProgress();
                        
                        delay(function (e) {
                            cachedQueryString = $searchInput.value;
                            $.get('?query='+queryString, function (data) {
                                // renderResults(data);
                                // renderPagination(data);
                                // highlightSearchTerm()();

                                showProgress(true);
                            } );
                        }, 200)();
                    }
                    
                }
            }

            $($searchInput).keyup(onType());
        }
    });
</script>

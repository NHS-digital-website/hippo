<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<!--Need to have a single setBundle call as subsequent ones will overwrite the previous values-->
<@hst.setBundle basename="month-names,facet-headers,facet-labels"/>
<#assign facetMaxCount=6/>

<#if facets??>
    <div class="filter">
        <div class="filter-head">
            <@hst.link siteMapItemRefId="search" var="searchLink" navigationStateful=true/>
            <span class="filter-head__title">Filter by:</span>
            <a class="filter-head__reset button button--tiny" href="${searchLink}" title="Reset">Reset</a>
        </div>

        <#list facets.folders as facet>
            <#if facet.folders?has_content>

                <#assign unselectedItems = [] />
                <#assign selectedItems = [] />
                <#list facet.folders as value>
                    <#if value.count &gt; 0>
                        <#if value.leaf>
                            <#assign selectedItems = selectedItems + [ value ] />
                        <#else>
                            <#assign unselectedItems = unselectedItems + [ value ] />
                        </#if>
                    </#if>
                </#list>
                <#assign facetItems = selectedItems + unselectedItems />

                <#assign filterSectionClassName = (facetItems?size gt 0)?then('filter-section filter-section--toggles is-open', 'filter-section') />

                <div class="${filterSectionClassName}">
                    <span class="filter-section__title"><@fmt.message key=facet.name /></span>
                    <div class="filter-section__contents">
                        <ul class="filter-list" title="<@fmt.message key=facet.name />" data-max-count="${facetMaxCount}" data-state="short">
                            <!--taxonomy facet is dealt with separately to render the tree structure-->
                            <#if facet.name="category">
                                <@taxonomyFacets taxonomy.rootTaxonomyFacets/>
                            <#else>
                                <#list facetItems as value>
                                    <#assign valueName="Not Defined"/>
                                    <#if facet.name="month">
                                        <@fmt.message key=value.name var="monthName"/>
                                        <#assign valueName=monthName/>
                                    <#elseif facet.name="category">
                                        <#assign valueName=taxonomy.getValueName(value.name)/>
                                    <#elseif facet.name="document-type">
                                        <@fmt.message key="facet."+value.name var="docType"/>
                                        <#assign valueName=docType/>
                                    <#elseif facet.name="assuredStatus">
                                        <#assign valueName=value.name?boolean?then('Yes','No')/>
                                    <#elseif facet.name="publicationStatus">
                                        <@fmt.message key="facet." + value.name?boolean?then("liveStatus", "upcomingStatus") var="publicationStatus"/>
                                        <#assign valueName=publicationStatus/>
                                    <#else>
                                        <#assign valueName=value.name/>
                                    </#if>

                                    <#assign listItemClassName = (value?index &gt;= facetMaxCount)?then("filter-list__item filter-list__item--no-children is-hidden", "filter-list__item filter-list__item--no-children")/>
                                    <li class="${listItemClassName}">
                                        <#if value.leaf>
                                            <@hst.facetnavigationlink var="link" remove=value current=facets />
                                            <a href="${link}" title="${valueName}" class="filter-link filter-link--active">${valueName}</a>
                                        <#else>
                                            <@hst.link var="link" hippobean=value navigationStateful=true/>
                                            <a href="<#outputformat "plainText">${link}</#outputformat>" title="${valueName}" class="filter-link">${valueName} (${value.count})</a>
                                        </#if>
                                    </li>
                                </#list>

                                <#if facetItems?size &gt; facetMaxCount >
                                    <li class="filter-vis-toggles">
                                        <a href="#" class="filter-vis-toggle filter-vis-toggle--show">Show all (${facetItems?size})</a>
                                        <a href="#" class=" filter-vis-toggle filter-vis-toggle--hide is-hidden">Show less (${facetMaxCount})</a>
                                    </li>
                                </#if>
                            </#if>
                        </ul>
                    </div>
                </div>
            </#if>
        </#list>

    </div>
</#if>

<#macro taxonomyFacets items>
    <#list items as taxonomyFacet>
        <#assign listItemClassName = taxonomyFacet.children?has_content?then("filter-list__item", "filter-list__item filter-list__item--no-children")/>
        <li class="${listItemClassName}">
            <#local value=taxonomyFacet.facetBean/>
            <#local valueName=taxonomyFacet.valueName/>

            <#if value.leaf>
                <@hst.facetnavigationlink var="link" removeList=taxonomyFacet.removeList current=facets />
                <a href="${link}" title="${valueName}" class="filter-link filter-link--active">${valueName}</a>
            <#else>
                <@hst.link var="link" hippobean=value navigationStateful=true/>
                <a href="<#outputformat "plainText">${link}</#outputformat>" title="${valueName}" class="filter-link">${valueName} (${value.count})</a>
            </#if>

            <#if taxonomyFacet.children?has_content>
                <ul <#if taxonomyFacet.facetBean.leaf><#else>class="is-hidden"</#if>>
                    <@taxonomyFacets taxonomyFacet.children/>
                </ul>
            </#if>
        </li>
    </#list>
</#macro>

<script>
    (function(){
        var oldBrowser = window.addEventListener ? false : true;
        var $visToggles = document.getElementsByClassName('filter-vis-toggles');
        var $listTitleToggles = document.getElementsByClassName('filter-section__title');

        var vjsUtils = window.vanillaJSUtils;

        function init () {
            for (var i = 0; i < $visToggles.length; i ++) {
                var $visToggle = $visToggles[i];

                if (!oldBrowser) {
                    $visToggle.addEventListener('click', handleVisToggleClick);
                } else {
                    $visToggle.attachEvent('onclick', handleVisToggleClick);
                }
            }

            for (var i = 0; i < $listTitleToggles.length; i ++) {
                var $listTitleToggle = $listTitleToggles[i];

                if ($listTitleToggle.parentNode.className.indexOf('filter-section--toggles') >= 0) {
                    if (!oldBrowser) {
                        $listTitleToggle.addEventListener('click', handleListTitleToggleClick);
                    }
                }
            }

            if (!oldBrowser) {
                window.addEventListener('resize', handleResize);
            } else {
                window.attachEvent('onresize', handleResize);
            }

            handleResize();
        }

        function handleResize() {
            if (window.outerWidth < 640) {
                collapseLists();
            } else {
                expandLists();
            }
        }

        function collapseLists() {
            for (var i = 0; i < $listTitleToggles.length; i ++) {
                var $listTitleToggle = $listTitleToggles[i];
                var $listTitleToggleParent = $listTitleToggle.parentNode;

                if ($listTitleToggleParent.className.indexOf('is-open') >= 0) {
                    toggleFilterSectionEl($listTitleToggleParent);
                }
            }
        }

        function expandLists() {
            for (var i = 0; i < $listTitleToggles.length; i ++) {
                var $listTitleToggle = $listTitleToggles[i];
                var $listTitleToggleParent = $listTitleToggle.parentNode;

                if ($listTitleToggleParent.className.indexOf('is-open') < 0) {
                    toggleFilterSectionEl($listTitleToggleParent);
                }
            }
        }

        function toggleUl(ulElement) {
            var $showToggleButton = ulElement.getElementsByClassName('filter-vis-toggle--show')[0];
            var $hideToggleButton = ulElement.getElementsByClassName('filter-vis-toggle--hide')[0];

            if ('full' == ulElement.dataset.state) {
                hideListItems(ulElement);
                ulElement.dataset.state = 'short';

                vjsUtils.removeClass($showToggleButton, 'is-hidden');
                vjsUtils.addClass($hideToggleButton, 'is-hidden');
            } else {
                showListItems(ulElement);
                ulElement.dataset.state = 'full'

                vjsUtils.addClass($showToggleButton, 'is-hidden');
                vjsUtils.removeClass($hideToggleButton, 'is-hidden');
            }
        }

        function toggleFilterSectionEl(filterSectionElement) {
            vjsUtils.toggleClass(filterSectionElement, 'is-open');
        }

        function hideListItems(ulElement) {
            setDisplayItems(ulElement);
        }

        function showListItems(ulElement) {
            setDisplayItems(ulElement, true);
        }

        function setDisplayItems(ulElement, display) {
            var listItems = ulElement.getElementsByClassName('filter-list__item');

            for (var i = 0; i < listItems.length; i ++) {
                var listItem = listItems[i];

                if (i >= ulElement.dataset.maxCount) {
                    if (display) {
                        vjsUtils.removeClass(listItem, 'is-hidden');
                    } else {
                        vjsUtils.addClass(listItem, 'is-hidden');
                    }
                }
            }
        }

        function handleVisToggleClick(event) {
            var ulElement = event.target.parentNode.parentNode;

            toggleUl(ulElement);

            event.preventDefault();
            return true;
        }

        function handleListTitleToggleClick(event) {
            var filterSectionElement = event.target.parentNode;

            toggleFilterSectionEl(filterSectionElement);

            event.preventDefault();
            return true;
        }

        init();
    }());
</script>

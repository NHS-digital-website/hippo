<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<!--Need to have a single setBundle call as subsequent ones will overwrite the previous values-->
<@hst.setBundle basename="month-names,facet-headers,facet-labels, doctype-labels"/>
<#assign facetMaxCount=6/>

<#if isContentSearch?? && isContentSearch>
    <#assign facetFields = hstRequest.requestContext.getAttribute("facetFields") />
    <#assign resetUrl = hstRequest.requestContext.getAttribute("resetUrl") />
    <div class="filter">
        <div class="filter-head">
            <@hst.link siteMapItemRefId="search" var="searchLink" navigationStateful=true/>
            <span class="filter-head__title">Filter by:</span>
            <a class="filter-head__reset button button--tiny"
               href="${resetUrl}"
               title="Reset">Reset</a>
        </div>

        <#if facetFields?? && facetFields?has_content>
            <#list facetFields?keys as key>
                <#if key == "xmPrimaryDocType">
                    <@facetPrimaryDocType facetFields[key]/>
                <#elseif key == "taxonomyClassificationField_taxonomyAllValues_keyPath">
                    <@facetContentSearchTaxonomy facetFields[key]/>
                <#elseif key == "searchTab">
                    <#continue>
                <#else>
                    <@RenderFacets facetFields[key] key/>
                </#if>
            </#list>
        </#if>

    </div>

<#else>
    <#if facets??>
        <div class="filter">
            <div class="filter-head">
                <@hst.link siteMapItemRefId="search" var="searchLink" navigationStateful=true/>
                <span class="filter-head__title">Filter by:</span>
                <a class="filter-head__reset button button--tiny"
                   href="${searchLink}" title="Reset">Reset</a>
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
                            <ul class="filter-list"
                                title="<@fmt.message key=facet.name />"
                                data-max-count="${facetMaxCount}"
                                data-state="short">
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
                                                <a href="${link}"
                                                   title="${valueName}"
                                                   class="filter-link filter-link--active">${valueName}</a>
                                            <#else>
                                                <@hst.link var="link" hippobean=value navigationStateful=true/>
                                                <a href="<#outputformat "plainText">${link}</#outputformat>"
                                                   title="${valueName}"
                                                   class="filter-link">${valueName}
                                                    (${value.count})</a>
                                            </#if>
                                        </li>
                                    </#list>

                                    <#if facetItems?size &gt; facetMaxCount >
                                        <li class="filter-vis-toggles">
                                            <a href="#"
                                               class="filter-vis-toggle filter-vis-toggle--show">Show
                                                all (${facetItems?size})</a>
                                            <a href="#"
                                               class=" filter-vis-toggle filter-vis-toggle--hide is-hidden">Show
                                                less </a>
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

</#if>

<#macro taxonomyFacets items>
    <#list items as taxonomyFacet>
        <#assign listItemClassName = taxonomyFacet.children?has_content?then("filter-list__item", "filter-list__item filter-list__item--no-children")/>
        <li class="${listItemClassName}">
            <#local value=taxonomyFacet.facetBean/>
            <#local valueName=taxonomyFacet.valueName/>

            <#if value.leaf>
                <@hst.facetnavigationlink var="link" removeList=taxonomyFacet.removeList current=facets />
                <a href="${link}" title="${valueName}"
                   class="filter-link filter-link--active">${valueName}</a>
            <#else>
                <@hst.link var="link" hippobean=value navigationStateful=true/>
                <a href="<#outputformat "plainText">${link}</#outputformat>"
                   title="${valueName}" class="filter-link">${valueName}
                    (${value.count})</a>
            </#if>

            <#if taxonomyFacet.children?has_content>
                <ul <#if taxonomyFacet.facetBean.leaf><#else>class="is-hidden"</#if>>
                    <@taxonomyFacets taxonomyFacet.children/>
                </ul>
            </#if>
        </li>
    </#list>
</#macro>

<#macro RenderFacets facetFields key>
    <#if facetFields?? && facetFields?has_content>
        <div class="filter-section filter-section--toggles is-open">
            <#assign facetLabel = getFacetLabel(key) />
            <span class="filter-section__title">
        <@fmt.message key="${facetLabel}"/>
             </span>
            <div class="filter-section__contents">
                <ul class="filter-list"
                    title="<@fmt.message key="${facetLabel}" />"
                    data-max-count="${facetMaxCount}"
                    data-state="short">

                    <#list facetFields as field>
                        <#assign facetLabel = "" />
                        <#if key == "publiclyAccessible" >
                            <#assign facetLabel = getPublicationStatusLabel(field.name) />
                        <#elseif key = "assuredStatus">
                            <#assign facetLabel = getAssuredLabel(field.name)/>
                        <#else>
                            <#assign facetLabel = field.name/>
                        </#if>

                        <#if field.name?? && field.name?has_content>
                            <#if field?index &lt; 6 || isSelected(key, field.name)>
                                <li class="filter-list__item filter-list__item--no-children">
                                    <a href="${field.facetUrl}"
                                       title="${facetLabel}"
                                       class="filter-link ${isSelected(key, field.name)?then("filter-link--active", "")}">${facetLabel}
                                        (${field.count})</a>
                                </li>
                            <#else>
                                <li class="filter-list__item filter-list__item--no-children is-hidden">
                                    <a href="${field.facetUrl}"
                                       title="${facetLabel}"
                                       class="filter-link ${isSelected(key, field.name)?then("filter-link--active", "")}">${facetLabel}
                                        (${field.count})</a>
                                </li>
                            </#if>
                        </#if>
                    </#list>
                    <#if facetFields?size &gt; facetMaxCount >
                        <li class="filter-vis-toggles">
                            <a href="#"
                               class="filter-vis-toggle filter-vis-toggle--show">Show
                                all (${ facetFields?size})</a>
                            <a href="#"
                               class=" filter-vis-toggle filter-vis-toggle--hide is-hidden">Show
                                less</a>
                        </li>
                    </#if>
                </ul>
            </div>
        </div>
    </#if>
</#macro>

<#macro facetPrimaryDocType facetFields>
    <#if facetFields?? && facetFields?has_content>
        <div class="filter-section filter-section--toggles is-open">
            <#assign facetLabel = getFacetLabel("xmPrimaryDocType") />
            <span class="filter-section__title">
        <@fmt.message key="${facetLabel}"/>
             </span>
            <div class="filter-section__contents">
                <ul class="filter-list"
                    title="<@fmt.message key="${facetLabel}" />"
                    data-max-count="${facetMaxCount}"
                    data-state="short">

                    <#list facetFields as field>
                        <#assign facetLabel = getDocTypeLabel(field.name) />
                        <#if field.name?? && field.name?has_content && getDocTypeLabel(field.name)??>
                            <#if field?index &lt; 6 || isSelected("xmPrimaryDocType", field.name)>
                                <li class="filter-list__item filter-list__item--no-children">
                                    <a href="${field.facetUrl}"
                                       title="<@fmt.message key= facetLabel />"
                                       class="filter-link ${isSelected("xmPrimaryDocType", field.name)?then("filter-link--active", "")}">
                                        <@fmt.message key= facetLabel />
                                        (${field.count})</a>
                                </li>
                            <#else>
                                <li class="filter-list__item filter-list__item--no-children is-hidden">
                                    <a href="${field.facetUrl}"
                                       title="<@fmt.message key= facetLabel />"
                                       class="filter-link ${isSelected("xmPrimaryDocType", field.name)?then("filter-link--active", "")}">
                                        <@fmt.message key= facetLabel />
                                        (${field.count})</a>
                                </li>
                            </#if>
                        </#if>
                    </#list>
                    <#if facetFields?size &gt; facetMaxCount >
                        <li class="filter-vis-toggles">
                            <a href="#"
                               class="filter-vis-toggle filter-vis-toggle--show">Show
                                all (${ facetFields?size})</a>
                            <a href="#"
                               class=" filter-vis-toggle filter-vis-toggle--hide is-hidden">Show
                                less </a>
                        </li>
                    </#if>
                </ul>
            </div>
        </div>
    </#if>
</#macro>

<#macro facetContentSearchTaxonomy facetFields>
    <#if facetFields?? && facetFields?has_content && taxonomyHierarchy??>
        <div class="filter-section filter-section--toggles is-open">
            <#assign facetLabel = getFacetLabel("topic") />
            <span class="filter-section__title">
        <@fmt.message key="${facetLabel}"/>
             </span>

            <#-- If no drilled in facets -->
            <#if !taxonomyHierarchy["0"]?? && taxonomyHierarchy["1"]??>
                <div class="filter-section__contents">
                    <ul class="filter-list"
                        title="<@fmt.message key="${facetLabel}" />"
                        data-max-count="${facetMaxCount}"
                        data-state="short">
                        <#assign fields = taxonomyHierarchy["1"] />
                        <#list fields as field>
                            <#if field.name?? && field.name?has_content>
                                <li class="filter-list__item ${isSelected("topic", field.name)?then("filter-list__item--no-children", "")}">
                                    <a href="${field.facetUrl}"
                                       title="<@fmt.message key= facetLabel />"
                                       class="filter-link ${isSelected("topic", field.name)?then("filter-link--active", "")}">
                                        <#if field.label??>
                                            ${field.label}
                                        <#else>
                                            ${field.name}
                                        </#if>
                                        <#if field.count??>(${field.count}) </#if></a>
                                </li>
                            </#if>
                        </#list>
                    </ul>
                </div>
            <#elseif taxonomyHierarchy["0"]??>
                <div class="filter-section__contents">
                    <ul class="filter-list"
                        title="<@fmt.message key="${facetLabel}" />"
                        data-max-count="${facetMaxCount}"
                        data-state="short">
                        <li class="filter-list__item ${isSelected("topic", field.name)?then("filter-list__item--no-children", "")}">
                            <#list 0..taxonomyTotalDepth?number as taxonomyLevel>
                            <#assign fields = taxonomyHierarchy["${taxonomyLevel}"] />
                            <#if taxonomyLevel == 0>
                            <@outputTaxonomyfields fields facetLabel true/>
                            <#else>
                            <ul>
                                <#if taxonomyLevel == taxonomyTotalDepth?number>
                                    <ul>
                                        <li class="filter-list__item ${isSelected("topic", field.name)?then("filter-list__item--no-children", "")}">
                                            <@outputTaxonomyfields fields facetLabel false />
                                        </li>
                                    </ul>
                                <#else>
                                    <li class="filter-list__item ${isSelected("topic", field.name)?then("filter-list__item--no-children", "")}">
                                        <@outputTaxonomyfields fields facetLabel false />
                                    </li>
                                </#if>
                                </#if>
                                </#list>
                            </ul>
                    </ul>
                </div>
            </#if>
        </div>
    </#if>
</#macro>

<#macro outputTaxonomyfields fields facetLabel root>
    <#list fields as field>
        <#if field.name?? && field.name?has_content>
            <li><a href="${field.facetUrl}"
                   title="<@fmt.message key= facetLabel />"
                   class="filter-link ${isSelected("topic", field.name)?then("filter-link--active ", "")} ${root?then("filter-link--root", "")}">
                    <#if field.label??>
                        ${field.label}
                    <#else>
                        ${field.name}
                    </#if>
                    <#if field.count??>(${field.count}) </#if></a>
            </li>
        </#if>
    </#list>
</#macro>

<#function isSelected parameter value>
    <#if hstRequest.requestContext.servletRequest.getParameter(parameter)?? &&  hstRequest.requestContext.servletRequest.getParameter(parameter) == value>
        <#return  true />
    <#else>
        <#return false />
    </#if>
</#function>


<#function getFacetLabel field>
    <#local labels = {
    "xmPrimaryDocType":             "document-type",
    "informationType":              "information-type",
    "geographicGranularity":        "geographical-granularity",
    "reportingLevel":               "reportingLevel",
    "geographicCoverage":           "geographical-coverage",
    "publishedBy":                  "publishedBy",
    "assuredStatus":                "assuredStatus",
    "publiclyAccessible":           "publicationStatus",
    "year":                         "year",
    "month":                         "month",
    "topic":             "topic"
    }/>
    <#return labels[field] />
</#function>

<#function getDocTypeLabel field>
    <#local labels = {
    "website:cyberalert":                               "cyber-alerts",
    "publicationsystem:dataset":                        "dataset",
    "website:news":                                     "news",
    "website:publishedworkchapter":                     "publishedworkchapter",
    "publicationsystem:series":                         "series",
    "website:service":                                  "service",
    "website:gdprtransparency":                         "gdprtransparency",
    "nationalindicatorlibrary:indicator":               "indicator",
    "website:event":                                    "event",
    "website:person":                                   "person",
    "website:businessunit":                             "businessunit",
    "website:publishedwork":                            "publishedwork",
    "website:blog":                                     "blog",
    "website:glossarylist":                             "glossarylist",
    "website:roadmap":                                  "roadmap",
    "homepage":                                         "homepage",
    "publication":                                      "publication",
    "website:supplementaryinformation":                 "supplementaryinformation",
    "website:apispecification":                         "apispecification"
    }/>

    <#if labels[field]??>
        <#return labels[field] />
    <#else>
        <#return null />
    </#if>
</#function>

<#function getPublicationStatusLabel field>
    <#local labels = {
    "true":                               "Published",
    "false":                              "Upcoming"
    }/>
    <#return labels[field] />
</#function>

<#function getAssuredLabel field>
    <#local labels = {
    "true":                               "Yes",
    "false":                              "No"
    }/>
    <#return labels[field] />
</#function>


<script>
    (function () {
        var oldBrowser = window.addEventListener ? false : true;
        var $visToggles = document.getElementsByClassName('filter-vis-toggles');
        var $listTitleToggles = document.getElementsByClassName('filter-section__title');

        var vjsUtils = window.vanillaJSUtils;

        function init() {
            for (var i = 0; i < $visToggles.length; i++) {
                var $visToggle = $visToggles[i];

                if (!oldBrowser) {
                    $visToggle.addEventListener('click', handleVisToggleClick);
                } else {
                    $visToggle.attachEvent('onclick', handleVisToggleClick);
                }
            }

            for (var i = 0; i < $listTitleToggles.length; i++) {
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
            for (var i = 0; i < $listTitleToggles.length; i++) {
                var $listTitleToggle = $listTitleToggles[i];
                var $listTitleToggleParent = $listTitleToggle.parentNode;

                if ($listTitleToggleParent.className.indexOf('is-open') >= 0) {
                    toggleFilterSectionEl($listTitleToggleParent);
                }
            }
        }

        function expandLists() {
            for (var i = 0; i < $listTitleToggles.length; i++) {
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

            for (var i = 0; i < listItems.length; i++) {
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

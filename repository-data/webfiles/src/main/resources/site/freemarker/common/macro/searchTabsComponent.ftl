<#ftl output_format="HTML">
<#macro searchTabsComponent contentNames>
    <@hst.setBundle basename="feature-toggles" scope="pageContext"/>
    <@fmt.message key="feature.tabs" var="featureTabs"/>
    <#if contentNames?seq_contains("tabs") && featureTabs?boolean>

        <div class="nav-tabs">
            <div data-uipath="ps.search-tabs" class="js-tabs">
                <#if isContentSearch?? && isContentSearch>
                    <#if searchTabs??>
                        <ul class="tabs-nav" role="tablist">
                            <#list searchTabs as tab>

                                <#if tab.name == "All">
                                    <li class="tabs-nav__item ${isAllSelected("searchTab", tab.name)?then("tabs-nav__item--active", "")}">
                                    <#else >
                                        <li class="tabs-nav__item ${isSelected("searchTab", tab.name)?then("tabs-nav__item--active", "")}">
                                </#if>
                                    <a tabindex="${tab?index + 0}"
                                       href="${tab.facetUrl}" class="tabs-link"
                                       title="${tab.name}"
                                       role="tab">${tab.name}</a>
                                </li>
                            </#list>
                        </ul>
                    </#if>

                <#else>
                    <@hst.include ref="tabs"/>
                </#if>
            </div>
        </div>
    </#if>
</#macro>

<#function isSelected parameter value>
    <#if hstRequest.requestContext.servletRequest.getParameter(parameter)?? &&  hstRequest.requestContext.servletRequest.getParameter(parameter) == value>
        <#return  true />
    <#else>
        <#return false />
    </#if>
</#function>

<#function isAllSelected parameter value>
    <#if  hstRequest.requestContext.servletRequest.getParameter(parameter)??>
        <#return false />
        <#else >
        <#return true />
    </#if>
</#function>

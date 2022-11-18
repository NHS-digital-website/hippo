<#ftl output_format="HTML">

<#macro nhsdRoadmap roadmapOptions = {}>
    <div class="nhsd-m-roadmap nhsd-!t-bg-pale-grey-80-tint nhsd-t-round nhsd-!t-padding-4 nhsd-!t-margin-bottom-6">
        <table class="nhsd-m-roadmap__table">
            <tr class="nhsd-m-roadmap__row nhsd-m-roadmap__row--date">
                <td class="nhsd-m-roadmap__cell nhsd-m-roadmap__cell--empty"></td>
                <#list roadmapOptions.dates as roadmapDate>
                    <th class="nhsd-m-roadmap__cell">
                        <div class="nhsd-m-roadmap__cell-container">
                            <div class="nhsd-m-roadmap__item">${roadmapDate}</div>
                        </div>
                    </th>
                </#list>
            </tr>
            <#list roadmapOptions.items as categoryItems>
                <tr class="nhsd-m-roadmap__row">
                    <td class="nhsd-m-roadmap__cell nhsd-m-roadmap__cell--heading">
                        <span>${categoryItems.name}</span>
                    </td>
                    <#list categoryItems.items as dateItems>
                        <td class="nhsd-m-roadmap__cell">
                            <div class="nhsd-m-roadmap__cell-container">
                                <#list dateItems as item>
                                    <div class="nhsd-m-roadmap__item"
                                         style="--item-vertical-position:${item.options.verticalPosition}; --item-length: ${item.options.periods}; --item-partial-length: ${item.options.periodPartialLength}; --date-offset: ${item.options.periodOffset};">
                                        <div class="nhsd-m-roadmap__item-content"><a href="<@hst.link hippobean=item.item />">${item.item.title}:</a> ${item.item.shortsummary}</div>
                                    </div>
                                </#list>
                            </div>
                        </td>
                    </#list>
                </tr>
            </#list>
        </table>
    </div>
</#macro>
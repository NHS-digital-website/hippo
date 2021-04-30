<#ftl output_format="HTML">

<#macro statOutput number prefix="" suffix="" textColour="">
    <p class="nhsd-t-heading-xxl nhsd-t-word-break ${textColour}">
        ${prefix}${number?number}${suffix}
    </p>
</#macro>


<#function getPrefix type>
    <h2>type: ${type}</h2>
    <#if type == 'money' >
        <#return '£' />
    <#elseif type == 'plus' >
        <#return '+' />
    <#elseif type == 'minus' >
        <#return '-' />
    </#if>
    <#return '' />
</#function>

<#function getSuffix type>
    <#if type == 'percentage' >
        <#return '%' />
    <#elseif type == 'plus' >
        <#return '+' />
    <#elseif type == 'millions' >
        <#return 'm' />
    <#elseif type == 'billions' >
        <#return 'b' />
    <#elseif type == 'thousands' >
        <#return 'k' />
    <#elseif type == 'times' >
        <#return 'x' />
    </#if>
    <#return '' />
</#function>

<#macro statistic stat iteration>
    <#local showStatistic = false />
    <#local statisticType = stat.statisticType />

    <@hst.html hippohtml=stat.headlineDescription contentRewriter=stripTagsWithLinksContentRewriter var="desc" />
    <@hst.html hippohtml=stat.furtherQualifyingInformation contentRewriter=stripTagsWithLinksContentRewriter var="info" />
    <#local prefixText = getPrefix(stat.prefix) />
    <#local suffixText = getSuffix(stat.suffix) />

    <#if stat.statisticType == "staticStatistic">
        <#local showStatistic = true />
        <#local number = stat.number />
        <#local trend = stat.trend />
    </#if>

    <#if stat.statisticType == "feedStatistic">
        <#local remoteStat="uk.nhs.digital.freemarker.statistics.RemoteStatisticFromUrl"?new() />
        <#local remote = (remoteStat(stat.urlOfNumber))! />
        <#if remote??>
            <#local showStatistic = true />
            <#if (remote.trend)??>
                <#local trend = remote.trend />
            </#if>
            <#if (remote.prefix)??>
                <#local prefixText = remote.prefix />
            </#if>
            <#if (remote.suffix)??>
                <#local suffixText = remote.suffix />
            </#if>
            <#if (remote.number)??>
                <#local number = remote.number />
            </#if>
            <#if (remote.headline)??>
                <#local desc = (remote.headline)?? />
            </#if>
            <#if (remote.information)??>
                <#local info = (remote.information)?? />
            </#if>
        </#if>
    </#if>

    <#if showStatistic && number??>

        <#if trend??>
            <#if trend == "auto">
                <#if number?number < 0 >
                    <#local trend = 'down' />
                <#else>
                    <#local trend = 'up' />
                </#if>
            </#if>
        </#if>

       <#local numberOutput = [number]/>
        <#if number?contains('/')>
            <#local numberOutput = number?split('/') />
        </#if>

        <#if iteration == "1">
            <#local colour = "grad-grey" />
        <#elseif iteration == "2">
            <#local colour = "grad-blue" />
        <#elseif iteration == "3">
            <#local colour = "grad-yellow" />
        <#elseif iteration == "4">
            <#local colour = "dark-blue" + " " + "nhsd-!t-col-white" />
            <#local textColour = "nhsd-!t-col-white" />
        <#elseif iteration == "5">
            <#local colour = "yellow" />
        <#elseif iteration == "6">
            <#local colour = "black" + " " +  "nhsd-!t-col-white" />
            <#local textColour = "nhsd-!t-col-white" />
        </#if>

        <div class="nhsd-m-statistics-block">
            <div class="nhsd-a-box nhsd-!t-bg-${colour}">
                <div class="nhsd-m-statistics-block__heading">
                    <#if trend??>
                        <#if trend == 'up' >
                            <span class="nhsd-a-icon nhsd-a-icon--size-m">
                                <svg fill="<#if textColour?has_content>#fff</#if>" xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                    <path d="M1,7.5L8,1l7,6.5L13.5,9L9,4.8L9,15H7L7,4.8L2.5,9L1,7.5z"/>
                                </svg>
                            </span>
                        <#elseif trend == 'down' >
                            <span class="nhsd-a-icon nhsd-a-icon--size-m">
                                <svg fill="<#if textColour?has_content>#fff</#if>" xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                    <path d="M15,8.5L8,15L1,8.5L2.5,7L7,11.2L7,1l2,0l0,10.2L13.5,7L15,8.5z"/>
                                </svg>
                            </span>
                        </#if>
                    </#if>

                    <#if numberOutput??>
                        <#list numberOutput as thisNumber>
                            <#if thisNumber?counter gt 1>
                                <p class="nhsd-t-heading-xxl ${textColour}">&nbsp;out of&nbsp;</p>
                            </#if>
                        <@statOutput number=thisNumber prefix=prefixText suffix=suffixText textColour=textColour />
                        </#list>
                    </#if>
                </div>

                <div class="nhsd-m-statistics-block__subtitle">
                    <p class="nhsd-t-heading-xs nhsd-t-word-break ${textColour}">${desc?no_esc}</p>
                </div>

                <#if info?has_content && info?length gt 0>
                <div class="nhsd-m-statistics-block__text">
                    <p class="nhsd-t-body nhsd-t-word-break ${textColour}">${info?no_esc}</p>
                </div>
                </#if>
            </div>
        </div>
    <#else>
        <p class="nhsd-t-body">The statistic is unavailable at this time. Please try again soon.</p>
    </#if>
</#macro>

<#ftl output_format="HTML">

<#macro statOutput prefix number suffix>
    <span class="statistic__prefix">${prefix}</span><span
        class='statistic__number'>${number?number}</span><span
        class="statistic__suffix">${suffix}</span>
</#macro>

<#macro statistic stat>
    <#local trend = stat.trend />
    <#local prefix = stat.prefix />
    <#local suffix = stat.suffix />
    <#local number = stat.number />
    <#local desc = stat.headlineDescription />
    <#local info = stat.furtherQualifyingInformation />

    <#local prefixText = '' />
    <#if prefix == 'money' >
        <#local prefixText = '£' />
    <#elseif prefix == 'plus' >
        <#local prefixText = '+' />
    <#elseif prefix == 'minus' >
        <#local prefixText = '-' />
    </#if>

    <#local suffixText = '' />
    <#if suffix == 'percentage' >
        <#local suffixText = '%' />
    <#elseif suffix == 'plus' >
        <#local suffixText = ' +' />
    <#elseif suffix == 'millions' >
        <#local suffixText = 'm' />
    <#elseif suffix == 'billions' >
        <#local suffixText = 'b' />
    </#if>

    <#local numberOutput = [number]/>
    <#if number?contains('/')>
        <#local numberOutput = number?split('/') />
    </#if>

    <#local numberTypeClass = 'statistic--single-number' />
    <#if numberOutput?size gt 1>
        <#local numberTypeClass = 'statistic--fraction' />
    </#if>
    <#assign statClasses = ['statistic', numberTypeClass] />

    <div class="${statClasses?join(" ")}">
        <p class="statistic__numberStat">
            <#if trend == 'up' >
                <span class="statistic__trend">
                    <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25"
                         viewBox="0 0 25 25" class="statistic__trend-arrow"
                         aria-labelledby="up-arrow-title"
                         role="img">
                            <title id="up-arrow-title">Upwards Trend</title>
                            <path fill="#003087" fill-rule="nonzero"
                                  d="M17.976016 25c.428291 0 .783162-.342633.783162-.770925V12.506118h5.469897c.31816 0 .599608-.19579.721977-.489476.036711-.097895.048948-.19579.048948-.293686 0-.208027-.085658-.416054-.244738-.575134L13.044542.220264C12.885462.073421 12.70191 0 12.506118 0c-.19579 0-.379344.073421-.538423.220264L.256975 11.147822C.097895 11.306902 0 11.514929 0 11.722956c0 .097896.024474.19579.061185.293686.122369.293686.403817.489477.721977.489477h5.469897v11.722956c0 .428292.35487.770925.783162.770925h10.939795z"/>
                    </svg>
                </span>
            <#elseif trend == 'down' >
                <span class="statistic__trend">
                    <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25"
                         viewBox="0 0 25 25" class="statistic__trend-arrow"
                         aria-labelledby="down-arrow-title"
                         role="img">
                        <title id="down-arrow-title">Downwards Trend</title>
                        <path fill="#003087" fill-rule="nonzero"
                              d="M17.976016 0c.428291 0 .783162.342633.783162.770925v11.722957h5.469897c.31816 0 .599608.19579.721977.489476.036711.097895.048948.19579.048948.293686 0 .208027-.085658.416054-.244738.575134l-11.71072 10.927558c-.15908.146843-.342633.220264-.538424.220264-.19579 0-.379344-.073421-.538423-.220264L.256975 13.852178C.097895 13.693098 0 13.485071 0 13.277044c0-.097896.024474-.19579.061185-.293686.122369-.293686.403817-.489477.721977-.489477h5.469897V.770925C6.25306.342633 6.60793 0 7.036221 0h10.939795z"/>
                    </svg>
                </span>
            </#if>

            <span class="statistic__number-output">
            <#list numberOutput as number>
                <#if number?counter gt 1>
                    <span class="statistic__out-of-text">out of</span>
                </#if>
                <@statOutput prefix=prefixText number=number suffix=suffixText />
            </#list>
            </span>
        </p>
        <div class="statistic__description"><@hst.html hippohtml=desc contentRewriter=gaContentRewriter /></div>
        <#if info?has_content && info.content?length gt 0>
            <div class="statistic__information"><@hst.html hippohtml=info contentRewriter=gaContentRewriter /></div>
        </#if>
    </div>
</#macro>

<#macro statistics section>
    <#local heading = section.heading />
    <#local colourScheme = section.colourScheme />
    <#local animated = section.animation />

    <#local headingTag = 'h3' />
    <#if section.headingLevel == 'main'>
        <#local headingTag = 'h2' />
    </#if>

    <#local colourClass = "statistics-section--${colourScheme}" />
    <#local animatedClass = "statistics-section--${animated}" />
    <#local containerClasses = ["statistics-section", colourClass, animatedClass] />
<div class="${containerClasses?join(' ')}">
    <#if section.heading?has_content>
        <${headingTag} id="${slugify(heading)}" class="statistics-section__heading">${heading}</${headingTag}>
    </#if>
    <#if section.modules?size gt 0 >
        <div class="statistics-section__modules">
            <#list section.modules as stat>
                <@statistic stat/>
            </#list>
        </div>
    </#if>
    </div>
</#macro>

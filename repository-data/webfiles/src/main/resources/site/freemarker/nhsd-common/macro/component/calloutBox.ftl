<#ftl output_format="HTML">

<#macro calloutBox callout classname="" title="">
    <#assign severity=(callout.severity?has_content?then(callout.severity, "information")) />
    <#assign update=callout.calloutType=="update" />
    <#assign calloutBoxId=callout.calloutType+"-"+callout.severity+"-"+callout.index />

    <#if severity == 'critical'>
        <#assign emphasisType = 'warning'/>
        <#assign borderColour = 'red' />
    <#elseif severity == 'important'>
        <#assign emphasisType = 'important' />
        <#assign borderColour = 'yellow' />
    <#elseif severity == 'information'>
        <#assign emphasisType = 'emphasis' />
        <#assign borderColour = 'blue' />
    </#if>

    <div class="nhsd-m-emphasis-box nhsd-m-emphasis-box--wide nhsd-m-emphasis-box--${emphasisType} nhsd-!t-margin-bottom-6"
         role="complementary" aria-labelledby="callout-box-heading-${calloutBoxId}"
         xmlns="http://www.w3.org/1999/html">
        <div class="nhsd-a-box nhsd-a-box--border-${borderColour}">
            <div class="nhsd-m-emphasis-box__icon-box">
                <span class="nhsd-a-icon nhsd-a-icon--size-xxxl">
                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16">
                        <path d="M8,16l-6.9-4V4L8,0l6.9,4v8L8,16z M2,11.5L8,15l6-3.5v-7L8,1L2,4.5V11.5z"/>
                            <#if emphasisType == "important">
                                <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16" width="72%" height="72%" x="14%" y="14%">
                                    <path d="M 7.3247844,11.545141 7.1559222,0.40326992 H 8.8440778 L 8.6752156,11.545141 H 7.3246603 Z M 7.1559222,15.59673 v -2.02577 h 1.6881556 v 2.02577 z" />
                                </svg>
                            <#elseif emphasisType == "warning">
                                <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16" width="72%" height="72%" x="14%" y="14%">
                                    <path d="M 7.3247844,11.545141 7.1559222,0.40326992 H 8.8440778 L 8.6752156,11.545141 H 7.3246603 Z M 7.1559222,15.59673 v -2.02577 h 1.6881556 v 2.02577 z" />
                                </svg>
                            <#elseif emphasisType == "emphasis">
                                <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16" width="72%" height="72%" x="14%" y="14%">
                                        <path d="M 8.0222943,0.10083332 C 9.0514482,0.0663166 9.8914825,1.1101201 9.7047927,2.108221 9.5845017,3.1166803 8.4669092,3.8629536 7.4951342,3.514078 6.4729725,3.2246191 5.9550564,1.9242715 6.4820495,1.0059055 6.7739153,0.44421952 7.3926681,0.09194504 8.0222943,0.10083332 Z M 10.30676,15.151259 c -1.5379202,0 -3.0758382,0 -4.61376,0 0,-0.373285 0,-0.746569 0,-1.119856 0.3881923,0 0.7763847,0 1.1645814,0 0,-2.508405 0,-5.0168103 0,-7.5252148 -0.3881967,0 -0.7763891,0 -1.1645814,0 0,-0.3881913 0,-0.776388 0,-1.1645812 1.1646166,0 2.329233,0 3.4938494,0 0,2.89658 0,5.793159 0,8.689742 0.3732885,0 0.7465717,0 1.1198576,0 1.9e-5,0.373301 4.4e-5,0.746606 4.4e-5,1.11991 z"></path>
                                </svg>
                            </#if>
                    </svg>
                </span>
            </div>

            <div class="nhsd-m-emphasis-box__content-box">
                <p class="nhsd-t-heading-s nhsd-t-word-break" id="nhsd-t-heading-s-${calloutBoxId}">
                    ${callout.title}
                </p>

                <#if callout.content?has_content>
                    <div class="nhsd-t-word-break">
                        <@hst.html hippohtml=callout.content contentRewriter=brContentRewriter />
                    </div>
                </#if>
            </div>
        </div>
    </div>
</#macro>

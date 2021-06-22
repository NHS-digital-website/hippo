<#ftl output_format="HTML">

<#function getImageData doc>
    <#local image = '' />
    <#local alttext = '' />

    <#if (doc.leadimagesection)?has_content && (doc.leadimagesection.leadImage)?has_content>
        <#local image = doc.leadimagesection.leadImage />
    <#elseif (doc.leadImage)?has_content>
        <#local image = doc.leadImage />
    <#elseif doc.summaryimage?? && doc.summaryimage.original?? >
        <#local image = doc.summaryimage />
    <#elseif doc.image?? && doc.image.original?? >
        <#local image = doc.image />
    </#if>

    <#if doc.leadimagesection?has_content && doc.leadimagesection.alttext?has_content>
        <#local alttext = doc.leadimagesection.alttext />
    <#elseif doc.leadImageAltText?has_content>
        <#local alttext = doc.leadImageAltText />
    <#elseif doc.altText?has_content>
        <#local alttext = doc.altText />
    <#elseif doc.title?has_content>
        <#local alttext = doc.title />
    </#if>

    <#return [image, alttext] />
</#function>

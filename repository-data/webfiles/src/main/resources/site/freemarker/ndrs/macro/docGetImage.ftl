<#ftl output_format="HTML">

<#function getImageData doc>
    <#local image = '' />
    <#local alttext = '' />
    <#if (doc.leadimagesection)?has_content && (doc.leadimagesection.leadImage)?has_content>
        <#local image = doc.leadimagesection.leadImage />
        <#if doc.leadimagesection?has_content && doc.leadimagesection.alttext?has_content>
            <#local alttext = doc.leadimagesection.alttext />
        </#if>
    <#elseif (doc.leadImage)?has_content>
        <#local image = doc.leadImage />
        <#if doc.leadImageAltText?has_content>
            <#local alttext = doc.leadImageAltText />
        </#if>
    <#elseif doc.summaryimage?? && doc.summaryimage.original?? >
        <#local image = doc.summaryimage />
    <#elseif doc.image?? && doc.image.original?? >
        <#local image = doc.image />
        <#if doc.altText?has_content>
            <#local alttext = doc.altText />
        </#if>
    <#elseif doc.bannerImage?? && doc.bannerImage.original?? >
        <#local image = doc.bannerImage />
    </#if>
    <#return [image, alttext] />
</#function>

<#ftl output_format="HTML">

<#macro responsiveImage image attrs={} variant="newsFeaturedPost">
    <#if !image[variant]?has_content>
        <#local variant = "newsFeaturedPost"/>
    </#if>
    <#local variantX2 = variant + "2x" />
    <#if !attrs.alt?has_content>
        <#local attrs += {
            'alt': ''
        }/>
    </#if>
    <#local attrString = "" />
    <#list attrs?keys as attr>
        <#local attrString += " " + attr + "=\"" + attrs[attr] + "\"" />
    </#list>
    <@hst.link var="picture" hippobean=image[variant] fullyQualified=true />
    <@hst.link var="picturex2" hippobean=image[variantX2] fullyQualified=true />
    <#local width = image[variant].width?c />
    <#local widthX2 = image[variantX2].width?c />
    <img
         class="nhsd-a-image"
         src="${picturex2}"
         srcset="${picture} ${width}w, ${picturex2} ${widthX2}w"
         sizes="100vw"
         ${attrString?no_esc}
        />
</#macro>

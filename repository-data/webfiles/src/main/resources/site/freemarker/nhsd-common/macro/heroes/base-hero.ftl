<#ftl output_format="HTML">

<#--
A macro to create a hero with no content.
Custom content can be embeded in the hero by nesting it inside this macro:
<@baseHero>content</baseHero>

Note: In most cases the <@hero /> macro should be used instead.
      The <@baseHero /> macro is intended to be implemented only by other hero
      macros where requirements differ from those of <@hero />

Params:

options (Hash, Optional)

Values:

colour        String                          Hero colour (lightGrey|darkGrey|blue|darkBlue|yellow)    Default: lightGrey
alignment     String                          Alignment of hero content (left|centre)                  Default: left
image         { src: String, alt: String }    Image src & alt. For use with image heros
video         String                          Video src. For use with video heros
digiblocks    Array                           Array of digiblocks to display (tl|tr|bl|br)             Default: ['tr']
uiPath        String                          UI path to use for automated tests                       Default: website.hero
badge         { src: String, alt: String }    Badge src & alt
---

heroType (String, Optional)

Values:

default|backgroundImage|image|accentedImage|accentedImageMirrored
-->

<#macro baseHero options = {} heroType = "default">
    <#assign uiPath = options.uiPath?has_content?then(options.uiPath, "website.hero") />

    <#assign bgClass="">
    <#assign heroClasses="">
    <#assign textClass="nhsd-!t-text-align-left">
    <#assign digiblockColorClass="">

    <#if heroType == "backgroundImage">
        <#assign heroClasses="nhsd-o-hero--background-image">
    <#elseif heroType == "image">
        <#assign heroClasses="nhsd-o-hero--image">
        <#assign bgClass="nhsd-!t-bg-grad-black">
        <#assign textClass += " nhsd-o-hero--light-text nhsd-!t-col-white">
    <#elseif heroType == "accentedImage">
        <#assign heroClasses="nhsd-o-hero--image-accented">
    <#elseif heroType == "accentedImageMirrored">
        <#assign heroClasses="nhsd-o-hero--image-accented nhsd-o-hero--image-accented-mirrored">
    </#if>

    <#if options.colour?has_content>
        <#if options.colour == "Dark Grey" || options.colour == "darkGrey">
            <#assign bgClass="nhsd-!t-bg-black">
            <#assign textClass += " nhsd-o-hero--light-text nhsd-!t-col-white">
            <#assign digiblockColorClass="nhsd-a-digiblocks--col-black">
        <#elseif options.colour == "Light Blue" || options.colour == "blue">
            <#assign bgClass="nhsd-!t-bg-bright-blue-20-tint">
            <#assign digiblockColorClass="nhsd-a-digiblocks--col-blue">
        <#elseif options.colour == "Dark Blue" || options.colour == "darkBlue" || options.colour == "Dark blue">
            <#assign bgClass="nhsd-!t-bg-blue">
            <#assign textClass += " nhsd-o-hero--light-text nhsd-!t-col-white">
            <#assign digiblockColorClass="nhsd-a-digiblocks--col-light-blue">
        <#elseif options.colour == "Dark Blue Multicolour">
            <#assign bgClass="nhsd-!t-bg-blue">
            <#assign textClass += " nhsd-o-hero--light-text nhsd-!t-col-white">
        <#elseif options.colour == "Yellow" || options.colour == "yellow">
            <#assign bgClass="nhsd-!t-bg-yellow-20-tint">
            <#assign digiblockColorClass="nhsd-a-digiblocks--col-yellow">
        <#elseif options.colour == "Blue grey">
            <#assign bgClass="nhsd-!t-bg-bright-blue-10-tint">
        <#elseif options.colour == "Light blue">
            <#assign bgClass="nhsd-!t-bg-bright-blue-20-tint">
        <#elseif options.colour == "Black">
            <#assign bgClass="nhsd-!t-bg-black">
            <#assign textClass += " nhsd-o-hero--light-text nhsd-!t-col-white">
        <#elseif options.colour == "Mid blue">
            <#assign bgClass="nhsd-!t-bg-accessible-blue">
            <#assign textClass += " nhsd-o-hero--light-text nhsd-!t-col-white">
        </#if>
    </#if>

    <#if (options.alignment?has_content && options.alignment == "centre") || heroType == "backgroundImage">
        <#assign textClass += " nhsd-!t-text-align-center"/>
    <#elseif heroType != "accentedImage" && heroType != "accentedImageMirrored">
        <#assign heroClasses += " nhsd-o-hero--left-align"/>
    </#if>

    <div class="nhsd-o-hero ${heroClasses} ${textClass} ${bgClass}">
        <div class="nhsd-o-hero__content-container">
            <div class="nhsd-o-hero__inner-content-container">
                <#nested/>
            </div>
        </div>

        <#if heroType == "image" || heroType == "backgroundImage" || heroType == "accentedImage" || heroType == "accentedImageMirrored">
            <#assign imageClasses = ""/>
            <#if heroType == "image">
                <#assign imageClasses = "nhsd-a-image--maintain-ratio nhsd-a-image--position-right"/>
            <#elseif heroType == "backgroundImage">
                <#assign imageClasses = "nhsd-a-image--cover"/>
            </#if>
            <div class="nhsd-o-hero__image-container">
                <#if options.video?has_content>
                    <div class="nhsd-o-hero__iframe-wrapper">
                        <iframe class="nhsd-o-banner__iframe" src="${options.video}" allow="autoplay; encrypted-media; picture-in-picture" allowfullscreen></iframe>
                    </div>
                <#else>
                    <figure class="nhsd-a-image ${imageClasses}">
                        <picture class="nhsd-a-image__picture">
                            <#if options.image?has_content && options.image.src?has_content>
                                <img src="${options.image.src}" alt="<#if options.image.alt?has_content>${options.image.alt}</#if>">
                            <#else>
                                <img src="https://digital.nhs.uk/binaries/content/gallery/website/about-nhs-digital/fibre_57101102_med.jpg" alt="" />
                            </#if>
                        </picture>
                    </figure>
                </#if>
            </div>
        <#else>
            <#assign digiblocks = ['tr'] />
            <#if options.digiblocks?has_content>
                <#assign digiblocks = options.digiblocks />
            </#if>
            <#list digiblocks as digiblock>
                <div class="nhsd-a-digiblocks nhsd-a-digiblocks--pos-${digiblock} ${digiblockColorClass}">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 550 550"><g><g transform="translate(222, 224)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon></g><g transform="translate(328.5, 367.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon></g><g transform="translate(151, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#3C4D57"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#32434C"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#313D45"></polygon></g><g transform="translate(80, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon></g></g><g><g transform="translate(186.5, 203.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon></g><g transform="translate(186.5, 285.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(222, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(9, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(257.5, 449.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#F5D507"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F2CB0C"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EEC000"></polygon></g></g><g><g transform="translate(186.5, 203.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#6D7B86"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#62717A"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#5C6B75"></polygon></g><g transform="translate(399.5, 326.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon></g><g transform="translate(222, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon></g></g><g><g transform="translate(328.5, 162.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#F5D507"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F2CB0C"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EEC000"></polygon></g><g transform="translate(399.5, 244.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(115.5, 162.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#6D7B86"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#62717A"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#5C6B75"></polygon></g><g transform="translate(186.5, 244.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon></g><g transform="translate(328.5, 326.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon></g><g transform="translate(257.5, 326.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#3C4D57"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#32434C"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#313D45"></polygon></g></g><g><g transform="translate(328.5, 244.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#DADFDF"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#CDD5D6"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#C5CDCF"></polygon></g><g transform="translate(257.5, 285.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon></g><g transform="translate(44.5, 203.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#6D7B86"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#62717A"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#5C6B75"></polygon></g><g transform="translate(151, 265)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon></g></g><g><g transform="translate(435, 142)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon></g></g><g><g transform="translate(328.5, 39.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon></g><g transform="translate(222, 19)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(257.5, 80.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#DADFDF"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#CDD5D6"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#C5CDCF"></polygon></g></g></svg>
                </div>
            </#list>
        </#if>

        <#if (heroType == "accentedImage" || heroType == "accentedImageMirrored") && (!options.colourBar?has_content || options.colourBar)>
            <span class="nhsd-a-colour-bar"></span>
        </#if>

        <#-- Only intended to be used on publication pages -->
        <#if options.badge?has_content>
            <div class="nhsd-o-hero__stamp">
                <img src="${options.badge.src}" data-uipath="${uiPath}.national-statistics" alt="${options.badge.alt}" />
            </div>
        </#if>
    </div>
</#macro>

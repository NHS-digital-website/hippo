<#ftl output_format="HTML">

<#include "./base-hero.ftl">
<#include "./full-hero-content.ftl">
<#include "./simple-hero-content.ftl">

<#--
A macro to create a hero with content support.
Custom or page specific content can be included by nesting it inside this macro:
<@hero>content</hero>
In most cases default options can be set with,
getHeroOptions() & getHeroOptionsWithMetaData()

Params:

options (Hash, Optional)

Values:

introTags     Array                           Array of tags as Strings
introText     String                          Intro text
title         String                          Title text
summary       String                          Summary text
buttons       Array                           Array of buttons { text, src, type, classes, srText }
metaData      Array                           Array of meta data items {title, value, schemaOrgTag }
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

<#macro hero options = {} heroType = "default">
    <#assign validatedType = heroType/>
    <#if !['accentedImage', 'accentedImageMirrored', 'image', 'backgroundImage', 'blackHero', 'whiteHero', 'blackBackground']?seq_contains(validatedType)>
        <#assign validatedType = "default"/>
    </#if>

    <@baseHero options validatedType>
        <#if heroType == "backgroundImage">
            <@simpleHeroContent options>
                <#nested />
            </@simpleHeroContent>
        <#else>
            <@fullHeroContent options heroType>
                <#nested />
            </@fullHeroContent>
        </#if>
    </@baseHero>
</#macro>

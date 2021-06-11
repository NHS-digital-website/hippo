<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#assign lettersOfTheAlphabet = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"]/>

<#macro alphabeticalFilterNav blockGroups ariaLabel="">
<#if blockGroups?has_content>
<#if ariaLabel?? && ariaLabel?has_content>
    <#assign ariaLabel = "aria-labelledby=" + ariaLabel/>
</#if>

<nav class="nhsd-m-character-block-list" ${ariaLabel}>
    <ul data-uipath="website.glossary.az-nav">
    <#list lettersOfTheAlphabet as letter>
        <#if blockGroups[letter]??>
            <li>
                <a class="nhsd-a-character-block" href="#${slugify(letter)}" aria-label="Jump to articles starting with the letter '${letter}'">${letter}</a>
            </li>
        <#else>
            <li>
                <span class="nhsd-a-character-block nhsd-a-character-block--disabled">${letter}</span>
            </li>
        </#if>
    </#list>
    </ul>
</nav>
</#if>
</#macro>

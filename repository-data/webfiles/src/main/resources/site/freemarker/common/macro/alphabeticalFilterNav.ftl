<#ftl output_format="HTML">

<#assign lettersOfTheAlphabet = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"]/>

<#macro alphabeticalFilterNav blockGroups>
<#if blockGroups?has_content>
<div class="article-section-nav">
    <h2 class="article-section-nav__title">Refine results</h2>
    <hr>
    <nav role="navigation">
        <ol class="article-section-nav__menu list--reset">
        <#list lettersOfTheAlphabet as letter>
            <#assign className = "is-disabled"/>
            <#if blockGroups[letter]??>
                <#assign className = ""/>
            </#if>
            <li class="${className}"><a href="#section-${letter}">${letter}</a></li>
        </#list>
        </ol>
    </nav>
</div>
</#if>
</#macro>

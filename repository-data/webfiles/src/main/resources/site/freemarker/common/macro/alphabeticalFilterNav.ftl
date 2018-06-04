<#ftl output_format="HTML">

<#assign lettersOfTheAlphabet = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"]/>

<#macro alphabeticalFilterNav blockGroups>
<#if blockGroups?has_content>
<div class="article-section-nav">
    <h2 class="article-section-nav__title">Refine results</h2>
    <hr>
    <nav role="navigation">
        <ol class="article-section-nav__menu article-section-nav__menu--push">
        <#list lettersOfTheAlphabet as letter>
            <#if blockGroups[letter]??>
                <li>
                    <a href="#section-${letter}" aria-label="Jump to articles starting with the letter '${letter}'">${letter}</a>
                </li>
            <#else>
                <li class="is-disabled">${letter}</li>
            </#if>
        </#list>
        </ol>
    </nav>
</div>
</#if>
</#macro>

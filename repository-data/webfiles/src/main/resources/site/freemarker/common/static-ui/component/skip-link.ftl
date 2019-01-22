<#ftl output_format="HTML">
<#include "../../macro/component/skipLink.ftl">

<div class="grid-row">
    <div class="column column--two-thirds">
        <div class="article-section article-section--summary">
            <h1>Skip link</h1>
            <div class="article-section--summary">
                <p><b>Use the skip link component to help keyboard-only users skip to the main content on a page.</b>
                    To view the skip link component, click inside this example and press tab on your keyboard. This
                component is best demonstrated with a screen reader.</p>
            </div>

            <#-- Over ride parameters -->
            <p><@skipLink anchorTo="foo" label="foo content" /></p>
            <p><@skipLink anchorTo="moo" label="moo content" /></p>

            <div id="foo">You are in foo.</div>
            <hr>
            <div id="moo">You are in moo.</div>

        </div>
    </div>
</div>



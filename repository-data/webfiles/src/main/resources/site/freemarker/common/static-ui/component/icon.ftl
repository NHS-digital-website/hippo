<#ftl output_format="HTML">
<#include "../../macro/component/icon.ftl">

<div class="grid-row">
    <div class="column column--two-thirds">
        <div class="article-section article-section--summary">
            <h1>Icon</h1>
            <p>Icons are used to give visual meaning to an action or section. For example they can be used as part of a <a href="block-link">Block link</a> component.</p>
        </div>


        <div class="article-section article-section--summary">
            <h2>Icon sizing</h2>
            <p>When using the icon macro use the <code>size</code> to pass in as the second value.</p>
            <br>

            <ul class="list list--reset">
                <li>
                    <@icon name="pdf" />
                    <span class="icon-size icon-size--default">Size: <code>default</code></span>
                </li>
                <li>
                    <@icon name="pdf" size="2x" />
                    <span class="icon-size icon-size--2x">Size: <code>2x</code></span>
                </li>
                <li>
                    <@icon name="pdf" size="3x" />
                    <span class="icon-size icon-size--3x">Size: <code>3x</code></span>
                </li>
            </ul>
        </div>
        <div class="article-section article-section--summary">

            <h2>Document type icons</h2>
            <p>Doctype icons represent a type of file when a preview or image is unavailable.</p>
            <p>When using the icon macro use the <code>name</code> to pass in as the first value.</p>
            <br>

            <ul class="list list--reset">
                <li>
                    <@icon />
                    <span>Default icon</span>
                </li>
                <li>
                    <@icon name="pdf" />
                    <span>Name: <code>pdf</code></span>
                </li>
                <li>
                    <@icon name="word" />
                    <span>Name: <code>word</code></span>
                </li>
                <li>
                    <@icon name="ppt" />
                    <span>Name: <code>ppt</code></span>
                </li>
                <li>
                    <@icon name="zip" />
                    <span>Name: <code>zip</code></span>
                </li>
                <li>
                    <@icon name="video" />
                    <span>Name: <code>video</code></span>
                </li>
                <li>
                    <@icon name="spreadsheet" />
                    <span>Name: <code>spreadsheet</code></span>
                </li>
                <li>
                    <@icon name="csv" />
                    <span>Name: <code>csv</code></span>
                </li>
                <li>
                    <@icon name="text" />
                    <span>Name: <code>text</code></span>
                </li>
                <li>
                    <@icon name="image" />
                    <span>Name: <code>image</code></span>
                </li>
            </ul>
        </div>
    </div>
</div>



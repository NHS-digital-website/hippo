<#ftl output_format="HTML">
<#include "../../macro/component/infoGraphic.ftl">

<div class="grid-row">
    <div class="column column--two-thirds">
        <div class="article-section article-section--summary">
            <h1>Infographic</h1>
            <p>Infographics are used to convey meaningful informative information, generally used with statistics. They comprise a headline an image, explanatory text and qualifying text.</p>
        </div>


        <div class="article-section article-section--summary">
            <h2>White infographic</h2>
            <div style="background-color: gainsboro; padding: 10px;">
                <@infoGraphic
                    headline="484,700"
                    explanatory="Loreum ipsum dolor sit amet consecteur adiplici elit sed do eisomoud"
                    qualifying="Infographic secondary content that is likely to be longer than we always expect" />
            </div>

            <hr>

            <h2>Light blue infographic</h2>
            <@infoGraphic
                color="light-blue"
                headline="484,700"
                explanatory="Loreum ipsum dolor sit amet consecteur adiplici elit sed do eisomoud"
                qualifying="Infographic secondary content that is likely to be longer than we always expect" />

            <hr>

            <h2>Blue infographic</h2>
            <@infoGraphic
                color="blue"
                headline="484,700"
                explanatory="Loreum ipsum dolor sit amet consecteur adiplici elit sed do eisomoud"
                qualifying="Infographic secondary content that is likely to be longer than we always expect" />
        </div>

    </div>
</div>



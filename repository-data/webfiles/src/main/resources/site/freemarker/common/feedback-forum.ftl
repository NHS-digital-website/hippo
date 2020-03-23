<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<h1> Test @@@@ </h1>
<#if isPreview?? &&isPreview>
    <h1> In peview mode</h1>
    <form action="<@hst.actionURL/>" class="form-wrapper" method="post">

        <div class="form-group">
            <div class="row">
                <div class="col-xs-6">
                    <label for="subject"> Subject * </label>
                    <input type="text" id="subject" name="subject" value=""
                           class="form-control" minlength="2"
                           placeholder="Your Name"
                           required="">
                </div>
            </div>
        </div>

        <div class="form-group">
            <div class="row">
                <div class="col-xs-7">
                    <label for="comments"> Comments *</label>
                    <textarea id="comments" name="comments" class="form-control"
                              placeholder="Your Message" rows="3"
                              required=""></textarea>
                </div>
            </div>
        </div>

        <div class="form-group">
            <div class="row">
                <div class="col-xs-6">
                    <label for="subject"> URL</label>
                    <input type="text" id="url" name="url" value="${url}"
                           class="form-control" minlength="2">
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-2 col-sm-2 offset2">
                <input type="submit" value="Submit" class="button large">
            </div>
        </div>
    </form>


</#if>


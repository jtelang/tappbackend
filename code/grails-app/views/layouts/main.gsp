<!DOCTYPE html>
<g:if test="${request.xhr}">
    <g:render template="/layouts/mainContent" />
</g:if>
<g:else>
    <html>
        <head>
            <title><g:layoutTitle default="Grails" /></title>
            <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
            <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
            <g:layoutHead />
            <g:javascript library="application" />
            <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
            <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.8.23/jquery-ui.min.js"></script>
            <script src="https://raw.github.com/deigote/ajaxify/master/ajaxify.js"></script>
        </head>
        <body>
            <div id="loading" class="loading" style="display:none;">
                <img src="${resource(dir:'images',file:'spinner.gif')}" alt="${message(code:'spinner.alt',default:'Loading...')}" />
            </div>
            <div id="logo"><a href="/"><img src="${resource(dir:'images',file:'logo.jpg')}" alt="logo" border="0" /></a></div>
            <div id="mainContent">
                <div id="content">
                    <g:render template="/layouts/mainContent" />
                </div>
            </div>
        </body>
    </html>
</g:else>

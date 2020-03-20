<#ftl output_format="HTML">

<script>
    var cookieConsent = getCookie("CookieConsent");
    if (cookieConsent != "") {
        if (cookieConsent.indexOf("statistics:true") >= 0 && getCookie("visitortracking") == '') {
            setVisitorTracking('allowed');
        }

        if (cookieConsent.indexOf("statistics:false") >= 0) {
            clearTrackingInfo();
        }
    }

    function setVisitorTracking(status) {
        // function may be called directly

        var cookieConsent = getCookie("CookieConsent");
        if (cookieConsent.indexOf("statistics:false") >= 0) {
            if (status == 'allowed') {
                alert('Only necessary cookies have been allowed. To enable tracking, all cookies must be allowed in Cookie consent');
            }
            return;
        } else {
          var CookieDate = new Date;
          CookieDate.setFullYear(CookieDate.getFullYear() +2);
          document.cookie = 'visitortracking=' + status + '; expires=' + CookieDate.toUTCString() + '; path=/';
        }

        if (status == 'disallowed')
        {
            clearTrackingInfo();
        } 

        getVisitorTrackingStatus();
    }


    function getVisitorTrackingStatus() {
        $('#tracking-status').html(getCookie("visitortracking"));
    }


    function getCookie(cname) {
        var name = cname + "=";
        var decodedCookie = decodeURIComponent(document.cookie);
        var ca = decodedCookie.split(';');
        for(var i = 0; i <ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) == ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) == 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    }


    function clearTrackingInfo() {
        $.ajax({
            url: '/gdpr/visitorinfo',
            type: 'DELETE',
            success: function(result) {
                showTrackingInfo();
            }
        });
    }


    function showTrackingInfo() {
        $.ajax({
            url: '/gdpr/visitorinfo',
            type: 'GET',
            success: function(result) {
                $('.container-tracking-info').html('<pre>' + JSON.stringify(result, undefined, 4) + '</pre>');
            }
        });
    }


    $(function() {
        var status = getCookie("visitortracking");
        if (status == '') {
            status = 'disallowed (cookies disabled)'
        }
        $('#tracking-status').html(status);
    });

</script>

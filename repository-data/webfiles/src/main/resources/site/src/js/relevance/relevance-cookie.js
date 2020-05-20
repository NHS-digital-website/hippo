import $ from "jquery";

function getCookie(cname) {
    const name = `${cname}=`;
    const decodedCookie = decodeURIComponent(document.cookie);
    const ca = decodedCookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function showTrackingInfo() {
    $.ajax({
        url: '/gdpr/visitorinfo',
        type: 'GET',
        success(result) {
            $('.container-tracking-info').html(`<pre>${JSON.stringify(result, undefined, 4)}</pre>`);
        }
    });
}

function clearTrackingInfo() {
    $.ajax({
        url: '/gdpr/visitorinfo',
        type: 'DELETE',
        success() {
            showTrackingInfo();
        }
    });
}

function getVisitorTrackingStatus() {
    $('#tracking-status').html(getCookie("visitortracking"));
}

function setVisitorTracking(status) {
    // function may be called directly

    const cookieConsent = getCookie("CookieConsent");
    if (cookieConsent.indexOf("statistics:false") >= 0) {
        if (status === 'allowed') {
            alert('Only necessary cookies have been allowed. To enable tracking, all cookies must be allowed in Cookie consent');
        }
        return;
    }
    const CookieDate = new Date;
    CookieDate.setFullYear(CookieDate.getFullYear() + 2);
    document.cookie = `visitortracking=${status}; expires=${CookieDate.toUTCString()}; path=/`;


    if (status === 'disallowed') {
        clearTrackingInfo();
    }

    getVisitorTrackingStatus();
}

export function initCookieConsent() {

    const cookieConsent = getCookie("CookieConsent");
    if (cookieConsent !== "") {
        if (cookieConsent.indexOf("statistics:true") >= 0 && getCookie("visitortracking") === '') {
            setVisitorTracking('allowed');
        }

        if (cookieConsent.indexOf("statistics:false") >= 0) {
            clearTrackingInfo();
        }
    }

    $(function () {
        let status = getCookie("visitortracking");
        if (status === '') {
            status = 'disallowed (cookies disabled)'
        }
        $('#tracking-status').html(status);
    });
}

var tryEndpointNowWindow = null;

function tryEndpointNow(endpointPathInSpec) { // method invocation is rendered by Codegen's index.mustache template

    const endpointAnchorPath = endpointPathInSpec.replace(/_/g, '-').toLowerCase();

    // tryEndpointNowBaseUrl is populated in apispecification.ftl
    const targetWindowUrl = tryEndpointNowBaseUrl + 'api-spec-try-it-now#' + endpointAnchorPath;

    const targetWindowName = 'TryItNow';

    const targetWindowFeatures = 'menubar=no,toolbar=no,location=no,status=no,width=1000,height=1000,left=100,top=100';

    if (tryEndpointNowWindow == null || tryEndpointNowWindow.closed) {

        tryEndpointNowWindow = window.open(targetWindowUrl, targetWindowName, targetWindowFeatures)

    } else {

        tryEndpointNowWindow.location = targetWindowUrl;
        tryEndpointNowWindow.location.reload()
        tryEndpointNowWindow.focus();
    }
}
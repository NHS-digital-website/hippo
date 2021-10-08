export default function parse(url) {
    let urlSplit = url.split('#');
    urlSplit = urlSplit[0].split('?');

    if (!urlSplit[1]) return {};

    const params = {};
    const urlParams = urlSplit[1].split('&');
    urlParams.forEach(param => {
        const paramParts = param.split('=');
        params[paramParts[0]] = paramParts[1] ? paramParts[1] : '';
    });

    return params;
};
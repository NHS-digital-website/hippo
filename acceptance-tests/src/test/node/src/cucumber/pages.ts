const pages: [{
    name: string,
    url: string
}] = [{
    name: 'General test document',
    url: '/website-acceptance-tests/general-test-document'
}];

export default function getPageUrl(pageName: string) {
    const page = pages.find(page => page.name === pageName);
    return page ? page.url : null;
}

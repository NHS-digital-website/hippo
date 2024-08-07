package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.core.parameters.Parameter;

class RecentUpdatesComponentInfoImpl implements RecentUpdatesComponentInfo {

    private int pageSize = 10;
    private String pickerPath = "";
    private boolean includeChildren = true;
    private String type = "";

    public RecentUpdatesComponentInfoImpl(int pageSize, String pickerPath, boolean includeChildren, String type) {
        this.pageSize = pageSize;
        this.pickerPath = pickerPath;
        this.includeChildren = includeChildren;
        this.type = type;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public Boolean getShowPagination() {
        return false;
    }

    @Override
    public String getPath() {
        return pickerPath;
    }

    @Override
    public Boolean getIncludeSubtypes() {
        return includeChildren;
    }

    @Override
    public String getDocumentTypes() {
        return type;
    }

    @Parameter(
        name = "publisheddate",
        required = false,
        defaultValue = "hippostdpubwf:publicationDate"
    )
    @Override
    public String getPublishedDate() {
        return "publisheddate";
    }

    @Parameter(
        name = "sortField",
        required = false,
        defaultValue = "hippostdpubwf:publicationDate"
    )
    @Override
    public String getSortField() {
        return "hippostdpubwf:publicationDate";
    }

    @Override
    public String getSortOrder() {
        return "desc";
    }
}

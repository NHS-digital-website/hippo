package uk.nhs.digital.common.components.catalogue;

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

    @Override
    public String getPublishedDate() {
        return "website:publisheddate";
    }

    @Override
    public String getSortField() {
        return "website:publisheddate";
    }

    @Override
    public String getSortOrder() {
        return "desc";
    }
}

package uk.nhs.digital.common.components.catalogue;

class RecentUpdatesComponentInfoImpl implements RecentUpdatesComponentInfo {

    private int pageSize = 10;
    private String pickerPath = "";

    public RecentUpdatesComponentInfoImpl(int pageSize, String pickerPath) {
        this.pageSize = pageSize;
        this.pickerPath = pickerPath;
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
        return false;
    }

    @Override
    public String getDocumentTypes() {
        return "website:service";
    }

    @Override
    public String getPublishedDate() {
        return "publisheddate";
    }

    @Override
    public String getSortField() {
        return "publisheddate";
    }

    @Override
    public String getSortOrder() {
        return "desc";
    }
}

package uk.nhs.digital.ps.test.acceptance.models;

public class Folder {

    private String name;
    private String parentPath;

    public Folder(String parentPath, String name) {
        this.name = name;
        this.parentPath = parentPath;
    }

    public String getName() {
        return name;
    }

    public String getParentPath() {
        return parentPath;
    }

    public String getPath() {
        return parentPath + "/" + name;
    }
}

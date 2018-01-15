package uk.nhs.digital.ps.migrator.model.hippo;

public class ResourceLink  {

    private final String name;
    private final String uri;

    public ResourceLink(String name, String uri) {
        this.name = name;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return "ResourceLink{" +
            "name='" + name + '\'' +
            ", uri='" + uri + '\'' +
            '}';
    }
}

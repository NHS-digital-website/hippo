package uk.nhs.digital.ps.test.acceptance.models;

import org.apache.commons.io.FilenameUtils;

import java.nio.file.Path;
import java.text.MessageFormat;

@SuppressWarnings("WeakerAccess") // builder's methods are intentionally public
public class ResourceBuilder {

    private String name;
    private Path path;

    public static ResourceBuilder newResource() {
        return new ResourceBuilder();
    }

    //<editor-fold desc="BUILDER METHODS">
    public ResourceBuilder withName(final String name) {
        vetSettingProperty("name");

        return cloneAndAmend(builder -> builder.name = name);
    }

    public ResourceBuilder withFile(final Path path) {

        final String fileName = FilenameUtils.getBaseName(path.getFileName().toString());

        return cloneAndAmend(builder -> {
            builder.path = path;
            builder.name = fileName;
        });
    }

    public Path getPath() {
        return path;
    }
    //</editor-fold>

    public Resource build() {
        return new Resource(this);
    }

    String getName() {
        return name;
    }

    private ResourceBuilder() {
        // no-op; made private to promote the use of factory methods
    }

    private ResourceBuilder(final ResourceBuilder original) {
        name = original.getName();
        path = original.getPath();
    }

    private ResourceBuilder cloneAndAmend(final PropertySetter propertySetter) {
        final ResourceBuilder clone = new ResourceBuilder(this);
        propertySetter.setProperties(clone);
        return clone;
    }

    private void vetSettingProperty(final String propertyName) {
        if (path != null) {
            throw new IllegalStateException(MessageFormat.format(
                "Setting of {0} is not permitted as this attachment is already backed by an actual file and " +
                    "changing its {0} would leave it in inconsistent state. To set arbitrary {0} use use a builder " +
                    "without the path set.", propertyName)
            );
        }
    }

    @FunctionalInterface
    interface PropertySetter {
        void setProperties(ResourceBuilder builder);
    }
}

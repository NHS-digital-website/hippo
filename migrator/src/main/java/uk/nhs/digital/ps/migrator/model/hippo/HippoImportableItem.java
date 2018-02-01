package uk.nhs.digital.ps.migrator.model.hippo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import uk.nhs.digital.ps.migrator.misc.TextHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static java.util.Collections.reverse;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

public abstract class HippoImportableItem {

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    public static final String EMPTY_DATE = "0001-01-01T12:00:00.000Z";

    private static final String ROOT_PATH_PREFIX = "/content/documents/corporate-website/publication-system";

    protected final Folder parentFolder;

    private String localizedName;
    private String jcrNodeName;
    private String jcrPath;

    protected HippoImportableItem(final Folder parentFolder,
                                  final String localizedName
    ) {
        this.parentFolder = parentFolder;

        setLocalizedName(localizedName);

        updateJcrNodeName();

        updateJcrPath();
    }

    /**
     * Absolute path locating a node in JCR repository, for example
     * '{@code /content/documents/corporate-website/publication-system/my-publication}'
     */
    public String getJcrPath() {
        return jcrPath;
    }

    /**
     * Machine-friendly node name as seen in JCR tree of nodes in the Console; a.k.a. 'url name'.
     */
    public String getJcrNodeName() {
        return jcrNodeName;
    }

    public void setJcrNodeName(final String jcrNodeName) {
        this.jcrNodeName = jcrNodeName;
        updateJcrPath();
    }

    /** Human-friendly node name as displayed in the tree of folders and documents in CMS. */
    public String getLocalizedName() {
        return localizedName;
    }

    /**
     * <p>
     * Sets the human-friendly name of a node as displayed in the tree of folders and documents in CMS.
     * </p><p>
     * NOTE: also updates {@linkplain #jcrNodeName} and {@linkplain #jcrPath} with values derived from
     * the display name, using default algorithm.
     * </p>
     */
    public void setLocalizedName(final String localizedName) {
        this.localizedName = localizedName;
        updateJcrNodeName();
    }

    public int getDepth() {
        final AtomicInteger depth = new AtomicInteger(0);

        visitAncestorFolders(folder -> depth.addAndGet(1));

        return depth.get();
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, SHORT_PREFIX_STYLE);
    }

    private void updateJcrNodeName() {
        setJcrNodeName(TextHelper.toLowerCaseDashedValue(getLocalizedName()));
    }

    private void updateJcrPath() {

        final List<String> pathComponents = new ArrayList<>();
        pathComponents.add(jcrNodeName);

        visitAncestorFolders(folder -> pathComponents.add(folder.getJcrNodeName()));

        reverse(pathComponents);

        this.jcrPath = pathComponents.stream().collect(joining("/", ROOT_PATH_PREFIX + "/", ""));
    }

    private void visitAncestorFolders(final Consumer<Folder> folderVisitor) {
        for (Folder currentParent = this.parentFolder;
             currentParent != null;
             currentParent = currentParent.parentFolder) {

            folderVisitor.accept(currentParent);
        }
    }

    public static Date parseDate(String dateStr) {
        try {
            return DATE_FORMAT.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}

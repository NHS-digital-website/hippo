package uk.nhs.digital.ps.migrator.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Ignore;
import org.junit.Test;
import uk.nhs.digital.ps.migrator.model.hippo.Folder;
import uk.nhs.digital.ps.migrator.model.hippo.Publication;

@Ignore("These are just experiments of tools (potentially) useful for the migrator, not actual tests")
public class PojosToJsonTest {

    private static ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

    @Test
    public void testPublication() throws Exception {

        // given
        final Folder rootFolder = new Folder(
            null,
            "root-folder"
        );

        final Folder publicationParentFolder = new Folder(
            rootFolder,
            "publication-parent-folder"
        );

        final Publication value = new Publication(
            publicationParentFolder,
            "publication-name",
            "publication-title"
        );

        // when
        final String json = objectWriter.writeValueAsString(value);

        // then
        System.out.println(json);
    }
}

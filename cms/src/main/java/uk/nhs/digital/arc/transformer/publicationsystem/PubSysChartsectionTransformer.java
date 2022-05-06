package uk.nhs.digital.arc.transformer.publicationsystem;

import org.apache.commons.io.IOUtils;
import org.apache.jackrabbit.value.BinaryImpl;
import org.onehippo.forge.content.pojo.model.BinaryValue;
import org.onehippo.forge.content.pojo.model.ContentNode;
import uk.nhs.digital.arc.json.PublicationBodyItem;
import uk.nhs.digital.arc.json.publicationsystem.PublicationsystemChartSection;
import uk.nhs.digital.arc.storage.ArcFileData;
import uk.nhs.digital.arc.storage.ArcStorageManager;
import uk.nhs.digital.arc.transformer.abs.AbstractSectionTransformer;
import uk.nhs.digital.arc.util.FilePathData;
import uk.nhs.digital.arc.util.HighchartsInputConversionForArc;

import java.io.IOException;
import java.io.InputStream;
import javax.jcr.Binary;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class PubSysChartsectionTransformer extends AbstractSectionTransformer {

    private final PublicationsystemChartSection chartSection;

    public PubSysChartsectionTransformer(Session session, PublicationBodyItem section, String docbase, ArcStorageManager storageManager) {
        super(session, docbase, storageManager);
        chartSection = (PublicationsystemChartSection) section;
    }

    @Override
    public ContentNode process() {
        ContentNode sectionNode = new ContentNode(PUBLICATIONSYSTEM_BODYSECTIONS, PUBLICATIONSYSTEM_CHARTSECTION);

        sectionNode.setProperty(PUBLICATIONSYSTEM_TITLE, chartSection.getTitleReq());
        sectionNode.setProperty(PUBLICATIONSYSTEM_TYPE, chartSection.getTypeReq());
        sectionNode.setProperty(PUBLICATIONSYSTEM_YTITLE, chartSection.getyTitleReq());
        getChartDataAsJsonFromContentOfResource(sectionNode);

        return sectionNode;
    }

    /**
     * Since we only have one concrete implementation of {@link uk.nhs.digital.arc.storage.ArcStorageManager}, which is {@link uk.nhs.digital.arc.storage.S3StorageManager}
     * then we are really only considering S3 based data this point in time, although it could be implemented easily enough to locate its data
     * from an alternate source using a descendent of the {@link uk.nhs.digital.arc.storage.ArcStorageManager} interface
     *
     * @param sectionNode is the node to which we attach the storage meta data once loaded
     */
    private void getChartDataAsJsonFromContentOfResource(ContentNode sectionNode) {
        FilePathData sourceFilePathData = new FilePathData(docbase, chartSection.getDataFileReq());
        ArcFileData metaData = storageManager.getFileMetaData(sourceFilePathData);

        if (sourceFilePathData.isS3Protocol()) {
            try {
                Binary binaryData = getJsonDataAndCreateBinaryObjectFromProvidedFile(sectionNode, metaData.getDelegateStream());

                ContentNode newNode = new ContentNode(PUBLICATIONSYSTEM_DATAFILE, PUBLICATIONSYSTEM_RESOURCE);

                this.addFileRelatedProperties(newNode,
                    new BinaryValue(IOUtils.toByteArray(binaryData.getStream())),
                    metaData.getContentType(),
                    sourceFilePathData.getFilename());

                sectionNode.addNode(newNode);
            } catch (IOException | RepositoryException exception) {
                exception.printStackTrace();
            }
        }
    }

    private Binary getJsonDataAndCreateBinaryObjectFromProvidedFile(ContentNode sectionNode, InputStream inputStream) throws IOException, RepositoryException {
        Binary binary = new BinaryImpl(inputStream);
        HighchartsInputConversionForArc conversion = new HighchartsInputConversionForArc();

        String json = conversion.process(chartSection.getTypeReq(),
            chartSection.getTitleReq(),
            chartSection.getyTitleReq(),
            binary);

        sectionNode.setProperty(PUBLICATIONSYSTEM_CHARTCONFIG, json);
        return binary;
    }
}

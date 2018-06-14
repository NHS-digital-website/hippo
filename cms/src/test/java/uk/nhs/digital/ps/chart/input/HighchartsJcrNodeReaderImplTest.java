package uk.nhs.digital.ps.chart.input;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.mock;
import static uk.nhs.digital.ps.PublicationSystemConstants.*;

import org.apache.sling.testing.mock.jcr.MockJcr;
import org.junit.Before;
import org.junit.Test;
import uk.nhs.digital.ps.chart.*;

import java.util.UUID;
import javax.jcr.*;

public class HighchartsJcrNodeReaderImplTest {

    private HighchartsJcrNodeReaderImpl highchartsJcrNodeReader;

    private HighchartsParameters chartConfig;
    private Node chartConfigNode;

    private HighmapsParameters mapConfig;
    private Node mapConfigNode;

    private Node documentVariantNode;

    @Before
    public void setUp() throws RepositoryException {
        highchartsJcrNodeReader = new HighchartsJcrNodeReaderImpl();

        documentVariantNode = MockJcr.newSession().getRootNode()
            .addNode("document", "hippo:handle")
            .addNode("document", "irrelevant:doctype");
    }

    @Test
    public void readChartParameters() throws RepositoryException {
        // given
        initValidDocumentNodeStructure();

        // when
        AbstractHighchartsParameters parameters = highchartsJcrNodeReader.readParameters(chartConfigNode);

        // then
        assertThat("Correct parameter type returned", parameters, instanceOf(HighchartsParameters.class));
        assertThat("Correct parameter values", parameters, samePropertyValuesAs(chartConfig));
    }

    @Test
    public void readMapParameters() throws RepositoryException {
        // given
        initValidDocumentNodeStructure();

        // when
        AbstractHighchartsParameters parameters = highchartsJcrNodeReader.readParameters(mapConfigNode);

        // then
        assertThat("Correct parameter type returned", parameters, instanceOf(HighmapsParameters.class));
        assertThat("Correct parameter values", parameters, samePropertyValuesAs(mapConfig));
    }

    private void initValidDocumentNodeStructure() throws RepositoryException {

        // node structure:
        // /
        // +- document                              (hippo:handle)
        //    +- document                           (irrelevant:doctype)               documentVariantNode
        //       +- publicationsystem:bodySections  (publicationsystem:chartSection)   chartConfigNode
        //       |  - publicationsystem:title       (String)
        //       |  - publicationsystem:type        (String)
        //       |  - publicationsystem:yTitle      (String)
        //       |  +- publicationsystem:dataFile   (publicationsystem:resource)
        //       |     - jcr:data                   (Binary)
        //       |
        //       +- publicationsystem:bodySections  (publicationsystem:non-chart-section)
        //       |
        //       +- publicationsystem:bodySections  (publicationsystem:mapSection)      mapConfigNode
        //          - publicationsystem:title       (String)
        //          - publicationsystem:type        (String)
        //          - publicationsystem:yTitle      (String)
        //          +- publicationsystem:dataFile   (publicationsystem:resource)
        //             - jcr:data                   (Binary)

        // chart section A parameters
        chartConfig = new HighchartsParameters(ChartType.FUNNEL_PLOT.name(), newRandomString(), newRandomString(), mock(Binary.class));

        // chart section B parameters
        mapConfig = new HighmapsParameters(ChartType.AREA_MAP.name(), MapSource.BRITISH_ISLES_COUNTIES.name(), newRandomString(), mock(Binary.class));

        // nodes' creation
        chartConfigNode = addChartConfigNode(documentVariantNode, chartConfig, "[A]");
        documentVariantNode.addNode(
            // non-chart section node to ensure query takes node types into account
            // and ignores the irrelevant ones
            "publicationsystem:bodySections", "publicationsystem:non-chart-section"
        );
        mapConfigNode = addMapConfigNode(documentVariantNode, mapConfig, "[B]");
    }

    private Node addChartConfigNode(final Node documentVariantNode,
                                    final HighchartsParameters chartConfig,
                                    final String nodeNameSuffix
    ) throws RepositoryException {

        final Node chartConfigNode = documentVariantNode.addNode(
            "publicationsystem:bodySections" + nodeNameSuffix, "publicationsystem:chartSection"
        );

        chartConfigNode.setProperty(PROPERTY_CHART_TITLE, chartConfig.getTitle());
        chartConfigNode.setProperty(PROPERTY_CHART_TYPE, chartConfig.getChartType().toString());
        chartConfigNode.setProperty(PROPERTY_CHART_YTITLE, chartConfig.getYTitle());

        final Node dataFileNode = chartConfigNode.addNode(
            NODE_TYPE_DATA_FILE, "publicationsystem:resource"
        );
        dataFileNode.setProperty("jcr:data", chartConfig.getInputFileContent());

        return chartConfigNode;
    }

    private Node addMapConfigNode(final Node documentVariantNode,
                                  final HighmapsParameters mapConfig,
                                  final String nodeNameSuffix
    ) throws RepositoryException {

        final Node chartConfigNode = documentVariantNode.addNode(
            "publicationsystem:bodySections" + nodeNameSuffix, "publicationsystem:mapSection"
        );

        chartConfigNode.setProperty(PROPERTY_CHART_TITLE, mapConfig.getTitle());
        chartConfigNode.setProperty(PROPERTY_MAP_SOURCE, mapConfig.getMapSource().name());

        final Node dataFileNode = chartConfigNode.addNode(
            NODE_TYPE_DATA_FILE, "publicationsystem:resource"
        );
        dataFileNode.setProperty("jcr:data", mapConfig.getInputFileContent());

        return chartConfigNode;
    }

    private String newRandomString() {
        return UUID.randomUUID().toString();
    }
}

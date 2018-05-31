package uk.nhs.digital.ps.workflow.highcharts;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.nhs.digital.ps.PublicationSystemConstants.*;

import org.apache.sling.testing.mock.jcr.MockJcr;
import org.apache.sling.testing.mock.jcr.MockQueryResult;
import org.hippoecm.repository.api.WorkflowContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.onehippo.repository.documentworkflow.DocumentVariant;
import uk.nhs.digital.common.util.json.JsonSerialiser;
import uk.nhs.digital.ps.ChartConfig;
import uk.nhs.digital.ps.chart.HighchartsInputParser;
import uk.nhs.digital.ps.chart.SeriesChart;

import java.util.UUID;
import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class HighchartsInputConversionTaskTest {

    @Mock private WorkflowContext workflowContext;
    @Mock private JsonSerialiser jsonSerialiser;
    @Mock private HighchartsInputParser highchartsInputParser;
    @Mock private DocumentVariant documentVariant;

    private Repository repository;
    private Session workflowSession;
    private Node documentVariantNode;

    private ChartConfig chartConfigA;
    private String chartConfigJsonA;
    private SeriesChart seriesChartA;
    private Node chartConfigNodeA;

    private ChartConfig chartConfigB;
    private String chartConfigJsonB;
    private SeriesChart seriesChartB;
    private Node chartConfigNodeB;

    private HighchartsInputConversionTask highchartsInputConversionTask;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        repository = MockJcr.newRepository();
        workflowSession = repository.login();

        given(workflowContext.getInternalWorkflowSession()).willReturn(workflowSession);

        documentVariantNode = workflowSession.getRootNode()
            .addNode("document", "hippo:handle")
            .addNode("document", "irrelevant:doctype");

        given(documentVariant.getNode(workflowSession)).willReturn(documentVariantNode);

        highchartsInputConversionTask = new HighchartsInputConversionTask(
            jsonSerialiser, highchartsInputParser
        );
        highchartsInputConversionTask.setWorkflowContext(workflowContext);
        highchartsInputConversionTask.setVariant(documentVariant);
    }

    @Test
    public void parsesAllChartsInputs_onDocumentSave() throws Exception {

        // given
        initValidDocumentNodeStructure();

        // when
        highchartsInputConversionTask.doExecute();

        // then
        then(highchartsInputParser).should().parse(chartConfigA);
        then(jsonSerialiser).should().toJson(seriesChartA);
        assertThat("Chart JSON config set on node A",
            chartConfigNodeA.getProperty(PROPERTY_CHART_CONFIG).getString(),
            is(chartConfigJsonA)
        );

        then(highchartsInputParser).should().parse(chartConfigB);
        then(jsonSerialiser).should().toJson(seriesChartB);
        assertThat("Chart JSON config set on node B",
            chartConfigNodeB.getProperty(PROPERTY_CHART_CONFIG).getString(),
            is(chartConfigJsonB)
        );
    }

    @Test
    public void doesNothing_whenNoChartSections_onDocumentSave() throws Exception {

        // given
        // setUp

        // when
        highchartsInputConversionTask.doExecute();

        // then
        then(highchartsInputParser).shouldHaveZeroInteractions();
        then(jsonSerialiser).shouldHaveZeroInteractions();
    }

    @Test
    public void reThrowsParsingException_onDocumentSave() throws Exception {

        // given
        initValidDocumentNodeStructure();

        final RuntimeException expectedException = new RuntimeException(
            "parsing exception" + newRandomString()
        );
        given(highchartsInputParser.parse(chartConfigA)).willThrow(expectedException);

        // when/then
        assertExceptionReThrown(expectedException);
    }

    @Test
    public void reThrowsJsonConversionException_onDocumentSave() throws Exception {

        // given
        initValidDocumentNodeStructure();

        final RuntimeException expectedException = new RuntimeException(
            "json serialising exception" + newRandomString()
        );
        given(jsonSerialiser.toJson(seriesChartA)).willThrow(expectedException);

        // when/then
        assertExceptionReThrown(expectedException);
    }

    private void assertExceptionReThrown(final RuntimeException expectedException) {
        try {
            // when
            highchartsInputConversionTask.doExecute();

            // then
            fail("No exception was thrown.");
        } catch (final RepositoryException actualException) {
            assertThat("Exception has expected cause.",
                actualException.getCause(), is(expectedException)
            );
        } catch (final Exception actualException) {
            fail("Unexpected exception has been thrown: " + actualException);
        }
    }

    private void initValidDocumentNodeStructure() throws RepositoryException {

        // node structure:
        // /
        // +- document                              (hippo:handle)
        //    +- document                           (irrelevant:doctype)               documentVariantNode
        //       +- publicationsystem:bodySections  (publicationsystem:chartSection)   chartConfigNodeA
        //       |  - publicationsystem:chartConfig (String)                           chartConfigJsonA
        //       |  - publicationsystem:title       (String)
        //       |  - publicationsystem:type        (String)
        //       |  - publicationsystem:yTitle      (String)
        //       |  +- publicationsystem:dataFile   (publicationsystem:resource)
        //       |     - jcr:data                   (Binary)
        //       |
        //       +- publicationsystem:bodySections  (publicationsystem:non-chart-section)
        //       |
        //       +- publicationsystem:bodySections  (publicationsystem:chartSection)   chartConfigNodeB
        //          - publicationsystem:chartConfig (String)                           chartConfigJsonB
        //          - publicationsystem:title       (String)
        //          - publicationsystem:type        (String)
        //          - publicationsystem:yTitle      (String)
        //          +- publicationsystem:dataFile   (publicationsystem:resource)
        //             - jcr:data                   (Binary)

        // chart section A parameters

        chartConfigA = newChartConfig();
        chartConfigJsonA = newRandomString();
        seriesChartA = mock(SeriesChart.class);
        given(highchartsInputParser.parse(chartConfigA)).willReturn(seriesChartA);
        given(jsonSerialiser.toJson(seriesChartA)).willReturn(chartConfigJsonA);

        // chart section B parameters

        chartConfigB = newChartConfig();
        chartConfigJsonB = newRandomString();
        seriesChartB = mock(SeriesChart.class);
        given(highchartsInputParser.parse(chartConfigB)).willReturn(seriesChartB);
        given(jsonSerialiser.toJson(seriesChartB)).willReturn(chartConfigJsonB);

        // nodes' creation

        chartConfigNodeA = addChartConfigNode(documentVariantNode, chartConfigA, "[A]");
        documentVariantNode.addNode(
            // non-chart section node to ensure query takes node types into account
            // and ignores the irrelevant ones
            "publicationsystem:bodySections", "publicationsystem:non-chart-section"
        );
        chartConfigNodeB = addChartConfigNode(documentVariantNode, chartConfigB, "[B]");

        // query for chart config nodes

        final String documentVariantNodePath = documentVariantNode.getPath();
        MockJcr.addQueryResultHandler(
            workflowSession,
            mockQuery -> mockQuery.getStatement().matches(
                "SELECT \\* FROM \\[publicationsystem:chartSection].*ISDESCENDANTNODE \\(\\['"
                    + documentVariantNodePath + "']\\).*"
            )
                ? new MockQueryResult(asList(chartConfigNodeA, chartConfigNodeB))
                : null
        );
    }

    private Node addChartConfigNode(final Node documentVariantNode,
                                    final ChartConfig chartConfig,
                                    final String nodeNameSuffix
    ) throws RepositoryException {

        final Node chartConfigNode = documentVariantNode.addNode(
            "publicationsystem:bodySections" + nodeNameSuffix, "publicationsystem:chartSection"
        );

        chartConfigNode.setProperty(PROPERTY_CHART_TITLE, chartConfig.getTitle());
        chartConfigNode.setProperty(PROPERTY_CHART_TYPE, chartConfig.getType());
        chartConfigNode.setProperty(PROPERTY_CHART_YTITLE, chartConfig.getYTitle());

        final Node dataFileNode = chartConfigNode.addNode(
            NODE_TYPE_DATA_FILE, "publicationsystem:resource"
        );
        dataFileNode.setProperty("jcr:data", chartConfig.getInputFileContent());

        return chartConfigNode;
    }

    private ChartConfig newChartConfig() {
        return new ChartConfig(newRandomString(), newRandomString(), newRandomString(), mock(Binary.class));
    }

    private String newRandomString() {
        return UUID.randomUUID().toString();
    }
}

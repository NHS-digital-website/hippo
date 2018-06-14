package uk.nhs.digital.ps.workflow.highcharts;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.nhs.digital.ps.PublicationSystemConstants.PROPERTY_CHART_CONFIG;
import static uk.nhs.digital.ps.chart.ChartType.AREA_MAP;
import static uk.nhs.digital.ps.chart.MapSource.BRITISH_ISLES_COUNTIES;

import org.apache.sling.testing.mock.jcr.MockJcr;
import org.apache.sling.testing.mock.jcr.MockQueryResult;
import org.hippoecm.repository.api.WorkflowContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.onehippo.repository.documentworkflow.DocumentVariant;
import uk.nhs.digital.common.util.json.JsonSerialiser;
import uk.nhs.digital.ps.chart.*;
import uk.nhs.digital.ps.chart.input.HighchartsInputParser;
import uk.nhs.digital.ps.chart.input.HighchartsJcrNodeReader;
import uk.nhs.digital.ps.chart.model.HighchartsModel;
import uk.nhs.digital.ps.chart.model.HighmapsModel;

import java.util.List;
import java.util.UUID;
import javax.jcr.*;

public class HighchartsInputConversionTaskTest {

    @Mock private WorkflowContext workflowContext;
    @Mock private JsonSerialiser jsonSerialiser;
    @Mock private HighchartsInputParser highchartsInputParser;
    @Mock private HighchartsJcrNodeReader highchartsJcrNodeReader;
    @Mock private DocumentVariant documentVariant;

    private Session workflowSession;
    private Node documentVariantNode;

    private HighchartsParameters chartConfigA;
    private String chartConfigJsonA;
    @Mock private HighchartsModel seriesChartA;
    @Mock private Node chartConfigNodeA;

    private HighchartsParameters chartConfigB;
    private String chartConfigJsonB;
    @Mock private HighchartsModel seriesChartB;
    @Mock private Node chartConfigNodeB;

    private HighmapsParameters mapConfig;
    private String mapConfigJson;
    @Mock private HighmapsModel mapModel;
    @Mock private Node mapConfigNode;

    private HighchartsInputConversionTask highchartsInputConversionTask;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        workflowSession = MockJcr.newSession();

        given(workflowContext.getInternalWorkflowSession()).willReturn(workflowSession);

        documentVariantNode = workflowSession.getRootNode()
            .addNode("document", "hippo:handle")
            .addNode("document", "irrelevant:doctype");

        given(documentVariant.getNode(workflowSession)).willReturn(documentVariantNode);

        highchartsInputConversionTask = new HighchartsInputConversionTask(
            jsonSerialiser, highchartsInputParser, highchartsJcrNodeReader);
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
        then(highchartsInputParser).should().parse(refEq(chartConfigA));
        then(jsonSerialiser).should().toJson(refEq(seriesChartA));
        then(chartConfigNodeA).should().setProperty(PROPERTY_CHART_CONFIG, chartConfigJsonA);

        then(highchartsInputParser).should().parse(refEq(chartConfigB));
        then(jsonSerialiser).should().toJson(refEq(seriesChartB));
        then(chartConfigNodeB).should().setProperty(PROPERTY_CHART_CONFIG, chartConfigJsonB);

        then(highchartsInputParser).should().parse(refEq(mapConfig));
        then(jsonSerialiser).should().toJson(refEq(mapModel));
        then(mapConfigNode).should().setProperty(PROPERTY_CHART_CONFIG, mapConfigJson);
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
        given(highchartsInputParser.parse(refEq(chartConfigA))).willThrow(expectedException);

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
        given(jsonSerialiser.toJson(refEq(seriesChartA))).willThrow(expectedException);

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
        given(highchartsInputParser.parse(refEq(chartConfigA))).willReturn(seriesChartA);
        given(jsonSerialiser.toJson(seriesChartA)).willReturn(chartConfigJsonA);

        // chart section B parameters

        chartConfigB = newChartConfig();
        chartConfigJsonB = newRandomString();
        given(highchartsInputParser.parse(refEq(chartConfigB))).willReturn(seriesChartB);
        given(jsonSerialiser.toJson(seriesChartB)).willReturn(chartConfigJsonB);

        // map section parameters

        mapConfig = new HighmapsParameters(AREA_MAP.name(), BRITISH_ISLES_COUNTIES.name(), newRandomString(), mock(Binary.class));
        mapConfigJson = newRandomString();
        given(highchartsInputParser.parse(refEq(mapConfig))).willReturn(mapModel);
        given(jsonSerialiser.toJson(mapModel)).willReturn(mapConfigJson);

        // node parameter reading

        given(highchartsJcrNodeReader.readParameters(chartConfigNodeA)).willReturn(chartConfigA);
        given(highchartsJcrNodeReader.readParameters(chartConfigNodeB)).willReturn(chartConfigB);
        given(highchartsJcrNodeReader.readParameters(mapConfigNode)).willReturn(mapConfig);

        // query for chart config nodes

        addQueryResult("publicationsystem:chartSection", asList(chartConfigNodeA, chartConfigNodeB));
        addQueryResult("publicationsystem:mapSection", singletonList(mapConfigNode));
    }

    private void addQueryResult(String primaryType, List<Node> result) throws RepositoryException {
        final String documentVariantNodePath = documentVariantNode.getPath();
        MockJcr.addQueryResultHandler(
            workflowSession,
            mockQuery -> mockQuery.getStatement().matches(
                "SELECT \\* FROM \\["
                    + primaryType
                    + "].*ISDESCENDANTNODE \\(\\['"
                    + documentVariantNodePath + "']\\).*"
            )
                ? new MockQueryResult(result)
                : null
        );
    }

    private HighchartsParameters newChartConfig() {
        return new HighchartsParameters(ChartType.PIE.name(), newRandomString(), newRandomString(), mock(Binary.class));
    }

    private String newRandomString() {
        return UUID.randomUUID().toString();
    }
}

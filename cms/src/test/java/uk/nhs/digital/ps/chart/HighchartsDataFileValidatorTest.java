package uk.nhs.digital.ps.chart;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;

import org.hippoecm.repository.impl.NodeDecorator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Violation;
import org.onehippo.cms7.services.HippoServiceRegistry;
import uk.nhs.digital.ps.chart.input.HighchartsInputParser;
import uk.nhs.digital.ps.chart.input.HighchartsJcrNodeReader;

import java.util.Optional;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class HighchartsDataFileValidatorTest {

    @Mock
    private HighchartsInputParser highchartsInputParser;
    @Mock
    private HighchartsJcrNodeReader highchartsJcrNodeReader;
    @Mock
    private AbstractHighchartsParameters highchartsParameters;

    @Mock
    private ValidationContext validationContextMock;

    @Mock
    private Node nodeMock;

    @Mock
    private Violation violation;

    private HighchartsDataFileValidator highchartsDataFileValidator;

    private NodeDecorator nodeDecorator;

    @Before
    public void setUp() {
        openMocks(this);

        HippoServiceRegistry.register(highchartsInputParser, HighchartsInputParser.class);
        HippoServiceRegistry.register(highchartsJcrNodeReader, HighchartsJcrNodeReader.class);

        highchartsDataFileValidator = new HighchartsDataFileValidator();
    }

    @After
    public void tearDown() {
        HippoServiceRegistry.unregister(highchartsInputParser, HighchartsInputParser.class);
        HippoServiceRegistry.unregister(highchartsJcrNodeReader, HighchartsJcrNodeReader.class);
    }

    @Test
    public void reportsValidationViolation_forInvalidChartInputFile() throws Exception {
        setUpChartInput();
        given(highchartsInputParser.parse(highchartsParameters)).willThrow(new RuntimeException());
        given(validationContextMock.createViolation()).willReturn(violation);
        given(validationContextMock.getParentNode()).willReturn(nodeMock);

        final Optional<Violation> violation = highchartsDataFileValidator.validate(validationContextMock, nodeDecorator);

        assertThat("Violation present", violation.isPresent());
    }

    @Test
    public void reportsNoViolation() throws Exception {
        setUpChartInput();

        given(validationContextMock.getParentNode()).willReturn(nodeMock);
        final Optional<Violation> violation = highchartsDataFileValidator.validate(validationContextMock, nodeDecorator);

        assertThat("Violation is not present", !violation.isPresent());
    }

    private void setUpChartInput() throws RepositoryException {
        given(validationContextMock.getDocumentNode()).willReturn(nodeMock);
        given(nodeMock.getParent()).willReturn(nodeMock);
        given(highchartsJcrNodeReader.readParameters(nodeMock)).willReturn(highchartsParameters);
    }

}

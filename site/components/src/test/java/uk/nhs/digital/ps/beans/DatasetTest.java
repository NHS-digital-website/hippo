package uk.nhs.digital.ps.beans;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.manager.ObjectConverter;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.standard.HippoFolder;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.mock.core.request.MockHstRequestContext;
import org.hippoecm.hst.provider.jcr.JCRValueProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import uk.nhs.digital.ps.site.exceptions.DataRestrictionViolationException;
import uk.nhs.digital.ps.test.util.ReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jcr.Node;
import javax.jcr.NodeIterator;


@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PowerMockRunnerDelegate(DataProviderRunner.class)
@PrepareForTest(HstQueryBuilder.class)
public class DatasetTest {

    private static final String ATTACHMENTS_PROPERTY_KEY = "publicationsystem:Files-v3";
    private static final String RELATED_LINKS_PROPERTY_KEY = "publicationsystem:RelatedLinks";
    private static final String RESOURCE_LINKS_PROPERTY_KEY = "publicationsystem:ResourceLinks";

    @Mock private JCRValueProvider valueProvider;
    @Mock private Node node;
    @Mock private NodeIterator nodeIterator;
    @Mock private ObjectConverter objectConverter;
    @Mock private Node folderNode;
    @Mock private HippoFolder folder;
    @Mock private Publication publication;

    @Mock private HstRequestContext requestContext;
    @Mock private HstQueryBuilder queryBuilder;
    @Mock private HstQuery query;
    @Mock private HstQueryResult queryResult;

    private final Map<String, Object> beanProperties = new HashMap<>();
    private Dataset dataset;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        beanProperties.clear();

        dataset = new Dataset();

        given(valueProvider.getProperties()).willReturn(beanProperties);
        given(valueProvider.getParentJcrNode()).willReturn(folderNode);

        given(nodeIterator.hasNext()).willReturn(false);
        given(node.getNodes(ATTACHMENTS_PROPERTY_KEY)).willReturn(nodeIterator);
        given(node.getNodes(RELATED_LINKS_PROPERTY_KEY)).willReturn(nodeIterator);
        given(node.getNodes(RESOURCE_LINKS_PROPERTY_KEY)).willReturn(nodeIterator);

        // Mock parent publication
        given(folder.getBean("content", Publication.class)).willReturn(publication);
        given(objectConverter.getObject(folderNode)).willReturn(folder);

        given(folderNode.getNodes()).willReturn(nodeIterator);

        // Mock this static method so we can simulate the building and execution of a query
        PowerMockito.mockStatic(HstQueryBuilder.class);
        PowerMockito.when(HstQueryBuilder.create(folder)).thenReturn(queryBuilder);

        given(queryBuilder.ofTypes(any(Class.class))).willReturn(queryBuilder);
        given(queryBuilder.orderByDescending(anyString())).willReturn(queryBuilder);
        given(queryBuilder.build()).willReturn(query);
        given(query.execute()).willReturn(queryResult);

        ReflectionHelper.callMethod(RequestContextProvider.class, "set", HstRequestContext.class, requestContext);

        ReflectionHelper.setField(dataset, "name", "arbitrary-name");
        ReflectionHelper.setField(dataset, "valueProvider", valueProvider);
        ReflectionHelper.setField(dataset, "node", node);
        ReflectionHelper.setField(dataset, "objectConverter", objectConverter);
    }

    @Test
    @UseDataProvider("gettersForbiddenInUpcomingPublication")
    public void restrictsGetters_whenPublicationNotPubliclyAvailable(
        final Method forbiddenGetter) throws Exception {

        // given
        given(publication.isPubliclyAccessible()).willReturn(false);

        try {
            // when
            forbiddenGetter.invoke(dataset);

            // then
            fail("No exception was thrown from getter " + forbiddenGetter);

        } catch (final InvocationTargetException exception) {
            assertThat("Getter " + forbiddenGetter + " throws exception of correct type.", exception.getCause(),
                instanceOf(DataRestrictionViolationException.class)
            );
            assertThat("Getter " + forbiddenGetter + " throws exception with correct message.", exception.getCause().getMessage(),
                startsWith("Property is not available when parent publication is flagged as 'not publicly accessible':")
            );
        } catch (final Exception exception) {
            System.out.println(exception.getMessage());
            throw new Error("Failed to test '" + forbiddenGetter + " because of an error;"
                + " see stack trace below for details.", exception
            );
        }
    }

    @Test
    @UseDataProvider("allPublicGetters")
    public void permitsAllGetters_whenPublicationFlaggedAsPubliclyAvailable(final Method permittedGetter) throws Exception {

        // given
        given(publication.isPubliclyAccessible()).willReturn(true);

        // some getters have arguments so we need to construct mocked instances and pass them in as parameters
        Object[] args = Arrays.stream(permittedGetter.getParameterTypes())
            .map(DatasetTest::newInstanceForClass)
            .toArray();

        try {
            // when
            permittedGetter.invoke(dataset, args);

            // then
            // pass
        } catch (final Throwable throwable) {
            throwable.printStackTrace(); // helps with debugging
            throw new AssertionError(
                "No exception was expected to be thrown from '" + permittedGetter + "' but one was emitted;"
                    + " see stack trace below for details.", throwable
            );
        }
    }

    /**
     * Create an instance of a class that we can pass as a parameter in our calls to the getters
     * Currently only implemented for HstRequestContext
     */
    private static <R> R newInstanceForClass(Class<R> cls) {
        if (cls.equals(HstRequestContext.class)) {
            return (R) new MockHstRequestContext();
        } else {
            throw new RuntimeException("Don't know how to construct class " + cls);
        }
    }

    @DataProvider
    public static List<Method> allPublicGetters() {

        return stream(Dataset.class.getDeclaredMethods())
            .filter(method -> Modifier.isPublic(method.getModifiers()))
            .filter(method -> !Modifier.isStatic(method.getModifiers()))
            .filter(method -> method.getName().startsWith("get") || method.getName().startsWith("is"))
            .filter(method -> !method.getReturnType().equals(Void.class))
            .collect(toList());
    }

    @DataProvider
    public static List<Method> gettersForbiddenInUpcomingPublication() {

        final List<String> gettersPermittedWhenUpcoming = asList(
            "getTitle",
            "getSummary",
            "getNominalDate",
            "getParentPublication",
            "isPubliclyAccessible",
            "getSelfLinkBean",
            "getSeosummary"
        );

        return allPublicGetters().stream()
            .filter(method -> !gettersPermittedWhenUpcoming.contains(method.getName()))
            .collect(toList());
    }
}

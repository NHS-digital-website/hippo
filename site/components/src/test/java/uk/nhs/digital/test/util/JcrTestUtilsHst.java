package uk.nhs.digital.test.util;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static uk.nhs.digital.test.util.ExceptionTestUtils.wrapCheckedException;

import org.apache.sling.testing.mock.jcr.MockQueryResult;
import org.apache.sling.testing.mock.jcr.NhsdMockQueryManager;
import org.hippoecm.hst.configuration.hosting.Mount;
import org.hippoecm.hst.container.ModifiableRequestContextProvider;
import org.hippoecm.hst.content.beans.manager.ObjectConverterImpl;
import org.hippoecm.hst.content.beans.query.HstQueryManagerImpl;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.request.ResolvedMount;
import org.hippoecm.hst.mock.core.request.MockHstRequestContext;
import org.hippoecm.repository.util.DateTools;
import org.onehippo.repository.mock.MockNode;
import org.onehippo.repository.mock.MockNodeFactory;
import org.onehippo.repository.mock.MockSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * <p>
 * Enables mocking of JCR repository, seeded with YAML files as exported from admin console.
 * <p>
 * It differs from {@linkplain MockJcrRepoProvider} in that the latter requires YAML to be in
 * format that, while simpler, is less realistic and needs to be fully hand-crafted in the source.
 */
public class JcrTestUtilsHst {

    public static MockSession mockJcrRepo(
        final String repositoryYaml,
        final Map<String, Class<? extends HippoBean>> jcrPrimaryNodeTypeBeanPairs,
        final String expectedQuery,
        final String... expectedNodesAbsolutePaths
    ) throws IOException, RepositoryException {
        return mockJcrRepo(repositoryYaml, jcrPrimaryNodeTypeBeanPairs, null, expectedQuery, expectedNodesAbsolutePaths);
    }

    public static MockSession mockJcrRepo(
        final String repositoryYaml,
        final Map<String, Class<? extends HippoBean>> jcrPrimaryNodeTypeBeanPairs,
        final MockHstRequestContext hstRequestContext,
        final String expectedQuery,
        final String... expectedNodesAbsolutePaths
    ) throws IOException, RepositoryException {

        final NhsdMockQueryManager jcrQueryManager = new NhsdMockQueryManager();

        final MockNode root = MockNode.root(jcrQueryManager);

        MockNodeFactory.importYaml(repositoryYaml, root);

        final HstQueryManagerImpl hstQueryManager = new HstQueryManagerImpl(null,
            new ObjectConverterImpl(
                Optional.ofNullable(jcrPrimaryNodeTypeBeanPairs).orElse(emptyMap()),
                new String[0]
            ),
            DateTools.Resolution.MILLISECOND
        );

        final List<Node> expectedNodes = Optional.ofNullable(expectedNodesAbsolutePaths)
            .map(Arrays::stream).orElse(Stream.empty())
            .map(path -> wrapCheckedException(() -> root.getNode(path)))
            .collect(toList());

        jcrQueryManager.registerResultHandler(query -> {

            if (query.getStatement().equals(expectedQuery)) {
                return new MockQueryResult(expectedNodes);
            }

            return new MockQueryResult(emptyList());
        });

        final MockSession session = root.getSession();

        ReflectionTestUtils.setField(hstQueryManager, "session", session);

        final Mount mount = mock(Mount.class);
        final ResolvedMount resolvedMount = mock(ResolvedMount.class);

        given(resolvedMount.getMount()).willReturn(mount);
        given(mount.isPreview()).willReturn(false);

        MockHstRequestContext requestContext = hstRequestContext;
        if (requestContext == null) {
            requestContext = new MockHstRequestContext();
            ModifiableRequestContextProvider.set(requestContext);
        }

        requestContext.setDefaultHstQueryManager(hstQueryManager);
        requestContext.setResolvedMount(resolvedMount);
        requestContext.setSession(session);

        return session;
    }
}

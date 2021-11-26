package uk.nhs.digital;

import static java.text.MessageFormat.format;

import org.apache.jackrabbit.util.Text;
import org.hippoecm.repository.HippoStdNodeType;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

public class JcrQueryHelper {

    private static QueryResult executeSql2Query(Session session, String sql2QueryTemplate, Object... queryArgs) throws RepositoryException {
        String query = format(sql2QueryTemplate, queryArgs);

        return session
            .getWorkspace()
            .getQueryManager()
            .createQuery(query, Query.JCR_SQL2)
            .execute();
    }

    public static QueryResult findDescendantVariants(Node node, String decendantPrimaryType, String decendantVariant) throws RepositoryException {
        String queryTemplate = "SELECT * FROM [{0}] WHERE ISDESCENDANTNODE ([''{1}'']) AND [{2}] = ''{3}''";

        return executeSql2Query(node.getSession(), queryTemplate,
            decendantPrimaryType, node.getPath(), HippoStdNodeType.HIPPOSTD_STATE, Text.escape(decendantVariant));
    }

    public static QueryResult findDescendantNodes(Node node, String decendantPrimaryType) throws RepositoryException {
        String queryTemplate = "SELECT * FROM [{0}] WHERE ISDESCENDANTNODE ([''{1}''])";

        return executeSql2Query(node.getSession(), queryTemplate,
            decendantPrimaryType, node.getPath());
    }

    public static QueryResult executeJcrXpathQuery(final Session session, final String queryStatement) {
        try {
            return session
                .getWorkspace()
                .getQueryManager()
                .createQuery(queryStatement, Query.XPATH)
                .execute();

        } catch (final Exception e) {
            throw new RuntimeException(String.format("Failed to execute query %s.", queryStatement), e);
        }
    }
}

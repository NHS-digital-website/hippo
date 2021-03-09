package uk.nhs.digital.common.components.apicatalogue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static uk.nhs.digital.common.components.apicatalogue.Section.section;
import static uk.nhs.digital.common.components.apicatalogue.Subsection.subsection;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DepthFirstFiltersWalkerTest {

    private DepthFirstFiltersWalker filtersWalker;

    private Filters filters;

    private List<String> keysOfActualNodesVisited = new ArrayList<>();

    private FilterVisitor filterVisitor = new FilterVisitor() {

        @Override public void visit(final Section section) {
            keysOfActualNodesVisited.add(section.getKey());
        }

        @Override public void visit(final Subsection subsection) {
            keysOfActualNodesVisited.add(subsection.getKey());
        }
    };

    @Before
    public void setUp() throws Exception {
        keysOfActualNodesVisited.clear();

        filtersWalker = new DepthFirstFiltersWalker();

        filters = Filters.filters(
            filtersWalker,
            section("Section A",
                subsection("Tag A-A", "tag-a-a"),
                subsection("Tag A-B", "tag-a-b")
            ),
            section("Section B",
                subsection("Tag B-A", "tag-b-a"),
                subsection("Tag B-B", "tag-b-b")
            )
        );
    }

    @Test
    public void visitsEachNodeBeforeItsChildren() {

        // given
        // setUp()

        // when
        filtersWalker.walk(filters, filterVisitor, true);

        // then
        assertThat("Tags are recorded in the node-before-children order.",
            keysOfActualNodesVisited,
            contains(
                "section-a",
                "tag-a-a",
                "tag-a-b",
                "section-b",
                "tag-b-a",
                "tag-b-b"
            )
        );
    }

    @Test
    public void visitsEachNodeAfterItsChildren() {

        // given
        // setUp()

        // when
        filtersWalker.walk(filters, filterVisitor, false);

        // then
        assertThat("Tags are recorded in the children-before-parent order.",
            keysOfActualNodesVisited,
            contains(
                "tag-a-a",
                "tag-a-b",
                "section-a",
                "tag-b-a",
                "tag-b-b",
                "section-b"
            )
        );
    }
}
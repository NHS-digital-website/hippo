package uk.nhs.digital.common.components.catalogue.filters;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static uk.nhs.digital.common.components.catalogue.filters.FiltersTestUtils.section;
import static uk.nhs.digital.common.components.catalogue.filters.FiltersTestUtils.subsection;

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

        filters = FiltersTestUtils.filters(
            section("Section A",
                subsection("Tag A-A", "tag-a-a"),
                subsection("Tag A-B", "tag-a-b",
                    subsection("Tag A-B-A", "tag-a-b-a"),
                    subsection("Tag A-B-B", "tag-a-b-b")
                )
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
        filtersWalker.walkVisitingBeforeDescending(filters, filterVisitor);

        // then
        assertThat("Tags are recorded in the parent-before-children order.",
            keysOfActualNodesVisited,
            is(asList(
                "section-a",
                "tag-a-a",
                "tag-a-b",
                "tag-a-b-a",
                "tag-a-b-b",
                "section-b",
                "tag-b-a",
                "tag-b-b"
            ))
        );
    }

    @Test
    public void visitsEachNodeAfterItsChildren() {

        // given
        // setUp()

        // when
        filtersWalker.walkVisitingAfterDescending(filters, filterVisitor);

        // then
        assertThat("Tags are recorded in the children-before-parent order.",
            keysOfActualNodesVisited,
            is(asList(
                "tag-a-a",
                "tag-a-b-a",
                "tag-a-b-b",
                "tag-a-b",
                "section-a",
                "tag-b-a",
                "tag-b-b",
                "section-b"
            ))
        );
    }
}
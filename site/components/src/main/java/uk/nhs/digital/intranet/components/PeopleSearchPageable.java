package uk.nhs.digital.intranet.components;

import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.intranet.model.Person;

import java.util.ArrayList;
import java.util.List;

public class PeopleSearchPageable extends Pageable<Person> {

    private final List<Person> allItems;
    private final List<Person> items;
    private final List<List<Person>> pagedItems;

    private static final Logger log = LoggerFactory.getLogger(PeopleSearchPageable.class);

    PeopleSearchPageable() {
        allItems = new ArrayList<>();
        items = new ArrayList<>();
        pagedItems = new ArrayList<>();
    }

    PeopleSearchPageable(List<Person> people, int currentPage, int pageSize) {
        super(people.size(), currentPage, pageSize);

        this.allItems = people;

        this.items = getPageItems(currentPage);

        pagedItems = new ArrayList<>();
        for (int page = 1; this.getPageSize() >= page; page++) {
            pagedItems.add(getPageItems(page));
        }
    }

    private List<Person> getPageItems(int currentPage) {
        List<Person> items = new ArrayList<>();
        int fromIndex = (currentPage - 1) * this.getPageSize();
        if (fromIndex >= 0 && fromIndex <= this.allItems.size()) {
            int toIndex = currentPage * this.getPageSize();
            if (toIndex > this.allItems.size()) {
                toIndex = this.allItems.size();
            }

            try {
                items = this.allItems.subList(fromIndex, toIndex);
            } catch (IndexOutOfBoundsException e) {
                log.error("Sublist out of bounds: fromIndex=" + fromIndex + ", toIndex=" + toIndex + ", list size=" + items.size(), e);
            }
        }

        return items;
    }

    @Override
    public List<Person> getItems() {
        return items;
    }

    public List<Person> getAllItems() {
        return allItems;
    }

    public List<List<Person>> getPagedItems() {
        return pagedItems;
    }
}

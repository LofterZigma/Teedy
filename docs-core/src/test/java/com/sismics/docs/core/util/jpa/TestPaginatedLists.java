package com.sismics.docs.core.util.jpa;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test of paginated list utilities.
 *
 * @author Codex
 */
public class TestPaginatedLists {

    @Test
    public void createDefaultTest() {
        PaginatedList<Object> paginatedList = PaginatedLists.create(null, null);
        Assert.assertEquals(10, paginatedList.getLimit());
        Assert.assertEquals(0, paginatedList.getOffset());
    }

    @Test
    public void createMaxPageSizeTest() {
        PaginatedList<Object> paginatedList = PaginatedLists.create(200, 5);
        Assert.assertEquals(100, paginatedList.getLimit());
        Assert.assertEquals(5, paginatedList.getOffset());
    }

    @Test
    public void sortCriteriaTest() {
        SortCriteria defaultCriteria = new SortCriteria(null, null);
        Assert.assertEquals(0, defaultCriteria.getColumn());
        Assert.assertTrue(defaultCriteria.isAsc());

        SortCriteria explicitCriteria = new SortCriteria(3, false);
        Assert.assertEquals(3, explicitCriteria.getColumn());
        Assert.assertFalse(explicitCriteria.isAsc());
    }
}

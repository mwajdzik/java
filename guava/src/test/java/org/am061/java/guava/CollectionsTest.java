package org.am061.java.guava;

import com.google.common.collect.*;
import org.junit.Test;

import java.util.*;

import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CollectionsTest {

    @Test
    public void testIterables() {
        List<Integer> l1 = Lists.newArrayList(1, 2, 3);
        Set<String> s1 = Sets.newLinkedHashSet(Lists.newArrayList("a", "b", "c"));

        Iterable iterable = Iterables.concat(l1, s1);
        Iterator iterator = iterable.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());
        assertEquals(2, iterator.next());
        assertEquals(3, iterator.next());
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
    }

    @Test
    public void testMaps() {
        Map<String, Integer> salary = ImmutableMap.<String, Integer>builder()
                .put("John", 1000).put("Jane", 1500).put("Adam", 2000).put("Tom", 2000)
                .build();


        ImmutableSortedMap<String, Integer> sortedSalary = new ImmutableSortedMap
                .Builder<String, Integer>(Ordering.natural())
                .put("John", 900).put("Jane", 1500).put("Adam", 2000).put("Tom", 2300)
                .build();


        assertEquals(1500, salary.get("Jane").longValue());
        assertEquals("Adam", sortedSalary.firstEntry().getKey());
        assertEquals("Tom", sortedSalary.lastEntry().getKey());
    }

    @Test
    public void testMultiMaps() {
        Multimap<String, String> multimap = ArrayListMultimap.create();
        multimap.put("fruit", "apple");
        multimap.put("fruit", "banana");
        multimap.put("pet", "cat");
        multimap.put("pet", "dog");

        assertTrue(multimap.get("fruit").contains("apple") && multimap.get("fruit").contains("banana"));
        assertTrue(multimap.get("pet").contains("cat") && multimap.get("pet").contains("dog"));
    }

    @Test
    public void testTable() {
        Table<String, String, Integer> distance = HashBasedTable.create();
        distance.put("London", "Paris", 340);
        distance.put("New York", "Los Angeles", 3940);
        distance.put("London", "New York", 5576);

        assertEquals(3940, distance.get("New York", "Los Angeles").intValue());
        assertEquals(distance.columnKeySet(), newHashSet("Paris", "New York", "Los Angeles"));
        assertEquals(distance.rowKeySet(), newHashSet("London", "New York"));
    }
}

package org.am061.java.guava;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCollect {

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
}

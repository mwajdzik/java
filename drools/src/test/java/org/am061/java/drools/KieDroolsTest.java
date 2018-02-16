package org.am061.java.drools;

import com.google.common.base.Stopwatch;
import org.am061.java.drools.model.Product;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class KieDroolsTest {

    private static KieSession kSession;
    private static Stopwatch stopwatch = Stopwatch.createUnstarted();

    @BeforeClass
    public static void beforeClass() {
        stopwatch.start();

        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();

        // Get the session named kseesion-rule that we defined in kmodule.xml
        // by default the session returned is always stateful.
        kSession = kieContainer.newKieSession("ksession-rule");
    }

    @AfterClass
    public static void afterClass() {
        kSession.dispose();

        stopwatch.stop();
        System.out.println("Time elapsed: " + stopwatch);
    }

    @Test
    public void testDrools() {
        Product p1 = new Product("gold");
        Product p2 = new Product("silver");
        Product p3 = new Product("diamond");
        Product p4 = new Product("copper");
        Product p5 = new Product("platinum");

        FactHandle fact1 = kSession.insert(p1);
        FactHandle fact2 = kSession.insert(p2);
        FactHandle fact3 = kSession.insert(p3);
        FactHandle fact4 = kSession.insert(p4);
        FactHandle fact5 = kSession.insert(p5);

        assertNotNull(fact1);
        assertNotNull(fact2);
        assertNotNull(fact3);
        assertNotNull(fact4);
        assertNotNull(fact5);

        kSession.fireAllRules();

        assertEquals(p1.getDiscount(), 25);
        assertEquals(p2.getDiscount(), 50);
        assertEquals(p3.getDiscount(), 15);
        assertEquals(p4.getDiscount(), 45);
        assertEquals(p5.getDiscount(), 0);
    }
}

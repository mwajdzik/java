package org.am061.java.drools;

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

public class TestKieDrools {

    private static KieSession kSession;

    @BeforeClass
    public static void beforeClass() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();

        // Get the session named kseesion-rule that we defined in kmodule.xml
        // by default the session returned is always stateful.
        kSession = kContainer.newKieSession("ksession-rule");
    }

    @AfterClass
    public static void afterClass() {
        kSession.destroy();
    }

    @Test
    public void testDrools() {
        Product p1 = new Product("gold");
        Product p2 = new Product("silver");
        Product p3 = new Product("diamond");

        FactHandle fact1 = kSession.insert(p1);
        FactHandle fact2 = kSession.insert(p2);
        FactHandle fact3 = kSession.insert(p3);

        assertNotNull(fact1);
        assertNotNull(fact2);
        assertNotNull(fact3);

        kSession.fireAllRules();

        assertEquals(p1.getDiscount(), 25);
        assertEquals(p2.getDiscount(), 0);
        assertEquals(p3.getDiscount(), 15);
    }
}

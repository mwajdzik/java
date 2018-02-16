package org.am061.java.drools;

import com.google.common.base.Stopwatch;
import org.am061.java.drools.model.Product;
import org.drools.core.io.impl.ClassPathResource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class FileSystemDroolsTest {

    private static StatelessKieSession kSession;
    private static Stopwatch stopwatch = Stopwatch.createUnstarted();

    @BeforeClass
    public static void beforeClass() {
        stopwatch.start();

        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write("src/main/resources/normal_RULE.drl", new ClassPathResource("rules/rules.drl"));

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();

        KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        KieBase kieBase = kieContainer.getKieBase();

        kSession = kieBase.newStatelessKieSession();
    }

    @AfterClass
    public static void afterClass() {
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

        kSession.execute(Arrays.asList(p1, p2, p3, p4, p5));

        assertEquals(p1.getDiscount(), 25);
        assertEquals(p2.getDiscount(), 0);
        assertEquals(p3.getDiscount(), 15);
        assertEquals(p4.getDiscount(), 0);
        assertEquals(p5.getDiscount(), 0);
    }
}

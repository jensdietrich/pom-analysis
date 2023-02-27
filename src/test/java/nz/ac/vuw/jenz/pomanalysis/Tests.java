package nz.ac.vuw.jenz.pomanalysis;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Tests {


    private void assertMatch(Predicate<File> filter, String pathToPom) {
        File pom = new File(Tests.class.getResource(pathToPom).getFile());
        Assumptions.assumeTrue(pom.exists());
        assertTrue(filter.test(pom));
    }

    private void assertNoMatch(Predicate<File> filter, String pathToPom) {
        File pom = new File(Tests.class.getResource(pathToPom).getFile());
        Assumptions.assumeTrue(pom.exists());
        assertFalse(filter.test(pom));
    }

    @Test
    public void testPomWithShadePlugin () throws Exception {
        assertMatch(Filters.USES_SHADE_PLUGIN,"/alluxio_alluxio/underfs/oss/pom.xml");
        assertMatch(Filters.USES_SHADE_PLUGIN,"/spotify_docker-client/pom.xml");
    }

    @Test
    public void testPomWithShadePluginWithRelocations() throws Exception {
        assertNoMatch(Filters.USES_SHADE_PLUGIN_WITH_RELOCATIONS,"/alluxio_alluxio/underfs/oss/pom.xml");
        assertMatch(Filters.USES_SHADE_PLUGIN_WITH_RELOCATIONS,"/spotify_docker-client/pom.xml");
    }


}

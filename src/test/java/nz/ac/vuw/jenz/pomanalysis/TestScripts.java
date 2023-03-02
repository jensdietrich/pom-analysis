package nz.ac.vuw.jenz.pomanalysis;

import nz.ac.vuw.jenz.pomanalysis.scripts.GitHubApiScripts;
import nz.ac.vuw.jenz.pomanalysis.scripts.MavenCentral;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static nz.ac.vuw.jenz.pomanalysis.scripts.MavenCentral.search;

public class TestScripts {
    @Test
    public void testMavenCentralSearchFails () throws Exception {
        String groupId = "org.apache.logging.log4ju";
        String artifactId = "log4j-core";
        assertFalse(search(artifactId, groupId));
    }
    @Test
    public void testMavenCentralSearchSucceeds () throws Exception {
        String groupId = "org.apache.logging.log4j";
        String artifactId = "log4j-core";
        assertTrue(search(artifactId, groupId));
    }
    @Test
    public void testMavenCentralSearchByPackage () throws Exception {
        String name = "org.apache.logging.log4j";
        System.out.println(MavenCentral.searchByPackage(name));
    }
    @Test
    public void testGitHubSearch () throws Exception {
        String owner = "Netflix";
        GitHubApiScripts.searchForPoms(owner);
    }
    @Test
    public void testGitHubDownload () throws Exception {
        String owner = "Netflix";
        String repo = "spark";
        String path = "external/kinesis-asl-assembly/pom.xml";
        GitHubApiScripts.downloadPom(owner, repo, path, "output");
    }
}

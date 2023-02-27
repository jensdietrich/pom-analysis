package nz.ac.vuw.jenz.pomanalysis;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Analyse the set of poms.
 * @author jens dietrich
 */
public class ExtractPomsWithShadePlugin {

    public static void main(String[] args) throws Exception {

        File sourceFolder = new File("poms");
        File targetFolder = new File("poms-with-shade-plugins");

        List<File> poms = Files.walk(sourceFolder.toPath())
            .map(p -> p.toFile())
            .filter(f -> !f.isDirectory())
            .filter(f -> f.getName().equals("pom.xml"))
            .collect(Collectors.toList());

        System.out.println("" + poms.size() + " poms detected");

        List<File> pomsWithShadingPlugin = poms.stream()
            .filter(Filters.USES_SHADE_PLUGIN)
            .collect(Collectors.toList());

        int counter = 0;
        // copy
        for (File pom:pomsWithShadingPlugin) {
            System.out.println(pom.getAbsolutePath());
            String newPath = pom.getAbsolutePath().replace(sourceFolder.getAbsolutePath(),targetFolder.getAbsolutePath());
            File pom2 = new File(newPath);
            pom2.getParentFile().mkdirs();
            Files.copy(pom.toPath(), new File(newPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
            counter = counter + 1;
        }
        System.out.println("" + counter + " poms using the shade plugin detected");
    }


}

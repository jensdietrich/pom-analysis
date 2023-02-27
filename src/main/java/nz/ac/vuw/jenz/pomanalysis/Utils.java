package nz.ac.vuw.jenz.pomanalysis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Utils {

    static List<File> filterPoms(File folder, Predicate<File> filter) throws IOException {
        List<File> poms = Files.walk(folder.toPath())
            .map(p -> p.toFile())
            .filter(f -> !f.isDirectory())
            .filter(f -> f.getName().equals("pom.xml"))
            .collect(Collectors.toList());

        List<File> matchingPoms = poms.stream()
            .filter(filter)
            .collect(Collectors.toList());

        return matchingPoms;
    }

    static int countFilteredPoms(File folder, Predicate<File> filter) throws IOException {
        List<File> poms = Files.walk(folder.toPath())
            .map(p -> p.toFile())
            .filter(f -> !f.isDirectory())
            .filter(f -> f.getName().equals("pom.xml"))
            .collect(Collectors.toList());

        return (int)poms.stream()
            .filter(filter)
            .count();
    }

    static void filterPomsAndPrint(File folder, Predicate<File> filter) throws IOException {
        List<File> matchingPoms = filterPoms(folder,filter);
        for (File f : matchingPoms) {
            System.out.println(f.getAbsolutePath());
        }
    }
}

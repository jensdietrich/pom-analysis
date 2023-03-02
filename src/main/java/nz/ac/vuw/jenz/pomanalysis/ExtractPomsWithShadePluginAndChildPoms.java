package nz.ac.vuw.jenz.pomanalysis;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Analyse the set of poms.
 * @author jens dietrich
 */
public class ExtractPomsWithShadePluginAndChildPoms {

    public static void main(String[] args) throws Exception {

        if (args.length<2) throw new IllegalArgumentException("Two arguments required - the path to input and output folders");
        File sourceFolder = new File(args[0]);
        File targetFolder = new File(args[1]);

        List<File> poms = Files.walk(sourceFolder.toPath())
            .map(p -> p.toFile())
            .filter(f -> !f.isDirectory())
            .filter(f -> f.getName().equals("pom.xml"))
            .collect(Collectors.toList());

        System.out.println("" + poms.size() + " poms detected");

        List<File> pomsWithShadingPlugin = poms.stream()
            .filter(Filters.USES_SHADE_PLUGIN)
            .collect(Collectors.toList());

        HashMap<String,List<String>> reposWithShadedPoms = new HashMap<>();
        for(File f: pomsWithShadingPlugin) {
            String key = getUserRepo(f.toString());
            List<String> repoPoms = null;
            if (reposWithShadedPoms.containsKey(key)) {
                repoPoms = reposWithShadedPoms.get(key);
            } else {
                repoPoms = new ArrayList<String>();
                reposWithShadedPoms.put(key, repoPoms);
            }
            repoPoms.add(f.toString());
        }

        List<File> pomsWithParents = poms.stream()
                .filter(Filters.HAS_PARENT)
                .filter(p -> {
                    String pathStr = p.toString();
                    String repo = getUserRepo(pathStr);
                    if (!reposWithShadedPoms.keySet().contains(repo))
                        return false;

                    return true;
                })
                .collect(Collectors.toList());

        System.err.println(pomsWithParents.size());
        int counter = 0;

        System.out.println("" + counter + " poms using the shade plugin detected");
    }

    public static String getUserRepo(String pathStr) {
        String subStr = "";
        int i = pathStr.indexOf("/PomFiles");
        if (i == -1) {
            i = pathStr.indexOf("/ExtraPomFiles");
            if (i == -1)
               ;
            else
                subStr = pathStr.substring(i + 15);
        } else {
            subStr = pathStr.substring(i + 10);
        }
        return subStr.split("/")[0];
    }

    public static String getRepoPath(String pathStr) {
        String subStr = "";
        int i = pathStr.indexOf("/PomFiles");
        if (i == -1) {
            i = pathStr.indexOf("/ExtraPomFiles");
            if (i == -1)
                ;
            else
                subStr = pathStr.substring(i + 15);
        } else {
            subStr = pathStr.substring(i + 10);
        }
        return subStr;
    }

}

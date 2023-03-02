package nz.ac.vuw.jenz.pomanalysis;

import java.io.File;

/**
 * Analyse the set of poms.
 * @author jens dietrich
 */
public class AnalysePoms {

    public static void main(String[] args) throws Exception {

        if (args.length<1) throw new IllegalArgumentException("One argument required - the path to input folder");
        File sourceFolder = new File(args[0]);

        int totalPoms = Utils.countFilteredPoms(sourceFolder, f -> true);
        int validPoms = Utils.countFilteredPoms(sourceFolder, Filters.IS_VALID_XML);
        int usesStaging = Utils.countFilteredPoms(sourceFolder, Filters.USES_STAGING_PLUGIN);
        int hasModules = Utils.countFilteredPoms(sourceFolder, Filters.HAS_MODULES);
        int hasParents = Utils.countFilteredPoms(sourceFolder, Filters.HAS_PARENT);
        int usesShading = Utils.countFilteredPoms(sourceFolder, Filters.USES_SHADE_PLUGIN);

        System.out.println("analysing all poms: " + totalPoms );
        System.out.println("Valid poms: " + validPoms);
        System.out.println("poms with staging plugin: " + usesStaging);
        System.out.println("poms with modules: " + hasModules);
        System.out.println("poms with parents: " + hasParents);
        System.out.println("poms with shading plugin: " + usesShading);


    }


}

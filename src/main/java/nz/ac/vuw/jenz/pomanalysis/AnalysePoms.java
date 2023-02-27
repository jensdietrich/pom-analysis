package nz.ac.vuw.jenz.pomanalysis;

import java.io.File;

/**
 * Analyse the set of poms.
 * @author jens dietrich
 */
public class AnalysePoms {

    public static void main(String[] args) throws Exception {

        File sourceFolder = new File("poms-with-shade-plugins");

        System.out.println("analysing all poms using shade plugin: " + Utils.countFilteredPoms(sourceFolder, f -> true));
        System.out.println("poms with staging plugin: " + Utils.countFilteredPoms(sourceFolder, Filters.USES_STAGING_PLUGIN));
        System.out.println("poms with modules: " + Utils.countFilteredPoms(sourceFolder, Filters.HAS_MODULES));
        System.out.println("poms with parents: " + Utils.countFilteredPoms(sourceFolder, Filters.HAS_PARENT));

        Utils.filterPomsAndPrint(sourceFolder, Filters.USES_SHADE_PLUGIN);
    }


}

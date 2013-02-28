package edu.pitt.sis.cls.pbn;

public class Driver {
    private static final String SAMPLE_PBN = "sample.pbn";

    private static void usage() {
        System.out.println("usage: <program> [source-file [output-file]]");
        System.exit(1);
    }

    public static void main(String[] args) throws Exception {
        String sourceFilename = null;
        String outputFilename = null;

        if (args.length > 2)
            usage();
        if (args.length > 0)
            sourceFilename = args[0];
        if (args.length > 1)
            outputFilename = args[1];

        new Generator().run(sourceFilename, outputFilename);
    }
}

package edu.pitt.sis.cls.pbn;

public class Driver {
    private static void usage() {
        System.out.println("usage: <program> source-file [output-file]");
        System.exit(1);
    }

    public static void main(String[] args) throws Exception {
        String sourceFilename = null;
        String outputFilename = null;

        if (args.length == 0 || args.length > 2)
            usage();
        if (args.length > 0)
            sourceFilename = args[0].trim();
        if (args.length > 1)
            outputFilename = args[1].trim();

        if (outputFilename == null || outputFilename.length() == 0)
            outputFilename = getOutputFilename(sourceFilename);

        new Generator().run(sourceFilename, outputFilename);
    }

    private static String getOutputFilename(String sourceFilename) {
        String[] segments = sourceFilename.split("\\.");
        String outputExtension = "xdsl";
        String extension;
        String firstPart;

        if (segments.length == 1)
            firstPart = sourceFilename;
        else {
            extension = segments[segments.length - 1];
            firstPart = sourceFilename.substring(
                    0, sourceFilename.length() - (extension.length() + 1));
        }

        return String.format("%s.%s", firstPart, outputExtension);
    }
}

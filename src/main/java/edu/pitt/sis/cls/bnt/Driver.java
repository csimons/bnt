package edu.pitt.sis.cls.bnt;

import java.io.File;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

public class Driver {
	private final Logger LOG;

	public Driver() {
		LOG = Logger.getLogger(this.getClass().getCanonicalName());
	}

	private static void usage() {
		System.out.println(Constants.USAGE);
		System.exit(1);
	}

	public static void main(String[] args) throws Exception {
		(new Driver()).run(args);
	}

	public void run(String[] args) throws Exception {
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

		NodePool nodePool
			= (new Generator()).constructPoolFromFile(sourceFilename);

		String output = (new XDSLWriter()).format(nodePool);
		LOG.debug("XDSL:\n" + output);

		LOG.debug("Writing to file [" + outputFilename + "]");
		File outputFile = new File(outputFilename);
		PrintWriter pw = new PrintWriter(outputFile);
		pw.print(output);
		pw.close();
	}

	private static String getOutputFilename(String sourceFilename) {
		String[] segments = sourceFilename.split("\\.");
		String outputExtension = Constants.OUT_EXTENSION;
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

BNT: Bayesian Network Templates


BUILDING

1. Install the JDK (versions 5 and up should work).
   a. Ensure that the JAVA_HOME environment variable is set.
   b. Ensure that JAVA_HOME/bin is present in your PATH.

2. Install Apache Maven (version 3+ recommended).
   a. Ensure the M2_HOME environment variable is set.
   b. Ensure that M2_HOME/bin is present in your PATH.

3. From the project root directory, run "mvn clean install".  The
   build should succeed.


RUNNING

1. The general workflow is (a) to create a template file ("BNTX" format)
   and then (b) to run the compiled JAR, specifying the input file and
   optionally the output file.  A sample BNTX template is included with
   this distribution in the src/main/resources/ directory.  To test the
   program, compile the template to an XDSL file like so:

   $ java -jar target/bnt-0.1.jar src/main/resources/sample.bntx ./out.xdsl

   This will create a file "out.xdsl" in the working directory.  This file
   can be opened with GeNIe for examination.  If the optional second
   argument, the output filename, is not specified, the system will create
   a file with an analogous filename in the working directory.  The system
   WILL NOT check whether it is overwriting another file when writing output.


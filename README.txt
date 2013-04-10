BNT: Bayesian Network Templates


BUILDING

1. Install the JDK (versions 5 and up should work).
   a. Ensure that the JAVA_HOME environment variable is set.
   b. Ensure that JAVA_HOME/bin is present in your PATH.

2. Install Apache Maven (version 3+ recommended).
   a. Ensure the M2_HOME environment variable is set.
   b. Ensure that M2_HOME/bin is present in your PATH.

3. From the project root directory, run "mvn clean install".
   The build should succeed.


RUNNING

1. Create a BNT template file.  A sample file has been included:

      src/main/resources/sample.bntx

2. Run the compiled JAR, specifying the input file and optionally
   the output file.  A sample BNTX template is included with this
   distribution in the src/main/resources/ directory.  To test the
   program, compile the template to an XDSL file like so:

   $ java -jar target/bnt-0.1.jar src/main/resources/sample.bntx

   This will create a file "sample.xdsl" in the working directory.
   This file can be opened with GeNIe for examination.  If the
   optional second argument, the output filename, is not specified,
   the system will create a file with a filename analogous to the
   input-file filename but in the working directory.

   WARNING: The system WILL NOT check whether it is overwriting
   another file when writing output.  Deal with it.

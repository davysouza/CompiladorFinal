/* =========================================================================== *
    Federal University of São Carlos - UFSCar, campus Sorocaba/SP - Brazil

    Bachelor of Computer Science

    Course: Compilers
    Professor: Tiemi Christine Sakata, PhD

    Compiler - 3rd phase

    Instructions
    ----------

        The third phase of work is the implementation of a compiler that performs
    the lexical, syntactic and semantic analysis of grammar of the language, in
    addition to generating code in C language. The grammar used will be a simplified
    version of the Pascal programming language. Besides the above functions, this
    compiler includes rules for procedures and functions. Nested subroutines are
    not allowed.

    Args
    ----

        One or two arguments are expected for the compiler. The args[0] has the
    name of the input file (Pascal code). If exist, args[1] has the name of the
    output file (C code), otherwise the name of the input file will be the name
    of the output file.

* ============================================================================ *
    Student:

    Name: Davy A. de Souza, Undergraduate

* =========================================================================== */
package Compiler;

import AST.PW;
import AST.Program;
import java.io.*;

/**
 *
 * @author Davy
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        File file;
        FileReader stream;
        int numChRead;
        Program program;

        if (args.length != 1 && args.length != 2) {
            System.out.println("Usage:\n Main input [output]");
            System.out.println("input is the file to be compiled");
            System.out.println("output is the file where the generated code will be stored");
        } else {
            file = new File(args[0]);
            if (!file.exists() || !file.canRead()) {
                System.out.println("Either the file " + args[0] + " does not exist or it cannot be read");
                return;
            }
            try {
                stream = new FileReader(file);
            } catch (FileNotFoundException e) {
                System.out.println("Something wrong: file does not exist anymore");
                throw new RuntimeException();
            }

            // one more character for '\0' at the end that will be added by the compiler
            char[] input = new char[(int) file.length() + 1];

            try {
                numChRead = stream.read(input, 0, (int) file.length());
            } catch (IOException e) {
                System.out.println("Error reading file " + args[0]);
                return;
            }

            if (numChRead != file.length()) {
                System.out.println("Read error");
                return;
            }

            try {
                stream.close();
            } catch (IOException e) {
                System.out.println("Error in handling the file " + args[0]);
                return;
            }

            Compiler compiler = new Compiler();

            String outputFileName;
            if (args.length == 2) {
                outputFileName = args[1];
            } else {
                outputFileName = args[0];
                int lastIndex = outputFileName.lastIndexOf('.');
                if (lastIndex == -1) {
                    lastIndex = outputFileName.length();
                }
                StringBuilder sb = new StringBuilder(outputFileName.substring(0, lastIndex));
                sb.append(".c");
                outputFileName = sb.toString();
            }

            FileOutputStream outputStream;
            try {
                outputStream = new FileOutputStream(outputFileName);
            } catch (IOException e) {
                System.out.println("File " + args[1] + " was not found");
                return;
            }

            PrintWriter printWriter = new PrintWriter(outputStream);

            // the generated code goes to a file and so are the errors
            program = compiler.compile(input, new PrintWriter(System.out));

            if (program != null) {
                PW pw = new PW();
                pw.set(printWriter);
                program.genC(pw);
                if (printWriter.checkError()) {
                    System.out.println("There was an error in the output");
                }
            }
        }
    }
}

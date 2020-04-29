package com.company.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class JavaCompiler {

    private String data;

    public JavaCompiler() {

    }

    public String compileAndRun(String filename) throws IOException, InterruptedException {
        Path p = Paths.get(filename);
        String name = p.getFileName().toString();
        String path = p.getParent().toString();

        String [] compile = {"javac", filename};
        String [] run = {"java", "-cp", path, name};

        // Compile
        var process = Runtime.getRuntime().exec(compile);
        int fc = process.getErrorStream().read();

        // If we have a errors in our code!
        if( fc != -1 ) {
            // Do something if errors were found
            data = getOutput(process.getErrorStream());
            System.out.println(data);
        }
        // Wait javac to complete
        int value = process.waitFor();

        // If success, then compile
        if(value == 0) {
            var runProcess = Runtime.getRuntime().exec(run);
            int fcs = runProcess.getErrorStream().read();
            // If the app crashed
            if(fcs != -1) {
                // handle error stream
                data = getOutput(runProcess.getErrorStream());
                System.out.println(data);
            } else {
                // handle input stream
                //String data = getOutput(runProcess.getInputStream());
                data = getOutput(runProcess.getInputStream());
                System.out.println(data);
            }
        }
        return data;
    }

    // Transforms Stream into String
    public static String getOutput(InputStream stream) throws IOException {
        String result = "";
        try(BufferedReader bufferedReader
                    = new BufferedReader(new InputStreamReader(stream))) {
            result = bufferedReader.lines().collect(Collectors.joining("\n"));
        }
        return result;
    }

}

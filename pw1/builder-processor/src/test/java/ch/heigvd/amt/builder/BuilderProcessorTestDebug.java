package ch.heigvd.amt.builder;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class BuilderProcessorTestDebug {
    /**
     * Execute this test in debug mode and the annotation processor can be debbuged as well.
     * Source files writtent by the annotation processor to its filer (this.processingEnv.getFiler()) will be dumped to files
     * in ./target/generated-test-sources/ folder.
     */
    @Test
    public void processWithBuilder() {
        Compilation compilation =
                javac()
                        .withProcessors(new BuilderProcessor())
                        .compile(JavaFileObjects.forSourceString("ch.heigvd.amt.builder.Animal",
                                """
                                 package ch.heigvd.amt.builder; 
                                 @GenerateBuilder
                                 public class Animal {
                                    private String name;
                                 }
                                """));
        for (var x : compilation.generatedSourceFiles()) {
            String[] s = x.toUri().getPath().split("/");
            String l = s[s.length - 1];

            Path filePath = Paths.get("./target/generated-test-sources/" + l);
            try (Reader reader = x.openReader(true);
                 BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                reader.transferTo(writer);
                System.out.println("File written successfully to: " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        assertThat(compilation).succeeded();
    }
}

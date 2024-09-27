package ch.heigvd.amt.builder;

import com.squareup.javapoet.*;

import java.lang.reflect.Field;
import java.util.Map;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import java.util.Set;

/**
 * This class is an annotation processor that generates a builder class for each class annotated with @GenerateBuilder.
 */
@SupportedAnnotationTypes("ch.heigvd.amt.builder.GenerateBuilder")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class BuilderProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // TODO: implement this method to generate a builder class for each class annotated with @GenerateBuilder
        for (Element element : roundEnv.getElementsAnnotatedWith(GenerateBuilder.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                throw new IllegalStateException("Only classes can be annotated with @GenerateBuilder");
            }

            var typeElement = (TypeElement) element;
            var className = ClassName.get(typeElement);
            var builderClassName = ClassName.get(
                    className.packageName(),
                    "%sBuilder".formatted(className.simpleName())
            );

            var builder = TypeSpec
                    .classBuilder(builderClassName)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

            var buildMethodBuilder = MethodSpec.methodBuilder("build")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(className)
                    .addStatement("$1T result = new $1T()", className)
                    .addStatement("Class<?> clazz = result.getClass()");

            for (Element enclosedElement : typeElement.getEnclosedElements()) {
                if (enclosedElement.getKind() == ElementKind.FIELD) {
                    var fieldType = TypeName.get(enclosedElement.asType());
                    var fieldName = enclosedElement.getSimpleName().toString();

                    builder.addField(FieldSpec.builder(fieldType, fieldName, Modifier.PRIVATE).build());
                    builder.addMethod(
                            MethodSpec.methodBuilder("set%s".formatted(capitalize(fieldName)))
                                    .addModifiers(Modifier.PUBLIC)
                                    .returns(builderClassName)
                                    .addParameter(fieldType, fieldName)
                                    .addStatement("this.$1L = $1L", fieldName)
                                    .addStatement("return this")
                                    .build()
                    );

                    buildMethodBuilder.addCode(
                            CodeBlock.builder()
                                            .beginControlFlow("try")
                                            .addStatement("$2T $1LField = clazz.getDeclaredField($1S)", fieldName, Field.class)
                                            .addStatement("$1LField.setAccessible(true)", fieldName)
                                            .addStatement("$1LField.set(result, this.$1L)", fieldName)
                                            .nextControlFlow("catch (Exception e)")
                                            .addStatement("e.printStackTrace()")
                                            .endControlFlow()
                                            .build()
                    );
                }
            }

            builder.addMethod(
                    buildMethodBuilder
                            .addStatement("return result")
                            .build()
            );

            JavaFile javaFile = JavaFile.builder(builderClassName.packageName(), builder.build()).build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (Exception e) {
                throw new RuntimeException("Failed to write the builder class to file", e);
            }
        }
        return true;
    }

    private static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}

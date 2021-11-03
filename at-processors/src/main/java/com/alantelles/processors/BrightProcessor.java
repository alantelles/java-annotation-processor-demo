package com.alantelles.processors;

import com.alantelles.annotations.Bright;
import com.squareup.javapoet.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.*;
import java.util.Date;
import java.util.Set;

@SupportedAnnotationTypes({"com.alantelles.annotations.Bright"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class BrightProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (Element element : roundEnvironment.getElementsAnnotatedWith(Bright.class)) {

            String filePath = "config/implementations/hello.txt";

            try {
                BufferedReader reader = new BufferedReader(new FileReader(filePath));
                String line = reader.readLine();
                messager.printMessage(Diagnostic.Kind.NOTE, line);
                reader.close();
            } catch (IOException e) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Error: " + e.getMessage());
            }

            if (element.getKind() != ElementKind.CLASS) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Can only be applied to class.");
                return true;
            }

            FieldSpec name = FieldSpec.builder(String.class, "name")
                    .addModifiers(Modifier.PRIVATE).build();

            MethodSpec setName = MethodSpec.methodBuilder("setName")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID)
                    .addParameter(String.class, "name")
                    .addStatement("this.name = name")
                    .build();

            MethodSpec getName = MethodSpec.methodBuilder("getName")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(String.class)
                    .addStatement("return this.name")
                    .build();

            TypeSpec newClass = TypeSpec
                    .classBuilder("ExtraImpl")
                    .addModifiers(Modifier.PUBLIC)
                    .addField(name)
                    .addMethod(setName)
                    .addMethod(getName)
                    .build();

            try {
                JavaFile.builder("com.alantelles.app", newClass).build().writeTo(filer);
            } catch (IOException e) {
                System.out.println("IOException thrown!!!!");
            }


        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("last-used.txt"));
            writer.write(new Date().toString());
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }


}

package com.test.yin.compiler.activity;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.test.yin.compiler.activity.method.ConstantBuilder;
import com.test.yin.compiler.activity.method.InjectMethodBuilder;
import com.test.yin.compiler.activity.method.SaveStateMethodBuilder;
import com.test.yin.compiler.activity.method.StartMethodBuilder;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author leilongling@kungeek.com
 * @version 3.2
 * date 2019/7/6.
 */
public class ActivityClassBuilder {
    public static final String POSIX = "Builder";
    public static final String METHOD_NAME = "start";
    public static final String METHOD_NAME_NO_OPTIONAL = METHOD_NAME + "WithoutOptional";
    public static final String METHOD_NAME_FOR_OPTIONAL = METHOD_NAME + "WithOptional";
    public static final String METHOD_NAME_FOR_OPTIONALS = METHOD_NAME + "WithOptionals";
    public ActivityClass activityClass;

    public ActivityClassBuilder(ActivityClass activityClass) {
        this.activityClass = activityClass;
    }

    public void build(Filer filer, Types typeUtils, Elements elementUtils) {
        if (activityClass.isAbstract) return;
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(activityClass.simpleName + POSIX)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        new ConstantBuilder(activityClass).build(typeBuilder);

        new StartMethodBuilder(activityClass).build(typeBuilder, typeUtils, elementUtils);

        new SaveStateMethodBuilder(activityClass).build(typeBuilder, typeUtils, elementUtils);

        new InjectMethodBuilder(activityClass).build(typeBuilder, typeUtils, elementUtils);

        writeJavaToFile(filer, typeBuilder.build());
    }

    private void writeJavaToFile(Filer filer, TypeSpec typeSpec) {
        JavaFile file = JavaFile.builder(activityClass.packageName, typeSpec).build();
        try {
            file.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

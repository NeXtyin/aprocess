package com.test.yin.compiler.activity.method;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.test.yin.compiler.activity.ActivityClass;
import com.test.yin.compiler.activity.entity.Field;

import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author leilongling@kungeek.com
 * @version 3.2
 * date 2019/7/7.
 */
public class SaveStateMethodBuilder {
    ActivityClass activityClass;

    public SaveStateMethodBuilder(ActivityClass activityClass) {
        this.activityClass = activityClass;
    }

    public void build(TypeSpec.Builder typeBuilder, Types typeUtils, Elements elementUtils) {
        MethodSpec.Builder savaStateMethodBuilder = MethodSpec.methodBuilder("savaState")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(TypeName.VOID)
                .addParameter(TypeName.get(elementUtils.getTypeElement("android.app.Activity").asType()), "instance")
                .addParameter(TypeName.get(elementUtils.getTypeElement("android.os.Bundle").asType()), "outState")
                .beginControlFlow("if(instance instanceof $T)", activityClass.typeElement)
                .addStatement("$T typeInstance = ($T)instance", activityClass.typeElement, activityClass.typeElement)
                .addStatement("$T intent = new $T()",
                        TypeName.get(elementUtils.getTypeElement("android.content.Intent").asType()),
                        TypeName.get(elementUtils.getTypeElement("android.content.Intent").asType()))
                ;
        for (Field field : activityClass.fields) {
            String name = field.name;
            savaStateMethodBuilder.addStatement("intent.putExtra($S, typeInstance.$L)",
                    name, name);
        }

        savaStateMethodBuilder.addStatement("outState.putAll(intent.getExtras())").endControlFlow();
        typeBuilder.addMethod(savaStateMethodBuilder.build());
    }

}

package com.test.yin.compiler.activity.method;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.test.yin.compiler.activity.ActivityClass;
import com.test.yin.compiler.activity.entity.Field;
import com.test.yin.compiler.activity.entity.OptionalField;

import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author leilongling@kungeek.com
 * @version 3.2
 * date 2019/7/7.
 */
public class InjectMethodBuilder {
    ActivityClass activityClass;

    public InjectMethodBuilder(ActivityClass activityClass) {
        this.activityClass = activityClass;
    }

    public void build(TypeSpec.Builder typeBuilder, Types typeUtils, Elements elementUtils) {
        MethodSpec.Builder injectMethodBuilder = MethodSpec.methodBuilder("inject")
                .addParameter(TypeName.get(elementUtils.getTypeElement("android.app.Activity").asType()), "instance")
                .addParameter(TypeName.get(elementUtils.getTypeElement("android.os.Bundle").asType()), "savedInstanceState")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(TypeName.VOID)
                .beginControlFlow("if(instance instanceof $T)", activityClass.typeElement)
                .addStatement("$T typeInstance = ($T)instance", activityClass.typeElement, activityClass.typeElement)
                .addStatement("$T extras = savedInstanceState == null ? typeInstance.getIntent().getExtras() : savedInstanceState",
                        TypeName.get(elementUtils.getTypeElement("android.os.Bundle").asType()))
                .beginControlFlow("if(extras != null)");
        for (Field field : activityClass.fields) {
            String name = field.name;
            TypeName typeName = TypeName.get(field.symbol.type).box();
            TypeName bundleUtils = TypeName.get(elementUtils.getTypeElement("com.test.yin.runtime.utils.BundleUtils").asType());
            if (field instanceof OptionalField) {
                injectMethodBuilder.addStatement("$T $LValue = $T.<$T>get(extras, $S, $L)",
                        typeName, name, bundleUtils, typeName, name, ((OptionalField) field).defaultValue);
            } else {
                injectMethodBuilder.addStatement("$T $LValue = $T.<$T>get(extras, $S)",
                        typeName, name, bundleUtils, typeName, name);
            }
            injectMethodBuilder.addStatement("typeInstance.$L = $LValue", name, name);
        }
        injectMethodBuilder.endControlFlow().endControlFlow();
        typeBuilder.addMethod(injectMethodBuilder.build());

    }

}

package com.test.yin.compiler.activity.method;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.test.yin.compiler.activity.ActivityClass;
import com.test.yin.compiler.activity.entity.Field;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author leilongling@kungeek.com
 * @version 3.2
 * date 2019/7/6.
 */
public class StartMethod {
    ActivityClass activityClass;
    String name;

    private List<Field> fields = new ArrayList<>();
    private boolean isStaticMethod = true;

    public StartMethod(ActivityClass activityClass, String name) {
        this.activityClass = activityClass;
        this.name = name;
    }

    public StartMethod staticMethod(boolean isStaticMethod) {
        this.isStaticMethod = isStaticMethod;
        return this;
    }

    public void addAllField(List<Field> fields) {
        this.fields.addAll(fields);
    }

    public void addField(Field field) {
        this.fields.add(field);
    }

    public StartMethod copy(String name) {
        StartMethod startMethod = new StartMethod(activityClass, name);
        startMethod.fields.addAll(this.fields);
        return startMethod;
    }

    public void build(TypeSpec.Builder typeBuilder, Types typeUtils, Elements elementUtils) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(TypeName.get(elementUtils.getTypeElement("android.content.Context").asType()),
                        "context");
        methodBuilder.addStatement("$T intent = new $T(context, $T.class)",
                TypeName.get(elementUtils.getTypeElement("android.content.Intent").asType()),
                TypeName.get(elementUtils.getTypeElement("android.content.Intent").asType()),
                activityClass.typeElement);

        for (Field field : fields) {
            methodBuilder.addParameter(TypeName.get(field.symbol.type), field.name)
                    //intent.putExtra("age", age)
            .addStatement("intent.putExtra($S, $L)", field.name, field.name);
        }

        if (isStaticMethod) {
            methodBuilder.addModifiers(Modifier.STATIC);
        } else {
            methodBuilder.addStatement("fillIntent(intent)");
        }

        methodBuilder.addStatement("$T.INSTANCE.startActivity(context, intent)",
                TypeName.get(elementUtils.getTypeElement("com.test.yin.runtime.ActivityBuilder").asType()));
        typeBuilder.addMethod(methodBuilder.build());

    }

}

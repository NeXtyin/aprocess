package com.test.yin.compiler.activity.method;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.test.yin.compiler.activity.ActivityClass;
import com.test.yin.compiler.activity.ActivityClassBuilder;
import com.test.yin.compiler.activity.entity.Field;
import com.test.yin.compiler.activity.entity.OptionalField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static com.test.yin.compiler.activity.ActivityClassBuilder.METHOD_NAME;
import static com.test.yin.compiler.activity.ActivityClassBuilder.METHOD_NAME_FOR_OPTIONALS;
import static com.test.yin.compiler.activity.ActivityClassBuilder.METHOD_NAME_NO_OPTIONAL;

/**
 * @author leilongling@kungeek.com
 * @version 3.2
 * date 2019/7/6.
 */
public class StartMethodBuilder {
    public ActivityClass activityClass;

    public StartMethodBuilder(ActivityClass activityClass) {
        this.activityClass = activityClass;
    }

    public void build(TypeSpec.Builder typeBuilder, Types typeUtils, Elements elementUtils) {
        StartMethod startMethod = new StartMethod(activityClass, METHOD_NAME);
        Set<Field> fields = activityClass.fields;
//        List<Field> allFields = new ArrayList<>();
        List<Field> requiredFields = new ArrayList<>();
        List<Field> optionalFields = new ArrayList<>();
        for (Field field : fields) {
//            allFields.add(field);
            if (field instanceof OptionalField) {
                optionalFields.add(field);
            } else {
                requiredFields.add(field);
            }

        }

//        startMethod.addAllField(allFields);
        startMethod.addAllField(requiredFields);

        StartMethod startMethodNoOptional = startMethod.copy(METHOD_NAME_NO_OPTIONAL);

        startMethod.addAllField(optionalFields);

        startMethod.build(typeBuilder, typeUtils, elementUtils);

        if (!optionalFields.isEmpty()) {
            startMethodNoOptional.build(typeBuilder, typeUtils, elementUtils);
        }

        if (optionalFields.size() < 3) {
            for (Field field : optionalFields) {
                StartMethod copy = startMethodNoOptional.copy(ActivityClassBuilder.METHOD_NAME_FOR_OPTIONAL + field.name);
                copy.addField(field);
                copy.build(typeBuilder, typeUtils, elementUtils);
            }
        } else {
            String builderName = activityClass.simpleName + ActivityClassBuilder.POSIX;
            MethodSpec.Builder fillIntentBuilder = MethodSpec.methodBuilder("fillIntent")
                    .addModifiers(Modifier.PRIVATE)
                    .addParameter(TypeName.get(elementUtils.getTypeElement("android.content.Intent").asType()), "intent");
            ClassName builderClassName = ClassName.get(activityClass.packageName, builderName);
            for (Field field : optionalFields) {
                typeBuilder.addField(FieldSpec.builder(TypeName.get(field.symbol.type), field.name, Modifier.PRIVATE).build());
                typeBuilder.addMethod(MethodSpec.methodBuilder(field.name)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(TypeName.get(field.symbol.type), field.name)
                        .addStatement("this.$L = $L", field.name, field.name)
                        .addStatement("return this")
                        .returns(builderClassName)
                        .build());
                if (field.isPrimitive) {
                    fillIntentBuilder.addStatement("intent.putExtra($S, $L)", field.name, field.name);
                } else {
                    fillIntentBuilder.beginControlFlow("if($L != null)", field.name)
                            .addStatement("intent.putExtra($S, $L)", field.name, field.name)
                            .endControlFlow();
                }
            }
            typeBuilder.addMethod(fillIntentBuilder.build());
            startMethodNoOptional.copy(METHOD_NAME_FOR_OPTIONALS)
                    .staticMethod(false)
                    .build(typeBuilder, typeUtils, elementUtils);

        }

    }

}

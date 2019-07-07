package com.test.yin.compiler.activity.method;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;
import com.test.yin.compiler.activity.ActivityClass;
import com.test.yin.compiler.activity.entity.Field;

import java.util.Set;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * @author leilongling@kungeek.com
 * @version 3.2
 * date 2019/7/6.
 */
public class ConstantBuilder {
    public ActivityClass activityClass;

    public ConstantBuilder(ActivityClass activityClass) {
        this.activityClass = activityClass;
    }

    public void build(TypeSpec.Builder typeBuilder) {
        Set<Field> fields = activityClass.fields;
        for (Field field : fields) {
            typeBuilder.addField(FieldSpec.builder(String.class, field.prefix +  field.name.toUpperCase(), PUBLIC, STATIC, FINAL)
                    .initializer("$S", field.name)
                    .build());
        }
    }

}

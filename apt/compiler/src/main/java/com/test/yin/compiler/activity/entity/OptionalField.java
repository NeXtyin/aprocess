package com.test.yin.compiler.activity.entity;

import com.sun.tools.javac.code.Symbol;
import com.test.yin.annotations.Optional;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;

import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author leilongling@kungeek.com
 * @version 3.2
 * date 2019/6/13.
 */
public class OptionalField extends Field{

//    public Symbol symbol;

    public Object defaultValue = null;

//    Types typeUtils;
//    Elements elementUtils;

    {
        Optional optional = symbol.getAnnotation(Optional.class);
        if (symbol.type.getKind() == TypeKind.BOOLEAN) {
            defaultValue = optional.booleanValue();
        } else if (symbol.type.getKind() == TypeKind.BYTE
                ||symbol.type.getKind() == TypeKind.SHORT
                ||symbol.type.getKind() == TypeKind.INT
                ||symbol.type.getKind() == TypeKind.LONG
                ||symbol.type.getKind() == TypeKind.CHAR) {
            defaultValue = optional.intValue();
        } else if (symbol.type.getKind() == TypeKind.FLOAT
                || symbol.type.getKind() == TypeKind.DOUBLE) {
            defaultValue = optional.floatValue();
        } else {
            if (typeUtils.isSameType(symbol.type, elementUtils.getTypeElement("java.lang.String").asType())) {
                defaultValue = optional.stringValue();
            }
        }
    }

    public OptionalField(Symbol symbol, Types typeUtils, Elements elementUtils) {
        super(symbol, typeUtils, elementUtils);
//        this.symbol = symbol;
//        this.typeUtils = typeUtils;
//        this.elementUtils = elementUtils;
    }



    @Override
    public int compareTo(@NotNull Field field) {
        if (field instanceof Field) {
            return super.compareTo(field);
        }
        return 1;
    }
}

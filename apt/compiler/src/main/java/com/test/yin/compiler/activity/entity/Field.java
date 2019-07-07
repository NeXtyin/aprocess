package com.test.yin.compiler.activity.entity;

import com.sun.tools.javac.code.Symbol;

import org.jetbrains.annotations.NotNull;

import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author leilongling@kungeek.com
 * @version 3.2
 * date 2019/6/13.
 */
public class Field implements Comparable<Field> {

    public String prefix = "PARAMETER_";
    public Symbol symbol;

    public String name;
    public boolean isPrivate;
    public boolean isPrimitive;

    Types typeUtils;
    Elements elementUtils;



    public Field(Symbol symbol, Types typeUtils, Elements elementUtils) {
        this.symbol = symbol;
        name = symbol.getQualifiedName().toString();
        isPrivate = symbol.isPrivate();
        isPrimitive = symbol.type.isPrimitive();
        this.typeUtils = typeUtils;
        this.elementUtils = elementUtils;
    }

    @Override
    public int compareTo(@NotNull Field field) {
        return name.compareTo(field.name);
    }

    @Override
    public String toString() {
        return symbol.type + " " + name;
    }

}

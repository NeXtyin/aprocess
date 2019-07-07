package com.test.yin.compiler.activity;

import com.test.yin.compiler.activity.entity.Field;

import java.util.Set;
import java.util.TreeSet;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

/**
 * @author leilongling@kungeek.com
 * @version 3.2
 * date 2019/6/13.
 */
public class ActivityClass {
    public TypeElement typeElement;
    public String simpleName;
    public String packageName;
    public Set<Field> fields = new TreeSet<>();

    public boolean isAbstract;

    public ActivityClassBuilder builder = new ActivityClassBuilder(this);

    public ActivityClass(TypeElement element) {
        typeElement = element;
        simpleName = typeElement.getSimpleName().toString();
        packageName = ((PackageElement)typeElement.getEnclosingElement()).asType().toString();
        isAbstract = element.getModifiers().contains(Modifier.ABSTRACT);
    }

    @Override
    public String toString() {
        return packageName + "." +  simpleName + " and Fields == "  + fields.toString();
    }
}

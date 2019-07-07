package com.test.yin.compiler;

import com.sun.tools.javac.code.Symbol;
import com.test.yin.annotations.Builder;
import com.test.yin.annotations.Optional;
import com.test.yin.annotations.Required;
import com.test.yin.compiler.activity.ActivityClass;
import com.test.yin.compiler.activity.entity.Field;
import com.test.yin.compiler.activity.entity.OptionalField;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * @author leilongling@kungeek.com
 * @version 3.2
 * date 2019/6/12.
 */
public class BuilderProcessor extends AbstractProcessor {


    private Messager mMessager;
    private Types mTypeUtils;
    private Elements mElementUtils;
    private Filer mFiler;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(Builder.class.getCanonicalName());
        set.add(Required.class.getCanonicalName());
        set.add(Optional.class.getCanonicalName());
//        mMessager.printMessage(Diagnostic.Kind.WARNING, "haha getCanonicalName = " + Builder.class.getCanonicalName());
//        mMessager.printMessage(Diagnostic.Kind.WARNING, "haha getCanonicalName = " + Required.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
//        AptContext.init(processingEnv);
        mMessager = processingEnvironment.getMessager();
        mTypeUtils = processingEnvironment.getTypeUtils();
        mElementUtils = processingEnvironment.getElementUtils();
        mFiler = processingEnvironment.getFiler();
//        String string = mElementUtils.getTypeElement("java.lang.String").asType().toString();
//        mMessager.printMessage(Diagnostic.Kind.WARNING, "elementUtils-->typemirror == " + string);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Map<Element, ActivityClass> activityClassMap = new HashMap<>();

        Set<? extends Element> builderElements = roundEnvironment.getElementsAnnotatedWith(Builder.class);
        for (Element element : builderElements) {
            if (element.getKind() == ElementKind.CLASS) {
                try {
//                    mMessager.printMessage(Diagnostic.Kind.WARNING, "haha class name = " + element.getSimpleName());
//                    mMessager.printMessage(Diagnostic.Kind.WARNING, "haha package name = " + element.getEnclosingElement().asType().toString());
//                    mMessager.printMessage(Diagnostic.Kind.WARNING, "haha package SimpleName = " + element.getEnclosingElement().getSimpleName());
                    if (mTypeUtils.isSubtype(element.asType(), mElementUtils.getTypeElement("android.app.Activity").asType())) {
                        mMessager.printMessage(Diagnostic.Kind.WARNING, "activityClassMap add --> " + element);
                        activityClassMap.put(element, new ActivityClass((TypeElement) element));
                    }
                } catch (Exception e) {
                    mMessager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                }
            }
        }

        Set<? extends Element> requiredElements = roundEnvironment.getElementsAnnotatedWith(Required.class);
        for (Element element : requiredElements) {
            if (element.getKind() == ElementKind.FIELD) {
                try {
                    ActivityClass activityClass = activityClassMap.get(element.getEnclosingElement());
                    if (activityClass != null) {
                        mMessager.printMessage(Diagnostic.Kind.WARNING, element.getEnclosingElement() + " $activityClass add Field(Field) -> " + element);
                        activityClass.fields.add(new Field((Symbol.VarSymbol) element, mTypeUtils, mElementUtils));
                    }
                } catch (Exception e) {

                }
            }
        }

        Set<? extends Element> optionalElements = roundEnvironment.getElementsAnnotatedWith(Optional.class);
        for (Element element : optionalElements) {
            if (element.getKind() == ElementKind.FIELD) {
                try {
                    ActivityClass activityClass = activityClassMap.get(element.getEnclosingElement());
                    if (activityClass != null) {
                        mMessager.printMessage(Diagnostic.Kind.WARNING, element.getEnclosingElement() + "activityClass add Field(OptionalField) -> " + element);
                        activityClass.fields.add(new OptionalField((Symbol.VarSymbol) element, mTypeUtils, mElementUtils));
                    }
                } catch (Exception e) {

                }
            }
        }

        for (ActivityClass activityClass : activityClassMap.values()) {
            mMessager.printMessage(Diagnostic.Kind.WARNING, activityClass.toString());
            activityClass.builder.build(mFiler, mTypeUtils, mElementUtils);
        }

        return true;
    }
}

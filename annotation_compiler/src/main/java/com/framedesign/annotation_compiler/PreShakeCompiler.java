package com.framedesign.annotation_compiler;

import com.framedesign.annotation.PreShake;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.swing.text.View;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * 抖动注解处理器
 *
 * @author liyong
 * @date 2019-10-23
 */
@AutoService(Processor.class)//注解处理器
public class PreShakeCompiler extends AbstractProcessor {

    private Filer mFiler;

    /**
     * 初始化的第一个方法
     *
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        System.out.print("PreShakeCompiler init");
        mFiler = processingEnvironment.getFiler();
        Logger.getGlobal().info("PreShakeCompiler init");
    }

    /**
     * 返回支持的注解类型
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(PreShake.class.getCanonicalName());
        return types;
    }

    /**
     * 声明我们的注解处理器支持的java版本
     *
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(PreShake.class);
        Map<String, String> map = new HashMap<>();
        for (Element element : elementsAnnotatedWith) {
            TypeElement typeElement = (TypeElement) element;
            PreShake preShake = typeElement.getAnnotation(PreShake.class);
            String viewName = typeElement.getQualifiedName().toString();
            System.out.print("PreShakeCompiler process=" + viewName);
            Logger.getGlobal().info("PreShakeCompiler process=" + viewName);


            try {
              Class<?>  aclass = Class.forName(viewName);

              if (aclass.newInstance() instanceof View){
                View view = (View) aclass.newInstance();

              }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
//            try {
//                JavaFileObject source = mFiler.createSourceFile(viewName);
//                Writer writer = source.openWriter();
//                writer.write("fsajdfkajlfdsfdsjlsfadjlkfkljfsdalk");
//                writer.flush();
//                writer.close();
//            } catch (IOException e) {
//
//            }

        }


        return true;
    }
}

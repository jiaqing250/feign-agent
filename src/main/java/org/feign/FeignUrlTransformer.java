package org.feign;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.Map;

/**
 * @author 2022-08-25 zfz
 */
public class FeignUrlTransformer implements ClassFileTransformer {


    @Override
    public byte[] transform(ClassLoader loader, String classPath, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) {

        CtClass ctClass = null;
        try {
            if (null == classPath || classPath.trim().length() == 0) {
                return classfileBuffer;
            }

            // 将classPath转成className形式
            String className = classPath.replaceAll("/", ".");
            if (!"org.springframework.cloud.openfeign.FeignClientsRegistrar".equals(className)) {
                // 如果不是目标要增强的类则结束
                return classfileBuffer;
            }

            // 获取class加载的池子,从中取出我们要去增强的类
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));

            // 目标类: PingController
            ctClass = classPool.get(className);

            // 目标方法: ping
            CtMethod ctMethod = ctClass.getDeclaredMethod("getUrl", new CtClass[]{classPool.get(Map.class.getName())});
            ctMethod.setBody("{ String serviceNameUrl = resolve(this.environment.getProperty(\"feign.url.\"+$1.get(\"name\")));" +
                    " String url = resolve(this.environment.getProperty(\"feign.url\"));" +
                    " if (serviceNameUrl != null && !serviceNameUrl.isEmpty()) {" +
                    "     return getUrl(serviceNameUrl);" +
                    " } else {" +
                    "     return getUrl(url);" +
                    " }" +
                    "}");
            ctClass.writeFile();
            ctClass.toClass();

            return ctClass.toBytecode();
        } catch (Exception e) {
            // 暂时不做相关异常处理
            e.printStackTrace();
        } finally {
            if (null != ctClass) {
                ctClass.detach();
            }
        }
        return classfileBuffer;
    }
}

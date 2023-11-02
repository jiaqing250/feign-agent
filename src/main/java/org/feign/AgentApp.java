package org.feign;

import java.lang.instrument.Instrumentation;

/**
 * @author 2022-08-25 zfz
 */
public class AgentApp {
    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("agent feign start.");
        instrumentation.addTransformer(new FeignUrlTransformer());
    }
}

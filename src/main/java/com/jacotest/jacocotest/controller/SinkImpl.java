package com.jacotest.jacocotest.controller;

import org.benf.cfr.reader.api.OutputSinkFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// 自定义的输出 Sink 实现
import org.benf.cfr.reader.api.OutputSinkFactory;
import org.benf.cfr.reader.api.SinkReturns;
import org.benf.cfr.reader.api.SinkReturns.Decompiled;
import org.benf.cfr.reader.util.output.SinkDumperFactory;

public class SinkImpl implements OutputSinkFactory {


    @Override
    public List<SinkClass> getSupportedSinks(SinkType sinkType, Collection<SinkClass> collection) {
        // 返回支持的 Sink 类型
        List<SinkClass> supportedSinks = new ArrayList<>();
        System.out.println("sinkType = " + sinkType.name());
        System.out.println("sinkType = " + sinkType.name());

        supportedSinks.add(SinkClass.DECOMPILED);
        // 如果需要支持其他类型的 Sink，在这里进行相应的处理
        return supportedSinks;
    }

    @Override
    public <T> Sink<T> getSink(SinkType sinkType, SinkClass sinkClass) {
        // 根据 Sink 类型返回相应的 Sink 实例
        if (sinkClass == SinkClass.DECOMPILED) {
            // 返回一个新的 DecompiledSink 实例
            return new DecompiledSink<>();
        }
        // 如果需要支持其他类型的 Sink，在这里进行相应的处理
        return null;
    }

    // 自定义的 Decompiled 类型的输出 Sink
    private static class DecompiledSink<SinkDumperFactory> implements Sink<SinkDumperFactory> {
        @Override
        public void write(SinkDumperFactory t) {
            // 在这里处理反编译结果的输出
            System.out.println("Class : " + t.getClass());
            System.out.println(t.toString());

        }


    }
}



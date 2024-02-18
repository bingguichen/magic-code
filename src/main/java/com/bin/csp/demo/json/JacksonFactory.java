package com.bin.csp.demo.json;

/**
 * Jackson实现工厂
 *
 * @author BinChan
 */
public class JacksonFactory implements JsonFactory {
    private static final JacksonFactory FACTORY = new JacksonFactory();

    public static JacksonFactory inst() {
        return FACTORY;
    }

    @Override
    public AbstractJson getJson() {
        return new Jackson();
    }
}

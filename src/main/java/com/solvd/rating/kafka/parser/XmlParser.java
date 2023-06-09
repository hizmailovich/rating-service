package com.solvd.rating.kafka.parser;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class XmlParser {

    @SneakyThrows
    public static String getValue(String node) {
        XML xml = new XMLDocument(Objects.requireNonNull(
                XmlParser.class
                        .getClassLoader()
                        .getResource("consumer.xml")));
        return xml.nodes("//" + node)
                .get(0)
                .xpath("text()")
                .get(0);
    }

}

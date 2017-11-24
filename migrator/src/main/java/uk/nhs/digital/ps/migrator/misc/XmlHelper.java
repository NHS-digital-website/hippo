package uk.nhs.digital.ps.migrator.misc;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

public class XmlHelper {


    private static final Logger log = LoggerFactory.getLogger(XmlHelper.class);

    public static <T> T loadFromXml(final Path path, final Class<T> returnType) {

        log.debug("Loading XML as {} from {}", returnType, path);

        SAXBuilder saxBuilder = new SAXBuilder();
        Document document;
        try {
            document = saxBuilder.build(path.toFile());
        } catch (JDOMException | IOException e) {
            throw new RuntimeException("Failed to parse " + path, e);
        }

        Constructor<T> declaredConstructor;
        try {
            declaredConstructor = returnType.getDeclaredConstructor(Element.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Failed to find constructor taking single argument of type " + Element.class
                .getName() + " in " + returnType.getName());
        }

        T object;
        try {
            object = declaredConstructor.newInstance(document.getRootElement());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to instantiate new " + returnType.getName(), e);
        }

        return object;
    }

}

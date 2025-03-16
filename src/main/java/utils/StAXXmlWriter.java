package utils;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class StAXXmlWriter {
    private static final String DIRECTORY = "src/main/resources/xml_statistics"; // Save files in this folder

    public void writeStatistics(Map<String, AtomicInteger> statistics, String attribute) {
        ensureDirectoryExists(); // Ensure the 'xml_statistics' directory exists
        String fileName = DIRECTORY + File.separator + "statistics_by_" + attribute + ".xml";

        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            XMLStreamWriter writer = createXmlWriter(fileOutputStream);
            try {
                writeXmlHeader(writer);
                writeStatisticsData(writer, statistics);
                closeXmlDocument(writer);
                System.out.println("✅Statistics saved to " + fileName);
            } finally {
                writer.close();
            }
        } catch (Exception e) {
            System.err.println("Error writing XML file: " + fileName);
            e.printStackTrace();
        }
    }

    private void ensureDirectoryExists() {
        File directory = new File(DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private XMLStreamWriter createXmlWriter(FileOutputStream fileOutputStream) throws XMLStreamException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        return factory.createXMLStreamWriter(fileOutputStream, "UTF-8");
    }

    private void writeXmlHeader(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeCharacters("\n");
        writer.writeStartElement("statistics");
        writer.writeCharacters("\n");
    }

    private void writeStatisticsData(XMLStreamWriter writer, Map<String, AtomicInteger> statistics) {
        statistics.entrySet().stream()
                .sorted(Comparator.comparingInt((Map.Entry<String, AtomicInteger> e) -> e.getValue().get()).reversed())
                .forEach(entry -> writeStatisticsEntry(writer, entry.getKey(), entry.getValue().get()));
    }

    private void writeStatisticsEntry(XMLStreamWriter writer, String value, int count) {
        try {
            writer.writeCharacters("    ");
            writer.writeStartElement("item");

            writer.writeCharacters("\n        ");
            writeElement(writer, "value", value);

            writer.writeCharacters("\n        ");
            writeElement(writer, "count", String.valueOf(count));

            writer.writeCharacters("\n    ");
            writer.writeEndElement();
            writer.writeCharacters("\n");

        } catch (XMLStreamException e) {
            System.err.println("Error writing entry: " + value);
            e.printStackTrace();
        }
    }

    private void writeElement(XMLStreamWriter writer, String elementName, String value) throws XMLStreamException {
        writer.writeStartElement(elementName);
        writer.writeCharacters(value);
        writer.writeEndElement();
    }

    private void closeXmlDocument(XMLStreamWriter writer) throws XMLStreamException  {
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeEndDocument();
    }
}


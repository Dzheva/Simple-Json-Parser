package org.example;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@XmlRootElement(name = "statistics")
public class XmlStatisticsWriter {
    private Map<String, Long> attributeCounts;

    public Map<String, Long> getAttributeCounts() {
        return attributeCounts;
    }

    public void setAttributeCounts(Map<String, Long> statistics) {
        this.attributeCounts = sortMapByValueDESC(statistics);
    }

    public static Map<String, Long> sortMapByValueDESC(Map<String, Long> map) {
        return map.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}

package service;

import utils.StAXXmlWriter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class StatisticsService {
    private final ConcurrentHashMap<String, AtomicInteger> statistics = new ConcurrentHashMap<>();
    private final StAXXmlWriter stAXXmlWriter;

    public StatisticsService(StAXXmlWriter stAXXmlWriter) {
        this.stAXXmlWriter = stAXXmlWriter;
    }

    public void increment(String key) {
        statistics.computeIfAbsent(key, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public void saveToXml(String attribute) {
        stAXXmlWriter.writeStatistics(statistics, attribute);
    }
}


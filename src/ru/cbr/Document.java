package ru.cbr;

import java.util.Date;

public class Document {
    protected Long printDurationInSecond;
    protected String type;
    protected String paperSize;
    protected Date printTime;

    public Document(long printDurationInSecond, String type, String paperSize) {
        this.printDurationInSecond = printDurationInSecond;
        this.type = type;
        this.paperSize = paperSize;
    }

    public void setPrintTime(Date printTime) {
        this.printTime = printTime;
    }

    @Override
    public String toString() {
        return "Document{" +
                "printDurationInSecond=" + printDurationInSecond +
                ", type='" + type + '\'' +
                ", paperSize='" + paperSize + '\'' +
                ", printTime=" + printTime +
                '}';
    }
}

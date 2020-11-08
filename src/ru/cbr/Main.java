package ru.cbr;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Manager m = new Manager();
        List<Document> docs = Arrays.asList(
                new Document(2, "WORD", "a4"),
                new Document(1, "txt", "a3"),
                new Document(2, "docx", "a2"),
                new Document(1, "temp", "a1"),
            new Document(1, "pdf", "a5"),
            new Document(1, "text", "a9")
        );
        for(Document doc : docs) {
            m.add(doc);
        }
        m.cancelPrinting(docs.get(1));
        Thread.sleep(4 * 1000);
        System.out.println("Не обработанные:");
        for(Document doc : m.stop()) {
            System.out.println("Не обработан " + doc);
        }

        System.out.println("Среднее время " + m.getAvgDurationInSecond());

        System.out.println("Напечатанные:");
        for(Document doc : m.getPrintedDocs(Manager.Order.BY_PRINT_TIME)) {
            System.out.println("Напечатан " + doc);
        }
        Thread.sleep(4 * 1000);
        System.out.println("Напечатанные:");
        for(Document doc : m.getPrintedDocs(Manager.Order.BY_PRINT_TIME)) {
            System.out.println("Напечатан " + doc);
        }
    }
}

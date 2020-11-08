package ru.cbr;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

public class Manager implements Runnable {
    private final ConcurrentLinkedQueue<Document> unprintedDocks;
    private final ConcurrentLinkedQueue<Document> printedDocks;
    private AtomicReference<Status> status;

    public Manager() {
        unprintedDocks = new ConcurrentLinkedQueue<>();
        printedDocks = new ConcurrentLinkedQueue<>();
        status = new AtomicReference<>(Status.RUNNING);
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * Остановка диспетчера. Печать документов в очереди отменяется.
     * На выходе должен быть список ненапечатанных документов.
     */
    public List<Document> stop() {
        status.set(Status.TERMINATED);
        ArrayList<Document> unprinted = new ArrayList<>(unprintedDocks);
        unprintedDocks.clear();
        return unprinted;
    }

    /**
     * Принять документ на печать. Метод не должен блокировать выполнение программы.
     * @param doc
     */
    public void add(Document doc) {
        unprintedDocks.add(doc);
    }

    /**
     * Отменить печать принятого документа, если он еще не был напечатан.
     */
    public void cancelPrinting(Document doc) {
        unprintedDocks.remove(doc);
    }

    /**
     * Получить отсортированный список напечатанных документов.
     * Список может быть отсортирован на выбор:
     * по порядку печати, по типу документов, по продолжительности печати, по размеру бумаги.
     */
    public Document[] getPrintedDocs(Order order) {
        Document[] docs = printedDocks.toArray(new Document[0]);
        if(order == Order.BY_DURATION) {
            Arrays.sort(docs, new Comparator<Document>() {
                public int compare(Document o1, Document o2) {
                    return o1.printDurationInSecond.compareTo(o2.printDurationInSecond);
                }
            });
        }
        if(order == Order.BY_PAPER) {
            Arrays.sort(docs, new Comparator<Document>() {
                public int compare(Document o1, Document o2) {
                    return o1.paperSize.compareTo(o2.paperSize);
                }
            });
        }
        if(order == Order.BY_TYPE) {
            Arrays.sort(docs, new Comparator<Document>() {
                public int compare(Document o1, Document o2) {
                    return o1.type.compareTo(o2.type);
                }
            });
        }
        if(order == Order.BY_PRINT_TIME) {
            Arrays.sort(docs, new Comparator<Document>() {
                public int compare(Document o1, Document o2) {
                    return o1.printTime.compareTo(o2.printTime);
                }
            });
        }
        return docs;
    }

    /**
     * Рассчитать среднюю продолжительность печати напечатанных документов
     */
    public double getAvgDurationInSecond() {
        Document[] docs = printedDocks.toArray(new Document[0]);
        if (docs.length == 0)
            return 0;
        double totalDuration = 0;
        for (Document d : docs) {
            totalDuration += d.printDurationInSecond;
        }
        return totalDuration / docs.length;
    }

    @Override
    public void run() {
        while (status.get() == Status.RUNNING) {
            Document d = unprintedDocks.poll();
            if(d == null)
                continue;
            try {
                Thread.sleep(d.printDurationInSecond * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            d.setPrintTime(new Date());
            System.out.println("Printed" + d);
            printedDocks.add(d);
        }
    }
    public enum Order {
        BY_PRINT_TIME, BY_TYPE, BY_DURATION, BY_PAPER
    }
    enum Status {
        TERMINATED, RUNNING
    }
}

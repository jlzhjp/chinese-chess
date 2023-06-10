package edu.henu.chineseChess.common.socketManager.sinks;

import edu.henu.chineseChess.common.socketManager.Sink;

import java.io.PrintWriter;

public class PrintWriterSink implements Sink {
    private final PrintWriter printWriter;

    public PrintWriterSink(PrintWriter writer) {
        this.printWriter = writer;
    }

    @Override
    public boolean isClosed() {
        return !printWriter.checkError();
    }

    @Override
    public void println(String message) {
        printWriter.println(message);
        printWriter.flush();
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }
}

package io.github.jlzhjp.chess.common.socketManager;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class LineBuffer implements Iterator<String> {
    private final Queue<String> lines = new LinkedList<>();

    private final StringBuilder lineBuilder = new StringBuilder();

    public void put(ByteBuffer byteBuffer, int len) {
        byte[] buffer = new byte[len];
        byteBuffer.get(buffer);
        String result = new String(buffer);
        for (char c : result.toCharArray()) {
            if (c == '\n') {
                lines.add(lineBuilder.toString());
                lineBuilder.setLength(0);
            } else {
                lineBuilder.append(c);
            }
        }
    }

    public void clear() {
        lineBuilder.setLength(0);
    }

    @Override
    public boolean hasNext() {
        return lines.peek() != null;
    }

    @Override
    public String next() {
        return lines.poll();
    }
}

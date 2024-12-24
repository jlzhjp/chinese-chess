package io.github.jlzhjp.chess.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Singletons {
    public static final ExecutorService executorService = Executors.newCachedThreadPool();
    public static boolean isDarkMode = false;
}

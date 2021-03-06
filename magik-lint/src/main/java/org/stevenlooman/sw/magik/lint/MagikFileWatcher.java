package org.stevenlooman.sw.magik.lint;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MagikFileWatcher {

  abstract static class ChangesListener {
    abstract void onChanged(Collection<Path> paths);
  }

  private Path dir;
  private ChangesListener listener;
  private Map<WatchKey, Path> watchKeys;
  private WatchService watchService;

  MagikFileWatcher(Path dir, ChangesListener listener) throws IOException {
    this.dir = dir;
    this.listener = listener;
    this.watchKeys = new HashMap<>();
    this.watchService = dir.getFileSystem().newWatchService();
  }

  void run() throws IOException, InterruptedException {
    // register all directories
    for (Path path : MagikFileWatcher.scanDirectories(dir)) {
      WatchKey key = path.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
      watchKeys.put(key, path);
    }

    // start watchting
    for (;;) {
      WatchKey key = watchService.take();

      for (WatchEvent<?> event : key.pollEvents()) {
        WatchEvent.Kind<?> kind = event.kind();

        if (kind == OVERFLOW) {
          continue;
        }

        @SuppressWarnings("unchecked")
        WatchEvent<Path> ev = (WatchEvent<Path>)event;
        Path pathBase = watchKeys.get(key);
        Path pathRelative = ev.context();
        Path path = pathBase.resolve(pathRelative);
        File file = path.toFile();

        if (!file.exists()) {
          continue;
        } else if (file.isDirectory() && kind == ENTRY_CREATE) {
          for (Path dir : MagikFileWatcher.scanDirectories(path)) {
            WatchKey newKey = dir.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            watchKeys.put(newKey, path);
          }

          Collection<Path> paths = MagikFileScanner.scanMagikFiles(path);
          listener.onChanged(paths);
        } else if (file.getName().toLowerCase().endsWith(".magik")) {
          List<Path> paths = Arrays.asList(path);
          listener.onChanged(paths);
        }
      }

      boolean valid = key.reset();
      if (!valid) {
        watchKeys.remove(key);
      }
    }
  }

  static Collection<Path> scanDirectories(Path start) throws IOException {
    List<Path> dirs = new ArrayList<>();

    Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attr) {
        if (!path.endsWith(".")
            && path.toFile().isHidden()) {
          return FileVisitResult.SKIP_SUBTREE;
        }

        dirs.add(path);
        return FileVisitResult.CONTINUE;
      }
    });

    return dirs;
  }

}
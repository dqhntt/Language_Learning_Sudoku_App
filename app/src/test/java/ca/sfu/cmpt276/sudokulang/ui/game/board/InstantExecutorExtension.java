package ca.sfu.cmpt276.sudokulang.ui.game.board;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.arch.core.executor.TaskExecutor;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

// Cite: https://github.com/mitchtabian/Unit-Testing-2/blob/master/app/src/test/java/com/codingwithmitch/unittesting2/util/InstantExecutorExtension.java
public class InstantExecutorExtension implements AfterEachCallback, BeforeEachCallback {
    @Override
    public void afterEach(ExtensionContext context) {
        ArchTaskExecutor.getInstance().setDelegate(null);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        ArchTaskExecutor.getInstance().setDelegate(new TaskExecutor() {
            @Override
            public void executeOnDiskIO(@NonNull Runnable runnable) {
                runnable.run();
            }

            @Override
            public void postToMainThread(@NonNull Runnable runnable) {
                runnable.run();
            }

            @Override
            public boolean isMainThread() {
                return true;
            }
        });
    }
}

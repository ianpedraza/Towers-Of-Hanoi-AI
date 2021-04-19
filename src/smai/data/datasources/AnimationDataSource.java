package smai.data.datasources;

import smai.common.utils.AnimationListener;
import smai.data.renders.AnimationPanel;
import smai.domain.Answer;

public abstract class AnimationDataSource implements Runnable {

    private Thread thread;
    protected Answer answer;
    protected AnimationPanel canvas;
    protected AnimationListener callback;

    protected AnimationDataSource(AnimationListener callback) {
        this.callback = callback;
        this.thread = new Thread(this);
    }

    protected abstract void start();

    protected abstract void render();

    public abstract void pause();

    public abstract void resume();

    protected abstract void stop();

    public final void play() {
        if (this.answer == null || this.canvas == null) {
            System.out.println("Answer or canvas undefined");
            return;
        }

        this.resume();
    }

    public final void play(Answer answer, AnimationPanel canvas) {
        this.answer = answer;
        this.canvas = canvas;

        try {
            if (thread.isAlive()) {
                kill();
            }

            this.thread = new Thread(this);
            thread.start();
        } catch (Exception e) {
            this.callback.onAnimationError(e);
        }

    }

    private void kill() throws InterruptedException {
        this.stop();
        thread.join();
    }

    @Override
    public final void run() {
        this.start();
        this.render();
        this.callback.onAnimationComplete();
    }

}

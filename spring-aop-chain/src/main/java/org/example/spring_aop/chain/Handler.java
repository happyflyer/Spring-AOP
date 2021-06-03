package org.example.spring_aop.chain;

/**
 * @author lifei
 */
public abstract class Handler {
    private Handler successorHandler;

    public Handler getSuccessorHandler() {
        return successorHandler;
    }

    public void setSuccessorHandler(Handler successorHandler) {
        this.successorHandler = successorHandler;
    }

    public void execute() {
        this.handleProcess();
        if (successorHandler != null) {
            this.successorHandler.execute();
        }
    }

    /**
     * handleProcess
     */
    protected abstract void handleProcess();
}

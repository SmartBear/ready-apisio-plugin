package com.smartbear.apisio.ui;

import com.eviware.soapui.support.action.swing.DefaultActionList;
import com.eviware.x.form.XFormDialog;
import com.eviware.x.form.support.ADialogBuilder;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

public abstract class WizardDialogBase implements AutoCloseable {
    protected final XFormDialog dialog;
    private final AbstractAction nextAction;
    private final AbstractAction prevAction;
    private final AbstractAction finishAction;
    private final AbstractAction cancelAction;
    private boolean dialogResult = false;
    private int pageIndex = 0;
    private final int pageCount;

    public WizardDialogBase(Class<? extends Object> formClass, int pageCount) {
        DefaultActionList actions = new DefaultActionList();
        this.prevAction = new AbstractAction("Prev") {
            @Override
            public void actionPerformed(ActionEvent e) { prevActionHandler(e); }
        };
        this.nextAction = new AbstractAction("Next") {
            @Override
            public void actionPerformed(ActionEvent e) { nextActionHandler(e); }
        };
        this.finishAction = new AbstractAction("Finish") {
            @Override
            public void actionPerformed(ActionEvent e) { finishActionHandler(e); }
        };
        this.cancelAction = new AbstractAction("Cancel") {
            @Override
            public void actionPerformed(ActionEvent e) { cancelActionHandler(e); }
        };
        actions.addAction(prevAction);
        actions.addAction(nextAction);
        actions.addAction(finishAction);
        actions.addAction(cancelAction);

        this.pageCount = pageCount;
        this.dialog = ADialogBuilder.buildDialog(formClass, actions, false);

        updateActionsState();
    }

    protected boolean showDialog() {
        dialog.show();
        return dialogResult;
    }

    @Override
    public void close() {
        dialog.release();
    }

    public abstract void updatePageContent(int pageIndex);

    public abstract void readPageContent(int pageIndex);

    public abstract boolean validate(int pageIndex);

    private void prevActionHandler(ActionEvent e) {
        if (pageIndex > 0) {
            readPageContent(pageIndex);
            pageIndex -= 1;
            updateActionsState();
            updatePageContent(pageIndex);
        }
    }

    private void nextActionHandler(ActionEvent e) {
        if (pageIndex < (pageCount - 1) && validate(pageIndex)) {
            readPageContent(pageIndex);
            pageIndex += 1;
            updateActionsState();
            updatePageContent(pageIndex);
        }
    }

    private void updateActionsState() {
        prevAction.setEnabled(pageIndex > 0);
        nextAction.setEnabled(pageIndex < (pageCount - 1));
        finishAction.setEnabled(pageIndex == (pageCount - 1));
    }

    private void finishActionHandler(ActionEvent e) {
        if (validate(pageIndex)) {
            readPageContent(pageIndex);
            dialogResult = true;
            dialog.setVisible(false);
        }
    }

    private void cancelActionHandler(ActionEvent e) {
        dialogResult = false;
        dialog.setVisible(false);
    }
}

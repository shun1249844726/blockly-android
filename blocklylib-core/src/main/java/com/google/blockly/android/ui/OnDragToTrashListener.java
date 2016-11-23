/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.blockly.android.ui;

import android.view.DragEvent;
import android.view.View;

import com.google.blockly.android.clipboard.BlockClipDataHelper;
import com.google.blockly.android.control.BlocklyController;

public class OnDragToTrashListener implements View.OnDragListener {
    protected final BlocklyController mController;
    protected final BlockClipDataHelper mClipHelper;

    public OnDragToTrashListener(BlocklyController controller) {
        mController = controller;
        mClipHelper = controller.getClipDataHelper();
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        boolean trashable = isTrashableBlock(event);
        if (trashable && event.getAction() == DragEvent.ACTION_DROP) {
            // Above isTrashableBlock(..) should guarantee against nulls.
            mController.trashRootBlock(mClipHelper.getPendingDrag(event).getRootDraggedBlock());
            // TODO(#): Sound Effect.
        }
        return trashable;
    }

    /**
     * Check whether the drag contains a block dragged out of the a WorkspaceView within the current
     * Activity.
     *
     * @param event The DragEvent to check.
     * @return True if it is a trashable block. Otherwise false.
     */
    protected boolean isTrashableBlock(DragEvent event) {
        // Ignore ClipDescription, immediately look for PendingDrag.
        // ClipDescription may be null if the user happens to drag over the system bar.
        PendingDrag drag = mClipHelper.getPendingDrag(event);
        return drag != null && drag.getDragInitiator() instanceof WorkspaceView;
    }
}
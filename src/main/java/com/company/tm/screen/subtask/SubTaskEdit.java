package com.company.tm.screen.subtask;

import io.jmix.ui.screen.*;
import com.company.tm.entity.SubTask;

@UiController("tm_SubTask.edit")
@UiDescriptor("sub-task-edit.xml")
@EditedEntityContainer("subTaskDc")
public class SubTaskEdit extends StandardEditor<SubTask> {
}
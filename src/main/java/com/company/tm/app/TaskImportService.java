package com.company.tm.app;

import com.company.tm.entity.Project;
import com.company.tm.entity.Task;
import com.company.tm.entity.TaskDTO;
import io.jmix.core.DataManager;
import io.jmix.core.EntitySet;
import io.jmix.core.SaveContext;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component("tm_TaskImportService")
public class TaskImportService {
    private static final Logger log = LoggerFactory.getLogger(TaskImportService.class);

    @Autowired
    protected DataManager dataManager;


    public int getImportTasks() {
        List<TaskDTO> taskDTOList = obtainExternalTaskNames();
        Project defaultProject = getDefaultProject();

        List<Task> taskList = taskDTOList.stream()
                .map(taskDTO -> {
                    Task task = dataManager.create(Task.class);
                    task.setName(taskDTO.getName());
                    task.setProject(defaultProject);

                    return task;
                })
                .collect(Collectors.toList());

        EntitySet entitySet = dataManager.save(new SaveContext().saving(taskList));
        int size = entitySet.size();
        log.info("{} tasks imported", size);
        return size;
    }


    private List<TaskDTO> obtainExternalTaskNames() {
        return Stream.iterate(0, i -> i).limit(5)
                .map(taskDto -> {
                    TaskDTO taskDTO = dataManager.create(TaskDTO.class);
                    taskDTO.setName("Task " + RandomStringUtils.randomAlphabetic(5));

                    return taskDTO;
                })
                .collect(Collectors.toList());
    }


    private Project getDefaultProject() {
        Optional<Project> entity = dataManager.load(Project.class)
                .query("select p from tm_Project p where p.defaultProject = :defaultProject1")
                .parameter("defaultProject1", true)
                .optional();

        return entity.orElse(null);
    }
}
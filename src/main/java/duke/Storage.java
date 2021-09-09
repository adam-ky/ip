package duke;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import duke.DukeException.MissingDescriptionException;
import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.TaskList;
import duke.task.Todo;


/**
 * Deals with loading tasks from the file and saving tasks in the file
 *
 * @author Adam Ho
 */
public class Storage {
    private static final String BAR = " | ";
    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Writes the list of tasks to a text file.
     * @throws DukeException The exception is thrown if an error occurred while writing to the text file.
     */
    public void saveTasks(TaskList tasks) throws DukeException {
        try {
            FileWriter fw = new FileWriter(filePath);
            ArrayList<Task> taskList = tasks.getTaskList();
            for (Task task : taskList) {
                String taskDetails = writeTaskDetails(task);
                fw.write(taskDetails);
            }
            fw.close();
        } catch (IOException e) {
            throw new DukeException("Unable to save your tasks");
        }

    }

    private String writeTaskDetails(Task task) {
        String taskDetails;
        String done = task.checkStatus() ? "1" : "0";
        if (task instanceof Todo) {
            taskDetails = "T" + BAR + done + BAR + task.getDescription() + "\n";
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            taskDetails = "D" + BAR + done + BAR + deadline.getDescription()
                    + BAR + deadline.getBy() + "\n";
        } else {
            Event event = (Event) task;
            taskDetails = "E" + BAR + done + BAR + event.getDescription()
                    + BAR + event.getAt() + "\n";
        }
        return taskDetails;
    }

    /**
     * Reads the list of tasks from a text file.
     * @throws DukeException The exception is thrown if the text file does not exist in the directory.
     */
    public ArrayList<Task> loadTasks() throws DukeException {
        try {
            ArrayList<Task> taskList = new ArrayList<>();
            File f = new File(filePath);
            Scanner s = new Scanner(f);
            while (s.hasNext()) {
                Task t = readTaskDetails(s);
                taskList.add(t);
            }
            return taskList;
        } catch (FileNotFoundException e) {
            throw new DukeException("Oops couldn't load your file :(");
        }
    }

    private Task readTaskDetails(Scanner s) throws MissingDescriptionException {
        String[] taskFormat = s.nextLine().split(" \\| ");
        Task t;
        if (taskFormat[0].equals("T")) {
            t = new Todo(taskFormat[2]);
        } else if (taskFormat[0].equals("D")) {
            t = new Deadline(taskFormat[2], taskFormat[3]);
        } else {
            t = new Event(taskFormat[2], taskFormat[3]);
        }
        t.setDone(taskFormat[1].equals("1"));
        return t;
    }

    /**
     * Clears the list of tasks from a text file
     * @throws DukeException The exception is thrown if an error occurred while writing to the text file.
     */
    public void clearTasks() throws DukeException {
        try {
            new FileWriter(filePath, false).close();
        } catch (IOException e) {
            throw new DukeException("Unable to clear tasks Perhaps something went wrong with your data file?");
        }
    }
}

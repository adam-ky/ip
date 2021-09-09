package duke;

import duke.command.Command;
import duke.task.TaskList;
import javafx.scene.image.Image;

/**
 * Represents a chatbot to manage a list of tasks.
 *
 * @author Adam Ho
 */
public class Duke {
    private static final String FILEPATH = "./data/duke.txt";
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    private Image user = new Image(this.getClass().getResourceAsStream("/images/cat.jpg"));
    private Image duke = new Image(this.getClass().getResourceAsStream("/images/samoyed.png"));

    public Duke() {
        this(FILEPATH);
    }

    /**
     * Creates a Duke chatbot object with the specified
     * text file to store existing task data.
     *
     * @param filepath The path of the file's location.
     */
    public Duke(String filepath) {
        ui = new Ui();
        storage = new Storage(filepath);
        try {
            tasks = new TaskList(storage.loadTasks());
        } catch (DukeException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Runs the Duke chatbot object and accepts input from users to
     * manage a to-do task list. The chatbot stops running when the
     * user inputs the exit command.
     */
    public void run() {
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (DukeException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Returns a response to user input.
     *
     * @param input The input from the user.
     */
    public String getResponse(String input) {
        try {
            Command c = Parser.parse(input);
            String response = c.execute(tasks, ui, storage);

            // Ensure that there is a response to return
            assert(response != null);
            assert (!response.equals(""));

            return response;
        } catch (DukeException e) {
            return ui.showError(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Duke(FILEPATH).run();
    }
}

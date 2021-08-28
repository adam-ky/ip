package duke;

import java.io.IOException;
abstract class Command {

    abstract public void execute(TaskList tasks, Ui ui, Storage storage) throws IOException;

    public boolean isExit() {
        return false;
    };
}

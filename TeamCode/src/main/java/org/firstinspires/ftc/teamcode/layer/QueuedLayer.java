package org.firstinspires.teamcode.layer;

import java.util.Iterator;
import java.util.List;
import org.firstinspires.teamcode.task.Task; // (Remove this comment) doesn't exist yet
// (Remove this comment) we don't have to import the other stuff because it's in the same package

// (Remove this comment) please write implementations for these methods and a javadoc for the class.

/**
 * Javadoc goes here. For help writing javadocs: https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html#:~:text=Writing%20Doc%20Comments-,Format%20of%20a%20Doc%20Comment,-A%20doc%20comment
 */
public abstract class QueuedLayer implements Layer {
  /**
   * Also describe fields, even private ones, with javadocs.
   */
  private Iterator<Task> subtaskIter;

  public QueuedLayer(LayerSetupInfo info) {
    subtaskIter = null;
  }

  public boolean isTaskDone() {
    // (Remove) check if there's nothing left in subtaskIter, or if subtaskIter is null
  }

  public void update() {
    // (Remove) return the next subtask
  }

  protected void setSubtasks(List<Task> subtasks) {
    // (Remove) make an iterator out of the 'subtasks' list and assign it to subtaskIter
  }
}

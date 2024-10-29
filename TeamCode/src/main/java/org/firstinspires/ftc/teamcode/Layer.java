from task import UnsupportedTaskError

public interface Layer{

  boolean isTaskDone();
 
  void update();

  void acceptTask(Task task);

} 
   


from task import UnsupportedTaskError

    public class InputGenerator implements Layer
    {
        public boolean isTaskDone()
         {
            return false;
         }
         
         public void update() 
         {
            //Unimplemented, - Cheering for you!!!!
         }

         public void acceptTask(Task task)
         {
            throw new UnsupportedTaskError(this, task);
         }
                  
    }


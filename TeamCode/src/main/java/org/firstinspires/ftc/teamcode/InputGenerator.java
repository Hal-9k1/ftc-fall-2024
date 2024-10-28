from task import UnsupportedTaskError

    public InputGenerator implements Layer
    {
        public isTaskDone()
         {
          false;
         }
         
         public void acceptTask(task);
         {
         throw new UnsupportedTaskError(task)
         }
                  
    }


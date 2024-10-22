/** 
 * Exception raised by layers when 
 * accepting a task type they don't 
 * support. */
public class UnsupportedTaskError extends IllegalArgumentException 
{
    public UnsupportedTaskError(Layer layer, Task task)
    {
        super(String.format("Layer '%s' does not support task of type '%s'.", layer.getClass().getName(), task.getClass().getName()));
    }
}
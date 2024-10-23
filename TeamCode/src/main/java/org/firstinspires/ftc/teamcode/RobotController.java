package main.java.org.firstinspires.ftc.teamcode;

import java.util.List;
import java.util.ArrayList;
import layer.LayerSetUpInfo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.function.Consumer;
// import task interface and what not
// Some of the imports above may have different names after translation.

class RobotController{

    LayerSetUpInfo layerSetupInfo;
    List<Consumer<Boolean>> updateListeners;
    List<Object<? extends Layer>> layerObjects; // Added layerObjects as a field

    // Swap robot with what the variable is actually named.
    RobotController(HardwareMap robot){
        this.layerSetupInfo = new LayerSetUpInfo(robot);
        this.updateListeners = new ArrayList<>();
        this.layerObjects = new ArrayList<>();
    }

    void setup(List<Class<? extends Layer>> capCObjects){
         for(Class<? extends Layer> items: capCObjects){
            this.layerObjects.add(items.getConstructor(LayerSetUpInfo.class).newInstance(LayerSetUpInfo));
         }
    }

    boolean update(){
            // Call all update listeners
        for(Consumer<Boolean> listener: updateListeners){
            listener(false);
        }
            // Do work on layers
            int i = 0;
            // Possible way to implement sentinel? could be wrong, and I just realized that task is actually a marker interface now.
            Object<? extends task> currentTask = null; 
            for(Object<? extends Layer> layer: this.layerObjects){
                i++;
                if(!layer.isTaskDone()){
                    task = layer.update();
                    break;
                }
            if(currentTask == null){
                // No tasks left in any layer
                for(Consumer<Boolean> listener: updateListeners){
                 listener(true);   
                }
                this.updateListeners.clear();
                return true;
            }
            for(int j=i-1; j>-1; j--){
                this.layerObjects[j].acceptTask(currentTask);
                currentTask = this.layerObject[j].update();
            return false;
            }
            }
    }

    void add_update_listener(Consumer<Boolean> listener){
        updateListeners.add(listener);
    }
}
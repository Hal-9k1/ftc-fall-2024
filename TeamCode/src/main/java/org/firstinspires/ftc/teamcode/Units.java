import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;

public class Units
{
    private static double pi = Math.PI;
    public static void main(String[] args)
    {
        Dictionary<String, Double> unitsPerM = new Hashtable<>();
        unitsPerM.put("mm", 1000);
        unitsPerM.put("cm", 100);
        unitsPerM.put("in", (100 / 2.54));
        unitsPerM.put("ft", (100/2.54/12));
        unitsPerM.put("m", 1);

        Dictionary<String, Double> unitsPerRad = new Hashtable<>();
        unitsPerM.put("deg", (180/pi));
        unitsPerM.put("rev", (1/2/pi));
        unitsPerM.put("rad", 1);

        Set<String> validUnits = new HashSet<>();
        validUnits.addAll(unitsPerM.keys());
        validUnits.addAll(unitsPerRad.keys());

        List<String> conversionTable = new Hashset<>();
        conversionTable.addAll(unitsPerM);
        conversionTable.addAll(unitsPerRad);

    }

    public static double convert(double value, String fromUnit, String toUnit)
    {
        if (!validUnits.contains(fromUnit))
        {
            throw new IllegalArgumentException("Invalid fromUnit " + fromUnit);
        }
        else if (!validUnits.contains(toUnit))
        {
            throw new IllegalArgumentException("Invalid toUnit " + toUnit);
        }
        for (Dictionary<String, Double> table : conversionTable; conversionTable.contains(table))
        {
            if (fromUnit.contains(table) && toUnit.contains(table))
            {
                return (value * table.get(fromUnit) / table.get(toUnit));
            }
        }
    }
}
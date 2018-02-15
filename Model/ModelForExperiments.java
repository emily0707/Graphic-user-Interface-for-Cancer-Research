package Model;


import Util.ErrorMsg;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// to hold probe data on the homepage for each bead plates 
// pass and change data between homepage and add bead pages
/**
 *
 * @author feiping
 */
public class ModelForExperiments {
    private ObservableList<bead> analytes = FXCollections.observableArrayList();
    //initial probes list
   private ObservableList<probeTableData> probesToLoad = FXCollections.observableArrayList();
    // private final static Context instance = new Context();
    private final static ModelForExperiments instance = new ModelForExperiments();
    //probe data contains both bead class number(region number ) and analyte name.
    private HashMap<Integer, HashMap<Integer,  ObservableList<bead>>> data = new HashMap<>(); 
    
    //proble data contains only analyte name and counts. created to pupoluate into the homepage proble tables
    private HashMap<Integer, HashMap<Integer, ObservableList<probeTableData>>> probesListForPopulate = new HashMap<>();
    
    //user input data for each experiment 
     HashMap<Integer, List<UserInputForBeadPlate>> userInputsForBeadPlateMap= new HashMap<>();
    
    private int currentExperiment;
    private int currentPlate;
    private int numberOfExpriments; // total number of experiments
    private List<String> fileNames = new ArrayList<>(); // xml files upload by users
    private Map<Integer, List<String>> XMLFileMap = new HashMap<>(); // xml files for each experiemnt, experiment nubmer is key.
    private String  directory; //absolute  directory of xml files. 
    private ObservableList<Integer> experiments; // for choice box display # of experiments 
    
    public static ModelForExperiments getInstance() {
        return instance;
    }

    public void setDirectory(String  directory)
    {
        this.directory = directory;
    }
    
    public String  getDirectory()
    {
        return directory;
    }
    
    public void setAnalytes(ObservableList<bead> analytes)
    {
        this.analytes = analytes;
    }
    
    public ObservableList<bead>  getAnalytes()
    {
        return analytes;
    }
    
    public int getCurrentPlate()
    {
        return currentPlate;
    }
    
    public void setCurrentPlate(int plate)
    {
        currentPlate = plate;
    }
   public void setProbesForLoad(ObservableList<probeTableData> probesToLoad )
   {
       this.probesToLoad = probesToLoad;
   }
    public ObservableList<probeTableData>  getProbesForLoad( )
   {
       generateBeadsToLoad();
       return probesToLoad;
   }
   
    private void generateBeadsToLoad()
{
    probesToLoad.add(new probeTableData(1, "TCR"));
     probesToLoad.add(new probeTableData(2, "CD"));
      probesToLoad.add(new probeTableData(3, "LAT"));
       probesToLoad.add(new probeTableData(4, "ZAP"));
       probesToLoad.add(new probeTableData(5, "SLP"));


}
   public void swap(int experiment, int index1, int index2) {
        String s1 = XMLFileMap.get(experiment).get(index1);
        String s2 = XMLFileMap.get(experiment).get(index2);
        XMLFileMap.get(experiment).set(index2, s1);
        XMLFileMap.get(experiment).set(index1, s2);  
    }
   
   public void setUserInputsForBeadPlateMap( HashMap<Integer, List<UserInputForBeadPlate>> userInputsForBeadPlateMap)
   {
       this.userInputsForBeadPlateMap = userInputsForBeadPlateMap;
   }
      
   public void setUserInputsForOneExperiment( int experiment, List<UserInputForBeadPlate> input)
   {
       userInputsForBeadPlateMap.put(experiment, input);
   }
   
    public HashMap<Integer, List<UserInputForBeadPlate>>  getUserInputsForBeadPlateMap( )
   {
       return userInputsForBeadPlateMap;
   }
    

   
    public HashMap<Integer,  HashMap<Integer,ObservableList<bead>>> getData()
    {
        return data;
    }
    
    //initilize probe list map after user upload xml files or manually set up experiments. 
   public void initilizeProbeListForPopulate()
   {
       //if not empty clear it first. (for manually set up experiments)
       if(probesListForPopulate.size()!=0) 
           probesListForPopulate.clear();
       
       for(int i = 1; i <=numberOfExpriments; i++ )
       {
            HashMap<Integer, ObservableList<probeTableData>> probesList = new HashMap<>();
            for( int j =0; j < XMLFileMap.get(i).size();j++)
            {
                ObservableList<probeTableData> list =  FXCollections.observableArrayList();
                probesList.put(j, list);
            }
            probesListForPopulate.put(i,  probesList);           
       }
   }
    //set probles in to the hashtable that holds probes data for each experiment and each bead plate. 
    public void setProbes(int experiement, int beadPlate, ObservableList<bead> probes)
    {
        if(!data.containsKey(experiement))
        {
            data.put(experiement, new HashMap<Integer,ObservableList<bead>>());
        }
           ObservableList<bead> tobe =  FXCollections.observableArrayList(probes);
           //ObservableList<bead> experimentBeads = FXCollections.observableArrayList();
            data.get(experiement).put(beadPlate, tobe);           
    }
    public void setProbeListForPopulate(HashMap<Integer, HashMap<Integer, ObservableList<probeTableData>>> probeLists)
    {
        this.probesListForPopulate = probeLists;
    }
    
    public void setProbeListForOnePlate(int experiement, int beadPlate, ObservableList<probeTableData> probes)
    {
        probesListForPopulate.get(experiement).put(beadPlate,probes);
    }
    
    public HashMap<Integer, HashMap<Integer, ObservableList<probeTableData>>> getProbeListForPopulate()
    {
        return probesListForPopulate;
    }
    
    // this function is for test 
    public ObservableList<probeTableData> getProbesForOnePlate(int experiement, int beadPlate)
    {
        if(!probesListForPopulate.containsKey(experiement)) 
        {
            ErrorMsg error = new ErrorMsg();
            error.showError("not contains the experiment " +  experiement + " information!");
            return null;
        }
        
        if(!probesListForPopulate.get(experiement).containsKey(beadPlate))
        {
            ErrorMsg error = new ErrorMsg();
            error.showError("bead Plate " +  beadPlate + " dose not exisit!");
            return null;
        }    
            return probesListForPopulate.get(experiement).get(beadPlate);

    }
    
    //create probe container for each experiemnt
    public void addExperiement(int experiements)
    {
        for(int i = 0; i < experiements; i++)
        {
            data.put(i, new HashMap<Integer, ObservableList<bead>>() );
        }
    }

    public void setCurrentExperiment(Integer experiement) {
        currentExperiment = experiement;
    }
    
    public int getCurrentExperiment() {
         return currentExperiment;
    }
  
    
    //initialize the probe list for the experiment
    public void initializeProbeListMap(int experiment )
    {
        if(!probesListForPopulate.containsKey(experiment))
        {
             probesListForPopulate.put(experiment, new HashMap<Integer, ObservableList<probeTableData>>());
             List<String> s = XMLFileMap.get(experiment);
             int size = s.size();
             for(int i = 1; i <= XMLFileMap.get(experiment).size();i++)
             {
                 ObservableList<probeTableData> probes = FXCollections.observableArrayList();
                 probesListForPopulate.get(experiment).put(i, probes);
             }
        }
    }
    
    // add probe list to certain plate. 
    public void addOneProbeListForPopulate(int experiment, int plate, ObservableList<probeTableData> probes)
    {
        probesListForPopulate.get(experiment).put(plate, probes);
    }
    
    public  HashMap<Integer, HashMap<Integer, ObservableList<probeTableData>>> getProbeMapForPopulate()
    {
        return probesListForPopulate;
    }
    
    // get probe list for cetain plate. 
    public  ObservableList<probeTableData> getProbeListForPopulate(int experiment, int plate)
    {
        if(!probesListForPopulate.containsKey(experiment)  || !probesListForPopulate.get(experiment).containsKey(plate)) return null;
        return probesListForPopulate.get(experiment).get(plate);
    }
    

    // for all xmlfiles user uploaded
    public void setXMLFiles(List<String> list)
    {
        fileNames = list;
    }
    //get all xml files user uploaded
    public List<String> getXMLFiles()
    {
        return fileNames;
    }
    
    public void setNumberOfExperiments(int n)
    {
        numberOfExpriments = n;
    }
    public int getNumberOfExperiments()
    {
        return numberOfExpriments;
    }
    
    public void setExperiments( ObservableList<Integer> experiments)
    {
        this.experiments = experiments;
    }
    
    public  ObservableList<Integer>  getExperiments( )
    {
        return experiments;
    }
    //put xml files map in to model 
    public void setExperimentsMap(Map<Integer, List<String>> map)
    {
        this.XMLFileMap = map;
    }
    
    // replace xml file list for one experiment
    public void seXMLfileListForOneExperiment(int experiment, List<String> list)
    {
        XMLFileMap.put(experiment, list);

    }
    public Map<Integer, List<String>>  getExperimentsXMLFileMap( )
    {
        return XMLFileMap;
    }
 
    
    
}

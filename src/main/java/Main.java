import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    //static NavigableMap<String, Double> input = new TreeMap<>();
    static BST tree;
    static String filenameInput = "data.txt";
    static String filenameOutput = "res.txt";
    // Create an empty hash map
    static HashMap<String, Double> input = new HashMap<>();

    /**
     * Coge el HashMap de entrada, ordena de mayor a menor y a los dos ultimos los suma.
     * Se vuelve a llamar a la funcion hasta que tengamos solo 2.
     * Con dos hacemos backtracking y los vamos añadiendo al arbol binario
     * @param data HashMap con los datos
     * @param tree Arbol binario resultante
     */
    public static void sortAndMerge(HashMap<String, Double> data, BST tree){
        if (data.size() == 2){
            // Cogemos los dos ultimos
            String[] keys = data.keySet().toArray(new String[0]);
            // los añadimos al arbol
            BST.Node s1 = new BST.Node(data.get(keys[0]), keys[0]);
            BST.Node s2 = new BST.Node(data.get(keys[1]), keys[1]);
            tree.insert(tree.root, s1, s2);
        } else {
            // Ordenamos la tabla
            List<Map.Entry<String, Double>> list = new LinkedList<>(data.entrySet());
            list.sort(new Comparator<Map.Entry<String, Double>>() {
                @Override
                public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                    if (o1.getValue() > o2.getValue()){
                        return -1;
                    }
                    return 1;

                }
            });
            HashMap<String, Double> temp = new LinkedHashMap<>();
            for(int i = 0; i < list.size() - 2; i++){
                temp.put(list.get(i).getKey(), list.get(i).getValue());
            }
            String key1 = list.get(list.size() - 2).getKey();
            String key2 = list.get(list.size() - 1).getKey();
            double s1 = list.get(list.size() - 2).getValue();
            double s2 = list.get(list.size() - 1).getValue();

            double sum = s1 + s2;
            temp.put(key1+key2, sum);

            sortAndMerge(temp, tree);

            BST.Node x1 = new BST.Node(s1, key1);
            BST.Node x2 = new BST.Node(s2, key2);
            tree.insert(tree.root, x1, x2);
        }
    }

    public static void readFile(){
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filenameInput));
            String line = reader.readLine();
            while (line != null) {
                String[] data = line.split(",");
                if (data.length > 0 && data.length < 3){
                    input.put(data[0], Double.parseDouble(data[1]));
                } else {
                    System.out.println("Error formato");
                    System.exit(0);
                }
                System.out.println(line);
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double getPromedio(){
        double promedio = 0.00;
        int llargariaBits;
        double proba;
        String[] allKeys = input.keySet().toArray(new String[0]);

        for(int i = 0; i< input.size();i++){
            llargariaBits = tree.parseString(tree.root, allKeys[i]).length();
            proba = input.get(allKeys[i])/100;
            promedio += llargariaBits*proba;

        }

        return promedio;
    }

    public static double getEntropy(){
        double entropy = 0.0;
        double proba;
        String[] allKeys = input.keySet().toArray(new String[0]);

        for (int i = 1; i < input.size(); i++) {
            proba = input.get(allKeys[i])/100;
            entropy -= (proba * (log2(proba)));
        }

        return entropy;
    }


    public static double log2(double N){

        double result = (Math.log(N) / Math.log(2));

        return result;
    }

    public static int lenMaxBinary(){
        String[] allKeys = input.keySet().toArray(new String[0]);
        int tmp, tmp2 = 0, indexLlarg = 0;

        for (int i = 0; i<input.size(); i++){
            tmp = tree.parseString(tree.root, allKeys[i]).length();
            if(tmp > tmp2){
                tmp2 = tmp;
            }
        }
        return tmp2;
    }

    public static void saveFile(String data){
        try{
            FileWriter file = new FileWriter(filenameOutput, true);
            BufferedWriter bufferedWriter = new BufferedWriter(file);
            bufferedWriter.write(data);
            bufferedWriter.newLine();
            bufferedWriter.close();
            file.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void saveResults(HashMap<String, Double> data){
        saveFile("__________");
        for (Map.Entry<String, Double> entry: data.entrySet()){
            saveFile(entry.getKey() + ": " + tree.parseString(tree.root, entry.getKey()));
        }
        saveFile("__________");
    }


    public static String getStringAleatori(){
        Random rand = new Random();
        String[] allKeys = input.keySet().toArray(new String[0]);
        String aleatori = "";
        for(int i = 0; i<10; i++){
            if(i == 9){
                aleatori += (allKeys[rand.nextInt(allKeys.length)]);
            }else{
                aleatori += (allKeys[rand.nextInt(allKeys.length)] + " ");
            }
        }
        return aleatori;
    }


    public static String getBinaryTraduction(String aleatori){
        String[] aleatoriKeys = aleatori.split(" ");
        String binaryTranslate = "";
        for(int i = 0; i<aleatoriKeys.length; i++){
            binaryTranslate += tree.parseString(tree.root, aleatoriKeys[i]);
        }

        return binaryTranslate;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        // Leemos el fichero
        readFile();
        //
        String[] allKeys = input.keySet().toArray(new String[0]);

        tree = new BST(100, String.join("", allKeys));
        sortAndMerge(input, tree);
        saveResults(input);

        //Creació de el string aleatori
        String aleatori = getStringAleatori();
        String binaryTraduction = getBinaryTraduction(aleatori);
        double promedio = getPromedio();


        System.out.println("El promitg és: " + promedio);
        System.out.println("L'entropía és: " + getEntropy());
        System.out.println("El factor de compressió és: " + (lenMaxBinary()/promedio) + ":1");

        System.out.println("__________________________________________________________________________");

        System.out.println("El string aleatori és: "+aleatori);
        System.out.println("La traducció amb Codificació Huffman és: "+binaryTraduction);

        System.out.println("finish");

    }

}

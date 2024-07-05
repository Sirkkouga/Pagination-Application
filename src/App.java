import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class App {

    private static int currentPage = 1;
    private static int currentLine = 0;
    /*
     * Añadir nueva página
     */
    private static String newPage(){
        String page = ("================= PAGE "+currentPage+" =================\n\n");
        currentPage++;
        return page;
    }

    /*
     * Crear una línea del fichero
     */
    private static String[] createLine(int numIt, String text){

        String res = "";

        //Crear linea de 80 caracteres o hasta que se acabe el fichero si hay menos
        int maxIndex = Math.min(numIt + 80, text.length());
        for (int i = numIt; i < maxIndex ; i++)
            res += text.charAt(i);

        
        //Comprobar si la palabra no se ha acabado
        boolean unfinishedWord = true;
        if (maxIndex < text.length() && text.charAt(maxIndex) == ' ')
                unfinishedWord = false;

        //Formatear bien el texto para que sea más legible
        res = correctStartString(res);
        if (unfinishedWord) 
            res = correctEndString(res);
        res += "\n";
        currentLine++;

        //-1 para evitar que el \n nos cuente como un caracter
        return new String[]{res, String.valueOf(numIt +res.length() - 1)}; 
    }

    /*  
     * Corregir el inicio de la linea
     */
    private static String correctStartString(String text){
        return text.stripLeading();
    }

    /*
     * Corregir el final de la linea, eliminando todo lo que haya detrás del último espacio
     */
    private static String correctEndString(String text){
        String res = text;
        int lastSpace = text.lastIndexOf(' '); 
        if (lastSpace != -1){
            res = text.substring(0, lastSpace); 
        }
        return res;
    }

    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new FileReader("src/document.txt")); //cambiar fichero si es necesario

        String content = br.readLine(); //Leer todo el fichero que sera de 1 linea
        br.close(); 

        String targetFile = "src/solution.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile))) {
            writer.write(newPage());
            
            for (int i = 0; i < content.length(); i++) {
                
                String line[] = createLine(i, content);
                i = Integer.parseInt(line[1]);
                writer.write(line[0]);

                if (currentLine == 25) { //25 lineas por página 
                    writer.write("\n");
                    writer.write(newPage());
                    currentLine = 0; //reseteamos las lineas de la página
                }
                
            }

            System.out.println("Archivo listo en: " + targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

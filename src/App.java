import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class App {

    private static int currentPage = 1;
    private static int currentLine = 0;
    private static int MAX_LINE_PER_PAGE = 25;
    private static int MAX_CHAR_PER_LINE = 80;
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
    private static String createLine(int numIt, String text){

        StringBuilder res = new StringBuilder();

        //Crear linea de 80 caracteres o hasta que se acabe el fichero si hay menos
        int maxIndex = Math.min(numIt + MAX_CHAR_PER_LINE, text.length());
        res.append(text.substring(numIt, maxIndex));

        
        //Comprobar si la palabra no se ha acabado
        boolean unfinishedWord = true;
        if (maxIndex < text.length() && text.charAt(maxIndex) == ' ')
                unfinishedWord = false;

        //Formatear bien el texto para que sea más legible
        res = correctStartString(res);
        if (unfinishedWord){ 
            res = correctEndString(res);
        }
        res.append("\n");
        currentLine++;

        //-1 para evitar que el \n nos cuente como un caracter
        return res.toString(); 
    }

    /*  
     * Corregir el inicio de la linea
     */
    private static StringBuilder correctStartString(StringBuilder text){
        StringBuilder res = new StringBuilder( text.toString().stripLeading() );
        return res;
    }

    /*
     * Corregir el final de la linea, eliminando todo lo que haya detrás del último espacio
     */
    private static StringBuilder correctEndString(StringBuilder text){
        int lastSpace = text.toString().lastIndexOf(' '); 
        if (lastSpace != -1){
            StringBuilder res = new StringBuilder (text.toString().substring(0, lastSpace)); 
            return res;
        }
        return text;
    }

    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new FileReader("src/document.txt")); //cambiar fichero si es necesario

        String content = br.readLine(); //Leer todo el fichero que sera de 1 linea
        br.close(); 

        String targetFile = "src/solution.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile))) {
            writer.write(newPage());
            
            for (int i = 0; i < content.length(); i++) {
                
                String line = createLine(i, content);
                i += line.length() - 1; //actualizamos el indice del fichero
                writer.write(line);

                if (currentLine == MAX_LINE_PER_PAGE) { //25 lineas por página 
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

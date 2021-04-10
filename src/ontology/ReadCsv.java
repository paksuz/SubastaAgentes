package ontology;


import ontology.Producto;
import java.io.BufferedReader;
import java.io.IOException;
import static java.lang.Math.random;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


 
public class ReadCsv {
    
  
        
   public static ArrayList<Producto> LeerProductos(String filename){
    ArrayList<Producto> productos = new ArrayList<>();
    Path pathtofile = Paths.get(filename);
    
    try(BufferedReader br = Files.newBufferedReader(pathtofile, StandardCharsets.US_ASCII)){
        
        String line = br.readLine();
        line= br.readLine();
       // System.out.println(line);
        while(line != null){
            String[] atributos = line.split(",");
            Producto producto = crearProducto(atributos);
            productos.add(producto);
            line = br.readLine();
            
        }
        
    }catch(IOException ioe){
        ioe.printStackTrace();
    }
    return productos;
}
   
   public static Producto crearProducto(String[] metadata){
       String nombre = metadata[1];
       String marca = metadata[2];
       double d = Double.parseDouble(metadata[3]);
       int precio = (int) d;
        if(precio==0){
            precio = 2;
        }
       return new Producto(nombre,marca,precio );
   }
     public static Producto crearObjetivo(String[] metadata){
       String nombre = metadata[2];
       String marca = metadata[3];
        //System.out.println("=" +metadata[3]);
       double d = Double.parseDouble(metadata[4]);
      
       int precio = (int) d;
        if(precio==0){
            precio = 2;
        }
       return new Producto(nombre,marca,precio,0 );
   }
   
    
     public static HashMap<Producto,String> LeerObjetivos(String filename){
    HashMap<Producto,String> productos = new HashMap<>();
    Path pathtofile = Paths.get(filename);
    
    try(BufferedReader br = Files.newBufferedReader(pathtofile, StandardCharsets.US_ASCII)){
        
        String line = br.readLine();
        line= br.readLine();
       // System.out.println(line);
        while(line != null){
            String[] atributos = line.split(",");
           // System.out.println("---------------------------");
           // System.out.println(atributos[4]);
           // System.out.println("---------------------------");
            Producto producto = crearObjetivo(atributos);
            
            productos.put(producto,producto.getNombre());
            line = br.readLine();
            
        }
        
    }catch(IOException ioe){
        ioe.printStackTrace();
    }
    return productos;
}
          public static HashMap<Producto,String> LeerObjetivosAfectivos(String filename){
    HashMap<Producto,String> productos = new HashMap<>();
    Path pathtofile = Paths.get(filename);
    
    try(BufferedReader br = Files.newBufferedReader(pathtofile, StandardCharsets.US_ASCII)){
        
        String line = br.readLine();
        line= br.readLine();
       // System.out.println(line);
        while(line != null){
            String[] atributos = line.split(",");
           // System.out.println("---------------------------");
           // System.out.println(atributos[4]);
           // System.out.println("---------------------------");
            Producto producto = crearObjetivoAfectivo(atributos);
            
            productos.put(producto,producto.getNombre());
            line = br.readLine();
            
        }
        
    }catch(IOException ioe){
        ioe.printStackTrace();
    }
    return productos;
}
    public static Producto crearObjetivoAfectivo(String[] metadata){
       String nombre = metadata[2];
       String marca = metadata[3];
        //System.out.println("=" +metadata[3]);
       double d = Double.parseDouble(metadata[4]);
      
       int precio = (int) d;
        if(precio==0){
            precio = 2;
        }
        Random rand = new Random();
        int factor = rand.nextInt(50 - -50 + 1) -50;
       return new Producto(nombre,marca,precio, factor );
   }
          
   
   
  }
   



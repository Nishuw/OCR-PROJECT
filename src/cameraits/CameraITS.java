/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraits;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import javax.imageio.ImageIO;


public class CameraITS {
    DataInputStream dis;
    
    public String getCameraIPAddress() {
        return cameraIPAddress;
    }
    public void setCameraIPAddress(final String cameraIPAddress) {
        this.cameraIPAddress = cameraIPAddress;
    }
    private String cameraIPAddress = "";
    private int cameraPort;
    
    
    public void setCameraPort(final int cameraPort) {
        this.cameraPort = cameraPort;
    }
    
    private String composeURL() {
        final String urlString = "http://" + this.cameraIPAddress;
        return urlString;
    }
    
     //envia comandos para a camera
    private boolean executeLeituraCommand(final String command) throws IOException {
        final URLConnection connection = null;
        HttpURLConnection huc = null;
        try {
            final URL u = new URL(this.composeURL() + command);
           
            huc = (HttpURLConnection)u.openConnection();
            System.out.println(huc.getContentType());
            final InputStream is = huc.getInputStream();
            
            Image image = null;
            image = ImageIO.read(is);
            is.close();
            this.saveToFile(image);
            return true;
        }
        catch (IOException e2) {
            try {
                huc.disconnect();
                Thread.sleep(10L);
            }
            catch (InterruptedException ie) {
                huc.disconnect();
                System.out.println(this.cameraIPAddress + " - Foi necessecao segunda requisição da imagem!!!");
                this.capturaFoto();
            }
            System.out.println(this.cameraIPAddress + " - Foi necessecao segunda requisição da imagem!!!");
            this.capturaFoto();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    //envia comando de foto para camera
    public void capturaFoto() {
        try {
            this.executeLeituraCommand("/api/snapshot.cgi?qualidade=100");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    
    //salva a foto tirada
    public void saveToFile(final Image image) {
        final String sistema = System.getProperty("os.name");
        
        final String savingFileSuffix = ".jpg";
        String savingExtra = "";
        String diretorio = "";
        diretorio = System.getProperty("user.dir") + "/";
        
        final Calendar now = Calendar.getInstance();
        savingExtra = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS%1$tL", now);
        String savingFileName;
        
        savingFileName = diretorio + this.cameraIPAddress + "-1-" + "-" + savingExtra + savingFileSuffix;

        try {
            final File file = new File(savingFileName);
            ImageIO.write((RenderedImage)image, "jpg", file);
        }
        catch (IOException exception) {
            System.out.println(exception);
        }
    }
    
    
    public boolean executeLeituraCommandDiretorio(final String command, final String diretorio) throws MalformedURLException, IOException {
        final URLConnection connection = null;
        InputStream is = null;
        HttpURLConnection huc = null;
        try {
            final URL u = new URL(this.composeURL() + command);
            
            huc = (HttpURLConnection)u.openConnection();
            System.out.println(huc.getContentType());
            is = huc.getInputStream();
            
            final BufferedInputStream bis = new BufferedInputStream(is);
            this.dis = new DataInputStream(bis);
            Image image = null;
            image = ImageIO.read(is);
            is.close();
            this.saveToFile(image, diretorio);
            return true;
        }
        catch (IOException e) {
            try {
                is.close();
                huc.disconnect();
                Thread.sleep(5L);
                e.printStackTrace();
            }
            catch (InterruptedException ie) {
                is.close();
                huc.disconnect();
                ie.printStackTrace();
            }
        }
        catch (Exception e2) {
            is.close();
            huc.disconnect();
            e2.printStackTrace();
        }
        return false;
    }
    
     public void saveToFile(final Image image, final String pasta)  {
        final String sistema = System.getProperty("os.name");
        
        final String savingFileSuffix = ".jpg";
        String savingExtra = "";
        String diretorio = "";
      
        diretorio = System.getProperty("user.dir") + "/" + pasta + "/Image";
        
        final Calendar now = Calendar.getInstance();
        savingExtra = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS%1$tL", now);
        String savingFileName;
       
           
                savingFileName = diretorio + this.cameraIPAddress + "-1-"  + "-" + savingExtra + savingFileSuffix;
      
        try {
            System.out.println("IMAGEM SALVA POR HTTP:" + savingFileName);
            final File file = new File(savingFileName);
            ImageIO.write((RenderedImage)image, "jpg", file);
        }
        catch (IOException exception) {
              System.out.println(exception);
        }
    }
    
}

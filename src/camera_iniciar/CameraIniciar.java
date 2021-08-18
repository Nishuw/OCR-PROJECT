/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package camera_iniciar;

import java.io.IOException;
import java.util.Scanner;
import cameraits.CameraITS;


public class CameraIniciar {

    
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("CAMERA IP: ");
        CameraITS cam = new CameraITS();
        cam.setCameraIPAddress(sc.nextLine());
        System.out.println("Numero de fotos: ");
        numeroFotos(sc.nextInt(), cam);

        cam.capturaFoto();
        cam.executeLeituraCommandDiretorio("/api/snapshot.cgi?qualidade=100", "/../../../../Imagens/");
        
        
    }
    
    public static void numeroFotos(int n, CameraITS objCam){
        for (int i = 0; i < n ; i++) {
            objCam.capturaFoto();
        }
    }
    
    
    
}

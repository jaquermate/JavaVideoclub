/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author jmartinezdejuan
 */
public class Pelicula {
    int id_pelicula;
    String titulo;
    int ano;
    String pais;
    String genero;
    int imdb;
    String clasificacion_imdb;
    String resumen;
    ImageIcon fotoCaratula;
    
    public ImageIcon cogeCaratula(String miRuta){
        URL nombreImagen = getClass().getResource(miRuta);
        fotoCaratula = new ImageIcon(new ImageIcon(nombreImagen).getImage().getScaledInstance(VentanaInicio.CARATULA_X , VentanaInicio.CARATULA_Y, Image.SCALE_DEFAULT));
        return fotoCaratula;
        //_miJLabel.setIcon(fotoCaratula);
    }
//    
}

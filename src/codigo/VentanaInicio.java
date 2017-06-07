package codigo;

import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jmartinezdejuan
 */
public class VentanaInicio extends javax.swing.JFrame {

    Connection conexion;//almacena la conexion del servidor de BBDD
    Statement estado; //almacena el estado de la conexion
    ResultSet resultado,resultado2;//amacena el resultado de la consulta a la BBDD
    String nombreLogin = "";
    String contraseña = "";
    int i = 0;
    static int CARATULA_X = 80;
    static int CARATULA_Y = 120;
    ArrayList<Pelicula> pelisAL = new ArrayList<>();
    ImageIcon []putaCaratuladeloshuevos=new ImageIcon[1000];
    JInternalFrame nuevoFrame=  new JInternalFrame();

    /**
     * Creates new form VentanaInicio
     */
    public void descargaDatosPelis() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://10.211.55.8/videoclub", "root", "qwerty");
            estado = conexion.createStatement();
            resultado = estado.executeQuery("SELECT * FROM videoclub.peliculas");

            while (resultado.next()) {
                Pelicula miPeli = new Pelicula();
                miPeli.id_pelicula = resultado.getInt("id_pelicula");
                miPeli.titulo = resultado.getString("titulo");
                miPeli.ano = resultado.getInt("año");
                miPeli.pais = resultado.getString("pais");
                miPeli.genero = resultado.getString("genero");
                miPeli.imdb = resultado.getInt("imdb");
                miPeli.clasificacion_imdb = resultado.getString("clasificacion_imdb");
                miPeli.resumen = resultado.getString("resumen");
                String urlPeli="/caratulas/" + String.format("%06d", miPeli.id_pelicula) + ".jpg";
                URL nombreImagen = getClass().getResource(urlPeli);
                miPeli.fotoCaratula=new ImageIcon(new ImageIcon(nombreImagen).getImage().getScaledInstance(VentanaInicio.CARATULA_X ,
                        VentanaInicio.CARATULA_Y, Image.SCALE_DEFAULT));
                putaCaratuladeloshuevos[i]=miPeli.fotoCaratula;
                i++;
                
                pelisAL.add(miPeli);
            }

        } catch (ClassNotFoundException ex) {
            System.out.println("No se ha encontrado el driver");
        } catch (SQLException ex) {
            System.out.println("NO SE HA PODIDO CONECTAR CON EL SERVIDOR");
        }
    }
    
    public void ponFotoaCaratulas(){
        
    }
    public void consulta1() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            //indico los parametros de la conexion
            conexion = DriverManager.getConnection("jdbc:mysql://10.211.55.8/videoclub", "root", "qwerty");
            //realizo la conexion
            estado = conexion.createStatement();
            //realizo la consulta
            resultado = estado.executeQuery("SELECT * FROM videoclub.usuarios");

            while (resultado.next()) {
                // String[] aux = new String[3];
//                aux[0] = resultado.getString("DNI");
//                aux[1] = resultado.getString("Nombre");
//                aux[2] = resultado.getString("Apellidos");
                //lista.add(aux);
                System.out.println(resultado.getString("Nombre"));
            }

        } catch (ClassNotFoundException ex) {
            System.out.println("No se ha encontrado el driver");
        } catch (SQLException ex) {
            System.out.println("NO SE HA PODIDO CONECTAR CON EL SERVIDOR");
        }
    }

    public void pintaFoto(JLabel _miJLabel, String miRuta, boolean añadir) {
        ImageIcon miImagen = null;
        URL nombreImagen = null;
        int anchoImagen = _miJLabel.getWidth();
        int altoImagen = _miJLabel.getHeight();
        nombreImagen = getClass().getResource(miRuta);
        miImagen = new ImageIcon(new ImageIcon(nombreImagen).getImage().getScaledInstance(anchoImagen, altoImagen, Image.SCALE_DEFAULT));
        _miJLabel.setIcon(miImagen);
        if (añadir) {
            // jPanel1.add(_miJLabel);
        }
    }
    public void infoPeli(String numeroPeli) throws SQLException{
                nuevoFrame=new JInternalFrame("INFO", true, true);
                resultado2 = estado.executeQuery("SELECT * FROM videoclub.peliculas where id_pelicula='"+numeroPeli+"'");
                while(resultado2.next()){
                    titulo.setText(resultado2.getString("titulo"));
                    ano.setText(Integer.toString(resultado2.getInt("año")));
                    pais.setText(resultado2.getString("pais"));
                    genero.setText(resultado2.getString("genero"));
                    imdb.setText(Integer.toString(resultado2.getInt("imdb")));
                    puntuacion_imdb.setText(resultado2.getString("clasificacion_imdb"));
                    jTextArea1.setText(resultado2.getString("resumen"));
                    
                    
                }
               
    }

    public  void generaYRellenaLabelsPelis() {
        jScrollPane1.setBounds(0, 0, 500, 400);
        int a=0;
        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 11; col++) {
                JLabel miJLabel = new JLabel();
                miJLabel.setBounds(20 + col * (CARATULA_X + 20), 20 + row * (CARATULA_Y + 20), CARATULA_X, CARATULA_Y);
                miJLabel.setText(Integer.toString(a));
                miJLabel.setIcon(putaCaratuladeloshuevos[a]);
               
                miJLabel.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent e){
                                    try {
                                        infoPeli(miJLabel.getText());
                                        jInternalFrame1.setVisible(true);
                                    } catch (SQLException ex) {
                                        Logger.getLogger(VentanaInicio.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    
                                }
                });
                 a++;
                jPanel1.add(miJLabel);
            }
        }
    }

    public VentanaInicio() {

        initComponents();
        jPanelLogin.setVisible(true);
        jLabelLoginError.setVisible(false);
        jPanelMain.setVisible(false);
        jPanelLoginCorrecto.setVisible(false);

        descargaDatosPelis();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelLoginCorrecto = new javax.swing.JPanel();
        jLabelMensajeEnhorabuena = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButtonVolver = new javax.swing.JButton();
        jPanelLogin = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabelOlvidoContraseña = new javax.swing.JLabel();
        jTextFieldUsuario = new javax.swing.JTextField();
        jButtonContinuar = new javax.swing.JButton();
        jLabelLoginError = new javax.swing.JLabel();
        jButtonRegistrarse = new javax.swing.JButton();
        jPasswordFieldContraseña = new javax.swing.JPasswordField();
        jPanelMain = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabelName = new javax.swing.JLabel();
        jLabelFotoUsuario = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanelPeliculas = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        titulo = new javax.swing.JLabel();
        ano = new javax.swing.JLabel();
        pais = new javax.swing.JLabel();
        genero = new javax.swing.JLabel();
        imdb = new javax.swing.JLabel();
        puntuacion_imdb = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabelMensajeEnhorabuena.setText("jLabel1");

        jCheckBox1.setText("No soy un robot");
        jCheckBox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBox1MouseClicked(evt);
            }
        });

        jButtonVolver.setText("Volver");
        jButtonVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVolverActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelLoginCorrectoLayout = new javax.swing.GroupLayout(jPanelLoginCorrecto);
        jPanelLoginCorrecto.setLayout(jPanelLoginCorrectoLayout);
        jPanelLoginCorrectoLayout.setHorizontalGroup(
            jPanelLoginCorrectoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLoginCorrectoLayout.createSequentialGroup()
                .addGroup(jPanelLoginCorrectoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLoginCorrectoLayout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(jCheckBox1))
                    .addGroup(jPanelLoginCorrectoLayout.createSequentialGroup()
                        .addGap(102, 102, 102)
                        .addComponent(jLabelMensajeEnhorabuena, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(152, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLoginCorrectoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButtonVolver)
                .addGap(56, 56, 56))
        );
        jPanelLoginCorrectoLayout.setVerticalGroup(
            jPanelLoginCorrectoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLoginCorrectoLayout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(jLabelMensajeEnhorabuena, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(jButtonVolver)
                .addGap(58, 58, 58))
        );

        jLabel1.setText("Usuario:");

        jLabel2.setText("Contraseña:");

        jLabelOlvidoContraseña.setText("He olvidado mi contraseña");
        jLabelOlvidoContraseña.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelOlvidoContraseñaMouseClicked(evt);
            }
        });

        jTextFieldUsuario.setText("Jesus Martinez");

        jButtonContinuar.setText("Continuar");
        jButtonContinuar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonContinuarActionPerformed(evt);
            }
        });

        jLabelLoginError.setText("Usuario o contraseña incorrectos");

        jButtonRegistrarse.setText("Registrase");
        jButtonRegistrarse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButtonRegistrarseMousePressed(evt);
            }
        });

        jPasswordFieldContraseña.setText("5464521");

        javax.swing.GroupLayout jPanelLoginLayout = new javax.swing.GroupLayout(jPanelLogin);
        jPanelLogin.setLayout(jPanelLoginLayout);
        jPanelLoginLayout.setHorizontalGroup(
            jPanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 609, Short.MAX_VALUE)
            .addGroup(jPanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelLoginLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLoginLayout.createSequentialGroup()
                            .addComponent(jButtonRegistrarse)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelOlvidoContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanelLoginLayout.createSequentialGroup()
                            .addGap(9, 9, 9)
                            .addGroup(jPanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabelLoginError)
                                .addGroup(jPanelLoginLayout.createSequentialGroup()
                                    .addGroup(jPanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel1))
                                    .addGap(42, 42, 42)
                                    .addGroup(jPanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextFieldUsuario)
                                        .addComponent(jPasswordFieldContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 163, Short.MAX_VALUE)
                            .addComponent(jButtonContinuar)
                            .addGap(100, 100, 100)))
                    .addContainerGap()))
        );
        jPanelLoginLayout.setVerticalGroup(
            jPanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 328, Short.MAX_VALUE)
            .addGroup(jPanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelLoginLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelLoginLayout.createSequentialGroup()
                            .addGroup(jPanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(jTextFieldUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(28, 28, 28)
                            .addGroup(jPanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(jPasswordFieldContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 126, Short.MAX_VALUE)
                            .addComponent(jLabelLoginError, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jButtonRegistrarse)
                            .addGap(10, 10, 10))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLoginLayout.createSequentialGroup()
                            .addGap(114, 114, 114)
                            .addComponent(jButtonContinuar)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelOlvidoContraseña)))
                    .addContainerGap()))
        );

        jLabelName.setText("jLabel4");

        jLabelFotoUsuario.setText("FotoUsuario");
        jLabelFotoUsuario.setPreferredSize(new java.awt.Dimension(85, 16));

        jLabel4.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 0, 0));
        jLabel4.setText("Películas disponibles");
        jLabel4.setToolTipText("");

        jScrollPane1.setToolTipText("");

        jInternalFrame1.setClosable(true);
        jInternalFrame1.setMaximizable(true);
        jInternalFrame1.setResizable(true);
        jInternalFrame1.setVisible(true);

        titulo.setFont(new java.awt.Font("Malayalam Sangam MN", 1, 14)); // NOI18N
        titulo.setText("jLabel5");
        titulo.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        ano.setFont(new java.awt.Font("Malayalam Sangam MN", 1, 14)); // NOI18N
        ano.setText("jLabel6");
        ano.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        pais.setFont(new java.awt.Font("Malayalam Sangam MN", 1, 14)); // NOI18N
        pais.setText("jLabel7");
        pais.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        genero.setFont(new java.awt.Font("Malayalam Sangam MN", 1, 14)); // NOI18N
        genero.setText("jLabel8");
        genero.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        imdb.setFont(new java.awt.Font("Malayalam Sangam MN", 1, 14)); // NOI18N
        imdb.setText("jLabel9");
        imdb.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        puntuacion_imdb.setFont(new java.awt.Font("Malayalam Sangam MN", 1, 14)); // NOI18N
        puntuacion_imdb.setText("jLabel10");
        puntuacion_imdb.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel11.setFont(new java.awt.Font("Malayalam Sangam MN", 1, 14)); // NOI18N
        jLabel11.setText("jLabel11");
        jLabel11.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel12.setFont(new java.awt.Font("Malayalam Sangam MN", 1, 14)); // NOI18N
        jLabel12.setText("jLabel12");
        jLabel12.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel13.setFont(new java.awt.Font("Malayalam Sangam MN", 1, 14)); // NOI18N
        jLabel13.setText("jLabel13");
        jLabel13.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel12)
                    .addComponent(jLabel11)
                    .addComponent(puntuacion_imdb)
                    .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(imdb)
                        .addComponent(genero)
                        .addComponent(pais)
                        .addComponent(ano)
                        .addComponent(titulo)))
                .addGap(151, 151, 151)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(titulo)
                .addGap(18, 18, 18)
                .addComponent(ano)
                .addGap(18, 18, 18)
                .addComponent(pais)
                .addGap(18, 18, 18)
                .addComponent(genero)
                .addGap(18, 18, 18)
                .addComponent(imdb)
                .addGap(18, 18, 18)
                .addComponent(puntuacion_imdb)
                .addGap(17, 17, 17)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addGap(18, 18, 18)
                .addComponent(jLabel13)
                .addContainerGap(189, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jInternalFrame1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(372, 372, 372)
                .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(770, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(151, 151, 151)
                .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(600, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout jPanelPeliculasLayout = new javax.swing.GroupLayout(jPanelPeliculas);
        jPanelPeliculas.setLayout(jPanelPeliculasLayout);
        jPanelPeliculasLayout.setHorizontalGroup(
            jPanelPeliculasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPeliculasLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1186, Short.MAX_VALUE)
                .addGap(6, 6, 6))
        );
        jPanelPeliculasLayout.setVerticalGroup(
            jPanelPeliculasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPeliculasLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelMainLayout = new javax.swing.GroupLayout(jPanelMain);
        jPanelMain.setLayout(jPanelMainLayout);
        jPanelMainLayout.setHorizontalGroup(
            jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelPeliculas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanelMainLayout.createSequentialGroup()
                .addGroup(jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(jPanelMainLayout.createSequentialGroup()
                        .addGroup(jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelMainLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelMainLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabelName, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelFotoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );
        jPanelMainLayout.setVerticalGroup(
            jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMainLayout.createSequentialGroup()
                .addGroup(jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelMainLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabelName, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelMainLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelFotoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanelPeliculas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1896, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelLoginCorrecto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1475, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelLoginCorrecto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
//TODO CODIGO QUE TIENE EL peli0

    private void jLabelOlvidoContraseñaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelOlvidoContraseñaMouseClicked
        //jLabel3.setText("<html><a href="http://www.google.com/">Enlace</a></html>"));
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI("https://help.netflix.com/es-es/node/365"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_jLabelOlvidoContraseñaMouseClicked

    private void jButtonRegistrarseMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonRegistrarseMousePressed
        this.setVisible(false);
        new Registrarse().setVisible(true);
    }//GEN-LAST:event_jButtonRegistrarseMousePressed

    private void jCheckBox1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBox1MouseClicked
        //  CERRAR ESTA VENTANA Y AVANZAR A LO SIGUIENTE
        jPanelLoginCorrecto.setVisible(false);
        jPanelMain.setVisible(true);
        jLabelName.setText(nombreLogin);
       
        pintaFoto(jLabelFotoUsuario, "/fotosUsuarios/" + contraseña + ".jpg", false);
        //pintaFoto(peli0, "/caratulas/000001.jpg",false);
        generaYRellenaLabelsPelis();
        jInternalFrame1.setVisible(false);

    }//GEN-LAST:event_jCheckBox1MouseClicked

    private void jButtonVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVolverActionPerformed
//        this.setVisible(false);
//        new VentanaInicio().setVisible(true);

    }//GEN-LAST:event_jButtonVolverActionPerformed

    private void jButtonContinuarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonContinuarActionPerformed
        jLabelLoginError.setVisible(false);
        nombreLogin = jTextFieldUsuario.getText();
        contraseña = jPasswordFieldContraseña.getText();
        boolean usuarioEncontrado = false;
        try {

            //indico los parametros de la conexion
            conexion = DriverManager.getConnection("jdbc:mysql://10.211.55.8/videoclub", "root", "qwerty");
            //realizo la conexion
            estado = conexion.createStatement();
            //realizo la consulta
            resultado = estado.executeQuery("SELECT * FROM videoclub.usuarios");

            while (!usuarioEncontrado && resultado.next()) {
                if ((resultado.getString("Nombre") + " " + (resultado.getString("Apellido"))).equals(nombreLogin) && resultado.getString("DNI").equals(contraseña)) {

                    jTextFieldUsuario.setText("Login Correcto");
                    System.out.println("ENHORABUENA");
                    usuarioEncontrado = true;
                    jPanelLogin.setVisible(false);
                    jPanelLoginCorrecto.setVisible(true);
                    jPanelLoginCorrecto.setBounds(400, 400, 500, 800);
                    jLabelMensajeEnhorabuena.setText("Enhorabuena " + nombreLogin + " has sido logueado correctamente");
                    break;

                } else {
                    jLabelLoginError.setVisible(true);
                   
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(VentanaInicio.class.getName()).log(Level.SEVERE, null, ex);
            jTextFieldUsuario.setText("Error");
        }
    }//GEN-LAST:event_jButtonContinuarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaInicio().setVisible(true);

            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ano;
    private javax.swing.JLabel genero;
    private javax.swing.JLabel imdb;
    private javax.swing.JButton jButtonContinuar;
    private javax.swing.JButton jButtonRegistrarse;
    private javax.swing.JButton jButtonVolver;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelFotoUsuario;
    private javax.swing.JLabel jLabelLoginError;
    private javax.swing.JLabel jLabelMensajeEnhorabuena;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JLabel jLabelOlvidoContraseña;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelLogin;
    private javax.swing.JPanel jPanelLoginCorrecto;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JPanel jPanelPeliculas;
    private javax.swing.JPasswordField jPasswordFieldContraseña;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextFieldUsuario;
    private javax.swing.JLabel pais;
    private javax.swing.JLabel puntuacion_imdb;
    private javax.swing.JLabel titulo;
    // End of variables declaration//GEN-END:variables
}

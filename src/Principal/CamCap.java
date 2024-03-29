package Principal;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.videoio.VideoCapture;
import org.opencv.core.Core;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author alfon
 */
public class CamCap extends javax.swing.JFrame {

    private DaemonThread myThread = null;
    int count = 0;

    Mat frame = new Mat();
    Mat area;
    Mat halo;
    Mat circulos = new Mat();

    MatOfByte mem = new MatOfByte();

    CascadeClassifier haloDetector;  // Objeto reconocedor de imagenes
    CascadeClassifier eyeDetector;   // Objeto para reconocer ojos

    MatOfRect haloDetections;   // Matriz donde se alojarán las caras detectadas
    MatOfRect eyeDetections;   // Matriz donde se alojarán los ojos detectadas

    String File_path = "";
    String base_path = "./data/haarcascades/";   // Carpeta donde se localizan los modelos de reconocimiento
    String haloFile = "halo.xml";  // Archivo referencia para reconocimiento de rostros
    String eyeFile = "haarcascade_eye.xml";  // Archivo referencia para reconocimiento de rostros

    class DaemonThread implements Runnable {

        protected volatile boolean runnable = false;
        VideoCapture webSource = null;
        String source = "";

        DaemonThread(String _source) {
            source = _source;
        }

        @Override
        public void run() {
            webSource = new VideoCapture();

            if (source.equals("Desde WebCam")) {
                webSource.open(0);
            } else {
                webSource.open(File_path);
            }

            haloDetector = new CascadeClassifier(base_path + haloFile); // Se crea un objeto CascadeClassifier que reconocera caras
            haloDetections = new MatOfRect(); // Se inicializa el objeto donde se guardaran las caras detectadas

            eyeDetector = new CascadeClassifier(base_path + eyeFile); // Se crea un objeto CascadeClassifier que reconocera caras
            eyeDetections = new MatOfRect(); // Se inicializa el objeto donde se guardaran las caras detectadas
            Size a = new Size(35.0, 35.0);

            synchronized (this) {
                while (runnable) {
                    if (webSource.grab()) {
                        try {
                            webSource.retrieve(frame); // Se obtiene un recuadro de la camara

                            haloDetector.detectMultiScale(frame, haloDetections, 8.5, 30, 1, a); // Se buscan las caras dentro de la imagen y se guardan en haloDetections

                            int i = 0;

                            for (Rect rect : haloDetections.toArray()) {

                                // Se crea un cuadrito verde por cada control detectada
                                Imgproc.rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                                        new Scalar(0, 250, 0));

                                halo = frame.submat(haloDetections.toArray()[i]);

                                /* eyeDetector.detectMultiScale(control, eyeDetections);

                                if (eyeDetections.toArray().length != 0) {

                                    System.out.println("Cara con " + eyeDetections.toArray().length + " ojos");

                                    for (int j = 0; j < eyeDetections.toArray().length; j++) {
                                        
                                        Rect rect2 = eyeDetections.toArray()[j];

                                        Imgproc.rectangle(frame, new Point(rect2.x, rect2.y), new Point(rect2.x + rect2.width, rect2.y + rect2.height),
                                        new Scalar(0, 255, 0));
                                        
                                        Mat eye = control.submat(eyeDetections.toArray()[j]);

                                    }
                                }*/
                            }

                            // Se codifica la imagen frame a un arreglo de memoria
                            Imgcodecs.imencode(".bmp", frame, mem);

                            // Se convierte el arreglo de bytes de la imagen a un objeto de la clase Image
                            Image im = ImageIO.read(new ByteArrayInputStream(mem.toArray()));

                            // Se despliega en el Panel
                            BufferedImage buff = (BufferedImage) im;
                            Graphics g = jPanel1.getGraphics();

                            if (g.drawImage(buff, 0, 0, getWidth(), getHeight() - 150, 0, 0, buff.getWidth(), buff.getHeight(), null)) {
                                if (runnable == false) {
                                    System.out.println("En metodo wait()");
                                    this.wait();
                                }
                            }
                        } catch (Exception ex) {
                            System.out.println("Error");
                        }
                    }
                }
            }

            webSource.release();
        }
    }

    public CamCap() {
        initComponents();
        this.setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CamCap");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Quicksand", 1, 14)); // NOI18N
        jButton1.setText("Iniciar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Quicksand", 1, 14)); // NOI18N
        jButton2.setText("Detener");
        jButton2.setEnabled(false);
        jButton2.setMaximumSize(new java.awt.Dimension(72, 24));
        jButton2.setMinimumSize(new java.awt.Dimension(72, 24));
        jButton2.setPreferredSize(new java.awt.Dimension(72, 24));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setPreferredSize(new java.awt.Dimension(320, 240));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 380, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setAlignmentX(0.1F);
        jPanel2.setAlignmentY(0.1F);

        jComboBox1.setFont(new java.awt.Font("Quicksand", 0, 12)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Desde WebCam", "Desde Archivo" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Quicksand", 1, 14)); // NOI18N
        jButton3.setText("...");
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Quicksand", 1, 14)); // NOI18N
        jLabel1.setText("Método de Captura");

        jLabel2.setFont(new java.awt.Font("Quicksand", 1, 20)); // NOI18N
        jLabel2.setText("CamCap - Detector de Objetos");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jButton3)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14))
        );

        jButton4.setFont(new java.awt.Font("Quicksand", 1, 14)); // NOI18N
        jButton4.setText("Salir");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if ((jButton1.getText()).equals("Iniciar")) {

            myThread = new DaemonThread(jComboBox1.getSelectedItem().toString());
            Thread t = new Thread(myThread);
            t.setDaemon(true);
            myThread.runnable = true;
            t.start();

            jButton1.setEnabled(false);
            jButton2.setEnabled(true);
            jComboBox1.setEnabled(false);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        if ((jButton2.getText()).equals("Detener")) {
            myThread.runnable = false;
            jButton2.setEnabled(false);
            jButton1.setEnabled(true);
            jComboBox1.setEnabled(true);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("AVI", "avi");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File_path = chooser.getSelectedFile().getPath();
            jButton1.setEnabled(true);
        } else {
            File_path = "";
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        if (jComboBox1.getSelectedItem().equals("Desde Archivo")) {
            jButton3.setEnabled(true);
            jButton1.setEnabled(false);
        } else {
            jButton3.setEnabled(false);
            jButton1.setEnabled(true);
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:


    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        if (myThread == null) {
        } else if (myThread.runnable) {
            myThread.runnable = false;
        }
    }//GEN-LAST:event_formWindowClosing

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //System.loadLibrary("OpenCV");
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CamCap().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables

}

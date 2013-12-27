/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sensorconfiguration.swing.ui;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 *
 * @author thirumalaisamy
 */
public class ProgressBar implements Runnable{
    private JProgressBar progressBar;
    private JLabel label ;
    private JDialog dialog;
    private JPanel panel;
    public ProgressBar(int max, String message){
         label = new JLabel(message);
         progressBar = new JProgressBar(0,max);
         progressBar.setIndeterminate(true);
        panel = new JPanel(new BorderLayout(5, 5));
        panel.add(label, BorderLayout.PAGE_START);
        panel.add(progressBar, BorderLayout.CENTER);
        
         dialog = new JDialog((JFrame)null, "Working ...");
        dialog.getContentPane().add(panel);
        dialog.setResizable(false);
        dialog.pack();
        dialog.setSize(500, dialog.getHeight());
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setAlwaysOnTop(false);
        dialog.setVisible(true);
        dialog.getContentPane().paintAll(panel.getGraphics());
    }

    @Override
    public void run() {
        
        
    }
    
    public void setProgress(int x,String message){
        label.setText(message);
        //dialog.validate();
        progressBar.setValue(x);
        dialog.getContentPane().paintAll(panel.getGraphics());
    }
    public void done(){
        System.err.println("hiihihihihihiihihihiihihihihi");
        dialog.removeAll();
        this.dialog.dispose();
            dialog.setVisible(false);
                
    }
}
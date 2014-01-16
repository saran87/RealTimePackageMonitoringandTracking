/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.desktop.client;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import rtpmt.location.tracker.LocationTracker;


/**
 *
 * @author kumar
 */
public class RealTimePackageTracking extends javax.swing.JFrame {
    
    Communicator communicator;
    /**
     * Creates new form RealTimePackageTracking
     */
    public RealTimePackageTracking() {
        try{
            initComponents();       
            communicator = new Communicator(this);
            communicator.searchForPorts();
        }
        catch(Exception ex){
           JOptionPane.showMessageDialog(this,ex.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jcmbComPort = new javax.swing.JComboBox();
        jblComPort = new javax.swing.JLabel();
        jbtnConnect = new javax.swing.JButton();
        jlblStatus = new javax.swing.JLabel();
        jblConnectedStatus = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        btnRefreshPorts = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        txtIPAddress = new javax.swing.JTextField();
        lblPort = new javax.swing.JLabel();
        txtPortNumber = new javax.swing.JTextField();
        btnForwardData = new javax.swing.JButton();
        btnClearLog = new javax.swing.JButton();
        jspTimer = new javax.swing.JSpinner();
        lblDataInterval = new javax.swing.JLabel();
        lblSec = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Real Time Package Tracking");
        setName("RealTImePackageTracking");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jcmbComPort.setToolTipText("");

        jblComPort.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jblComPort.setText("Com Port :");

        jbtnConnect.setText("Connect");
        jbtnConnect.setToolTipText("");
        jbtnConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnConnectActionPerformed(evt);
            }
        });

        jlblStatus.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlblStatus.setText("Status :");
        jlblStatus.setToolTipText("");

        jblConnectedStatus.setBackground(new java.awt.Color(0, 153, 0));
        jblConnectedStatus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jblConnectedStatus.setText("Not Connected");

        txtLog.setColumns(20);
        txtLog.setRows(5);
        jScrollPane1.setViewportView(txtLog);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Log");

        btnRefreshPorts.setText("Refresh Ports");
        btnRefreshPorts.setToolTipText("");
        btnRefreshPorts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshPortsActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Server :");

        jToolBar1.setRollover(true);

        txtIPAddress.setText("129.21.175.114");
        txtIPAddress.setToolTipText("");

        lblPort.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblPort.setText("Port :");

        txtPortNumber.setText("3000");

        btnForwardData.setText("Forward data");
        btnForwardData.setToolTipText("");
        btnForwardData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnForwardDataActionPerformed(evt);
            }
        });

        btnClearLog.setText("Clear Log");
        btnClearLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearLogActionPerformed(evt);
            }
        });

        jspTimer.setValue(1);

        lblDataInterval.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblDataInterval.setText("Data Interval:");

        lblSec.setText("sec");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnClearLog, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtIPAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jblComPort)
                                .addGap(18, 18, 18)
                                .addComponent(jcmbComPort, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(lblPort, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jlblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPortNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jblConnectedStatus))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnRefreshPorts, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblDataInterval, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jspTimer, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblSec)))
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jbtnConnect, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnForwardData, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 739, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcmbComPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jblComPort)
                    .addComponent(jlblStatus)
                    .addComponent(btnRefreshPorts)
                    .addComponent(jblConnectedStatus)
                    .addComponent(jbtnConnect))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtIPAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPort)
                            .addComponent(txtPortNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnForwardData)
                            .addComponent(jspTimer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDataInterval)
                            .addComponent(lblSec))))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnClearLog)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jcmbComPort.getAccessibleContext().setAccessibleName("");

        getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnConnectActionPerformed
        //connect to the com port and make connection
       
        /*for(;;){
           
          
            try {
                Thread.sleep(1000);
                 System.out.println(LocationTracker.getLocation().toString());
            } catch (InterruptedException ex) {
                Logger.getLogger(RealTimePackageTracking.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
        
        if(!communicator.getConnected())
        {
            communicator.connect();
            if(communicator.getConnected())
            {
                if(communicator.initIOStream())
                { 
                    if(communicator.initPacketReader())
                    {
                        communicator.initListener();
                        //start location tracker
                        if(LocationTracker.startTracking()){
                            
                            txtLog.append("Started Location tracking\n");
                        }
                        else{
                            txtLog.setForeground(Color.red);
                            txtLog.append("Location tracking not available\n");
                        }
                        jbtnConnect.setText("Disconnect");
                    }
                }
            }
        }
        else
        {
            communicator.disconnect();
            jbtnConnect.setText("Connect");
        }
    }//GEN-LAST:event_jbtnConnectActionPerformed
    
    /**
     * Window closing event
     * disconnect from com port before closing
     * @param evt 
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        
        if(communicator.getConnected())
        {
             communicator.disconnect();
        }
    }//GEN-LAST:event_formWindowClosing

    
    private void btnRefreshPortsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshPortsActionPerformed
        //refresh the ports and get the ports connected with a device 
        if(communicator != null){
            communicator.searchForPorts();
         }
    }//GEN-LAST:event_btnRefreshPortsActionPerformed

    private void btnForwardDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnForwardDataActionPerformed
        try
        {
            if(communicator != null){
                String ipAddress = txtIPAddress.getText();
                int portNumber = Integer.parseInt(txtPortNumber.getText());
                communicator.initalizeTCPClient(ipAddress,portNumber);
            }
        }
        catch(Exception ex){
            showModalMessage("Enter Proper values");
        }
    }//GEN-LAST:event_btnForwardDataActionPerformed

    private void btnClearLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearLogActionPerformed
        // TODO add your handling code here:
         txtLog.setText("");

    }//GEN-LAST:event_btnClearLogActionPerformed

    //to show message in modal dialog
    public void showModalMessage(String message){
        
        JOptionPane.showMessageDialog(this, message);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RealTimePackageTracking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RealTimePackageTracking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RealTimePackageTracking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RealTimePackageTracking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new RealTimePackageTracking().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClearLog;
    private javax.swing.JButton btnForwardData;
    private javax.swing.JButton btnRefreshPorts;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel jblComPort;
    public javax.swing.JLabel jblConnectedStatus;
    private javax.swing.JButton jbtnConnect;
    public javax.swing.JComboBox jcmbComPort;
    private javax.swing.JLabel jlblStatus;
    private javax.swing.JSpinner jspTimer;
    private javax.swing.JLabel lblDataInterval;
    private javax.swing.JLabel lblPort;
    private javax.swing.JLabel lblSec;
    private javax.swing.JTextField txtIPAddress;
    public javax.swing.JTextArea txtLog;
    private javax.swing.JTextField txtPortNumber;
    // End of variables declaration//GEN-END:variables
}

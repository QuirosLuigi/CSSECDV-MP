
package View;

import Controller.SQLite;
import Model.User;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends javax.swing.JPanel {

    public Frame frame;
    public SQLite sqlite;
    private int attempts;
    private boolean cooldown;
    

    
    public Login() {
        initComponents();
        sqlite = new SQLite(); // Create an instance of the SQLite class
        attempts = 0;
        cooldown = false;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        usernameFld = new javax.swing.JTextField();
         passwordFld = new javax.swing.JPasswordField();
         ((JPasswordField) passwordFld).setEchoChar('*');
        
        registerBtn = new javax.swing.JButton();
        loginBtn = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("SECURITY Svcs");
        jLabel1.setToolTipText("");

        usernameFld.setBackground(new java.awt.Color(240, 240, 240));
        usernameFld.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        usernameFld.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        usernameFld.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "USERNAME", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        passwordFld.setBackground(new java.awt.Color(240, 240, 240));
       
        passwordFld.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        passwordFld.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        passwordFld.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "PASSWORD", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        registerBtn.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        registerBtn.setText("REGISTER");
        registerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerBtnActionPerformed(evt);
            }
        });

        loginBtn.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        loginBtn.setText("LOGIN");
        loginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(200, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(registerBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(loginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(usernameFld)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(passwordFld, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap(200, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(88, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(usernameFld, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(passwordFld, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(registerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(loginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(126, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    private void loginBtnActionPerformed(java.awt.event.ActionEvent evt) {
        String username = usernameFld.getText();
        String passText = new String(((JPasswordField) passwordFld).getPassword());
        Boolean match = false;
        
        //Reject blank fields
        if (username.equals("")) {
            if (cooldown) {
                JOptionPane.showMessageDialog(null, "Cooldown in progress. Please try again after 30 seconds.");
                return;
            } else {
                JOptionPane.showMessageDialog(null, "ERROR: Username field is blank!");
            }
        } else if (passText.equals("")) {
            if (cooldown) {
                JOptionPane.showMessageDialog(null, "Cooldown in progress. Please try again after 30 seconds.");
                return;
            } else {
                JOptionPane.showMessageDialog(null, "ERROR: Password field is blank!");
            }
        }
        else {
            /* Compare Encrypted password on input vs Encrypted Password on Database */
            String password2 = encryptPassword(passText);
            
            if (cooldown) {
                JOptionPane.showMessageDialog(null, "Cooldown in progress. Please try again after 30 seconds.");
                return;
            }

            //check if it belongs to registered users
            int role = sqlite.validateUser(username, password2);
            

            if (role != -1) {
                
                //CAPTCHA Pop-up message
                String captchaCode = createRandom(6);
                JTextField captchaInput = new JTextField(10);

                Object[] message = {
                    "Enter the Captcha code:\n      "+ captchaCode, captchaInput
                };
                
                int captcha = JOptionPane.showConfirmDialog(null, message, "Captcha Login", JOptionPane.PLAIN_MESSAGE);

                if (captcha == JOptionPane.OK_OPTION) {
                    //If input matches the captcha
                    if (captchaInput.getText().equals(captchaCode)) {
                        match = true;
                        //Add login user to the logs
                        sqlite.addLogs("NOTICE", usernameFld.getText(), "User login successful", new Timestamp(new Date().getTime()).toString());

                        //Add to session table
                        sqlite.addSession(usernameFld.getText(), role);
                        System.out.println(">> User " + usernameFld.getText() + " is in session");

                        //Erase user inputs before going to the next page
                        usernameFld.setText("");
                        passwordFld.setText("");
                        
                        JOptionPane.showMessageDialog(null, "Login successful!");
                        
                        // Perform role-based navigation
                        switch (role) {
                            case 1:
                                // Disabled users
                                JOptionPane.showMessageDialog(null, "This account is disabled!");
                                break;
                            case 2: //Clients
                                JOptionPane.showMessageDialog(null,"Welcome " + username + "!\nRole: Client");
                                frame.ClientNav();
                                break;
                            case 3: //Staff
                                JOptionPane.showMessageDialog(null,"Welcome " + username + "!\nRole: Staff");
                                frame.StaffNav();
                                break;
                            case 4: //Manager
                                JOptionPane.showMessageDialog(null,"Welcome " + username + "!\nRole: Manager");
                                frame.ManagerNav();
                                break;
                            case 5: //Admin
                                JOptionPane.showMessageDialog(null,"Welcome " + username + "!\nRole: Administrator");
                                frame.AdminNav();
                                break;
                            default:
                                // Handle unexpected or unsupported role values
                                JOptionPane.showMessageDialog(null,"An error has occured. Please try again later.");
                                break;
                        }
                    } else {
                        //Entered code does not match captcha code
                        JOptionPane.showMessageDialog(null, "Entered code does not match. Login failed.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Entered code does not match. Login failed.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "ERROR: Invalid Username or Password!");
                System.out.println("User " + username + " login attempt failed");
            }
        }
        //If the user+pass did not match any in the database, INVALID + add to number of attempts
        if (!match) {
            attempts++;
            //Log failed login attempt
            sqlite.addLogs("NOTICE", usernameFld.getText(), "User login failed", new Timestamp(new Date().getTime()).toString());
            
            if (attempts == 5) {
                cooldown = true;
                startCooldown();
            }
        }    
    }
    
    private void startCooldown() {
        new Thread(() -> {
            try {
                Thread.sleep(30 * 1000); // 30 second cooldown
            } catch (InterruptedException e) {
                System.out.println("Interrupted cooldown");
            }
            attempts = 0;
            cooldown = false;
        }).start();
    }
                                        

    private void registerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerBtnActionPerformed
        usernameFld.setText("");
        passwordFld.setText("");
        frame.registerNav();
    }//GEN-LAST:event_registerBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton loginBtn;
    private static javax.swing.JTextField passwordFld;
    private javax.swing.JButton registerBtn;
    private javax.swing.JTextField usernameFld;
    // End of variables declaration//GEN-END:variables



    //Encrypts a given password
    public static String encryptPassword(String password) {
        try {
            // Generate a secret key from the password
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), new byte[16], 65536, 256);
            SecretKey secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

            // Encrypt the password using AES
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(password.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            // Handle the exception appropriately
            e.printStackTrace();
            return null;
        }
    }
    
    //Generates a random 6-character string for captcha
    public static String createRandom(int len) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < len; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }
}

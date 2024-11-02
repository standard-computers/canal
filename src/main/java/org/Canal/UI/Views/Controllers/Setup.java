package org.Canal.UI.Views.Controllers;

import org.Canal.Models.SupplyChainUnits.Transaction;
import org.Canal.Start;
import org.Canal.UI.Elements.Frame;
import org.Canal.UI.Elements.Input;
import org.Canal.UI.Views.HR.Organizations.OrgView;
import org.Canal.Utils.Configuration;
import org.Canal.Utils.Json;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.UUID;

public class Setup extends Frame {

    public Setup(ArrayList<Configuration> configs){
        super(new Transaction(".cnl.mfg", "/"));
        Input srvAdd = new Input("Database Address");
        Input instance = new Input("Instance Name");
        JButton proceed = new JButton("Begin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel p = new JPanel(new GridLayout(3, 1));
        p.add(srvAdd);
        p.add(instance);
        p.add(proceed);
        add(p);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        proceed.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String addr = srvAdd.value();
                if(configs == null){
                    if(addr.startsWith("http://") || addr.startsWith("https://")){
                        String l = addr.split("://")[1];
                        if(l.startsWith("127.0.0.1") || l.startsWith("localhost")){
                            Configuration cfg = new Configuration(addr, instance.value());
                            Json.save(Start.WINDOWS_SYSTEM_DIR + UUID.randomUUID().toString() + ".cnl.mfg", cfg);
                        }else{
                            //TODO attempt remote server connection
                        }
                    }else{
                        JOptionPane.showMessageDialog(Setup.this, "Address must start with HTTP(S)");
                    }
                }
            }
        });
    }

    public Setup(){
        this(null);
    }

    public static class ProductKey extends JFrame {
        public ProductKey(){
            setIconImage(new ImageIcon(OrgView.class.getResource("/product_key.png")).getImage());
            JLabel pkl = new JLabel("Enter Product Key");
            JLabel pkle = new JLabel("<html>Though open source, a $5 lifetime product key supports this project.<br/>It also makes this window go away 🙂</html>");
            pkl.setBorder(new EmptyBorder(5, 5, 5, 5));
            pkle.setBorder(new EmptyBorder(0, 10, 5, 10));
            pkle.setForeground(new Color(170, 169, 169));
            JTextField pkf = new JTextField("AAAA-X#AA-#A#A-AAAA-X#AA-AAAA-AAAA-AAAA");
            JButton proceed = new JButton("Check");
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            add(pkl, gbc);
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(pkle, gbc);
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            add(pkf, gbc);
            gbc.gridy = 3;
            gbc.gridx = 1;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.EAST;
            add(proceed, gbc);
            setLocationRelativeTo(null);
            setResizable(false);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
            pack();
        }
    }
}
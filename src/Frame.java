import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    Frame() {


        this.setTitle("Weatherly");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(450, 650);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        Panel panel = new Panel();
        panel.setBounds(0,0,this.getWidth(), this.getHeight());
        this.add(panel);

        this.setVisible(true);
    }
}

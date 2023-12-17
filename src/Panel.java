import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Panel extends JPanel{

    private JSONObject weatherData;

    private JTextField searchField;
    private JButton searchButton;
    private JLabel weatherImage,
            temperatureLabel,
            weatherDescLabel,
            humidityImage,
            humidityLabel,
    windsImage,
    windsLabel;
                    ;

    Panel() {
        this.setLayout(null);
        this.setBackground(Color.decode("#e3f6f5"));

        this.searchField = new JTextField();
        this.searchField.setBounds(15, 15, 350, 45);
        this.searchField.setFont(new Font("Dialog", Font.PLAIN, 24));
        this.searchField.setForeground(Color.decode("#272343"));
        this.searchField.setBorder(new EmptyBorder(5, 10, 5, 10));
        this.add(searchField);

        this.searchButton = new JButton("🔍");
        this.searchButton.setBounds(370, 15, 50, 45);
        this.searchButton.setFont(new Font("Dialog", Font.PLAIN, 16));
        this.searchButton.setCursor(Cursor.getPredefinedCursor((Cursor.HAND_CURSOR)));
        this.searchButton.setBackground(Color.decode("#ffd803"));
        UIManager.put("Button.select", Color.decode("#ffd803"));
        this.searchButton.setBorder(null);
        this.searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {;
                String userInput = searchField.getText();

                if(userInput.replaceAll("\\s", "").length() <= 0) {
                    return;
                }

                weatherData = Weatherly.getWeatherData(userInput);

                String weatherCondition = (String) weatherData.get("weather_condition");

                switch (weatherCondition) {
                    case "Clear":
                        weatherImage.setIcon(loadImage("src/assets/clear.png"));
                        break;
                    case "Cloudy":
                        weatherImage.setIcon(loadImage("src/assets/cloudy.png"));
                        break;
                    case "Rain":
                        weatherImage.setIcon(loadImage("src/assets/rain.png"));
                        break;
                    case "Snow":
                        weatherImage.setIcon(loadImage("src/assets/snow.png"));
                        break;
                }

                double temperature = (double) weatherData.get("temperature");
                temperatureLabel.setText(temperature + " C");

                weatherDescLabel.setText(weatherCondition);

                long humidity = (long) weatherData.get("humidity");
                humidityLabel.setText("<html><b>Humidity</b> " + humidity + "%</html");

                double windspeed = (double) weatherData.get("windspeed");
                windsLabel.setText("<html><b>Wind </b>" + windspeed + "km/h</html");
            }
        });
        this.add(searchButton);


        this.weatherImage = new JLabel(loadImage("src/assets/cloudy.png"));
        this.weatherImage.setBounds(0, 125, 450, 217);
        this.add(weatherImage);

        this.temperatureLabel = new JLabel("10 C");
        this.temperatureLabel.setBounds(0, 350, 450, 54);
        this.temperatureLabel.setFont(new Font("Dialog", Font.BOLD, 48));
        this.temperatureLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.temperatureLabel.setForeground(Color.decode("#272343"));
        this.add(temperatureLabel);

        this.weatherDescLabel = new JLabel("Cloudy");
        this.weatherDescLabel.setBounds(0, 405, 450, 36);
        this.weatherDescLabel.setFont(new Font("Dialog", Font.BOLD, 32));
        this.weatherDescLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.weatherDescLabel.setForeground(Color.decode("#272343"));
        this.add(weatherDescLabel);

        this.humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
        this.humidityImage.setBounds(15, 500, 74, 66);
        this.add(humidityImage);

        this.humidityLabel = new JLabel("<html><b>Humidity</b> 100%</html>");
        this.humidityLabel.setBounds(90, 500, 85, 55);
        this.humidityLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        this.humidityLabel.setForeground(Color.decode("#272343"));
        this.add(humidityLabel);

        this.windsImage = new JLabel(loadImage("src/assets/windspeed.png"));
        this.windsImage.setBounds(220, 500, 74, 66);
        this.add(windsImage);

        this.windsLabel = new JLabel("<html><b>Humidity</b> 15km</html>");
        this.windsLabel.setBounds(310, 500, 85, 55);
        this.windsLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        this.windsLabel.setForeground(Color.decode("#272343"));
        this.add(windsLabel);
    }

    private ImageIcon loadImage(String path) {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Could not find image path");
        return null;
    }

}

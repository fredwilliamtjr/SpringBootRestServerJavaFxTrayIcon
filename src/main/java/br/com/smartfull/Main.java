package br.com.smartfull;

import br.com.smartfull.controllers.HostServicesProvider;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.imageio.ImageIO;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

@SpringBootApplication
public class Main extends Application {

    private ConfigurableApplicationContext springContext;
    private Parent rootNode;

    private java.awt.SystemTray tray = null;
    private java.awt.TrayIcon trayIcon;
    private java.awt.Image iconeTray = null;
    private Image icone = null;
    public static String NOME = "Spring Boot, RestServer, JavaFx, TrayIcon";

    public static void main(final String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() throws Exception {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Main.class);
        builder.headless(false);
        springContext = builder.run();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        rootNode = fxmlLoader.load();
    }

    @Override
    public void start(Stage stage) throws Exception {

        HostServicesProvider.INSTANCE.init(getHostServices());

        this.stage = stage;
        stage.setTitle(NOME);
        stage.setScene(new Scene(rootNode));
        stage.show();

        icone = new Image("/fxml/l.png");
        iconeTray = ImageIO.read(getClass().getResource("/fxml/l.png")).getScaledInstance(16, 16, 16);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        stage.hide();
                    }
                });
            }
        });

        Platform.setImplicitExit(false);
        javax.swing.SwingUtilities.invokeLater(this::addAppToTray);

        Thread.sleep(500);
        stage.hide();

    }

    @Override
    public void stop() throws Exception {
        springContext.close();
    }


    private Stage stage;

    private void showStage() {
        if (stage != null) {
            stage.show();
            stage.toFront();
        }
    }

    private void addAppToTray() {
        try {
            java.awt.Toolkit.getDefaultToolkit();

            if (!java.awt.SystemTray.isSupported()) {
                System.out.println("Sistema operacional nao suporta system tray!");
            }

            tray = java.awt.SystemTray.getSystemTray();
            trayIcon = new java.awt.TrayIcon(iconeTray);

            trayIcon.addActionListener(event -> Platform.runLater(this::showStage));

            java.awt.MenuItem openItem = new java.awt.MenuItem("Maximizar " + NOME);
            openItem.addActionListener(event -> Platform.runLater(this::showStage));

            java.awt.Font defaultFont = java.awt.Font.decode(null);
            java.awt.Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);
            openItem.setFont(boldFont);

            java.awt.MenuItem exitItem = new java.awt.MenuItem("Sair");
            exitItem.addActionListener(event -> {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        Platform.exit();
                        System.exit(0);

                    }
                });

            });

            final java.awt.PopupMenu popup = new java.awt.PopupMenu();
            popup.add(openItem);
            popup.addSeparator();
            popup.add(exitItem);
            trayIcon.setPopupMenu(popup);

            MouseMotionListener mml = new MouseMotionListener() {
                public void mouseDragged(MouseEvent e) {
                }

                public void mouseMoved(MouseEvent e) {

                    trayIcon.setToolTip(NOME);
                }
            };
            trayIcon.addMouseMotionListener(mml);

            tray.add(trayIcon);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar system tray!");
        }
    }

}


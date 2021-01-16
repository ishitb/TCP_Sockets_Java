import javax.imageio.ImageIO;

import java.net.*;
import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * client
 */

// TEST CASE
// MATRIX -->
// 0   1   1   1   0
// 0   0   0   1   1
// 1   0   0   0   1
// 0   0   0   0   0
// 0   0   0   1   0
// REQUIRED PATH LENGTH --> 2
// SOURCE NODE --> C
// DESTINATION NODE --> D


// Class extending JPanel to draw the Graph GUI
class OutputPanel extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // Instatiating colors and fonts to include in the GUI
    static Color dark = new Color(41, 50, 65);
    static Color light = new Color(224, 251, 252);
    static Color accent1 = new Color(238, 108, 77);
    static Color accent2 = new Color(152, 193, 217);
    Font boldFont = new Font("Helvetica", Font.BOLD, 20);
    Font plainFont = new Font("Helvetica", Font.PLAIN, 20);

    String answer = "", sourceNode = "", destinationNode = "";
    int pathLength = 0;
    BufferedImage graphImage;

    // Constructor to take in the inputs 
    public OutputPanel(String answer, String source, String dest, int path, BufferedImage graphImage) {
        this.answer = answer;
        this.sourceNode = source;
        this.destinationNode = dest;
        this.pathLength = path;
        this.graphImage = graphImage;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Setting the background color
        this.setBackground(dark);

        Graphics2D g2d = (Graphics2D) g;

        // Setting the general font
        g2d.setFont(plainFont);

        // Computing the final answer string
        String finalAnswer = "";
        if (this.answer.equals("Y")) {
            finalAnswer = "Yes, there exists a path of length " + this.pathLength + " from node " + this.sourceNode
                    + " to node " + this.destinationNode;
        } else {
            finalAnswer = "No, there is no path of length " + this.pathLength + " from node " + this.sourceNode
                    + " to node " + this.destinationNode;
        }

        // Adding the graph image to the GUI
        g2d.drawImage(this.graphImage, 0, 120, this);

        // Adding the strings to GUI
        drawCenteredString(finalAnswer, "Final Answer", 1067, 50, g2d);
        drawCenteredString(Integer.toString(this.pathLength), "Required Path Length", 1067, 80, g2d);
        drawCenteredString(this.sourceNode, "Source Node", 1067, 110, g2d);
        drawCenteredString(this.destinationNode, "Destination Node", 1067, 140, g2d);
        drawCenteredString("", "Graph", 1067, 190, g2d);

    }

    // Function to draw a string center aligned horizontally
    public void drawCenteredString(String s, String heading, int w, int y, Graphics2D g2d) {
        heading = heading + ":  ";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (w - fm.stringWidth(s + heading)) / 2;

        g2d.setColor(accent2);
        g2d.setFont(this.boldFont);
        g2d.drawString(heading, x, y);

        g2d.setColor(accent1);
        g2d.setFont(this.plainFont);
        g2d.drawString(s, x + fm.stringWidth(heading) + 20, y);
    }

}

// Class extending JFrame to display the actual frame of GUI
class OutputFrame extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public OutputFrame(String answer, String source, String dest, int path, BufferedImage graphImage) {
        super("Graph Representation and Output");

        // Setting frame size
        this.setSize(1067, 1000);

        // Making frame to not be resizable
        this.setResizable(false);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adding the panel to the frame
        OutputPanel p = new OutputPanel(answer, source, dest, path, graphImage);
        this.setContentPane(p);

        this.setVisible(true);
    }
}

public class client {

    @SuppressWarnings("deprecation")
    public client(String address, int port) {
        try {
            // Initializing Socket with specified address and port
            Socket socket = new Socket(address, port);
            System.out.println(
                    "Connected to " + socket.getRemoteSocketAddress() + " :-\nServer: " + address + "Port: " + port);

            // Initializing input and output stream
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataInputStream clientInput = new DataInputStream(System.in);
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            int choice = 0;

            // Displaying menu to take in user's choice
            System.out.println("Choose an option:\n1. Check Path Program\n2. Exit Connection");
            System.out.print("Enter choice > ");
            choice = Integer.parseInt(clientInput.readLine());

            // Closing connection option
            if (choice == 2) {
                outputStream.writeUTF("close");
            }

            // Sending start signal to the server
            outputStream.writeUTF("start");

            // Taking in matrix input from user
            System.out.println("Enter the matrix:");
            for (int i = 0; i < 5; i++) {
                outputStream.writeUTF(clientInput.readLine());
            }

            // Taking in rewuired path length input from user
            System.out.println("Enter required length of path:");
            int requiredPathLength = Integer.parseInt(clientInput.readLine());
            outputStream.writeInt(requiredPathLength);

            // Taking in source node input from user
            System.out.println("Enter Source:");
            char sourceNode = clientInput.readLine().charAt(0);
            outputStream.writeChar(sourceNode);

            // Taking in destination node input from user
            System.out.println("Enter Destination:");
            char destinationNode = clientInput.readLine().charAt(0);
            outputStream.writeChar(destinationNode);

            // Reading answer from the server
            String answer = inputStream.readUTF();

            // Reading graph image from server
            BufferedImage readImage = ImageIO.read(inputStream);

            // Closing the connection
            outputStream.writeUTF("close");
            System.out.println("Connection closed!");
            inputStream.close();
            outputStream.close();
            socket.close();

            // Displaying the final output in Java GUI
            new OutputFrame(answer, Character.toString(sourceNode), Character.toString(destinationNode),
                    requiredPathLength, readImage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Instantiating a new client object with address - 127.0.0.1 and port - 3000
        new client("127.0.0.1", 3000);
    }
}

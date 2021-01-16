import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Server
 */

// Custom class Node to represent the 5 nodes of the graph
class Node {
    int xCenter; // x-coordinate of center
    int yCenter; // y-coorinate of center
    String nodeName; // Name of the node --> A, B, C, D, E

    public Node(int x, int y, String name) {
        this.xCenter = x;
        this.yCenter = y;
        this.nodeName = name;
    }

    // Returning xCenter
    public int getXCenter() {
        return this.xCenter;
    }

    // Returning yCenter
    public int getYCenter() {
        return this.yCenter;
    }

    // Returning nodeName
    public String getNodeName() {
        return this.nodeName;
    }
}

// Class extending JPanel to draw the Graph GUI
class GraphGUI extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // Instatiating colors to include in the GUI
    static Color dark = new Color(41, 50, 65);
    static Color light = new Color(224, 251, 252);
    static Color accent1 = new Color(238, 108, 77);
    static Color accent2 = new Color(152, 193, 217);

    int[][] matrix = new int[5][5];

    // Constructor to set the matrix
    public GraphGUI(int[][] matrix) {
        this.matrix = matrix;
    }

    // Predefined paint mathod which is used to draw the GUI
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        super.repaint();

        // Setting the background color
        this.setBackground(dark);

        Graphics2D g2d = (Graphics2D) g;

        // Adding background rectangle for image
        g2d.setColor(dark);
        g2d.fillRect(0, 0, 1067, 800);

        // Setting the general font
        g2d.setFont(new Font("Sans Serif", Font.BOLD, 25));

        // Making an array of the Node class with custom centers
        Node[] nodes = { 
            new Node(500, 100, "A"), 
            new Node(200, 350, "B"), 
            new Node(800, 350, "C"),
            new Node(300, 700, "D"), 
            new Node(700, 700, "E") 
        };

        // Array for links starting from each node with the x-y coordinated of the start
        // and end of the link
        // The empty elements are the ones never used but just to place the rest of the
        // elements in appropriate positions
        // FORMAT - x1, x2, y1, y2
        int[][][] linePoints = {
                { 
                    {}, 
                    { 504, 244, 142, 359 }, 
                    { 546, 806, 142, 359 }, 
                    { 516, 333, 151, 700 }, 
                    { 534, 716, 151, 700 } 
                },
                { 
                    {}, 
                    {}, 
                    { 250, 800, 375, 375 }, 
                    { 232, 317, 400, 700 }, 
                    { 247, 703, 391, 710 }, 
                },
                { 
                    {}, 
                    {}, 
                    {}, 
                    { 803, 347, 390, 709 }, 
                    { 817, 732, 400, 700 }, 
                },
                { 
                    {}, 
                    {}, 
                    {}, 
                    {}, 
                    { 350, 700, 725, 725 }, 
                } 
        };

        // Running a loop to draw all the 5 nodes
        for (int i = 0; i < 5; i++) {

            g2d.setColor(accent1);

            // Drawing a solid circle node with the given points and diameter 50px
            g2d.fillOval(nodes[i].getXCenter(), nodes[i].getYCenter(), 50, 50);

            g2d.setColor(light);

            // Giving a border to the nodes
            g2d.setStroke(new BasicStroke(3));
            g2d.drawOval(nodes[i].getXCenter(), nodes[i].getYCenter(), 50, 50);

            // Writing the node name
            g2d.drawString(nodes[i].getNodeName(), nodes[i].getXCenter() + 17, nodes[i].getYCenter() + 35);

        }

        g2d.setColor(accent2);
        g2d.setStroke(new BasicStroke(2));

        // Running a nested loop to draw the links between the nodes
        for (int i = 0; i < 5; i++) {
            for (int j = i + 1; j < 5; j++) {
                g2d.setColor(accent2);

                // A link is created if there is a connection between 2 nodes starting from
                // either of them
                if (matrix[i][j] == 1 || matrix[j][i] == 1)
                    g2d.drawLine(linePoints[i][j][0], linePoints[i][j][2], linePoints[i][j][1], linePoints[i][j][3]);

                g2d.setColor(light);

                // Adding arrow head to the link based on the condition where the link is
                // directed from
                if (matrix[i][j] == 1)
                    drawArrowHead((Graphics2D) g2d, linePoints[i][j][0], linePoints[i][j][2], linePoints[i][j][1],
                            linePoints[i][j][3]);

                // The link can be bi directional also
                if (matrix[j][i] == 1)
                    drawArrowHead((Graphics2D) g2d, linePoints[i][j][1], linePoints[i][j][3], linePoints[i][j][0],
                            linePoints[i][j][2]);
            }
        }
    }

    // Function to create arrow heads on the link
    private void drawArrowHead(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        double arrowAngle = Math.toRadians(30); // Setting an angle for the arrow
        double slopeAngle = Math.atan2(y2 - y1, x2 - x1); // Calculatin the angle of the slope of the line
        double arrowLength = 15; // Setting a length for the Arrow Head

        double angle1 = slopeAngle + arrowAngle; // Calculating angle for left Arrow Head
        double angle2 = slopeAngle - arrowAngle; // Calculating angle for right Arrow Head

        // Defining X and Y coordinate array for the arrow head
        // The first point has the coordinates of the link tail
        // The second and third points are calculated using the above angles
        double[] arrowPointsX = { x2, x2 - arrowLength * Math.cos(angle1), x2 - arrowLength * Math.cos(angle2) };
        double[] arrowPointsY = { y2, y2 - arrowLength * Math.sin(angle1), y2 - arrowLength * Math.sin(angle2) };

        // Declaring a path for the arrow head
        GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
        path.moveTo(arrowPointsX[0], arrowPointsY[0]);
        for (int i = 1; i < arrowPointsX.length; ++i) {
            path.lineTo(arrowPointsX[i], arrowPointsY[i]);
        }

        // Closing the path within the 3 points
        path.closePath();

        // Actually drawing and filling the path for Arrow Head
        g2d.fill(path);
        g2d.draw(path);

    }

}

// Class extending JFrame to display the actual frame of GUI
class GraphFrame extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public GraphFrame(int[][] graph, DataOutputStream outputStream) {

        // Setting name for the frame
        super("Graph Representation");

        // Adding a GraphGUI panel
        GraphGUI p = new GraphGUI(graph);

        // Setting frame size
        this.setSize(1067, 800);

        // Making frame to not be resizable
        this.setResizable(false);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adding the panel to the frame
        this.setContentPane(p);

        this.setVisible(false);

        // Calling function to send image to client
        sendPanelImage(p, outputStream);
    }

    // Function to send image to client
    private void sendPanelImage(Component panel, DataOutputStream outputStream) {
        // Instantiating a buffer image
        BufferedImage image = new BufferedImage(1067, 800, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        panel.paint(g2);
        try {
            // Sending the buffer image to the client
            ImageIO.write(image, "png", outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

public class server {

    public static boolean checkAllPaths(int graph[][], int pathLength, int source, int dest, boolean visited[]) {
        // Marking the source node as visited
        visited[source] = true;

        // Condition to check if the given source and destination nodes are linked
        // Then checking if the length of path between them is equal to the needed
        if (graph[source][dest] != 0 && pathLength == graph[source][dest])
            return true;

        // Running loop through each node of graph
        for (int i = 0; i < graph.length; i++) {

            // Checking if there is a link between the current node and source
            if (graph[source][i] != 0 && !visited[i]) {

                // Checking if there exists a subsequent path of the required length through
                // recursion
                if (checkAllPaths(graph, pathLength - graph[source][i], i, dest, visited))
                    return true;
            }
        }

        // Marking the current source as not visited so that it can be included in a
        // path from a different source
        visited[source] = false;

        // Returning false if there is no path or the path length is not as required
        return false;
    }

    // Function Assistance to check if the path is there or not
    public static String isPossible(int[][] matrix, int n, int s, int d) {
        boolean[] isVisited = new boolean[5];
        return checkAllPaths(matrix, n, s, d, isVisited) ? "Y" : "N";
    }

    public server(int port) {
        try {
            // Initializing the server with the specified port
            ServerSocket serv = new ServerSocket(port);

            // Setting a timeout for the server if the client doesn't connect
            serv.setSoTimeout(50000);

            System.out.println("Waiting for client on port " + port);

            // Connects to the client
            Socket socket = serv.accept();
            System.out.println("Client connected!");

            // Initializing input and output streams
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            int[][] matrix = new int[5][5];
            int requiredLength = 0, source = -1, destination = -1;

            // Reading input from the client
            String input = inputStream.readUTF();

            while (true) {

                // Condition to close the connection
                if (input.equals("close")) {
                    break;
                }

                // Starts the connection
                if (input.equals("start")) {

                    // Read the matrix from the client
                    for (int j = 0; j < 5; j++) {
                        String[] matrixRow = inputStream.readUTF().split(" ");

                        for (int i = 0; i < 5; i++) {
                            matrix[j][i] = Integer.parseInt(matrixRow[i]);
                        }
                    }

                    // Read the required path length from the client
                    requiredLength = inputStream.readInt();

                    // Read the source node from the client and converting to char
                    source = (int) inputStream.readChar() - 65;

                    // Read the destination node from the client and converting to char
                    destination = (int) inputStream.readChar() - 65;

                    // Writing the final answer to the client
                    outputStream.writeUTF(isPossible(matrix, requiredLength, source, destination));

                    // Making the GUI for the Graph
                    new GraphFrame(matrix, outputStream);
                }

                input = inputStream.readUTF();

            }

            // Closing the connection
            System.out.println("Connection Closed!");
            inputStream.close();
            serv.close();
            socket.close();

        } catch (SocketTimeoutException s) {
            System.out.println("Socket timed out!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Instantiating a new server object with port - 3000
        new server(3000);
    }
}
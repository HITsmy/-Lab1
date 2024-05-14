
import org.example.GraphOperations;
import org.example.Util;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class test {
    public static void main(String[] args) throws IOException, InterruptedException, AWTException {
        // 创建一个图
        Graph graph = new MultiGraph("My Graph");
        graph.addNode("A");
        graph.addNode("B");
        graph.addEdge("AB", "A", "B");

        // 显示图
        Viewer viewer = graph.display();
        View view =  viewer.getDefaultView();

        // 等待视图渲染完成（这里只是一个简单的等待，实际中可能需要更复杂的逻辑）
        Thread.sleep(1000);

        // 计算Viewer组件的大小和屏幕位置
        Rectangle bounds = view.getBounds();
        Point locationOnScreen = view.getLocationOnScreen();

        // 创建一个Robot对象
        Robot robot = new Robot();

        // 创建一个BufferedImage来保存截图
        BufferedImage image = robot.createScreenCapture(new Rectangle(locationOnScreen.x, locationOnScreen.y, bounds.width, bounds.height));

        // 保存图像到文件
        ImageIO.write(image, "png", new File("graph.png"));

        // 关闭Viewer（如果需要）
        viewer.close();

        System.out.println("Graph saved as image.");


    }
}


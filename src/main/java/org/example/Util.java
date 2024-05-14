package org.example;

import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Util {
    /**
     *
     * @param filename 文件名路径
     * @return 返回读取文件读取的字符串
     */
    public static StringBuilder read(String filename) throws IOException {
        FileReader fileReader = new FileReader(filename);
        BufferedReader reader = new BufferedReader(fileReader);
        StringBuilder textContent = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.replaceAll("\r\n", "");
            line = line.replaceAll("\n", " ");
            line = line.replaceAll("[^a-zA-Z' ']", " ");
            line = line.toLowerCase();
            textContent.append(line);
        }
        return textContent;
    }

    /**
     *
     * @param viewer 视图
     * @param filename 保存的文件名
     */
    public static void draw(Viewer viewer, String filename) throws InterruptedException, AWTException, IOException {
        View view =  viewer.getDefaultView();

        // 等待视图渲染完成（这里只是一个简单的等待，实际中可能需要更复杂的逻辑）
        Thread.sleep(5000);

        // 计算Viewer组件的大小和屏幕位置
        Rectangle bounds = view.getBounds();
        Point locationOnScreen = view.getLocationOnScreen();

        // 创建一个Robot对象
        Robot robot = new Robot();

        // 创建一个BufferedImage来保存截图
        BufferedImage image = robot.createScreenCapture(new Rectangle(locationOnScreen.x, locationOnScreen.y, bounds.width, bounds.height));

        // 保存图像到文件
        ImageIO.write(image, "png", new File(filename));

        System.out.println("Graph saved as image.");
    }

}

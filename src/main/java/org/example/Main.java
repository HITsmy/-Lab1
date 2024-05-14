package org.example;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.layout.Layouts;
import org.graphstream.ui.swingViewer.Viewer;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class Main {
    private static HashMap<String ,Node> nodeHashMap = new HashMap<>();
    private static HashMap<String, Edge> edgeHashMap = new HashMap<>();
    public static void main(String[] args) throws IOException, InterruptedException, AWTException {

        // 初始化空的有向图
        Graph graph = new MultiGraph("DirectedGraph");
        graph.setStrict(false);

        // 读取文件并切分字符串得到单词数组
        StringBuilder textContent = Util.read("src/main/java/org/example/text/text.txt");
        String[] words = textContent.toString().split( "\\s+");
        for (String word : words) {
            System.out.println(word);
        }

        // 根据单词数组生成有向图
        graph = GraphOperations.generateGraph(words);
        System.out.println();

        // 展示有向图
        Viewer viewer = graph.display();
        // 使用图布局算法
        viewer.enableAutoLayout(Layouts.newLayoutAlgorithm());
        Util.draw(viewer,"src/main/java/org/example/png/graph.png");

    }
}
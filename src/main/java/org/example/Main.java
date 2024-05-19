package org.example;
import org.example.Util;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.layout.Layouts;
import org.graphstream.ui.swingViewer.Viewer;
import org.example.GraphOperations;

import java.awt.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, AWTException {

        // 初始化空的有向图
        Graph graph = new MultiGraph("DirectedGraph");
        graph.setStrict(false);

        // 读取文件并切分字符串得到单词数组
        StringBuilder textContent = Util.read("src/main/java/org/example/text/text.txt");
        String[] words = textContent.toString().split( "\\s+");
        System.out.println("---- The words are: ----");
        for (int i=0; i<words.length; i++) {
            System.out.print(words[i]);
            if (i < words.length - 1)
                System.out.print(", ");
        }
        System.out.println("\n\n");

        // 根据单词数组生成有向图
        graph = GraphOperations.generateGraph(words);


        // 初始化工具
        Util.InitTools(GraphOperations.nodeHashMap.keySet(), GraphOperations.edgeHashMap.keySet(), graph);

        // 计算最短路径
        System.out.println("---- Shortest Path: ----");
        GraphOperations.calcShortestPath("new", "explore");
        Viewer viewer = graph.display();
        Util.draw(viewer, "src/main/java/org/example/png/shortest_distance.png");
        System.out.println("\n\n");

        // 生成文本
        System.out.println("---- Generate Text: ----");
        GraphOperations.generateNewText("seek to explore new and exciting synergies");
        System.out.println("\n\n");

        // 随机游走
        System.out.println("---- Random Walk: ----");
        GraphOperations.randomWalk("randomWalk");
        System.out.println("\n\n");

    }
}

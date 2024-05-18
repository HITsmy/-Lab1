package org.example.Tool;
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
        System.out.println(textContent);
        String[] words = textContent.toString().split( "\\s+");
        for (String word : words) {
           // System.out.println(word);
        }

        // 根据单词数组生成有向图
        graph = GraphOperations.generateGraph(words);
        System.out.println();
        Util util = new Util();
        // 初始化工具
        util.InitTools(GraphOperations.nodeHashMap.keySet(), GraphOperations.edgeHashMap.keySet(), graph);

        //GraphOperations.searchWord(util, "strange");
        //GraphOperations.queryBridgeWords(util, "to","strange");
        //System.out.println(GraphOperations.generateNewText("TTo strange worlds to seek out life new",util));
        //System.out.println(GraphOperations.randomWalk(util,"test1.txt"));
        GraphOperations.calcShortestPath("explore","new");

        // 展示有向图
        Viewer viewer = graph.display();



        // 使用图布局算法
        viewer.enableAutoLayout(Layouts.newLayoutAlgorithm());
        Util.draw(viewer,"src/main/java/org/example/png/graph.png");

    }
}


import org.example.GraphOperations;
import org.example.Tool.edge;
import org.example.Util;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.layout.Layouts;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;



public class test {
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


        Util.InitTools(GraphOperations.nodeHashMap.keySet(), GraphOperations.edgeHashMap.keySet(), graph);
        System.out.println(Util.nodes);
        for (Map.Entry<edge, Integer> entry : Util.weights.entrySet())
            System.out.println(entry.getKey().src+"->"+entry.getKey().dst+"       :"+entry.getValue());

        for (Map.Entry<String, List<edge> > entry : Util.links.entrySet())
            System.out.println(entry.getKey()+"      :" +entry.getValue().size());



    }
}


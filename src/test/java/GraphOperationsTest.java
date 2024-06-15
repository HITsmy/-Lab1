import junit.framework.TestCase;
import org.example.GraphOperations;
import org.example.Util;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.junit.Test;

import java.io.IOException;

public class GraphOperationsTest extends TestCase {
    @Test
    public void testCalcShortestPath() throws IOException, IOException {
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
        //初始化
        util.InitTools(GraphOperations.nodeHashMap.keySet(), GraphOperations.edgeHashMap.keySet(), graph);
        System.out.println(GraphOperations.calcShortestPath("Asd","bsd"));
    }

    @Test
    public void testQueryBridgeWords() throws IOException {
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
        //初始化
        util.InitTools(GraphOperations.nodeHashMap.keySet(), GraphOperations.edgeHashMap.keySet(), graph);
        GraphOperations.queryBridgeWords("t o","explore");

    }

}

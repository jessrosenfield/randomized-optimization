package org.jrosenfield3;

public class PrettyGraph {

    protected AdjacencyMatrix matrix;
    protected AdjacencyMatrix.Edge[] edges;
    protected int numVertices;
    protected int max;

    public PrettyGraph() {
        numVertices = 24;
        max = 500;
        matrix = new AdjacencyMatrix(numVertices);
        matrix.addEdge(0, 1);   //A, B
        matrix.addEdge(1, 2);   //B, C
        matrix.addEdge(2, 3);   //C, D
        matrix.addEdge(3, 4);   //D, E
        matrix.addEdge(4, 5);   //E, F
        matrix.addEdge(5, 6);   //F, G
        matrix.addEdge(6, 7);   //G, H
        matrix.addEdge(7, 0);   //H, A
        matrix.addEdge(8, 9);   //I, J
        matrix.addEdge(9, 10);  //J, K
        matrix.addEdge(10, 11); //K, L
        matrix.addEdge(11, 12); //L, M
        matrix.addEdge(12, 13); //M, N
        matrix.addEdge(13, 14); //N, O
        matrix.addEdge(14, 15); //O, P
        matrix.addEdge(15, 8);  //P, I
        matrix.addEdge(16, 17); //Q, R
        matrix.addEdge(17, 18); //R, S
        matrix.addEdge(18, 19); //S, T
        matrix.addEdge(19, 20); //T, U
        matrix.addEdge(20, 21); //U, V
        matrix.addEdge(21, 22); //V, W
        matrix.addEdge(22, 23); //W, X
        matrix.addEdge(23, 16); //X, Q
        matrix.addEdge(0, 8);   //A, I
        matrix.addEdge(1, 9);   //B, J
        matrix.addEdge(2, 10);  //C, K
        matrix.addEdge(3, 11);  //D, L
        matrix.addEdge(4, 12);  //E, M
        matrix.addEdge(5, 13);  //F, N
        matrix.addEdge(6, 14);  //G, O
        matrix.addEdge(7, 15);  //H, P
        matrix.addEdge(8, 16);  //I, Q
        matrix.addEdge(9, 17);  //J, R
        matrix.addEdge(10, 18); //K, S
        matrix.addEdge(11, 19); //L, T
        matrix.addEdge(12, 20); //M, U
        matrix.addEdge(13, 21); //N, V
        matrix.addEdge(14, 22); //O, W
        matrix.addEdge(15, 23); //P, X
        edges = matrix.getEdges();
    }

}
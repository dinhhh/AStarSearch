package PathFinding;

public class Node {

    // parent node coordinates
    public Integer parent_i, parent_j;
    // f = g + h
    public Double f, g, h;

    public Node() {

        this.parent_i = null;
        this.parent_j = null;

        this.f = null;
        this.g = null;
        this.h = null;

    }

    public Node(Integer parenti, Integer parentj, Double f, Double g, Double h) {
        this.parent_i = parenti;
        this.parent_j = parentj;
        this.f = f;
        this.g = g;
        this.h = h;
    }

}

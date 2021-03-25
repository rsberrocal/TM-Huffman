public class BST {
    static class Node{
        double value;
        String key;
        Node left;
        Node right;

        Node(double value, String key){
            this.value = value;
            this.key = key;
            right = null;
            left = null;
        }

        public boolean isLeaf() {
            return this.right == null && this.left == null;
        }
    }

    Node root;

    public BST(double value, String key){
        this.root = new Node(value, key);
    }

    public void insert(Node root, Node s1, Node s2){
        if (root.isLeaf()){
            if (s1.value > s2.value){
                root.right = s1;
                root.left = s2;
            } else {
                root.right = s2;
                root.left = s1;
            }
        } else {
            if ((root.left.key).contains(s1.key + s2.key)){
                insert(root.left, s1,s2);
            } else if ((root.right.key).contains(s1.key + s2.key)){
                insert(root.right, s1,s2);
            }
        }
    }

    public String parseBinary(Node root, String key){
        if (root.key.equals(key)){
            return "";
        } else {
            if (root.left.key.contains(key)){
                return "0" + parseBinary(root.left, key);
            } else {
                return "1" + parseBinary(root.right, key);
            }
        }
    }

}

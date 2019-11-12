package net.dolpen.mcmod.lib.math.tree;

public class TreeNode<T extends Comparable> {
    private T data;
    private TreeNode<T> left;
    private TreeNode<T> right;

    TreeNode(T obj) {
        left = right = null;
        data = obj;
    }

    public TreeNode<T> getRight() {
        return right;
    }

    public void setRight(TreeNode<T> right) {
        this.right = right;
    }

    public TreeNode<T> getLeft() {
        return left;
    }

    public void setLeft(TreeNode<T> left) {
        this.left = left;
    }

    public T getData() {
        return data;
    }
}

package net.dolpen.mcmod.lib.math.tree;

import java.util.Objects;

public class BinarySearchTree<T extends Comparable> {
    private TreeNode<T> root = null;

    public void add(T obj) {
        TreeNode<T> insert = new TreeNode<>(obj);
        if (Objects.isNull(root)) {
            root = insert;
            return;
        }
        TreeNode<T> current = root;
        while (true) {
            int x = current.getData().compareTo(obj);
            if (x > 0) {
                if (Objects.isNull(current.getLeft())) {
                    current.setLeft(insert);
                    return;
                }
                current = current.getLeft();
            } else {
                if (Objects.isNull(current.getRight())) {
                    current.setRight(insert);
                    return;
                }
                current = current.getRight();
            }
        }
    }


    public boolean contains(T obj) {
        // 現在ノードをルートノードとする
        TreeNode<T> current = root;
        // 次のノードが存在しない場合は探索終了
        while (Objects.nonNull(current)) {
            int x = current.getData().compareTo(obj);
            if (x == 0) return true;
            if (x > 0) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }
        return false;
    }
}
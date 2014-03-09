/**
 * Copyright 2012 Yahoo! Inc. Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License. 
 * See accompanying LICENSE file.
 */
package triemap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;


/**
 * You can store a set of integers(each considered a partial key) as key and 
 * anything as value. 
 * Ex: 
 * {0,1,3,4}, "sam"  
 * {0,1,3,5}, "hahaha" 
 * {1,3,4}, "dummy" 
 * {32,4},"dam" 
 * {4,4}, "cam"
 *
 * @author srikalyc
 * @param <K>
 * @param <V> value type.
 */
public class TrieMap<K extends Comparable<K>,V> {

    final TreeNode root = new TreeNode();
    int size = 0;
    
    // The following 2 variables are purely for iteration purposes.
    private final Stack<K> partialKeyStack = new Stack();
    private final Map<List<K>,V> keyListAndValues = new HashMap<>();
    
    public void printKeyValueEntries() {
        iterEntries(root, true);
    }
    public Map<List<K>,V> getKeyValueEntries() {
        iterEntries(root, false);
        return keyListAndValues;
    }
    /**
     * Recursive in nature.
     * @param t
     * @param print
     * @return 
     */
    private void iterEntries(TreeNode t, boolean print) {
        Iterator<TreeNode> iter = t.iterator();
        while (iter.hasNext()) {
            TreeNode treeItem = iter.next();
            boolean touched = false;
            if (treeItem.data != null) {
                touched = true;
                partialKeyStack.push(treeItem.data.partialKey);
                if (treeItem.data.value != null) {
                    List<K> key = new ArrayList<>();
                    for (int i = 0;i < partialKeyStack.size();i++) {
                        key.add(partialKeyStack.get(i));
                    }
                    keyListAndValues.put(key, treeItem.data.value);
                    if (print) {
                        System.out.println(partialKeyStack + "," + treeItem.data.value);
                    }
                }
            }
            if (treeItem.data != null && treeItem.data.child != null) {
                iterEntries(treeItem.data.child, print);
            }
            if (touched && !partialKeyStack.isEmpty()) {// If untouched do not pop!!.
                partialKeyStack.pop();
                touched = false;
            }
        }
        //partialKeyStack.clear();
    }
    /**
     * Used for both adding and updating. sI and eI are the number of elements 
     * from key which should be considered as key. The value is added to the tail.
     *
     * @param key
     * @param sI (start index)
     * @param eI (end index)
     * @param value
     */
    public void add(K[] key, int sI, int eI, V value) {
        TrieNode lastNode = root.get(key[sI]);

        if (lastNode == null) {
            lastNode = new TrieNode(key[sI], null);
            root.add(lastNode);
        }
        TrieNode curNode = lastNode;
        for (int i = sI + 1; i < eI; i++) {
            curNode = lastNode.getChild(key[i]);
            if (curNode == null) {
                curNode = lastNode.addChild(key[i], null);
                if (i == eI - 1) {
                    size++;// Only when you are adding newly increase the size.
                }
            }
            lastNode = curNode;
        }
        if (sI == eI - 1) {// Because the loop is never entered we take care of the edge case here.
            size++;// Only when you are adding newly increase the size.
        }
        curNode.value = value;
    }
    /**
     * Used for both adding and updating. sI and eI are the number of elements 
     * from key which should be considered as key. The value is added all along
     * the path until the tail(all the prefixes and suffixes of the key have 
     * values set now).
     * If there are 'm' elements in key then this method is O(m2) in time.
     * @param key
     * @param sI
     * @param eI
     * @param value
     */
    public void addAll(K[] key, int sI, int eI, V value) {
        TrieNode lastNode = root.get(key[sI]);

        if (lastNode == null) {
            lastNode = new TrieNode(key[sI], value);
            root.add(lastNode);
            size++;// Only when you are adding newly increase the size.
        }
        TrieNode curNode = lastNode;
        for (int i = sI + 1; i < eI; i++) {
            addPrefixes(key, i, eI, value);// Add the suffixes to the path.
            curNode = lastNode.getChild(key[i]);
            if (curNode == null) {
                curNode = lastNode.addChild(key[i], value);
                size++;// Only when you are adding newly increase the size.
            } else {
                curNode.value = value;
            }
            lastNode = curNode;
        }
        curNode.value = value;
    }
    
    /**
     * Same as add() method but sets the value only when it is the first time.
     *
     * @param key
     * @param sI (start index)
     * @param eI (end index)
     * @param value
     */
    public void addIfNull(K[] key, int sI, int eI, V value) {
        TrieNode lastNode = root.get(key[sI]);

        if (lastNode == null) {
            lastNode = new TrieNode(key[sI], null);
            root.add(lastNode);
        }
        TrieNode curNode = lastNode;
        for (int i = sI + 1; i < eI; i++) {
            curNode = lastNode.getChild(key[i]);
            if (curNode == null) {
                curNode = lastNode.addChild(key[i], null);
                if (i == eI - 1) {
                    size++;// Only when you are adding newly increase the size.
                }
            }
            lastNode = curNode;
        }
        if (sI == eI - 1) {// Because the loop is never entered we take care of the edge case here.
            if (curNode.value == null)// Because we will not change the value if value is not null.
                size++;// Only when you are adding newly increase the size.
        }
        if (curNode.value == null) {
            curNode.value = value;
        }
    }

    /**
     * Used for both adding and updating. sI and eI are the number of elements 
     * from key which should be considered as key. The value is added all along
     * the path until the tail(all the prefixes of the key have values set now).
     * If there are 'm' elements in key then this method is O(m) in time.
     *
     * @param key
     * @param sI
     * @param eI
     * @param value
     */
    public void addPrefixes(K[] key, int sI, int eI, V value) {
        TrieNode lastNode = root.get(key[sI]);

        if (lastNode == null) {
            lastNode = new TrieNode(key[sI], value);
            root.add(lastNode);
            size++;// Only when you are adding newly increase the size.
        }
        TrieNode curNode = lastNode;
        for (int i = sI + 1; i < eI; i++) {
            curNode = lastNode.getChild(key[i]);
            if (curNode == null) {
                curNode = lastNode.addChild(key[i], value);
                size++;// Only when you are adding newly increase the size.
            } else {
                curNode.value = value;
            }
            lastNode = curNode;
        }
        curNode.value = value;
    }
    /**
     * Check if key exists.
     *
     * @param key
     * @param sI
     * @param eI
     * @return
     */
    public boolean contains(K[] key, int sI, int eI) {
        TrieNode lastNode = root.get(key[sI]);
        TrieNode curNode = null;
        if (lastNode == null) {
            return false;
        }
        for (int i = sI + 1; i < eI; i++) {
            curNode = lastNode.getChild(key[i]);
            if (curNode == null) {
                return false;
            }
            lastNode = curNode;
        }
        return true;
    }
    

    /**
     * Return value corresponding to key if exists else null is returned.
     *
     * @param key
     * @param sI
     * @param eI
     * @return
     */
    public V get(K[] key, int sI, int eI) {
        TrieNode lastNode = root.get(key[sI]);
        TrieNode curNode = lastNode;
        if (lastNode == null) {
            return null;
        }
        for (int i = sI+1; i < eI; i++) {
            curNode = lastNode.getChild(key[i]);
            if (curNode == null) {
                return null;
            }
            lastNode = curNode;
        }
        return curNode.value;
    }
    /**
     * If key if exists delete the element.
     * @param key
     * @param sI
     * @param eI
     * @return 
     */
    public boolean remove(K[] key, int sI, int eI) {
        TrieNode lastNode = root.get(key[sI]);
        TrieNode curNode = lastNode;
        if (lastNode == null) {
            return false;
        }
        for (int i = sI+1; i < eI; i++) {
            curNode = lastNode.getChild(key[i]);
            if (curNode == null) {
                return false;
            }
            lastNode = curNode;
        }
        curNode.remove();
        size--;
        return true;
    }

    /**
     * Number of trie entries.
     *
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * Use this method to add when all the elements in the key are part of the
     * key.
     *
     * @param key
     * @param value
     */
    public void add(K[] key, V value) {
        add(key, 0, key.length, value);
    }

    /**
     * Same as add() method but sets the value only when it is the first time.
     * @param key
     * @param value 
     */
    public void addIfNull(K[] key, V value) {
        addIfNull(key, 0, key.length, value);
    }
    /**
     * Use this method to add when all the elements in the key are part of the
     * key and value has to be added all along the path(i.e prefixes).
     *
     * @param key
     * @param value
     */
    public void addPrefixes(K[] key, V value) {
        addPrefixes(key, 0, key.length, value);
    }

    /**
     * Use this method to add when all the elements in the key are part of the
     * key and value has to be added to all suffixes and prefixes.
     *
     * @param key
     * @param value
     */
    public void addAll(K[] key, V value) {
        addAll(key, 0, key.length, value);
    }
    /**
     * Use this method to search when all the elements in the key are part of
     * the key.
     *
     * @param key
     * @return
     */
    public boolean contains(K[] key) {
        return contains(key,0, key.length);
    }

    /**
     * Use this method to fetch value when all the elements in the key are part
     * of the key.
     *
     * @param key
     * @return
     */
    public V get(K[] key) {
        return get(key,0, key.length);
    }
    /**
     * Use this method to delete when all the elements in the key are part
     * of the key. Please NOTE that remove is a wild card remove which means that
     * if 
     * [{1,2,3},"A"]
     * [{1,2,3,4},"B"]
     * [{1,2,3,5},"C"]
     * and if you delete {1,2,3} then {1,2,3,4} and {1,2,3,5} will also be deleted.
     * @param key
     * @return 
     */
    public boolean remove(K[] key) {
        return remove(key,0, key.length);
    }

/**
 * TrieNode has partial key,value(may be null) and additional child which is a TreeNode.
 * @param <V> 
 */
    class TrieNode implements Comparable<TrieNode> {
        K partialKey;
        V value;
        /**
         * The other part of the whole key(suffix part) can be searched by going 
         * deep into the child tree.
         */
        TreeNode child = null;

        public TrieNode(K partialKey, V value) {
            this.partialKey = partialKey;
            this.value = value;
            child = new TreeNode();
        }
        public TrieNode getChild(K partialKey) {
            if (child != null)
                return child.get(partialKey);
            return null;
        }

        public TrieNode addChild(K partialKey, V value) {
            TrieNode node = getChild(partialKey);
            if (node == null) {
                node = new TrieNode(partialKey, value);
                child.add(node);
            } else {
                node.value = value;
            }
            return node;
        }
        
        public void remove() {
            value = null;
            child = null;
        }

        @Override
        public int compareTo(TrieNode otherTNode) {
            return partialKey.compareTo(otherTNode.partialKey);
        }

        @Override
        public String toString() {
            return "[" +partialKey + "," +value.toString() + "]";
        }

    }
    /**
     * Special Binary tree.
     * @param <V> 
     */
    class TreeNode implements Iterable<TreeNode>{

        TreeNode left;
        TreeNode right;
        
        TrieNode data;
        public void print() {
            if (data != null) {
                System.out.print("<" +data.partialKey + "," +data.value+ ">");
            }
            if (left != null) {
                left.print();
            }
            if (right != null) {
                right.print();
            }
        }
        public void add(TrieNode data) {
            if (this.data == null) {
                this.data = data;
                return;
            }
            if (data.compareTo(this.data) < 0) {
                if (left == null) {
                    left = new TreeNode();
                }
                left.add(data);
            } else {
                if (right == null) {
                    right = new TreeNode();
                }
                right.add(data);
            }
        }

        /**
         * This method is the exact reason we implemented a TreeNode and did not use
         * TreeNodeSet(you do not have get() in TreeNodeSet as it does not make sense) 
         * but in our case we should be able to search the TrieNode based on a part of 
         * the TrieNode i.e a key.
         *
         * @param partialKey
         * @return
         */
        public TrieNode get(K partialKey) {
            if (data == null) {
                return null;
            }
            if (data.partialKey.compareTo(partialKey) == 0) {
                return data;
            }
            if (partialKey.compareTo(data.partialKey) < 0) {
                if (left != null) {
                    return left.get(partialKey);
                }
            } else {
                if (right != null) {
                    return right.get(partialKey);
                }
            }
            return null;
        }

        private TreeNode currentIterNode = this;
        private final Stack<TreeNode> iterStack = new Stack<>();
        /**
         * If there is an iterator open already its next()/hasNext() will 
         * malfunction when this is called again.
         * @return 
         */
        @Override
        public Iterator<TreeNode> iterator() {
            currentIterNode = this;
            iterStack.clear();
            Iterator<TreeNode> iter = new Iterator<TreeNode>() {
                @Override
                public void remove() {
                    //TODO: Implement something here please.
                }
                boolean firstAccess = true;
                /**
                 * Part of the effort to enable inorder traversal without recursion.
                 */
                private void gotoLeftMost() {
                    while (currentIterNode != null) {
                        iterStack.push(currentIterNode);
                        currentIterNode = currentIterNode.left;
                    }
                }
                @Override
                public boolean hasNext() {
                    if (firstAccess) {
                        firstAccess = false;
                    }
                    gotoLeftMost();
                    return !iterStack.isEmpty();
                }
/**
 * Fetches elements in an inorder fashion.
 * @return 
 */
                @Override
                public TreeNode next() {
                    if (firstAccess) {
                        gotoLeftMost();
                        firstAccess = false;
                    }
                    TreeNode poppedItem = null;
                    if (!iterStack.isEmpty()) {
                        poppedItem = iterStack.pop();
                        if (poppedItem.right != null) {
                            currentIterNode = poppedItem.right;
                            gotoLeftMost();
                        }
                    }
                    return poppedItem;
                }
                
            };
            return iter;
        }
    }

    
}

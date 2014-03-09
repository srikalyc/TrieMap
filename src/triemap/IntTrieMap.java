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

/**
 * One can simply use NumTrieMap but this class is defined so that one can use
 * primitive arrays instead of the wrapper types.
 * @author srikalyc
 */
public class IntTrieMap extends NumTrieMap<Integer> {
    /**
     * Used for both adding and updating. sI and eI are the number of elements 
     * from key which should be considered as key. The value is added to the tail.
     * The current value is added by given "value".
     *
     * @param key
     * @param sI (start index)
     * @param eI (end index)
     * @param value
     */
    public void inc(int[] key, int sI, int eI, int value) {
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
        curNode.value += value;
    }

    /**
     * Used for both adding and updating. sI and eI are the number of elements 
     * from key which should be considered as key. The value is added all along
     * the path until the tail(all the prefixes of the key have values set now).
     * If there are 'm' elements in key then this method is O(m) in time.
     * The current value along the path is added by given "value".
     * @param key
     * @param sI
     * @param eI
     * @param value
     */
    public void incPrefixes(int[] key, int sI, int eI, int value) {
        TrieNode lastNode = root.get(key[sI]);

        if (lastNode == null) {
            lastNode = new TrieNode(key[sI], value);
            root.add(lastNode);
            size++;// Only when you are adding newly increase the size.
        } else {
            lastNode.value += value;
        }
        TrieNode curNode = lastNode;
        for (int i = sI + 1; i < eI; i++) {
            curNode = lastNode.getChild(key[i]);
            if (curNode == null) {
                curNode = lastNode.addChild(key[i], value);
                size++;// Only when you are adding newly increase the size.
            } else {
                curNode.value += value;
            }
            lastNode = curNode;
        }
    }
    /**
     * Used for both adding and updating. sI and eI are the number of elements 
     * from key which should be considered as key. The value is added all along
     * the path until the tail(all the prefixes and suffixes of the key have 
     * values set now). Each current value is added by given "value".
     * If there are 'm' elements in key then this method is O(m2) in time.
     * @param key
     * @param sI
     * @param eI
     * @param value
     */
    public void incAll(int[] key, int sI, int eI, int value) {
        TrieNode lastNode = root.get(key[sI]);

        if (lastNode == null) {
            lastNode = new TrieNode(key[sI], value);
            root.add(lastNode);
            size++;// Only when you are adding newly increase the size.
        } else {
            lastNode.value += value;
        }
        TrieNode curNode = null;
        for (int i = sI + 1; i < eI; i++) {
            incPrefixes(key, i, eI, value);// Add the suffixes to the path.
            curNode = lastNode.getChild(key[i]);
            if (curNode == null) {
                curNode = lastNode.addChild(key[i], value);
                size++;// Only when you are adding newly increase the size.
            } else {
                curNode.value += value;
            }
            lastNode = curNode;
        }
    }
    /**
     * Entire array is used as key.
     * @param key
     * @param value 
     */
    public void inc(int[] key, int value) {
        inc(key, 0, key.length, value);
    }
    /**
     * Entire array is used as key.
     * @param key
     * @param value 
     */
    public void incPrefixes(int[] key, int value) {
        incPrefixes(key, 0, key.length, value);
    }
    /**
     * Entire array is used as key.
     * @param key
     * @param value 
     */
    public void incAll(int[] key, int value) {
        incAll(key, 0, key.length, value);
    }
    
}

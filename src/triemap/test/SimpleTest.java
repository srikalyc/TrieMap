/**
 * Copyright 2014 Srikalyan Chandrashekar. Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License. 
 * See accompanying LICENSE file.
 */
package triemap.test;

import triemap.NumTrieMap;

/**
 * Simple unit tests. Please NOTE validation is not done.
 *
 * @author srikalyc
 */
public class SimpleTest {

    public static void main(String[] args) {
        SimpleTest test = new SimpleTest();
        test.test();
    }

    public void test() {

//        NumTrieMap trie2 = new NumTrieMap();
//        trie2.incAll(new Integer[]{3, 2, 1, 4}, 1);
//        trie2.incAll(new Integer[]{3, 2, 1}, 1);
//        trie2.incAll(new Integer[]{2, 1, 3, 2, 1, 4}, 1);
//        System.out.println(trie2.get(new Integer[]{3,2}));
//        trie2.printCallSeqEntries();

        NumTrieMap trie = new NumTrieMap();
        trie.incAll(new Integer[]{0,1}, 1);
        trie.incAll(new Integer[]{0,2,3}, 1);
        System.out.println(trie.get(new Integer[]{0,2,3}));
        System.out.println(trie.get(new Integer[]{0}));
        System.out.println(trie.get(new Integer[]{0,1}));
        System.out.println(trie.get(new Integer[]{2,3}));
        trie.printKeyValueEntries();
        
//        ByteTrieMap trie2 = new ByteTrieMap();
//        trie2.incAll(new byte[]{0,1}, 1);
//        trie2.incAll(new byte[]{0,2,3}, 1);
////        System.out.println(trie2.get(new Byte[]{3,2}));
//        trie2.printCallSeqEntries();
    }

}

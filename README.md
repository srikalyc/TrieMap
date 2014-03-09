———————————————————————————————————————————

Copyright 2012 Yahoo! Inc. Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. You may obtain a copy
 of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by 
applicable law or agreed to in writing, software distributed under the License 
is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
either express or implied. See the License for the specific language governing 
permissions and limitations under the License. See accompanying LICENSE file.

———————————————————————————————————————————

—————————————— About the TrieMap ——————————————

- This is an all Java TrieMap which takes an array of integers as key and any object as value.
-  You can store a set of integers(each considered a partial key) as key and anything as value. 
 Ex: 
  {0,1,3,4}, "sam"  
  {0,1,3,5}, "hahaha" 
  {1,3,4}, "dummy" 
  {32,4},"dam" 
  {4,4}, "cam"

——————— How to use as Associative Array —————————

1) For small data loads
- Simply use add/remove/get/contains methods whose 2nd argument is not comboSize.

2) For heavy data loads
- Define an array of int []arr = new int[256];
- Reuse this array as key and also do not forget to pass the start and end index of elements of the array(2nd and 3rd arguments to add/remove/contains/get methods) which form the key. This pattern is fly weight and Garbage collector just loves it.

—————————————— About the AllIntTrieMap ——————————————

- This is an all Java TrieMap which takes an array of integers as key and int as value.
- You can store a set of integers(each considered a partial key) as key and int value. 
 Ex: 
  {0,1,3,4}, 1  
  {0,1,3,5}, 1 
  {1,3,4}, 1 

- It has special methods as well namely 
 inc() -  Increments the current value at the tail.
 incPrefixes() - Increments the value all along the path.
 incAll() - Increments the value of suffixes of all the prefixes.



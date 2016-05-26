/*
   Copyright 2016 Cyberian Tiger

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package org.cyberiantiger.example.stringsets.model.longestchain;

import java.util.List;

/**
 * Class to represent a node in a directed graph of strings who's outgoing 
 * links are based on the last character of the string value of this node, and
 * the first character of the linked StringNode.
 * 
 * @author antony
 */
public class StringNode {
    private final String data;
    private List<StringNode> children;

    /**
     * Create a StringNode for the specified string.
     * @param data the string data for the node.
     */
    public StringNode(String data) {
        this.data = data;
    }

    /**
     * Get the String data for this node.
     * @return the string data.
     */
    public String getData() {
        return data;
    }

    /**
     * Set the children for this node, that is the list of other
     * StringNodes which start with the same letter as this node's
     * data ends with.
     * 
     * @param the children 
     */
    public void setChildren(List<StringNode> children) {
        this.children = children;
    }

    /**
     * Get the StringNode children for this node, that is the list of other
     * StringNodes which start with the same latter as this node's data
     * ends with.
     * @return the chilren
     */
    public List<StringNode> getChildren() {
        return children;
    }

    /**
     * Get the first character of this StringNode's data.
     * @return 
     */
    public char getFirstCharacter() {
        return data.charAt(0);
    }

    /**
     * Get the last character of this StringNode's data.
     * @return 
     */
    public char getLastCharacter() {
        return data.charAt(data.length() - 1);
    }

    @Override
    public String toString() {
        return data;
    }
}

# String Set REST application for Verto Analytics Job Application

## Assumptions

* String sets are ordered sets
* String sets cannot be empty
* String sets cannot contain empty strings
* exactly_in returns an alphabetically sorted list like longest and most_common, rather than a single string.
* Duplicated string sets are allowed.
* String sets are indexed by a numeric auto increment id.


## Installation

1. Build with maven, or alternatively download from the CI server: http://cyberiantiger.org/jenkins/job/string-sets/
2. Deploy the war file to your web application server (only tested with latest stable jetty) or feel free to use my test server at http://cyberiantiger.org/string-sets/

## Comments

longest_chain is an NP-Complete problem (or NP-Hard, not sure), the longest chain in a single string set is the same as the longest path in a directed cyclic graph which is know to be an NP-complete problem.

The implementation for longest_chain should run in O(N^2), however this assumes that the size of uploaded string sets is a constant. In terms of string set size (M) it scales very poorly, in the worst case O(M!) (for string sets such as "aaa, aba, aca, ada, aea, ....").

In terms of memory usage, the algorithm is fairly compact, and just requires a directed graph be created for each string set, which is probably just O(NM) (a little trick used is that for each node in the graph with the same end letter, they all use the same object for the list of outgoing nodes).

The graphs for each string set could be cached for further performance improvement (I haven't considered the time to construct the graph, I think compared to the time spent traversing it at scale it will be negligible anyway).

Adding further generated documentation for the REST protocol would be fairly easy, however due to time constraints (I have other jobs to apply for), I have not done this. Swagger seems like a good technology to use for this, though there are many options. Refer to the javadocs in StringSetResource.java for complete API documentation.

## Original Exercise Text
```
EXERCISE

Design and implement REST API supporting the operations described below. Use Java 8 or Scala and either Java EE or Play framework. Your backend storage may be non-persistent in-memory storage. Document your API and your code. Write tests. Send the whole package to us with detailed instructions for compilation and deployment. Describe what was the most difficult part of the assignment and why. If you need some clarifications with respect to the exercise, do not send us questions. Make an assumption, and send the list of assumptions made together with your solution.

Operations to implement:
1."upload" -- upload a set of strings to the server, verify its validity (it must be a set, so no duplicate strings are allowed)
2."search" -- find all the sets containing given string
3."delete" -- delete the specified set of strings from the server
4."set_statistic" -- return the statistics for one of the previously uploaded sets (number of strings, length of the shortest string, length of the longest string, average length, median length)
5."most_common" -- find the string used in the most sets; if there are a few such strings, return them all, sorted alphabetically
6."longest" -- find the longest string in all the sets; if there are a few such strings, return them all, sorted alphabetically
7."exactly_in" -- find the string which is present in exactly given number of sets
8."create_intersection" -- create a new set of strings which is an intersection of two previously uploaded sets

For bonus points (if you want to be considered for more senior position):

1.Implement one more endpoint:

"longest_chain" -- from the previously uploaded sets find the longest chain of strings such that:

every next string starts with the same character as the previous one ends with
every next string belongs to the same set as previous one, except one jump to another set is allowed
specific string from specific set may be used only once

Example:
Set 1: foo oomph hgf
Set 2: hij jkl jkm lmn
Set 3: abc cde cdf fuf fgh

The longest chain is: abc - cdf - fuf - fgh - (set changed here) - hij - jkl - lmn

2.Explain what is computational and space complexity of your implementation of
"longest_chain" operation.

3.Design and implement some kind of stress-testing for your application.
```

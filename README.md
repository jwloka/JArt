# J/Art — Java Automted Refactoring Tool


## “Tool-supported Discovery & Refactoring of Structural Weaknesses in Code” 


J/Art is a *proof-of-concept* implementation of an automated refactoring tool. The tool supports the developer in three important areas:
- It's able to detect structural weaknesses automatically, and to report them to the developer. A tool can perform this task much faster and more thorough, even though only a limited range of problems in the structure are detectable by tools.
- It can determine for certain problems which kinds of restructuring would provide the most benefit. This type of analysis is especially difficult for developers.
- For certain kinds of restructuring it can assess whether they are applicable, and it then can perform them in an automated fashion. Especially restructuring is particularly suited for automatization.

Simple program representations (abstract syntax tree) and commonplace static analysis are used in order to implement a prototypical add-in for the NetBeans IDE. **The tool and in particular its NetBeans integration is outdated and should be used for educational purposes only.** 

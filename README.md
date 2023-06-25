[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/MYVtI0hB)
[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-7f7980b617ed060a017424585567c406b6ee15c891e84e1186181d67ecf80aa0.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=11361325)
# Project name TODO-Application in MongoDB

## Description

A Todo-application that can handle different TODO:s (in text-form, fed in by the user) against MongoDB as a serversolution.
In the program you can perform CRUD operations against both TODO:s, as well as a user. And both can be connected to each other.

- What was your motivation?

I wanted to build a solution for this typer of program against MongoDB as a server solution as i think MongoDB is a fantastic serversolution, easy to read and understand + you dont have to have various different joins and be afraid of different injections, although, you should always stay on your toes anyway and be varyu of various different other attempts at breaking and getting into your program.  

- Why did you build this project? (Note: the answer is not "Because it was a homework assignment.")

Because (i actually) even though it was the given homework assignment, i wanted to create a more complex CRUD-application that can handle both users and different text-messages stored in a remote server. I have some personal exciting use and future implementations and ideas i can use for this.

- What problem does it solve?

Inputs different TODO:s into a remote stored database (but also has a backup in form the local mongoDB-solution if that fails) and they´re all connected to various created users.

- What did you learn?

To write cleaner and more easier and more maintainable code. And make this type of project specific against MongoDB, since i really like MongoDB as a server solution.

## Installation
Maven Dependencies in a pom.xml file thats included in this project shall have everything thats required to run this program.

## Usage
<B>For server connection:</B>  
* In src-root, alter the file <I>mongodb.properties</I> where the String ```connectionString=YourOwnConnectionStringToMongoDB``` shows up.  
Copy-paste the YourOwnConnectionStringToMongoDB-part with your own ConnectionString to MongoDB, and it shall work.

<B>For Local connection:</B>  
* If the server connection fails, it will automatically assume a local connection on port 27017.  
Make sure you have the MongoDB Compass installed on your computer from https://www.mongodb.com/try/download/shell to try, and that it is pointing towards port 27017.


## Credits
Classmates from school (ball-plank), Myself, My Family, Mighty Duck, and some chatGPT for debugging. 

I think its standard these days that you have to use chatGPT/google/StackOverflow or internet for some sort of help with the debugging. As long as you do it right and actively try to learn from 'what' it is that you´re copy-pasteing in and reading it, its ok. Its a learning process that as well.

## License
🏆 MIT License

## Badges
![badmath](https://img.shields.io/badge/Java-100%25-blue)

## Features
CRUD for a User (Unique)  
CRUD for TODO:s in the program (unique)

## Tests
Tests included (Mockito5 and JUNIT5) for every public method in the program

## Reviewing and Approval
This Project was reviewed and approved by classmate, on 2023-06-2, by:
Technologies Used :-

JDK 1.4 (As we may not get 1.5 to host the code and also it will not be poss for me to install 1.5 w/o admin rights)
Servlet 1.4 (But we will have to check if Tomcat 4.1 and the host guy are supporting this)
Tomcat – we will have to see as to what ver is available with the host…?
Struts – lets use the latest one….
Mysql – also let us see if we can get the version form him…..

Its better if we keep a sync b/w the production and dev otherwise it results in last minute hiccups.
 


ExamOnline will have two parts :
1.	Admin Panel
2.	User Panel.

First of all lets us proceed with the User Panel.
This time I propose some changes in the User Panel.
Once he clicks on the test we will start the test in a new screen.
We will also have to decide as to how to control the test both at the server side and the client side.
We will have to change the User table as we may not require so much data (city, address and all that stuff.) with us now.

We will create a simple panel where he can give any of the tests on the website, view his results for the test, his results will be mailed to him, and also review the test .i.e. with answers.
Also we will have to provide the facility to mark a question .i.e. he can mark a question and than come back and answer the marked questions and also change any answers if required. And than he has to submit the test.


Also what I had thought about the questions was that we will use xml files for that purpose….
As querying a database again n again will be costly I think….
But if load an xml file and and get questions and answers…… wil it be fast….. 
What do u say….
This will allow the user to complete n finish he test even if database is down in between …… I wanted to decrease the dependence of the app on the DB….. pls think of some architecture for this…. And whethere this will be scalable?
I have just worked on castor objects created out of xml files I m not sure about the parser thing.. if u have than pls think abt this.

Also we will have to divide the user part into modules so that we can work on those independently.


I am trying to document some scenarios here :-
1. User visits javabulls. New User Registers.
2. Logs in . Sees all the tests that are available to him. We wil create a control panel for him like we had been doing it previously.
3. He can view his previous reports. Mail those reports. Review his previous tests.
4. he can re take a test.. if he does that we will remove the previous data.
5. all the test activity will be in a new window and we will update the other window once he finishes the test.
6. Also if the test gets discontinued than we have to restart the etst from where he left last time( wil have to brainstorm to implement this option).



Use Case List for Test Taker
1.	Register New User
This UC will enable a new user to register himself with the testing system. The user needs to provide the following details :- UserName, Password,Confirm Password, Email, First Name, Last Name (optional).
Once he provides all the information an account will be created for the user and email is sent to the User giving a Authentication Code. This Authentication Code will be used when he logs in first time. This will help to confirm his email address. Authentication Code will be asked only during first login and not after that. 

2.	Modify User Info
a.	This UC will enable the user to change his personal information later on.

3.	Forgot Password
a.	This UC will be used to mail the password to the user once he submits the email address.
4.	Login/View Tests/Control Panel
a.	Once a user logs in he will be taken to the Control Panel.
b.	In the control Panel the user can view the Tests available.
c.	View the results of the tests taken.
d.	Review any of the taken tests.
e.	Mail/Publish the results.
5.	Take Test
a.	Once a User selects a test he is shown the test policy.
b.	If he clicks on start the test is started.
c.	The user should be able to restart the test incase there is an interruption in between.

**Now Take Test is dependent on the the way test is created. So now I think we need to come out with a solid architecture of how the tests will be created. We need formulate a generic design so that no matter what kind of test we get we should be able to generate the questions and evaluate and print the result.

We need to make a test Engine which will have sub components such as 
Test Policy
Test Evaluator – Will Use the Test Policy
Result Generator – Interact with Test Evaluator
Test Bank – All the questions.

We need to design the sub components in such a way that changing one does not affect the working the of the other components. This way the Test Taker module will just use the Test Policy and Test Bank to display the test and be independent of the type and all the differences between the different types of test.

Try to use:
1. Accept online payment by credit card/PayPal and allow user to take a test only if he has paid, we can have some paid + some free test, like what brainbench does
2. Send course invitations via email 
3. Limit number of users taking test at a time
4. Send newsletters to users on a new test.
5. Test result and certificate as downloadable PDF and Allow users to export reports 
6. Multiple Choice, Select All That Apply (Check boxes, For check boxes, if  'None of them' answer is selected, all other answers are automatically deselected, and vice-versa) )
7. Multiple Choice, Select Just One (Drop down list boxes)
8. Conditional Question Branch or Skip (Dynamically hide, display or branch to optional questions based on previous answers) 
9. Required Answer (Respondent cannot submit page unless a response is given to the question) 
10. Random Answer Sequence (Randomly re-orders set of answer options differently for each test taker)
11. Import/Export tests in csv format.
12. run test in "Test Mode", data not saved to database.
13. Spell check (http://jazzy.sourceforge.net/)
14. Time limit for sections and not for full test.




# COMP3111_4bits
COMP3111 Java Calendar

##Introduction
The objective of the project is to develop a calendar system that supports the scheduling of both personal events and collaborative activities. Examples of fully featured calendar systems are widely offered by many internet service providers such as Yahoo! Calendar and Google Calendar. Our objective is, however, to develop a subset of the many features available, supplemented by your own creativity.

##Phase 1
The basic required features in the Phase I of the calendar system development is to implement the basic event scheduling functionality for a single user. It includes the following requirements.

1. <b>Single user environment</b>: At this stage, you may assume that the calendar is used only by a single user to arrange her    personal schedule. No other users are present or can view/access the information of the calendar. 
2. <b>Basic event scheduling</b>: The calendar must be able to provide basic scheduling capabilities for an individual user.
  * The user can schedule an event with the starting time, end time, the event description, the event location, the     frequency, if a reminder is needed, how much time ahead the reminder should be triggered, and additional description for the event.
  * The time specification should be in intervals of 15 minutes. The event frequency can be one-time, daily, weekly, or monthly. Should a reminder be needed, it should be triggered and displayed to the user at or less than the specified time interval before the scheduled time of the event. The reminder should only be given ONCE.
  * The reminder information, the event location, and the event description can be optionally specified. Other information must be specified. The location must be selected from a list of predefined locations in the system
  * The successfully scheduled event should cause GUI to change accordingly. An entry should show in the daily view and the day when the event is scheduled should change color on the month view.
    
3. <b>Location information</b>: The location must be uniquely identified by its name. The user must select from a set of predefined locations. The locations can be added through a separate interface (Note that this interface does not exist in the current skeleton).
>**Partially implemented based on the slides https://course.cse.ust.hk/comp3111/project/start.pdf**
4. <b>Event validity</b>: The user can neither schedule multiple events that overlap in time or in space nor an event happens in the past. The user can only modify or delete any events that have not yet happened.
Notification: The calendar system should provide notification services to the user at a user-defined interval prior to the time when the event happens.
5. <b>Time machine</b>: Do not program directly using your computer clock because, for testing purposes, we will ask you to fast forward and rewind the clock that is used by the calendar system. During the evaluation of the project, the evaluator will use the interface you provide, either on the GUI or from the console, to use this time traveling feature and test the functionalities of the implementation. You must make sure your entire calendar system is based on a changeable clock.


###STORING EVENTS EXPLAINED
1. How ApptStorage works to store events in memory?
 * First of all, storing in memory means that every time we log out or exit the calendar, we lose the stored events. (Despite    the term 'memory', in this project of Phase 1, it's referring to the heap/dynamic memory, I guess). That is why in Phase 2    we will be dealing a lot with storing events in a XML file.

 * Second, ApptStorage is an <b>ABSTRACT</b> class, meaning that it has to be inherited. ApptStorageNullImpl inherits the abstract     class and become the object to store our events. ApptStorageControllerImpl is a stand-alone class (the name is misleading,    because it looks like it inherits ApptStorage) which manages the ApptStorageNullImpl. It can get or modify data inside the    ApptStorageNullImpl. The constructor of ApptStorageControllerImpl takes an instance of ApptStorageNullImpl as its   parameter.

2. Now how do these classes get created in essence with the Calendar GUI?
  * When we get logged in into the calendar grid GUI (CalGrid: the one with the tables, month, time, etc), during the creation of CalGrid, a <b><i>new</i></b> controller of type ApptStorageControllerImpl with parameter <b><i>new</i></b> ApptStorageNullImpl get passed into CalGrid from LoginDialog. Hence, we could say that the ApptStorageNullImpl creation starts in CalGrid. From here onwards, the CalGrid itself or the children components of CalGrid can refer to this 'memory'. That is why everytime we log out and exit from calendar grid (or we can say if the gui of CalGrid is disposed), we lose the memory.
<p align="center">
  <img src=https://cloud.githubusercontent.com/assets/8019899/6593645/42de73a2-c815-11e4-87dc-30891432fe47.png width="600px" height="500px" alt="StoragePhase1"/>
</p>


##Phase 2
The basic required features in the Phase II of the calendar system development is to implement the support of multiple users and use persistent storage for the calendar data. It includes the following requirements. 

1. <b>Registration</b>: The system support multi-users, and two types of users, administrators and normal users. So a user must register by setting the username and password, as well as to indicate whether it's a admin or a normal user.
2. <b>Security</b>: The system must protect one user's events from being viewed by others, unless these events are explicitly allowed to be publicly viewed. Therefore, the calendar system should implement a proper login functionality and should give the user a choice whether the event can be seen by other users at the time of scheduling.
3. <b>Group events</b>: The system must support the scheduling of group events involving more than one users. All users must be pre-defined.
  * Two group events involving totally different participants can overlap in time but not in location.
  * A group event uses a group facility, it must satisfy the compacity constraint information.
  * A group event cannot be scheduled unless the time slot works for every participant.
  * A group event must go through a confirmation process that asks each participant to indicate the willingness of the attendance before the schedule is finalized. The event is not scheduled unless all participants have positively confirmed. If one participant negatively confirms or the confirmation process is not complete, the event should not be scheduled. The initiator should not need to confirm.
  * Only the event initiator can delete or modify the group event. No confirmation from participants is needed.

4. <b>Persistence</b>: The system must support the safety of the data entered by storing the confirmed input to a permanent storage on the disk. Any valid information should be present when the calendar system is restarted.
5. <b>Maintenance</b>: The calendar must support account management functionalities as follows.
  * The creation of two classes of users: the regular users and the administrative users.
  * Each class of the users have different privileges. Regular users can ONLY create and change her or his own credentials including the compulsory information such as the full name (first and last), the password, and the email address. Other information can also be specified. The administrator can inspect, change, and remove any users in the system.
  * Only the administrator can specify, change, or remove location information such as meeting rooms. If a location is a group facility (has a tag to indicate it), it must be given a readable name and capacity information.
  * Although the administrator have many privileges, she must apply a courtesy policy which states that, if a user or a location is to be removed, she must receive the confirmation of the initiators of all concerned events before finalizing the removal, and also send a notification to the user when he/she tries to login.

6. <b>Intelligence</b>: The calendar supports a certain degree of computational intelligence to ease the scheduling of events. It should at least support two pieces of scheduling intelligence. First, after the initiator selects the participants and the date (may several days), it should provide a list of "schedulable" timeslots which are available for all the participants for the initiator to choose from. Second, it should be able to perform automatic event scheduling, especially for group events. All the participants can choose multiple available timeslots, it should locate the earliest timeslot that satisfies all the constraints from the participants.
    

##Architecture Document

For the system design document, you are required to write a design document that describes the use of design patterns in your project. You are required to identify the usages of two design patterns in the existing code and one new application of design patterns that can improve the existing design of your code. For the new application, you are not required to implement them in your code, since this is a design document. For each of the three descriptions of patterns, you should follow the following sectioning guidelines:

1. <b>Name</b>: The name of the pattern.
2. <b>Motivation</b>: What design problem, in the context of the calendar software, exists without the use of this particular pattern. You are encouraged to think in the context of real world usages that involve many users and a large amount of information.
3. <b>Solution</b>: How this pattern solves the problem for the calendar software in theory according to the real world usage.
4. <b>Class diagram</b>: Give the class diagram in each description that shows the proper inheritance relationships of Calendar classes with respect to the general pattern classes, in the style consistent with the lecture notes. For instance, you should diagram the generic classes of Observer pattern, and show how your classes inherit from them. You are not required to show all fields and methods for the clarity of the diagram. You can just show the necessary fields and methods that are relevant to the implementation of the specific pattern.

The requirement for the document is a A4/12 point font for no less than 3 pages. The document will be graded on the correctness and clarity of the presentation and the discussion.

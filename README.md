# COMP3111_4bits
COMP3111 Java Calendar

##Introduction
The objective of the project is to develop a calendar system that supports the scheduling of both personal events and collaborative activities. Examples of fully featured calendar systems are widely offered by many internet service providers such as Yahoo! Calendar and Google Calendar. Our objective is, however, to develop a subset of the many features available, supplemented by your own creativity.

##Phase 1
The basic required features in the Phase I of the calendar system development is to implement the basic event scheduling functionality for a single user. It includes the following requirements.

1. Single user environment: At this stage, you may assume that the calendar is used only by a single user to arrange her    personal schedule. No other users are present or can view/access the information of the calendar. 
2. Basic event scheduling: The calendar must be able to provide basic scheduling capabilities for an individual user.
  * The user can schedule an event with the starting time, end time, the event description, the event location, the     frequency, if a reminder is needed, how much time ahead the reminder should be triggered, and additional description for the event.
  * The time specification should be in intervals of 15 minutes. The event frequency can be one-time, daily, weekly, or monthly. Should a reminder be needed, it should be triggered and displayed to the user at or less than the specified time interval before the scheduled time of the event. The reminder should only be given ONCE.
  * The reminder information, the event location, and the event description can be optionally specified. Other information must be specified. The location must be selected from a list of predefined locations in the system
  * The successfully scheduled event should cause GUI to change accordingly. An entry should show in the daily view and the day when the event is scheduled should change color on the month view.
    
3. Location information: The location must be uniquely identified by its name. The user must select from a set of predefined locations. The locations can be added through a separate interface (Note that this interface does not exist in the current skeleton).
>**Partially implemented based on the slides https://course.cse.ust.hk/comp3111/project/start.pdf**
4. Event validity: The user can neither schedule multiple events that overlap in time or in space nor an event happens in the past. The user can only modify or delete any events that have not yet happened.
Notification: The calendar system should provide notification services to the user at a user-defined interval prior to the time when the event happens.
5. Time machine: Do not program directly using your computer clock because, for testing purposes, we will ask you to fast forward and rewind the clock that is used by the calendar system. During the evaluation of the project, the evaluator will use the interface you provide, either on the GUI or from the console, to use this time traveling feature and test the functionalities of the implementation. You must make sure your entire calendar system is based on a changeable clock.


###STORING EVENTS EXPLAINED
1. How ApptStorage works to store events in memory?
 * First of all, storing in memory means that every time we log out or exit the calendar, we lose the stored events. (Despite    the term 'memory', in this project of Phase 1, it's referring to the heap/dynamic memory, I guess). That is why in Phase 2    we will be dealing a lot with storing events in a XML file.

 * Second, ApptStorage is an <b>ABSTRACT</b> class, meaning that it has to be inherited. ApptStorageNullImpl inherits the abstract     class and become the object to store our events. ApptStorageControllerImpl is a stand-alone class (the name is misleading,    because it looks like it inherits ApptStorage) which manages the ApptStorageNullImpl. It can get or modify data inside the    ApptStorageNullImpl. The constructor of ApptStorageControllerImpl takes an instance of ApptStorageNullImpl as its   parameter.

2. Now how do these classes get created in essence with the Calendar GUI?
  * When we get logged in into the calendar grid GUI (CalGrid: the one with the tables, month, time, etc), during the creation of CalGrid, a <b><i>new</i></b> controller of type ApptStorageControllerImpl with parameter <b><i>new</i></b> ApptStorageNullImpl get passed into CalGrid from LoginDialog. Hence, we could say that the ApptStorageNullImpl creation starts in CalGrid. From here onwards, the CalGrid itself or the children components of CalGrid can refer to this 'memory'. That is why everytime we log out and exit from calendar grid (or we can say if the gui of CalGrid is disposed), we lose the memory.
<p align="center">
  <img src=https://cloud.githubusercontent.com/assets/8019899/6593645/42de73a2-c815-11e4-87dc-30891432fe47.png width="600px" height="500px" alt="StoragePhase1"/>
</p>

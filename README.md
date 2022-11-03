# Special Relativity Simulator
*--Tribute to Albert Einstein*

## Introduction
Special relativity is a significant subject of modern Physics. Based on the central it features 
theory that ***the speed of light is a constant value c in all inertial frames***, special relativity uncovers 
plentiful mysteries of events occurring at *relativistic speeds* (speeds close to the speed of light), such as 
*time dilation* and *length contraction*. Using a set of formulae called Lorentz-Transformation, we can easily figure 
out the time and location of one meetEvent in one frame based on the same pair of data measured in another frame. 

## What will the application do?
This application provides a simple simulation of frames and events happening at relativistic speeds. It represents
inertial frames (abbreviated as *frames* in this project) as tiny universes that moving relative to each other at a 
certain speed in the positive or negative x direction.  
From the perspective of a certain stationary frame F, each other frame F' has the following properties:
- The x coordinate, relative to the stationary frame F
- The proper time T: the reading on a clock that moves along with the frame F'
- The velocity v relative to F


The following functions should be available to users:
- Select an existing frame as the current reference frame
- Set the proper time of the current reference frame
- View the x position, the proper time and relative velocity in other frames from the perspective of 
the selected frame, based on formulae of Lorentz-Transformation
- Add a frame by stating its initial x coordinate, proper time and velocity relative to a chosen frame (note that this 
added frame's initial status will not respond to changes in the frame it refers to: its initial status is set and can 
only be directly edited by user)
- Edit the initial conditions (related to the absolute stationary frame) of a chosen frame
- Delete a certain frame (except for the absolute stationary frame)
- *By default, there is an "absolute" stationary frame that exists once the application is launched and cannot be 
deleted, so that users can add frames relative to it. Such absolute frames do not exist in special relativity; it is 
included only for convenience*


 ## Who will use it?
Since the application is a simplistic simulation, it might be useful for beginners in special relativity; it can be used
as a visualized Lorentz-Transformation calculator which simplifies manual calculation. It might also be good for virtual
simulations in some online lab courses that take data from applications.

## Why is this project of interest to you?
I am a student of Physics 200 in 2021 Winter Term, featuring concepts of special relativity and quantum. Although such 
physical concepts are interesting to me, figuring out the abstract relationship between moving frames is quite complex.
Therefore, I would like to simplify this process with a visualized application.  
Prior to this project, I have witnessed various physical-phenomenon simulators from many resources, including a 
Celestial Body Motion Simulator based on gravitational programmed by my friend. Most of these simulators feature 
classical physical phenomena such as mechanics and electrics that can be directly presented. Therefore, I decide to 
innovatively simulate an abstract physical concept.   


## User Stories
- As a user, I want to be able to add a frame to my simulator relative to any existing frame
- As s user, I want to be able to add an physicalEvent to my simulator relative to any existing frame
- As a user, I want to be able to select one frame as my current reference frame
- As a user, I want to be able to view the attributes of other frames and events in the perspective of the chosen frame 
based on Lorentz Transformation
- As a user, I want to be able to change the proper time of my chosen frame and see the corresponding changes of other 
frames and events
- As a user, I want to be able to edit the initial conditions (related to the absolute stationary frame) of 
a chosen frame
- As a user, I want to be able to delete an existing frame from my simulator; the events observed by this frame are 
also deleted
- As a user, I want to be able to delete an existing physicalEvent from my simulator; the corresponding slot for this physicalEvent in
the frame that observes it is also deleted
- As a user, I want to be able to save my frames in the current FrameWorld to file (and be reminded of the option to 
save or not before quiting the program)
- As a user, I want to be able to have an option to load my FrameWorld from file at any time
- As a user, I want to be able to see and control the visual display of Frames and Events in my FrameWorld, including 
options of displaying Events or not, playing the simulation by letting the proper time of the current Reference frame
run (either forward or backward), dragging the displayed image and resetting the image's centre to its original 
position

## Phase 4: Task 2
Thu Nov 25 10:34:14 PST 2021
RefFrame FRAME 1 added to Saved FrameWorld

Thu Nov 25 10:34:14 PST 2021
PhysicalEvent EVENT A added to Saved FrameWorld

Thu Nov 25 10:34:14 PST 2021
Event EVENT B is observed to occur at: (2.6794919243112294c*s, -24.641016151377542s) in Absolute Stationary Frame

Thu Nov 25 10:34:14 PST 2021
PhysicalEvent EVENT B added to Saved FrameWorld

Thu Nov 25 10:34:14 PST 2021
Absolute Stationary Frame is set as the new reference frame of Saved FrameWorld

Thu Nov 25 10:34:14 PST 2021
Proper Time of reference frame (Absolute Stationary Frame) of Saved FrameWorld is set to -20.1971s

Thu Nov 25 10:34:15 PST 2021
RefFrame FRAME 1 added to Current FrameWorld

Thu Nov 25 10:34:15 PST 2021
PhysicalEvent EVENT A added to Current FrameWorld

Thu Nov 25 10:34:15 PST 2021
PhysicalEvent EVENT B added to Current FrameWorld

Thu Nov 25 10:34:15 PST 2021
Absolute Stationary Frame is set as the new reference frame of Current FrameWorld

Thu Nov 25 10:34:30 PST 2021
RefFrame FRAME 2 added to Current FrameWorld

Thu Nov 25 10:34:33 PST 2021
FRAME 2 is set as the new reference frame of Current FrameWorld

Thu Nov 25 10:34:38 PST 2021
Event EVENT A is observed to occur at: (-12.0c*s, 23.96152422706632s) in FRAME 2

Thu Nov 25 10:34:38 PST 2021
Proper Time of reference frame (FRAME 2) of Current FrameWorld is set to 30.0s

Thu Nov 25 10:34:51 PST 2021
Frame FRAME 1's name is set to: FRAME 1-renamed

Thu Nov 25 10:34:51 PST 2021
Frame FRAME 1-renamed's initial position is set to: 0.0c*s

Thu Nov 25 10:34:51 PST 2021
Frame FRAME 1-renamed's occur time is set to: 0.0s

Thu Nov 25 10:34:51 PST 2021
Frame FRAME 1-renamed's initial Proper Time is set to: 30.0s

Thu Nov 25 10:34:51 PST 2021
Frame FRAME 1-renamed's initial velocity is set to: 0.5c

Thu Nov 25 10:34:59 PST 2021
Event EVENT B is deleted from Current FrameWorld

Thu Nov 25 10:34:59 PST 2021
Frame FRAME 1-renamed is deleted from Current FrameWorld

Thu Nov 25 10:35:01 PST 2021
Event EVENT A is deleted from Current FrameWorld

Thu Nov 25 10:35:02 PST 2021
Absolute Stationary Frame is set as the new reference frame of Current FrameWorld

Thu Nov 25 10:35:02 PST 2021
Frame FRAME 2 is deleted from Current FrameWorld

Thu Nov 25 10:35:08 PST 2021
RefFrame FRAME 1 added to Saved FrameWorld

Thu Nov 25 10:35:08 PST 2021
PhysicalEvent EVENT A added to Saved FrameWorld

Thu Nov 25 10:35:08 PST 2021
Event EVENT B is observed to occur at: (2.6794919243112294c*s, -24.641016151377542s) in Absolute Stationary Frame

Thu Nov 25 10:35:08 PST 2021
PhysicalEvent EVENT B added to Saved FrameWorld

Thu Nov 25 10:35:08 PST 2021
Absolute Stationary Frame is set as the new reference frame of Saved FrameWorld

Thu Nov 25 10:35:08 PST 2021
Proper Time of reference frame (Absolute Stationary Frame) of Saved FrameWorld is set to -20.1971s


## Phase 4: Task 3
My overall design of the program has a lot of repetition in the UI elements of RefFrame and PhysicalEvent classes; 
although the RefFrame and PhysicalEvent class themselves are designed to be subclasses of PhysicalObject so that 
repetition is minimized, the visual elements corresponding to them are not well-simplified due to some features of 
RefFrame and PhysicalEvent (for instance, deleting one frame will delete all the events observed by it: this requires 
each ContainerPanel List to contain a list of their elements in addition to the infoPanel field that already contains 
these elements; but if I give this responsibility to classes in model package only, the visual elements in UI must be
repainted thoroughly every time a change is made, which may cause the program to tun slow when the number of 
PhysicalObjects is large).

One possible way to improve is to apply the Observer pattern to the PhysicalObjects (Observable) and their corresponding
visual PhysicalObjectElements (Observer): whenever updates are made on the PhysicalObjects, the infoPanel of their 
corresponding elements are notified so that they can be automatically updated. This will enhance the coupling of the 
program. Similar pattern can be applied to frameWorld and the ContainerPanels as well, which will allow simplification 
of the process of deleting a RefFrame element. If this is done, I will be able to refactor the EventListPanel and 
FrameListPanel classes even further: the current version of the two classes are almost identical except for the deletion
function and the unique editor of FrameListPanel; while the editor can be made as a field of the 
GraphicRelativitySimulator, the deletion function will not require specific design if the Observer Pattern can be 
applied. In this way, I will not need to additionally design the two subclasses of ContainerPanel; instead, I can make
ContainerPanel Class concrete and initialize two objects that are respectively the FrameListPanel and EventListPanel.

Another refactoring I can make is to split the responsibilities of FrameWorld into RefFrame and PhysicalEvent classes 
and possibly their visual elements. Currently, FrameWorld holds two lists of PhysicalObjects, in order for the UI 
classes to easily extract the RefFrames and PhysicalEvents respectively. If I can give the responsibility of 
distinguishing the actual type of a PhysicalObject to the UI elements (using predicates like instanceof or functions 
like getClass()), I can largely simplify the design of FrameWorld class by merging the frameList and EventList into one;
this will allow me to combine the add and delete methods from two classes into one pair of add&remove function that 
functions for both classes; other functions, such as viewAllInGivenFrame, can also be simplified using only one 
for-each loop.

To go even further, I can choose to eliminate the entire FrameWorld class, apply composite pattern to RefFrame and 
PhysicalEvent classes (PhysicalObject as Component, Frame as composite, Event as Leaf), and make the entire world of 
Frames and Events into a tree structure; the responsibility of holding the information of the current reference frame 
will therefore be granted to the GraphicRelativitySimulator. This is technically a better solution to the current 
structure, because it allows the sub-frames to change when the super-frame that observes them have its initial status 
changed, which cannot be realized in the current version of FrameWorld; I have not implemented this design because I do 
not have much experience in designing data with a tree-structure, especially in cases of efficiently finding a given 
element, or preventing possible infinite recursions if the user assigns the child node to be the super-node of its 
original parent node (or parent of parent nodes, etc.).

I have also thought of storing the Frames and Events in sets or hashMaps instead of LinkedLists, which will largely
save time and space since Frames and Events are genuinely distinct. However, while set cannot maintain the insertion 
order, maps do not allow the program to modify the keys (names of PhysicalObjects) once they are set. Therefore, I used 
LinkedList, which maintains insertion order while being more efficient in adding and removing elements compared to 
ArrayList. 

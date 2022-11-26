# The State Machine
### **Await Select State (awaitSelect):**
This is the generic state the program is in most of the time. pointSelected and multiSelect can be access from this state.
### **Point Select (pointSelected):**
In this state there is a point selected. This point is attached to the mouse and can be moved to another location. When left click is pressed again the state will return to awaitSelect.
### **Multi Select (multiSelect):**
A list of points is initialized (or reset) here, and the selected point is added at index zero. Only two or three points can be selected. Selecting 2 points and press tab will change the state to line action, and selecting 3 points and pressing tab will change the state to quadraticBezierAction.
### **Line Action (lineAction):**
If there exists a line between the points selected it will be deleted otherwise it will attempt to add a new line between the points.
### **Quadratic Bezier Action (quadraticBezierAction):**
This will do the same thing as lineAction but with a quadratic bezier curve instead.

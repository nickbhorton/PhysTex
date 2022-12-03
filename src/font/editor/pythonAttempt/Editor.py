import pygame
import SymbolPoint
import Bezier
import os

stateDict = {
        "awaitSelect":0,
        "pointSelected":1,
        "multiSelect":2,
        "dragSelect":3,
        "multiMove":4,
        "lineAction":5,
        "quadraticBezierAction":6,
        "move":7}
stateDictReverse = {
        0:"awaitSelect",
        1:"pointSelected",
        2:"multiSelect",
        3:"dragSelect",
        4:"multiMove",
        5:"lineAction",
        6:"quadraticBezierAction",
        7:"move"}

DISPLAY_SIZE = [800, 800]

def xPointCoordsToPygameCoords(input, zoom, xOffset):
    return (input/(2**15))*zoom*DISPLAY_SIZE[0] + (xOffset)
def yPointCoordsToPygameCoords(input, zoom, yOffset):
    return 800 - ((input/(2**15))*zoom*DISPLAY_SIZE[1] + (yOffset))


def renderPoints(screen, points, zoom, xOffset, yOffset):
    for i, point in enumerate(points):
        x = xPointCoordsToPygameCoords(point.x, zoom, xOffset)
        y = yPointCoordsToPygameCoords(point.y, zoom, yOffset)
        color = (0,0,0)
        if point.type == None:
            color = (0,0,0)
        elif point.type == "start":
            color = (0,255,0)
        elif point.type == "middle":
            color = (255,0,255)
        elif point.type == "end":
            color = (255,0,0)
        if point.selected:
            color = (0,0,255)
        if point.dragSelected:
            color = (255, 180, 0)
        if x > 0 and x < 800 and y > 0 and y < 800:
            pygame.draw.circle(screen, color, (x,y), 5)

def renderBezier(screen, bezierList, zoom, xOffset, yOffset):
    for bezier in bezierList:
        if isinstance(bezier, Bezier.LinearBezier):
            x1 = xPointCoordsToPygameCoords(bezier.p1.x, zoom, xOffset)
            y1 = yPointCoordsToPygameCoords(bezier.p1.y, zoom, yOffset)
            x2 = xPointCoordsToPygameCoords(bezier.p2.x, zoom, xOffset)
            y2 = yPointCoordsToPygameCoords(bezier.p2.y, zoom, yOffset)
            pygame.draw.line(screen, (0,0,0), (x1,y1), (x2,y2))
        elif isinstance(bezier, Bezier.QuadraticBezier):
            x1 = xPointCoordsToPygameCoords(bezier.p1.x, zoom, xOffset)
            y1 = yPointCoordsToPygameCoords(bezier.p1.y, zoom, yOffset)
            x2 = xPointCoordsToPygameCoords(bezier.p2.x, zoom, xOffset)
            y2 = yPointCoordsToPygameCoords(bezier.p2.y, zoom, yOffset)
            x3 = xPointCoordsToPygameCoords(bezier.p3.x, zoom, xOffset)
            y3 = yPointCoordsToPygameCoords(bezier.p3.y, zoom, yOffset)
            numberOfVertex = 10
            vertexArray = []
            tInit = 1.0 / (numberOfVertex - 1)
            for i in range(0,numberOfVertex):
                t = tInit * i
                tSquared = t*t
                p1Mult = (1 - 2*t +tSquared)
                p2Mult = 2*t*(1-t)
                vertexArray.append([0,0])
                vertexArray[i][0] = x1 * p1Mult + x2 * p2Mult + x3 * tSquared   
                vertexArray[i][1] = y1 * p1Mult + y2 * p2Mult + y3 * tSquared
            for i in range(0,numberOfVertex-1):
                    startVertex = vertexArray[i]
                    endVertex = vertexArray[i+1]
                    pygame.draw.line(screen, (0 + i * 20,0,0), startVertex, endVertex, 2)
                


# For line functions
def xToZoomCoords(input, zoom, xOffset):
    return input * zoom + xOffset

def yToZoomCoords(input, zoom, yOffset):
    return 800 - (input * zoom + yOffset)
#
# For pygame to point coords transformations
def xPygameCoordsToPointCoords(input, zoom, xOffset):
    return int((2**15)*(input-xOffset)/(zoom*DISPLAY_SIZE[0]))

def yPygameCoordsToPointCoords(input, zoom, yOffset):
    return int((2**15)*(800 - input - yOffset)/(zoom*DISPLAY_SIZE[1]))

def renderBoundarys(screen, zoom, xOffset, yOffset):
        pygame.draw.line(screen, (255,0,0), 
            (xToZoomCoords(0, zoom, xOffset), yToZoomCoords(0, zoom, yOffset)),
            (xToZoomCoords(0, zoom, xOffset), yToZoomCoords(800, zoom, yOffset)))
        pygame.draw.line(screen, (255,0,0), 
            (xToZoomCoords(800, zoom, xOffset), yToZoomCoords(0, zoom, yOffset)),
            (xToZoomCoords(800, zoom, xOffset), yToZoomCoords(800, zoom, yOffset)))
        pygame.draw.line(screen, (255,0,0), 
            (xToZoomCoords(800, zoom, xOffset), yToZoomCoords(800, zoom, yOffset)),
            (xToZoomCoords(0, zoom, xOffset), yToZoomCoords(800, zoom, yOffset)))
        pygame.draw.line(screen, (255,0,0), 
            (xToZoomCoords(0, zoom, xOffset), yToZoomCoords(0, zoom, yOffset)),
            (xToZoomCoords(800, zoom, xOffset), yToZoomCoords(0, zoom, yOffset)))

def renderGrid(screen, zoom, xOffset, yOffset):
    for i in range(0,800,10):
        pygame.draw.line(screen, (200,200,200), 
                (xToZoomCoords(i, zoom, xOffset), yToZoomCoords(0, zoom, yOffset)),
                (xToZoomCoords(i, zoom, xOffset), yToZoomCoords(800, zoom, yOffset)))
        pygame.draw.line(screen, (200,200,200), 
                (xToZoomCoords(0, zoom, xOffset), yToZoomCoords(i, zoom, yOffset)),
                (xToZoomCoords(800, zoom, xOffset), yToZoomCoords(i, zoom, yOffset)))
    pygame.draw.line(screen, (0,0,255), 
                (xToZoomCoords(0, zoom, xOffset), yToZoomCoords(200, zoom, yOffset)),
                (xToZoomCoords(800, zoom, xOffset), yToZoomCoords(200, zoom, yOffset)))
    pygame.draw.line(screen, (0,0,255), 
                (xToZoomCoords(0, zoom, xOffset), yToZoomCoords(720, zoom, yOffset)),
                (xToZoomCoords(800, zoom, xOffset), yToZoomCoords(720, zoom, yOffset)))
    pygame.draw.line(screen, (0,0,255), 
                (xToZoomCoords(200, zoom, xOffset), yToZoomCoords(0, zoom, yOffset)),
                (xToZoomCoords(200, zoom, xOffset), yToZoomCoords(800, zoom, yOffset)))
    pygame.draw.line(screen, (0,0,255), 
                (xToZoomCoords(600, zoom, xOffset), yToZoomCoords(0, zoom, yOffset)),
                (xToZoomCoords(600, zoom, xOffset), yToZoomCoords(800, zoom, yOffset)))
def save(points, beziers, fileName):
    f = open(fileName, 'w+')
    f.write("#points\n")
    for p in points:
        if p.type != None:
            f.write(str(p.x) + ":" + str(p.y) + ":" + p.type + "\n")
        else:
            f.write(str(p.x) + ":" + str(p.y) + ":" + "none\n")
    f.write("#beziers\n")
    for b in beziers:
        if isinstance(b, Bezier.LinearBezier):
            p1Index = None
            p2Index = None
            for i, p in enumerate(points):
                if p == b.p1:
                    p1Index = i
                if p == b.p2:
                    p2Index = i
            f.write(str(p1Index) + ":" + str(p2Index) + "\n")
        elif isinstance(b, Bezier.QuadraticBezier):
            p1Index = None
            p2Index = None
            p3Index = None
            for i, p in enumerate(points):
                if p == b.p1:
                    p1Index = i
                if p == b.p2:
                    p2Index = i
                if p == b.p3:
                    p3Index = i
            f.write(str(p1Index) + ":" + str(p2Index) + ":" + str(p3Index) + "\n")
    f.write("#end\n")
    f.close()

def load(fileName):
    points = []
    beziers = []
    if os.path.isfile(fileName):
        f = open(fileName, "r")
        contents = f.read()
        splitOnEnter = contents.split("\n")
        lookAtIndex = 0
        while(splitOnEnter[lookAtIndex] != "#points"):
            lookAtIndex += 1

        lookAtIndex +=1
        while(splitOnEnter[lookAtIndex] != "#beziers"):
            pointParams = splitOnEnter[lookAtIndex].split(":")
            if pointParams[2] != "none":
                points.append(SymbolPoint.Point(int(pointParams[0]), int(pointParams[1]), pointParams[2]))
            else:
                points.append(SymbolPoint.Point(int(pointParams[0]), int(pointParams[1]), None))
            lookAtIndex += 1

        lookAtIndex += 1
        while(splitOnEnter[lookAtIndex] != "#end"):
            bezierParams = splitOnEnter[lookAtIndex].split(":")
            if len(bezierParams) == 2:
                beziers.append(Bezier.LinearBezier(points[int(bezierParams[0])], points[int(bezierParams[1])]))
                lookAtIndex += 1
            elif len(bezierParams) == 3:
                beziers.append(Bezier.QuadraticBezier(points[int(bezierParams[0])], points[int(bezierParams[1])], points[int(bezierParams[2])]))
                lookAtIndex += 1
            else:
                print("something is wrong")
        return (beziers, points)
    else:
        return (beziers, points)

def run(fileName):
    pygame.init()
    state = 0
    font = pygame.font.SysFont('helvetica', 24)
    screen = pygame.display.set_mode(DISPLAY_SIZE)
    zoom = 1.0
    xOffset = 0
    yOffset = 0
    moveBy = 0
    numberSelected = 0
    pointsSelected = []
    bezierList, points = load(fileName)
    lMouseDown = False
    lShiftDown = False
    selectedPoint = None
    dragStart = None
    dragEnd = None

    running = True
    # KEY TRACKERS
    while running:
        screen.fill((220, 220, 220))
        if state == stateDict["awaitSelect"]:
            renderGrid(screen, zoom, xOffset, yOffset)
            renderBoundarys(screen, zoom, xOffset, yOffset)
            renderBezier(screen, bezierList, zoom, xOffset, yOffset)
            renderPoints(screen, points, zoom, xOffset, yOffset)
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    running = False
                if event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_SPACE:
                        state = stateDict["move"]
                    if event.key == pygame.K_r:
                        xOffset = 0
                        yOffset = 0
                    if event.key == pygame.K_m:
                        points.append(SymbolPoint.Point(
                                xPygameCoordsToPointCoords(pygame.mouse.get_pos()[0], zoom, xOffset),
                                yPygameCoordsToPointCoords(pygame.mouse.get_pos()[1], zoom, yOffset)))
                    if event.key == pygame.K_LSHIFT:
                        lShiftDown = True
                    if event.key == pygame.K_s:
                        save(points, bezierList, fileName)
                if event.type == pygame.MOUSEBUTTONDOWN:
                    if event.button == 1:
                        noSelect = True
                        for point in points:
                            x = point.xToPygameCoords(zoom, xOffset, DISPLAY_SIZE)
                            y = point.yToPygameCoords(zoom, yOffset, DISPLAY_SIZE)
                            if selectedPoint == None:
                                mouse_pos = pygame.mouse.get_pos()
                                if (x-mouse_pos[0])**2 + (y-2*yOffset-mouse_pos[1])**2 < 15**2:
                                    noSelect = False
                                    selectedPoint = point
                                    selectedPoint.selected = True
                                    if (lShiftDown):
                                        pointsSelected = []
                                        pointsSelected.append(selectedPoint)
                                        numberSelected = 1
                                        state = stateDict["multiSelect"]
                                        break
                                    else:
                                        state = stateDict["pointSelected"]
                                        break
                        if noSelect:
                            dragStart = [xPygameCoordsToPointCoords(pygame.mouse.get_pos()[0], zoom, xOffset),
                                yPygameCoordsToPointCoords(pygame.mouse.get_pos()[1], zoom, yOffset)]
                            state = stateDict["dragSelect"]
                    if event.button == 3:
                        for point in points:
                            x = point.xToPygameCoords(zoom, xOffset, DISPLAY_SIZE)
                            y = point.yToPygameCoords(zoom, yOffset, DISPLAY_SIZE)
                            mouse_pos = pygame.mouse.get_pos()
                            if (x-mouse_pos[0])**2 + (y-2*yOffset-mouse_pos[1])**2 < 15**2:
                                for bezier in bezierList:
                                    if isinstance(bezier, Bezier.LinearBezier):
                                        if bezier.p1 == point or bezier.p2 == point:
                                            bezierList.remove(bezier)
                                            #print(bezierList)
                                            break
                                    if isinstance(bezier, Bezier.QuadraticBezier):
                                        if bezier.p1 == point or bezier.p2 == point or bezier.p3 == point:
                                            bezierList.remove(bezier)
                                            #print(bezierList)
                                            break
                                points.remove(point)
                                break

                if event.type == pygame.MOUSEWHEEL:
                    zoom *= 2*event.y
                    if event.y > 0:
                        xOffset = xOffset * 2 - (pygame.mouse.get_pos()[0]) 
                        yOffset = yOffset * 2 - (800 - pygame.mouse.get_pos()[1])
                    if event.y < 0:
                        xOffset = 0
                        yOffset = 0
                        zoom = 1
        elif state == stateDict["pointSelected"]:
            renderGrid(screen, zoom, xOffset, yOffset)
            renderBoundarys(screen, zoom, xOffset, yOffset)
            renderBezier(screen, bezierList, zoom, xOffset, yOffset)
            renderPoints(screen, points, zoom, xOffset, yOffset)
            selectedPoint.x = xPygameCoordsToPointCoords(pygame.mouse.get_pos()[0], zoom, xOffset)
            selectedPoint.y = yPygameCoordsToPointCoords(pygame.mouse.get_pos()[1], zoom, yOffset)
            posRendedAsTestSurface = font.render(str(pygame.mouse.get_pos()[0]) + "  " + str(pygame.mouse.get_pos()[1]), False, (0, 0, 0))
            screen.blit(posRendedAsTestSurface, 
                [pygame.mouse.get_pos()[0] - posRendedAsTestSurface.get_width()/2,
                 pygame.mouse.get_pos()[1] - 3*posRendedAsTestSurface.get_height()/2])
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    running = False
                if event.type == pygame.MOUSEBUTTONDOWN:
                    if event.button == 1:
                        if selectedPoint != None:
                            selectedPoint.selected = False
                            selectedPoint = None
                            state = stateDict["awaitSelect"]
        elif state == stateDict["multiSelect"]:
            renderGrid(screen, zoom, xOffset, yOffset)
            renderBoundarys(screen, zoom, xOffset, yOffset)
            renderBezier(screen, bezierList, zoom, xOffset, yOffset)
            renderPoints(screen, points, zoom, xOffset, yOffset)
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    running = False
                if event.type == pygame.KEYUP:
                    if event.key == pygame.K_LSHIFT:
                        for point in pointsSelected:
                            point.selected = False
                        selectedPoint = None
                        pointsSelected = []
                        numberSelected = 0
                        lShiftDown = False
                        state = stateDict["awaitSelect"]
                if event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_TAB:
                        if numberSelected == 2:
                            state = stateDict["lineAction"]
                        if numberSelected == 3:
                            state = stateDict["quadraticBezierAction"]
                    if event.key == pygame.K_x and numberSelected == 1:
                        # Create a mirror x point
                        points.append(SymbolPoint.Point(2**15 - selectedPoint.x, selectedPoint.y))
                    if event.key == pygame.K_c and numberSelected == 1:
                        # Create a mirror x point
                        points.append(SymbolPoint.Point(selectedPoint.x, 2**15 -  selectedPoint.y))

                if event.type == pygame.MOUSEBUTTONDOWN:
                    if event.button == 1:
                        for point in points:
                            x = point.xToPygameCoords(zoom, xOffset, DISPLAY_SIZE)
                            y = point.yToPygameCoords(zoom, yOffset, DISPLAY_SIZE)
                            mouse_pos = pygame.mouse.get_pos()
                            if (x-mouse_pos[0])**2 + (y-2*yOffset-mouse_pos[1])**2 < 15**2:
                                if numberSelected < 3:
                                    numberSelected += 1
                                    point.selected = True
                                    pointsSelected.append(point)
                                    break                 
        elif state == stateDict["lineAction"]:
            bezierToRemove = None
            for bezier in bezierList:
                if isinstance(bezier, Bezier.LinearBezier):
                    if bezier.p1 in pointsSelected and bezier.p2 in pointsSelected:
                        bezierToRemove = bezier
            # Removing Linear Bezier Proccess
            if bezierToRemove != None:
                if bezierToRemove.p1.type == "start":
                    bezierToRemove.p1.type = None
                elif bezierToRemove.p1.type == "middle":
                    bezierToRemove.p1.type = "end"
                if bezierToRemove.p2.type == "middle":
                    bezierToRemove.p2.type = "start"
                elif bezierToRemove.p2.type == "end":
                    bezierToRemove.p2.type = None
                bezierList.remove(bezierToRemove)
                #print(bezierList)
            # Adding Linear Bezier Proccess
            else:
                if (pointsSelected[0].type == None or pointsSelected[0].type == "end") and (pointsSelected[1].type == None or pointsSelected[1].type == "start"):
                    if pointsSelected[0].type == None:
                        pointsSelected[0].type = "start"
                    elif pointsSelected[0].type == "end":
                        pointsSelected[0].type = "middle"
                    if pointsSelected[1].type == None:
                        pointsSelected[1].type = "end"
                    elif pointsSelected[1].type == "start":
                        pointsSelected[1].type = "middle"
                    bezierList.append(Bezier.LinearBezier(pointsSelected[0], pointsSelected[1]))
                #print(bezierList)

            for point in pointsSelected:
                point.selected = False
            selectedPoint = None
            pointsSelected = []
            numberSelected = 0
            lShiftDown = False
            state = stateDict["awaitSelect"]
        elif state == stateDict["quadraticBezierAction"]:
            bezierToRemove = None
            for bezier in bezierList:
                if isinstance(bezier, Bezier.QuadraticBezier):
                    if bezier.p1 in pointsSelected and bezier.p2 in pointsSelected and bezier.p3 in pointsSelected:
                        bezierToRemove = bezier
            # Removing Quadratic Bezier Proccess
            if bezierToRemove != None:
                if bezierToRemove.p1.type == "start":
                    bezierToRemove.p1.type = None
                elif bezierToRemove.p1.type == "middle":
                    bezierToRemove.p1.type = "end"
                bezierToRemove.p2.type = None
                if bezierToRemove.p3.type == "middle":
                    bezierToRemove.p3.type = "start"
                elif bezierToRemove.p3.type == "end":
                    bezierToRemove.p3.type = None
                bezierList.remove(bezierToRemove)
                #print(bezierList)
            # Adding Quadratic Bezier Proccess
            else:
                if (pointsSelected[0].type == None or pointsSelected[0].type == "end")\
                        and pointsSelected[1].type == None\
                        and (pointsSelected[2].type == None or pointsSelected[2].type == "start"):
                    if pointsSelected[0].type == None:
                        pointsSelected[0].type = "start"
                    elif pointsSelected[0].type == "end":
                        pointsSelected[0].type = "middle"
                    pointsSelected[1].type = "middle"
                    if pointsSelected[2].type == None:
                        pointsSelected[2].type = "end"
                    elif pointsSelected[2].type == "start":
                        pointsSelected[2].type = "middle"
                    bezierList.append(Bezier.QuadraticBezier(pointsSelected[0], pointsSelected[1], pointsSelected[2]))
                    #print(bezierList)

            for point in pointsSelected:
                point.selected = False
            selectedPoint = None
            pointsSelected = []
            numberSelected = 0
            lShiftDown = False
            state = stateDict["awaitSelect"]
        elif state == stateDict["move"]:
            renderGrid(screen, zoom, xOffset, yOffset)
            renderBoundarys(screen, zoom, xOffset, yOffset)
            renderBezier(screen, bezierList, zoom, xOffset, yOffset)
            renderPoints(screen, points, zoom, xOffset, yOffset)
            for event in pygame.event.get():
                if lMouseDown:
                    if event.type == pygame.MOUSEMOTION:
                        xOffset += event.rel[0]
                        yOffset -= event.rel[1]
                if event.type == pygame.QUIT:
                    running = False
                if event.type == pygame.KEYUP:
                    if event.key == pygame.K_SPACE:
                        lMouseDown = False
                        state = stateDict["awaitSelect"]
                if event.type == pygame.MOUSEBUTTONDOWN:
                    if event.button == 1:
                        lMouseDown = True
                if event.type == pygame.MOUSEBUTTONUP:
                    if event.button == 1:
                        lMouseDown = False
        if state == stateDict["dragSelect"]:
            renderGrid(screen, zoom, xOffset, yOffset)
            renderBoundarys(screen, zoom, xOffset, yOffset)
            renderBezier(screen, bezierList, zoom, xOffset, yOffset)
            renderPoints(screen, points, zoom, xOffset, yOffset)
            dragEnd = [xPygameCoordsToPointCoords(pygame.mouse.get_pos()[0], zoom, xOffset),
                                yPygameCoordsToPointCoords(pygame.mouse.get_pos()[1], zoom, yOffset)]
            pygame.draw.line(screen, (0,0,255), 
                [
                    xPointCoordsToPygameCoords(dragStart[0], zoom, xOffset),
                    yPointCoordsToPygameCoords(dragStart[1], zoom, yOffset),],
                [
                    xPointCoordsToPygameCoords(dragStart[0], zoom, xOffset),
                    yPointCoordsToPygameCoords(dragEnd[1], zoom, yOffset),])
            pygame.draw.line(screen, (0,0,255), 
                [
                    xPointCoordsToPygameCoords(dragStart[0], zoom, xOffset),
                    yPointCoordsToPygameCoords(dragStart[1], zoom, yOffset),],
                [
                    xPointCoordsToPygameCoords(dragEnd[0], zoom, xOffset),
                    yPointCoordsToPygameCoords(dragStart[1], zoom, yOffset),])
            pygame.draw.line(screen, (0,0,255), 
                [
                    xPointCoordsToPygameCoords(dragEnd[0], zoom, xOffset),
                    yPointCoordsToPygameCoords(dragEnd[1], zoom, yOffset),],
                [
                    xPointCoordsToPygameCoords(dragStart[0], zoom, xOffset),
                    yPointCoordsToPygameCoords(dragEnd[1], zoom, yOffset),])
            pygame.draw.line(screen, (0,0,255), 
                [
                    xPointCoordsToPygameCoords(dragEnd[0], zoom, xOffset),
                    yPointCoordsToPygameCoords(dragEnd[1], zoom, yOffset),],
                [
                    xPointCoordsToPygameCoords(dragEnd[0], zoom, xOffset),
                    yPointCoordsToPygameCoords(dragStart[1], zoom, yOffset),])
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    running = False
                if event.type == pygame.MOUSEBUTTONUP:
                    if event.button == 1:
                        dragEnd = [xPygameCoordsToPointCoords(pygame.mouse.get_pos()[0], zoom, xOffset),
                                yPygameCoordsToPointCoords(pygame.mouse.get_pos()[1], zoom, yOffset)]
                        pointsSelected = []
                        for p in points:
                            if (p.x > dragStart[0] and p.x < dragEnd[0] or p.x < dragStart[0] and p.x > dragEnd[0])\
                                    and (p.y > dragStart[1] and p.y < dragEnd[1] or p.y < dragStart[1] and p.y > dragEnd[1]):
                                p.dragSelected = True
                                pointsSelected.append(p)
                        if len(pointsSelected) > 0:
                            state = stateDict["multiMove"]
                        else:
                            pointsSelected = []
                            dragStart = None
                            dragEnd = None
                            state = stateDict["awaitSelect"]


        if state == stateDict["multiMove"]:
            renderGrid(screen, zoom, xOffset, yOffset)
            renderBoundarys(screen, zoom, xOffset, yOffset)
            renderBezier(screen, bezierList, zoom, xOffset, yOffset)
            renderPoints(screen, points, zoom, xOffset, yOffset)
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    running = False
                if event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_1:
                        moveBy = 2**2
                    elif event.key == pygame.K_2:
                        moveBy = 2**3
                    elif event.key == pygame.K_3:
                        moveBy = 2**4
                    elif event.key == pygame.K_4:
                        moveBy = 2**5
                    elif event.key == pygame.K_5:
                        moveBy = 2**6
                    elif event.key == pygame.K_6:
                        moveBy = 2**7
                    elif event.key == pygame.K_7:
                        moveBy = 2**8
                    elif event.key == pygame.K_8:
                        moveBy = 2**9
                    elif event.key == pygame.K_9:
                        moveBy = 2**10
                    elif event.key == pygame.K_9:
                        moveBy = 2**11
                    elif event.key == pygame.K_0:
                        moveBy = 0
                    elif event.key == pygame.K_LEFT:
                        for p in pointsSelected:
                            p.x -= moveBy
                    elif event.key == pygame.K_RIGHT:
                        for p in pointsSelected:
                            p.x += moveBy
                    elif event.key == pygame.K_UP:
                        for p in pointsSelected:
                            p.y += moveBy
                    elif event.key == pygame.K_DOWN:
                        for p in pointsSelected:
                            p.y -= moveBy
                    if event.key == pygame.K_y:
                        newSelectedPoints = []
                        print(len(points))
                        for p in pointsSelected:
                            newPoint = SymbolPoint.Point(p.x, 2**15 - p.y)
                            newPoint.dragSelected = True
                            p.dragSelected = False
                            newSelectedPoints.append(newPoint)
                            points.append(newPoint)
                        print(len(points))
                        pointsSelected = newSelectedPoints
                if event.type == pygame.MOUSEBUTTONDOWN:
                    if event.button == 3:
                        for p in pointsSelected:
                            p.dragSelected = False
                        pointsSelected = []
                        dragStart = None
                        dragEnd = None
                        state = stateDict["awaitSelect"]
        



        # Render a text surface indicating the state
        stateRendedAsTestSurface = font.render(stateDictReverse[state], False, (0, 0, 0))
        screen.blit(stateRendedAsTestSurface, (10,10))
        #
        pygame.display.flip()
        # end while
    pygame.quit()


run("font/BlockFont/C.symbol")

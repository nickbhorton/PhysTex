class Point:
    def __init__(self, x, y, type=None):
        self.selected = False
        self.dragSelected = False
        self.x = x
        self.y = y
        self.type = type

    def xToPygameCoords(self, zoom, xOffset, DISPLAY_SIZE):
        return (self.x/(2**15))*zoom*DISPLAY_SIZE[0] + (xOffset)
    
    def yToPygameCoords(self, zoom, yOffset, DISPLAY_SIZE):
        return 800 - (self.y/(2**15))*zoom*DISPLAY_SIZE[1] + (yOffset)

    
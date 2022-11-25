package font.editor.javaAttempt;

import font.editor.javaAttempt.RenderedPoint;
import maths.Point;
import maths.curves.Bezier;
import maths.curves.QuadraticBezier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Canvas extends JPanel implements MouseListener {
    ArrayList<RenderedPoint> points;
    ArrayList<Bezier> bezierList;
    RenderedPoint selectedPoint = null;
    Bezier selectedBezier = null;
    RenderedPoint lastPoint;
    public Canvas(){
        this.setBackground(Color.lightGray);
        this.addMouseListener(this);
        points = new ArrayList<>();
        bezierList = new ArrayList<>();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for (int i = 0; i < points.size(); i++){
            RenderedPoint workingPoint = points.get(i);
            int x = workingPoint.getX();
            int y = workingPoint.getY();
            Color c = Color.black;
            if (workingPoint.getBezierCurvePartOf() == selectedBezier){
                c = Color.blue;
            }
            else {
                c = Color.BLACK;
            }
            g.setColor(c);
            g.fillOval(x-5,y-5, 10,10);
            if (selectedPoint == workingPoint) {
                g.setColor(Color.red);
                g.drawOval(x - workingPoint.clickableRadius / 2, y - workingPoint.clickableRadius / 2, workingPoint.clickableRadius, workingPoint.clickableRadius);
            }
            g.setColor(c);
        }
        for (int i = 0; i < bezierList.size(); i++) {
            Bezier workingBezier = bezierList.get(i);
            if (workingBezier instanceof QuadraticBezier){
                QuadraticBezier workingQuadraticBezier = (QuadraticBezier) workingBezier;
                Color c = Color.black;
                if (workingQuadraticBezier == selectedBezier){
                    c = Color.blue;
                }
                else {
                    c = Color.black;
                }
                g.setColor(c);
                RenderedPoint p1 = points.get(workingQuadraticBezier.al1);
                RenderedPoint p2 = points.get(workingQuadraticBezier.al2);
                RenderedPoint p3 = points.get(workingQuadraticBezier.al3);
                workingQuadraticBezier.setP1(new Point((double)p1.getX()/(double) 800, 1.0d - (double)p1.getY()/(double) 800));
                workingQuadraticBezier.setP2(new Point((double)p2.getX()/(double) 800, 1.0d - (double)p2.getY()/(double) 800));
                workingQuadraticBezier.setP3(new Point((double)p3.getX()/(double) 800, 1.0d - (double)p3.getY()/(double) 800));
                double[][] vertexArray = workingQuadraticBezier.getVertexArrayDouble(30, 800, 0, 0);
                for (int n = 0; n < vertexArray.length-1; n++){
                    double[] startVertex = vertexArray[n];
                    double[] endVertex = vertexArray[n+1];
                    g.drawLine((int)startVertex[0], 800-(int)startVertex[1], (int)endVertex[0], 800-(int)endVertex[1]);
                }
            }
        }
    }

    public RenderedPoint getPoint(int x, int y){
        for (int i = 0; i < points.size(); i++){
            if (Math.sqrt(Math.pow(x - points.get(i).getX(), 2) + Math.pow(y - points.get(i).getY(), 2)) < 25){
                return points.get(i);
            }
        }
        return null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (e.getButton() == 1){ // left click
            if (selectedPoint == null){
                selectedPoint = getPoint(x, y);
                selectedBezier = selectedPoint.getBezierCurvePartOf();
            }
            else {
                selectedPoint = null;
                selectedBezier = null;
            }
        }
        else if (e.getButton() == 2){ // middle click
            System.out.println("2");
        }
        else if (e.getButton() == 3){ // right click
            if (bezierList.size() == 0) {
                QuadraticBezier q = new QuadraticBezier();
                RenderedPoint p2 = new RenderedPoint(x, y, q);
                RenderedPoint p1 = new RenderedPoint(x + 50, y, q);
                RenderedPoint p3 = new RenderedPoint(x - 50, y, q);
                q.setP1(new Point((double)p1.getX()/(double) 800, 1.0d - (double)p1.getY()/(double) 800));
                q.setP2(new Point((double)p2.getX()/(double) 800, 1.0d - (double)p2.getY()/(double) 800));
                q.setP3(new Point((double)p3.getX()/(double) 800, 1.0d - (double)p3.getY()/(double) 800));
                // al# stores the index of the point in the points array
                q.al1 = points.size();
                points.add(p1);
                q.al2 = points.size();
                points.add(p2);
                q.al3 = points.size();
                points.add(p3);
                lastPoint = p3;
                bezierList.add(q);
            }
            else {
                QuadraticBezier q = new QuadraticBezier();
                RenderedPoint p1 = new RenderedPoint(lastPoint.getX(), lastPoint.getY(), q);
                RenderedPoint p2 = new RenderedPoint(x, y, q);
                RenderedPoint p3 = new RenderedPoint(x + 50, y, q);
                q.setP1(new Point((double)p1.getX()/(double) 800, 1.0d - (double)p1.getY()/(double) 800));
                q.setP2(new Point((double)p2.getX()/(double) 800, 1.0d - (double)p2.getY()/(double) 800));
                q.setP3(new Point((double)p3.getX()/(double) 800, 1.0d - (double)p3.getY()/(double) 800));
                q.al1 = points.size() - 1;
                q.al2 = points.size();
                points.add(p2);
                q.al3 = points.size();
                points.add(p3);
                lastPoint = p3;
                bezierList.add(q);
            }
            this.repaint();
        }
        else if (e.getButton() == 4){
            this.repaint();
        }
        else if (e.getButton() == 5){
            if (selectedPoint != null){
                selectedPoint.setX(x);
                selectedPoint.setY(y);
            }
            this.repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

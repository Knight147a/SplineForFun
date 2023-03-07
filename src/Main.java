import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Main extends Canvas implements MouseListener, MouseMotionListener{
    private final Point point0, point1, point2, point3;
    private Point draggedPoint;
    private final int pointRadius = 10;

    private Image offscreen;

    public Main() {
        point0 = new Point(100, 300);
        point3 = new Point(500, 300);
        point1 = new Point(250, 100);
        point2 = new Point(450, 100);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void update(Graphics g){
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        if (offscreen == null) {
            offscreen = createImage(getWidth(), getHeight());
        }
        Graphics offgc = offscreen.getGraphics();
        offgc.setColor(Color.BLACK);
        offgc.fillRect(0, 0, getWidth(), getHeight());

        offgc.setColor(Color.RED);
        offgc.fillOval(point0.x - pointRadius, point0.y - pointRadius, 2 * pointRadius, 2 * pointRadius);
        offgc.fillOval(point1.x - pointRadius, point1.y - pointRadius, 2 * pointRadius, 2 * pointRadius);
        offgc.fillOval(point2.x - pointRadius, point2.y - pointRadius, 2 * pointRadius, 2 * pointRadius);
        offgc.fillOval(point3.x - pointRadius, point3.y - pointRadius, 2 * pointRadius, 2 * pointRadius);

        int x_prev = point0.x;
        int y_prev = point0.y;
        int x = 0;
        int y = 0;

        for(double t = 0.00; t <= 1.001; t += 0.05){
            if(t > 0.00){
                x_prev = x;
                y_prev = y;
            }

            offgc.setColor(Color.BLACK);
            Point resultPoint1 = quadratic(point0, point1, point2, t, offgc);
            Point resultPoint2 = quadratic(point1, point2, point3, t, offgc);

            x = (int) (resultPoint1.x + t*(resultPoint2.x - resultPoint1.x));
            y = (int) (resultPoint1.y + t*(resultPoint2.y - resultPoint1.y));

            offgc.setColor(Color.getHSBColor((float) t, 1f, 1f));
            offgc.drawLine(resultPoint1.x, resultPoint1.y, resultPoint2.x, resultPoint2.y);

            offgc.setColor(Color.WHITE);
            offgc.drawLine(x_prev, y_prev, x, y);

        }
        g.drawImage(offscreen, 0, 0, null);
    }

    public Point quadratic(Point point_0, Point point_1, Point point_2, double t, Graphics offgc){
        int x, y, x1, y1, x2, y2;

        x1 = (int) (point_0.x + t*(point_1.x - point_0.x));
        y1 = (int) (point_0.y + t*(point_1.y - point_0.y));

        x2 = (int) (point_1.x + t*(point_2.x - point_1.x));
        y2 = (int) (point_1.y + t*(point_2.y - point_1.y));

        x = (int) (x1 + t*(x2 - x1));
        y = (int) (y1 + t*(y2 - y1));

        offgc.setColor(Color.getHSBColor((float) t, 1f, 1f));
        offgc.drawLine(x1, y1, x2, y2);
        return new Point(x, y);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (distance(x, y, point1.x, point1.y) <= pointRadius) {
            draggedPoint = point1;
        } else if (distance(x, y, point2.x, point2.y) <= pointRadius) {
            draggedPoint = point2;
        } else if (distance(x, y, point0.x, point0.y) <= pointRadius) {
            draggedPoint = point0;
        } else if (distance(x, y, point3.x, point3.y) <= pointRadius) {
            draggedPoint = point3;
        } else {
            draggedPoint = null;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (draggedPoint != null) {
            draggedPoint.setLocation(e.getX(), e.getY());
            repaint();
        }
    }

    private int distance(int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        return (int) Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}

    public static void main(String[] args) {
        Frame frame = new Frame("Cool stuff");
        Main canvas = new Main();
        canvas.setSize(600, 600);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
    }
}

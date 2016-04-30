package proba;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import javax.imageio.ImageIO;

public class JumbleImage extends Component {

	private static final long serialVersionUID = 1L;
	private int numlocs = 3;
    private int numcells = numlocs*numlocs;
    private int[] cells;
    private int[] completed;
    private Image bi;
    private Image blank;
    int w, h, cw, ch, moves = 0;
    Instant start;
    Instant end;
    
    public JumbleImage(String cim, int maxsize) {
        try {
            bi = ImageIO.read(new File(cim));
        } catch (IOException e) {
            System.out.println("Image could not be read");
            // System.exit(1);
        }
        if(bi.getWidth(null) >= bi.getHeight(null)){
        	bi = bi.getScaledInstance(maxsize, -1, Image.SCALE_SMOOTH);
        }
        else {
        	bi = bi.getScaledInstance(-1, maxsize, Image.SCALE_SMOOTH);
        }
        w = bi.getWidth(null);
        h = bi.getHeight(null);
        cw = w/numlocs;
        ch = h/numlocs;
        cells = new int[numcells];
        completed = new int[numcells];
        for (int i=0;i<numcells;i++) {
            cells[i] = i;
            completed[i] = i;
        }
        
        try {
            blank = ImageIO.read(new File("blank.jpg"));
        } catch (IOException e) {
            System.out.println("Blank image could not be read");
            // System.exit(1);
        }
        if(w >= h){
        	blank = blank.getScaledInstance(maxsize, -1, Image.SCALE_SMOOTH);
        }
        else {
        	blank = blank.getScaledInstance(-1, maxsize, Image.SCALE_SMOOTH);
        }
        start = Instant.now();
    }

    void jumble() {
        Random rand = new Random();
        int ri;
        for (int i=0; i<numcells; i++) {
            while ((ri = rand.nextInt(numlocs)) == i);

            int tmp = cells[i];
            cells[i] = cells[ri];
            cells[ri] = tmp;
        }
    }
    // x,y a képrészlet koordinátái, amire kattintottunk
    // tx,ty a "szürke" képrészlet koordinátái
    public void move(int x, int y) {
    	int tx = 0, ty = 0, ok = 0;
    	
    	x = x/(w/numlocs);
    	y = y/(h/numlocs);
    	
    	for(int i=0; i<numlocs; i++) {
    		for(int j=0; j<numlocs; j++) {
    			if(cells[i*numlocs+j] == cells.length-1) {
    				tx = i;
    				ty = j;
    				ok = 1;
    				break;
    			}
    		}
    		if(ok == 1) break;
    	}
    	if(Math.abs(x - tx) + Math.abs(y - ty) == 1) {
	    	int tmp = cells[x*numlocs+y];
            cells[x*numlocs+y] = cells[tx*numlocs+ty];
            cells[tx*numlocs+ty] = tmp;
            moves++;
    	}
    }
    
    public String getmoves(){
    	return "" + moves;
    }
    
    public int check()
    {
    	int good = 0;
    	for(int i=0; i<numlocs; i++) {
    		for(int j=0; j<numlocs; j++) {
    			if(completed[i*numlocs+j] == cells[i*numlocs+j]) {
    				good++;
    			}
    		}
    	}
    	return good;
    }
    
    public String elapsed_time(){
    	int[] time;
    	String result = "";
    	time = new int[3];
    	end = Instant.now();
    	long s = Duration.between(start, end).getSeconds();
    	time[0] = (int) (s/3600);
    	if(time[0] != 0) result += time[0] + ":";
    	s %= 3600;
    	time[1] = (int) (s/60);
    	result += time[1] + ":";
    	s %= 60;
    	time[2] = (int) s;
    	if(time[2] < 10) result += "0" + time[2];
    	else result += time[2];
    	return result;
    }

    public Dimension getPreferredSize() {
        return new Dimension(w, h);
    }

    // sx,sy az eredeti képen hol van a képrészlet
    // dx,dy ahova a képrészletet rakni akarjuk
    public void paint(Graphics g) {
        int sx, sy;
        for (int x=0; x<numlocs; x++) {
            int dx = x*cw;
            for (int y=0; y<numlocs; y++) {
                int dy = y*ch;
                int cell = cells[x*numlocs+y];
                sx = (cell / numlocs) * cw;
                sy = (cell % numlocs) * ch;
                if(cell == cells.length-1){
                	g.drawImage(blank,
                            dx, dy, dx+cw, dy+ch,
                            sx, sy, sx+cw, sy+ch,
                            null);
                }
                else {
                	g.drawImage(bi,
                            dx, dy, dx+cw, dy+ch,
                            sx, sy, sx+cw, sy+ch,
                            null);
                }
            }
        }
    }
}

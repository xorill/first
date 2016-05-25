package puzzle;

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
	private int numlocs = 0;
    private int numcells = 0;
    private int[] cells;
    private int[] completed;
    private Image bi;
    private Image blank;
    private Image numbered;
    int w, h, cw, ch, moves = 0, offset = 0;
    Instant start;
    Instant end;
    
    /**
     * A hálózatkezelést megvalósító objektum (multiplayer mód esetén).
     */
    private Network net = null;

    /**
     * True, ha multiplayer módban vagyunk, különben false.
     */
    private boolean multi;
    
    /**
     * True, ha elkezdõdött a játék, különben false.
     */
    private boolean started=false;
    
    /**
     * Multiplayer módban jelzi, hogy van-e élõ kapcsolat. (Volatile, mert a hálózati szál állítja!)
     */
    private volatile boolean conn=false;
    
    /**
     * Multiplayer módban az ellenfél lépéseinek száma.
     */
    private int oppMoves=0;
    
    /**
     * Multiplayer módban az ellenfél jó helyen lévõ elemeinek száma.
     */
    private int oppRight=0;
    
    boolean numbers=false;
    private boolean stop=false;
    String imgadr = "";
    
    
    
    /**
     * Paraméter nélküli konstruktor.
     * <p>
     * Létrehoz egy objektumot, de a képet és a tábla méretét még késõbb definiálni kell.
     */
    public JumbleImage(){
        try {
            blank = ImageIO.read(new File("blank.jpg"));
        } catch (IOException e) {
            System.out.println("Blank image could not be read");
            // System.exit(1);
        }
    }
    
    /**
     * Paraméteres konstruktor.
     * <p>
     * Létrehoz egy objektumot, megnyitja a megadott képet, beállítja a tábla méretét.
     * 
     * @param cim a képfájl neve
     * @param maxsize a kirajzolt kép mérete
     * @param pieces a tábla mérete
     */
    public JumbleImage(String cim, int maxsize, int pieces) {
        this();
        numlocs = pieces;
        numcells = numlocs*numlocs;
        cells = new int[numcells];
        setImage(cim,maxsize);
    }
    
    /**
     * Beállítja a megadott képet.
     * 
     * @param cim a képfájl neve
     * @param maxsize a kirajzolt kép mérete
     */
    public void setImage(String cim, int maxsize){
    	if(cim.indexOf("numbers")>=0){
    		cim="numbers" + numlocs + "x"+ numlocs + ".png";
    	}
    	imgadr=cim;
    	try {
            numbered = ImageIO.read(new File(numlocs + "x"+ numlocs + ".png"));
        } catch (IOException e) {
            System.out.println("Numbered image could not be read");
            // System.exit(1);
        }
    	try {
            bi = ImageIO.read(new File(cim));
        } catch (IOException e) {
            System.out.println("Image could not be read");
            System.out.println(cim);
            // System.exit(1);
        }
        if(bi.getWidth(null) >= bi.getHeight(null)){
        	bi = bi.getScaledInstance(maxsize, -1, Image.SCALE_SMOOTH);
        }
        else {
        	bi = bi.getScaledInstance(-1, maxsize, Image.SCALE_SMOOTH);
        }
        offset = 0;
        w = bi.getWidth(null);
        h = bi.getHeight(null);
        cw = w/numlocs;
        ch = h/numlocs;
        completed = new int[numcells];
        for (int i=0;i<numcells;i++) {
            cells[i] = i;
            completed[i] = i;
        }
        
        blank = blank.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        numbered = numbered.getScaledInstance(w, h, Image.SCALE_SMOOTH);
    }
    
    /**
     * Elindítja az idõzítõt.
     */
    void startTimer(){
    	started=true;
    	start = Instant.now();
    }

    void jumble() {
        Random rand = new Random();
        int ri;
        int to=0;
        int blank=numcells-1;
        int tmp;
        while(check()>0.2*numcells){
	        for (int i=0; i<40; i++) {
	        	ri = rand.nextInt(4);
	        	switch (ri){
		        	case 0:
		        		if(blank-numlocs >= 0) to=blank-numlocs;
		        		else blank=to;
		        		break;
		        	case 1:
		        		if(blank-1 >= 0) to=blank-1;
		        		else blank=to;
		        		break;
		        	case 2:
		        		if(blank+1 <= numcells-1) to=blank+1;
		        		else blank=to;
		        		break;
		        	case 3:
		        		if(blank+numlocs <= numcells-1) to=blank+numlocs;
		        		else blank=to;
		        		break;
	        	}
	        	tmp = cells[to];
                cells[to] = cells[blank];
                cells[blank] = tmp;
                blank=to;
	        }
            
        }
        stop = false;
    }
    // x,y a kÃ„â€šÃ‚Â©prÃ„â€šÃ‚Â©szlet koordinÃ„â€šÃ‹â€¡tÃ„â€šÃ‹â€¡i, amire kattintottunk
    // tx,ty a "szÃ„â€šÃ„Ërke" kÃ„â€šÃ‚Â©prÃ„â€šÃ‚Â©szlet koordinÃ„â€šÃ‹â€¡tÃ„â€šÃ‹â€¡i
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
    	if(good == numcells) stop = true;
    	return good;
    }
    
    public String elapsedTime(){
    	int[] time;
    	String result = "";
    	time = new int[3];
    	if(started==false)
    		return "0:00";
    	if(stop == false){
    		end = Instant.now();
    	}
    	long s = Duration.between(start, end).getSeconds() + offset;
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
    
    public String state(){
    	String status = "";
    	status += numlocs + ",";
    	status += moves + ",";
    	status += elapsedTime() + ",";
    	for(int i=0; i<numlocs; i++) {
    		for(int j=0; j<numlocs; j++) {
    				status += cells[i*numlocs+j] + ",";
    		}
    	}
    	return status;
    }
    
    public void loadState(String data){
    	moves = Integer.parseInt(data.substring(0, data.indexOf(',')));
	    data = data.substring(data.indexOf(',')+1);
	    int k = data.indexOf(',');
	    offset = Integer.parseInt(data.substring(k-2, k));
	    offset += Integer.parseInt(data.substring(k-5>=0 ? k-5 : k-4, k-3))*60;
	    if(k-6>0) offset += Integer.parseInt(data.substring(k-7, k-6))*3600;
	    data = data.substring(data.indexOf(',')+1);
    	for(int i=0; i<numlocs; i++) {
    		for(int j=0; j<numlocs; j++) {
    			cells[i*numlocs+j] = Integer.parseInt(data.substring(0,data.indexOf(',')));
    			System.out.print(cells[i*numlocs+j]);
    			if(data != "") data = data.substring(data.indexOf(',')+1);
    		}
    	}
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(w, h);
    }

    // sx,sy az eredeti kepen hol van a kepreszlet
    // dx,dy ahova a kepreszletet rakni akarjuk
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
                	if(numbers==true)
                		g.drawImage(numbered,
                            dx, dy, dx+cw, dy+ch,
                            sx, sy, sx+cw, sy+ch,
                            null);
                }
            }
        }
    }
    
    /**
     * Multiplayer mód indítása szerverként. 
     * <p>
     * @param port - ezen a porton várjuk a klienst
     */
    void startServer(int port) {
		if (net != null)
			net.disconnect();
		net = new NetworkServer(this);
		net.connect("localhost",port);
	}

    /**
     * Multiplayer mód indítása kliensként. 
     * <p>
     * @param ip - a szerver IP címe
     * @param port - ezen a porton csatlakozunk a szerverhez
     */
	void startClient(String ip, int port) {
		if (net != null)
			net.disconnect();
		net = new NetworkClient(this);
		net.connect(ip,port);
	}
    
    /**
     * Multiplayer módban a hálózati kapcsolat bontása.
     */
	void disconnect(){
		net.disconnect();
		conn=false;
	}
	
    /**
     * Hálózati vétel. 
     * <p>
     * Multiplayer módban van jelentõsége: feldolgozza a vett adatokat. Ez lehet
     * az ellenfél állása, illetve kliens esetén a kép neve és a kezdeti keverés.
     * 
     * @param s a vett sztring tömb
     */
    public void receive(String[] s){
    	if(s.length==2){ //ha ketelemu, akkor az ellenfel allasa van benne
    		oppMoves=Integer.parseInt(s[0]);
    		oppRight=Integer.parseInt(s[1]);
    	}
    	else{ //ha tobb, akkor a kep neve es a keveres
    		imgadr=s[0];
    		numlocs=Integer.parseInt(s[1]);
            numcells = numlocs*numlocs;
            cells = new int[numcells];
			setImage(imgadr,575);
			String[] c=s[2].split(",");
    		for(int i=0;i<numcells;i++)
    			cells[i]=Integer.parseInt(c[i]);
    		conn=true;
    	}
    }
        
    /**
     * Elküldi a kép nevét és a keverést. 
     * <p>
     * Multiplayer módban, szerver szerepben van jelentõsége: ez biztosítja, hogy
     * mindkét fél ugyanazon a képen dolgozzon, ugyanazzal a keveréssel.
     */
    public void sendState(){ //elkuldi a kep nevet es a keverest
    	String[] s=new String[3];
    	s[0]=imgadr;
    	s[1]=""+numlocs;
    	s[2]="";
    	for(int i=0;i<numcells;i++){
    		s[2]+=cells[i]+",";
    	}
    	net.send(s);
    }
    
    /**
     * Lépések és helyben lévõ elemek számának küldése. 
     * <p>
     * Multiplayer módban van jelentõsége: elküldi az ellenfélnek, hogy hány elemet
     * mozgattunk már meg, és éppen mennyi van a helyén.
     */
    public void send(){
    	String[] s=new String[2];
    	s[0]=""+getmoves();
    	s[1]=""+check();
    	net.send(s);
    }
    
    /**
     * Getter az oppMoves változóhoz.
     * <p>
     * Multiplayer módban van jelentõsége: jelzi, hogy az ellenfél hány elemet mozgatott már meg.
     * @return az ellenfél lépéseinek száma sztringként
     */
    public String getOppMoves(){
    	return ""+oppMoves;
    }
    
    /**
     * Getter az oppRight változóhoz.
     * <p>
     * Multiplayer módban van jelentõsége: jelzi, hogy az ellenfél hány elemet tett már a helyére.
     * @return az ellenfél jó helyen lévõ elemeinek száma sztringként
     */
    public String getOppRight(){
    	return ""+oppRight;
    }
    
    /**
     * Getter a multi változóhoz.
     * @return true, ha multiplayer módban vagyunk, különben false
     */
    public boolean isMulti(){
    	return multi;
    }

    /**
     * Setter a multi változóhoz.
     * <p>
     * Ezzel jelezhetjük, hogy multiplayer módban vagyunk-e.
     * @param b beállítja a multi változót
     */
    public void setMulti(boolean b){
    	multi=b;
    }
    
    /**
     * Getter a conn változóhoz.
     * <p>
     * Multiplayer módban van jelentõsége: jelzi, hogy éppen van-e élõ kapcsolat.
     * @return true, ha van élõ kapcsolat, különben false
     */
    public boolean isConnected(){
    	return conn;
    }
    
    /**
     * Setter a conn változóhoz.
     * <p>
     * Multiplayer módban van jelentõsége: ezzel jelezhetjük, hogy éppen van-e élõ kapcsolat.
     * @param b beállítja a conn változót
     */
    public void setConnected(boolean b){
    	conn=b;
    }
    
    /**
     * Getter a numcells változóhoz.
     * @return a cellák száma
     */
    public int getNumCells(){
    	return numcells;
    }
    
    /**
     * Setter a stop változóhoz.
     * <p>
     * A stop változó igazra állításával a játék megállítható.
     * @param b beállítja a stop változót
     */
    public void setStop(boolean b){
    	stop=b;
    }

    /**
     * Getter a stop változóhoz.
     * @return true, ha áll a játék, különben false
     */
    public boolean getStop(){
    	return stop;
    }
    
}

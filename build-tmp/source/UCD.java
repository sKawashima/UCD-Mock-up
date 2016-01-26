import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class UCD extends PApplet {

PImage back;
PImage chip;
float[][] buttonPos = {
	{1274,140,1},
	{1161,140,0},
	{1048,140,1},
	{ 935,140,1},
	{ 822,140,1},
	{ 708,140,1},
	{ 595,140,1},
	{ 482,140,0},
	{ 368,140,0},
	{ 255,140,0},
	{ 142,215,0},
	{ 255,290,0},
	{ 368,290,0},
	{ 482,290,0}
};
float[] busTime = {3, 1, 1, 1, 1, 1, 1, 1, 2, 3, 2, 1, 3, 2, 1, 1, 1, 1, 4};

Button[] button = new Button[14];
int selected = -1;
int count = 0;
int now = 0;

int status = 0;

PFont kozu20;
PFont kozu30;

public void setup() {
	kozu20 = createFont("KozGoPro-Medium",20,true);
	kozu30 = createFont("KozGoPro-Medium",30,true);

	
	back = loadImage("UCD.png");
	chip = loadImage("chip.png");

	for (int i = 0; i < 14; ++i) {
		button[i] = new Button(buttonPos[i]);
	}
	button[0].condition = 1;
	frameRate(10);
}

public void draw() {
	background(255);
	image(back,0,0,width,PApplet.parseFloat(back.height) / PApplet.parseFloat(back.width) * width);
	for (int i = 0; i < 14; ++i) {
		button[i].display();
	}

	if(count > busTime[now] * 30){
		now++;
		if(now == 19) now = 0;
		count = 0;
		if(status == 4) status = 0;
		if(status == 3) status = 4;
		if(nextStop(now) == selected) status = 3;
		button[nowStop(now)].condition = 1;
		button[preStop(now)].condition = 0;

	}
	/*
	if(now == 14){
		button[6].condition = 1;
		button[13].condition = 0;
	}else if(now > 14){
		button[20 - now].condition = 1;
		button[21 - now].condition = 0;
	}else if(now == 0){
		button[0].condition = 1;
		button[2].condition = 0;
	}else{
		button[now].condition = 1;
		button[now - 1].condition = 0;
	}
	*/
	
	if((count / 5) % 2 == 0){
		button[nextStop(now)].condition = 1;
	}else{
		if(nextStop(now) == selected) button[nextStop(now)].condition = 2;
		else button[nextStop(now)].condition = 0;
	}

	if(selected != -1){
		if(nowStop(now) == selected) selected = -1;
		if(((count / 5) % 2 == 0 && nextStop(now) == selected)|| status != 3){
			image(chip,1100,600,chip.width/2, chip.height/2);
		}
	}
	count++;
	//println("frameRate: "+frameRate);

	textFont(kozu20);

	fill(50, 255, 50);
	rect(100,600,50,50);
	fill(0);
	text("\u2026\u73fe\u5728\u5730",170,635);

	fill(255, 155, 50);
	rect(100,670,50,50);
	fill(0);
	text("\u2026\u6b62\u307e\u308b\u5834\u6240",170,705);

	textFont(kozu30);

	//\u72b6\u614b\u8868\u793a
	switch (status) {
		case 0:
			fill(200);
			rect(583,600,200,100);
			fill(50);
			text("\u4e57\u8eca",648,662);
			break;
		case 1:
			fill(100,200,100);
			rect(583,600,200,100);
			fill(0,50,0);
			textFont(kozu20);
			text("\u964d\u8eca\u5834\u6240\u9078\u629e\u524d",615,660);
			break;
		case 2:
			fill(200,150,100);
			rect(583,600,200,100);
			fill(50,25,0);
			textFont(kozu20);
			text("\u964d\u8eca\u5834\u6240\u9078\u629e\u5f8c",615,660);
			break;
		case 3:
			fill(200,200,100);
			rect(583,600,200,100);
			fill(50,50,0);
			textFont(kozu20);
			text("\u964d\u8eca\u5834\u6240\u76f4\u524d",625,660);
			break;
		case 4:
			fill(100,100,200);
			rect(583,600,200,100);
			fill(0,0,50);
			text("\u964d\u8eca",648,662);
			break;
		default :
			break;
	}

}

public void mousePressed() {
	for (int i = 0; i < 14; ++i) {
		if(button[i].chk_mouse()){
			button[i].condition = 2;
			selected = i;
			status = 2;
		}
	}

	if(mousePos(1108,1292,607,706)){
		if(selected != -1){
			button[selected].reset_condition();
			selected = -1;
			status = 1;
		}
	}
	if(mousePos(583,783,600,700)){
		if(status == 0) status++;
	}

	//println("mouseX: "+mouseX);
	//println("mouseY: "+mouseY);
}

public boolean mousePos(float x1, float x2, float y1, float y2){
	if(mouseX > x1 && mouseX < x2 && mouseY > y1 && mouseY < y2) return true;
	else return false;
}

public int nextStop(int n){
	println("n: "+n);
	if(n < 13) return n + 1;
	else if(n >= 13 && n < 18) return 19 - n;
	else if(n == 18) return 0;
	else return -1;
}

public int nowStop(int n){
	if(n < 14) return n;
	else return 20 - n;
}

public int preStop(int n){
	if(n == 0) return 2;
	else if(n < 15) return n - 1;
	else if(n <= 18) return 21 - n;
	else return -1;
}
/*
 *height: 0(short) or 1(long)
 *condition: 0(none) or 1(now) or 2(selected)
 */
class Button {
	float xPos, yPos, h, height, width;
	int condition;
	Button (float[] pos){
		xPos = pos[0];
		yPos = pos[1];
		h = pos[2];
		condition = 0;
		width = 75;
		if(h == 0) height = 75;
		else       height = 225;
	}

	public void display(){
		if(condition == 0) fill(255,255,255,50);
		else if(condition == 1) fill(50, 255, 50, 50);
		else if(condition == 2) fill(255, 155, 50, 80);

		if(height == 0) rect(xPos, yPos, width, height);
		else            rect(xPos, yPos, width, height);
	}

	public boolean chk_mouse(){
		if(mousePos(xPos, xPos + width, yPos, yPos + height)) return true;
		else return false;
	}

	public void reset_condition(){
		condition = 0;
	}
}
  public void settings() { 	size(1366,768); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "UCD" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}

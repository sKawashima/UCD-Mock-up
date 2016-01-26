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

	void display(){
		if(condition == 0) fill(255,255,255,50);
		else if(condition == 1) fill(50, 255, 50, 50);
		else if(condition == 2) fill(255, 155, 50, 80);

		if(height == 0) rect(xPos, yPos, width, height);
		else            rect(xPos, yPos, width, height);
	}

	boolean chk_mouse(){
		if(mousePos(xPos, xPos + width, yPos, yPos + height)) return true;
		else return false;
	}

	void reset_condition(){
		condition = 0;
	}
}
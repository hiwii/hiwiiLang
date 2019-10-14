package net.hiwii.system.syntax.round;

import java.math.RoundingMode;

import net.hiwii.cognition.Expression;

public class Rounding extends Expression {
	private String mode;

	public Rounding() {
		mode = "HALF_UP";
	}

	public Rounding(String mode) {
		this.mode = mode;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public RoundingMode toRoundingMode(){
		if(mode.equals("UP")){
			return RoundingMode.UP;
		}else if(mode.equals("DOWN")){
			return RoundingMode.DOWN;
		}else if(mode.equals("FLOOR")){
			return RoundingMode.FLOOR;
		}else if(mode.equals("CEILING")){
			return RoundingMode.CEILING;
		}else if(mode.equals("HALF_UP")){
			return RoundingMode.HALF_UP;
		}else if(mode.equals("HALF_DOWN")){
			return RoundingMode.HALF_DOWN;
		}else if(mode.equals("HALF_EVEN ")){
			return RoundingMode.HALF_EVEN;
		}else if(mode.equals("UNNECESSARY  ")){
			return RoundingMode.UNNECESSARY;
		}else{
			return RoundingMode.HALF_UP;
		}
	}

	@Override
	public String getClassName() {
		return "Round";
	}

	@Override
	public String toString() {
		return "Round(" + mode + ")";
	}
}

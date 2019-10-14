package net.hiwii.system.syntax.number;

import java.math.RoundingMode;

public class HiwiiMathContext {
	private boolean precise = true;
	private int precision = 4;
	private RoundingMode mode = RoundingMode.HALF_UP;
	private boolean preset = false;
	private boolean modeset = false;
	
	public boolean isPrecise() {
		return precise;
	}
	public void setPrecise(boolean precise) {
		this.precise = precise;
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	public RoundingMode getMode() {
		return mode;
	}
	public void setMode(RoundingMode mode) {
		this.mode = mode;
	}
	public boolean isPreset() {
		return preset;
	}
	public void setPreset(boolean preset) {
		this.preset = preset;
	}
	public boolean isModeset() {
		return modeset;
	}
	public void setModeset(boolean modeset) {
		this.modeset = modeset;
	}
}

package net.hiwii.http;

import java.io.File;
import java.util.Map;

public class MultiPartMessage extends HttpMessage {
	private Map<String, String> textRecords;
	private Map<String, File> mediaRecords;
	public Map<String, String> getTextRecords() {
		return textRecords;
	}
	public void setTextRecords(Map<String, String> textRecords) {
		this.textRecords = textRecords;
	}
	public Map<String, File> getMediaRecords() {
		return mediaRecords;
	}
	public void setMediaRecords(Map<String, File> mediaRecords) {
		this.mediaRecords = mediaRecords;
	}
}

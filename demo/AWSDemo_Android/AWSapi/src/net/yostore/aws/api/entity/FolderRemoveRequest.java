package net.yostore.aws.api.entity;

import java.io.StringWriter;

import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

public class FolderRemoveRequest {
	
	public FolderRemoveRequest(){}
	public FolderRemoveRequest(String uid, String token, String folderid){
		this._userid = uid;
		this._token = token;
		this._id = folderid;
	}

	private String _token;
	public String getToken(){ return this._token; }
	public void setToken(String value){ this._token = value; }

	private String _scrip=String.valueOf(System.currentTimeMillis());
	public String getScrip(){ return this._scrip; }
	public void setScrip(String value){ this._scrip = value; }

	private String _userid;
	public String getUserid(){ return this._userid; }
	public void setUserid(String value){ this._userid = value; }

	private String _id;
	public String getId(){ return this._id; }
	public void setId(String value){ this._id = value; }

	private boolean _ischildonly=false;
	public boolean getIschildonly(){ return this._ischildonly; }
	public void setIschildonly(boolean value){ this._ischildonly = value; }
	public String toXml(){
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "remove");
			serializer.startTag("", "token");
			serializer.text(this._token);
			serializer.endTag("", "token");
			serializer.startTag("", "scrip");
			serializer.text(this._scrip);
			serializer.endTag("", "scrip");
			serializer.startTag("", "userid");
			serializer.text(this._userid);
			serializer.endTag("", "userid");
			serializer.startTag("", "id");
			serializer.text(this._id);
			serializer.endTag("", "id");
			if (this._ischildonly){
				serializer.startTag("", "ischildonly");
				serializer.text("1");
				serializer.endTag("", "ischildonly");
			}
			serializer.endTag("", "remove");
			serializer.endDocument();
			return writer.toString();
//			return "?xml=" + URLEncoder.encode(writer.toString());

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}// end class 

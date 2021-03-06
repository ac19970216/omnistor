package com.ecareme;

import java.io.IOException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import asuswebstorage.user.info.userInfo;

import entity.AcquireTokenResponse;
import entity.DeletesharecodeResponse;

public class Deletesharecode {

	static String API = "/fsentry/deletesharecode/";// Service api

	public static void main(String[] args) throws Exception {
		Deletesharecode deletesharecode = new Deletesharecode();
		DeletesharecodeResponse rsp = deletesharecode.getResponse();
		System.out.println(rsp.toString());
	}

	public DeletesharecodeResponse getResponse() throws Exception {
		/*
		 * Fetching token from RequestServiceGateway by userid & pwd. Then you
		 * will get token, Inforelay and Webrelay domain/IP
		 */
		AcquireToken at = new AcquireToken();
		AcquireTokenResponse acquireTokenResponse = null;
		try {
			acquireTokenResponse = at.getResponse(userInfo.userid, userInfo.pwd);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		// The correct AcquireToken response payload's status value must equals
		// "0
		if (acquireTokenResponse == null
				|| !"0".equals(acquireTokenResponse.getStatus())) {
			throw new Exception(
					"Error : You have to check the AcquireToken api response");
		}

		String token = acquireTokenResponse.getToken();// Token value
		String server = acquireTokenResponse.getInfoRelay();// Connection server

		String urlstr = server + API;
		URL url = new URL(urlstr);

		/* The HttpsURLConnection start */
		HttpsURLConnection connection = (HttpsURLConnection) url
				.openConnection();
		connection.setConnectTimeout(60 * 1000);
		connection.setReadTimeout(60 * 1000);

		StringBuilder cookie = new StringBuilder();
		cookie.append("sid=").append(userInfo.sid).append(";");// Cookies have to add the
														// value of SID

		connection.addRequestProperty("cookie", cookie.toString());// User must
																	// add the
																	// cookies
																	// in the
																	// header
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		try {
			connection.connect();
		} catch (IOException ioe) {
			System.err.println("Get Connection Error:" + ioe.getMessage());
			throw ioe;
		}

		/*
		 * Preparing for DocumentBuilderFactory. This class is available at:
		 * http://download.oracle.com/javase/1.4.2/docs/api/javax/xml/parsers/
		 * DocumentBuilderFactory.html
		 */
		String root = "deletesharecode";// The root element of request payload
		String entrytype = "0";// The deletesharecoded file's type: 0 = Folder |
								// 1 = File
		String entryid = "44812268";// Deletesharecoded file's id
		String password = "";// The deletesharecoded file's password, it must require using lower-case letters and encrypt the password to its MD5 hash

		String[] elmName = { "token", "userid", "entrytype", "entryid",
				"password" };// Define each XML tag name
		String[] data = { token, userInfo.userid, entrytype, entryid, password };// Set each value of tag

		/*
		 * To create XML documents. DocumentBuilderFactory can obtain a parser
		 * that produces DOM object trees from XML documents
		 */
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory
				.newDocumentBuilder();
		Document document = documentBuilder.newDocument();
		Element rootElement = document.createElement(root);
		document.appendChild(rootElement);
		Element elm;
		int i;
		for (i = 0; i < data.length; i++) {
			elm = document.createElement(elmName[i]);
			elm.appendChild(document.createTextNode(data[i]));
			rootElement.appendChild(elm);
		}

		/*
		 * Used to process XML from a variety of sources and write the
		 * transformation output to server
		 */
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(connection.getOutputStream());
		transformer.transform(source, result);

		/* Get the response from the server and parse it */
		DocumentBuilderFactory documentBuilderFactoryResponse = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder documentBuilderResponse = documentBuilderFactoryResponse
				.newDocumentBuilder();
		Document documentResponse = documentBuilderResponse.parse(connection
				.getInputStream());
		Element rootResponse = (Element) documentResponse.getDocumentElement();
		NodeList nodelist;
		nodelist = rootResponse.getChildNodes();

		// Compose the payload
		DeletesharecodeResponse rsp = new DeletesharecodeResponse();
		int j;
		for (j = 0; j < nodelist.getLength(); j++) {
			if (nodelist.item(j).getNodeName().equals("status")) {
				rsp.setStatus(nodelist.item(j).getTextContent());
			}
			if (nodelist.item(j).getNodeName().equals("scrip")) {
				rsp.setScrip(nodelist.item(j).getTextContent());
			}
		}

		// The correct response payload's status value must equals "0"
		if (!"0".equals(rsp.getStatus())) {
			throw new Exception(
					"Error : You have to check Deletesharecode api response");
		}

		System.out.println(" --- FINISH DELETE SHARECODE --- \n");

		return rsp;
	}
}
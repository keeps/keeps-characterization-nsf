package pt.keep.validator.nsf;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import lotus.notes.Database;
import lotus.notes.Document;
import lotus.notes.DocumentCollection;
import lotus.notes.NotesException;
import lotus.notes.NotesThread;
import lotus.notes.Session;
import lotus.notes.View;
import lotus.notes.ViewColumn;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.lang.StringEscapeUtils;

import pt.keep.validator.nsf.result.Result;
import pt.keep.validator.nsf.result.ValidationInfo;


public class NsfCharacterizationTool {
	private static String version = "1.0";

	public String getVersion(){
		return version;
	}
	public String run(File f) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			Result res = process(f);
			JAXBContext jaxbContext = JAXBContext.newInstance(Result.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(res, bos);
			return bos.toString("UTF-8");
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return null;
	}

	public Result process(File f) {
		Result res = new Result();
		
		try {
			NotesThread.sinitThread();
			Session s = Session.newInstance();
			Database db = s.getDatabase("",f.getPath());
			if(db==null){
				ValidationInfo val = new ValidationInfo();
				val.setValid(false);
				res.setValidationInfo(val);
			}else{
				ValidationInfo val = new ValidationInfo();
				val.setValid(db.isOpen()?true:false);
				res.setValidationInfo(val);
				Map<String,String> features = new HashMap<String,String>();
				features.put("created", StringEscapeUtils.escapeXml(db.getCreated().toString()));
				features.put("documentCount", ""+db.getAllDocuments().getCount());
				features.put("percentUsed", ""+db.getPercentUsed());
				features.put("size", ""+db.getSize());
				features.put("sizeQuota", ""+db.getSizeQuota());
				features.put("categories", StringEscapeUtils.escapeXml(db.getCategories()));
				
				if(db.getViews()!=null){
				  String views = "";
				  for(Object o : db.getViews()){
				    View v = (View)o;
				    System.out.println("View:"+v.getName());
				    System.out.println(v.getColumns());
				    if(v.getFirstDocument()!=null){
				      Document d = v.getFirstDocument();
				      while(v!=null){
				        System.out.println(d.getColumnValues());
				        System.out.println(d.getAuthors());
				        System.out.println(d.getSize());
				        d = v.getNextDocument(d);
				      }
				      
				    }
				    System.out.println(v.getFirstDocument().getCreated());
				    views+=v.getName() + " ; ";
				  }
				  views = views.substring(0,views.length()-3);
				  features.put("views", StringEscapeUtils.escapeXml(views));
				}
				res.setFeatures(features);
			}
		}catch (Exception e) { 
			ValidationInfo val = new ValidationInfo();
			val.setValid(true);
			res.setValidationInfo(val);
		}finally { 
			NotesThread.stermThread(); 
		}

		return res;
	}

	


	private String extractData(Database db) {
		StringBuffer sb = new StringBuffer();
		try{
			for(Object o : db.getViews()){
				View v = (View)o;
				String viewName = v.getName();
				if(v.getFirstDocument()!=null){
					sb.append("<"+viewName+">");
					Document doc1 = v.getFirstDocument();
					while(doc1!=null){
						int i=0;
						sb.append("<data>");
						for(Object columnObject : v.getColumns()){
							ViewColumn viewColumn = (ViewColumn)columnObject;
							sb.append("<"+viewColumn.getTitle()+">"+doc1.getColumnValues().get(i)+"</"+viewColumn.getTitle()+">");
							i++;
						}
						sb.append("</data>");
						doc1 = v.getNextDocument(doc1);
					}
					sb.append("</"+viewName+">");
				}
			}
		}catch(NotesException ne){
			sb.delete(0, sb.length());
			sb.append("");
		}
		return sb.toString();
	}
	private void printHelp(Options opts) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar [jarFile]", opts);
	}
	
	private void printVersion() {
		System.out.println(version);
	}
	
	
	
	
	
	public static void main(String[] args) {
		try {
			NsfCharacterizationTool rct = new NsfCharacterizationTool();
			Options options = new Options();
			options.addOption("f", true, "file to analyze");
			options.addOption("v", false, "print this tool version");
			options.addOption("h", false, "print this message");

			CommandLineParser parser = new GnuParser();
			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption("h")) {
				rct.printHelp(options);
				System.exit(0);
			}
			
			if (cmd.hasOption("v")) {
				rct.printVersion();
				System.exit(0);
			}

			if (!cmd.hasOption("f")) {
				rct.printHelp(options);
				System.exit(0);
			}

			File f = new File(cmd.getOptionValue("f"));
			if (!f.exists()) {
				System.out.println("File doesn't exist");
				System.exit(0);
			}
			String toolOutput = rct.run(f);
			if(toolOutput!=null){
				System.out.println(toolOutput);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

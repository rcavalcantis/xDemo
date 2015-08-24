package demoiselle.xDemo.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.render.Box;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;


public class PdfUtils {

	
	public static void generatePDF(String xhtml, OutputStream output, byte[] signatureImage) throws DocumentException, IOException {
		render(xhtml,signatureImage).createPDF(output);
	}
	
	public static void generatePdf(File xhtml, OutputStream output, byte[] signatureImage) throws DocumentException, IOException {
		render(xhtml,signatureImage).createPDF(output);
	}
	
	private static ITextRenderer iniciarRenderer(byte[] signatureImage){
		ITextRenderer renderer = new ITextRenderer();
		ReplacedElementFactory ref = renderer.getSharedContext().getReplacedElementFactory();
		//renderer.getSharedContext().setReplacedElementFactory(new ProfileImageReplacedElementFactory(ref, signatureImage));
		return renderer;
	}
	
	private static ITextRenderer render(String xhtml, byte[] signatureImage){
		ITextRenderer renderer = iniciarRenderer(signatureImage);
		renderer.setDocumentFromString(xhtml);
		renderer.layout();
		return renderer;
	}
	
	private static ITextRenderer render(File xhtml, byte[] signatureImage) throws IOException{
		ITextRenderer renderer = iniciarRenderer(signatureImage);
		renderer.setDocument(xhtml);
		renderer.layout();
		return renderer;
	}
	
	public static boolean validateMessageSize(String html, String tagName, String idAttribute) {
		boolean valid = false;
		
		ITextRenderer renderer = render(html, null);
		Box messageBox = findBox(renderer.getRootBox(), tagName, idAttribute);
		if(messageBox != null && messageBox instanceof BlockBox) {
			BlockBox messageBlockBox = (BlockBox) messageBox;
			if(messageBox.getHeight() + 200 >= messageBlockBox.getChildrenHeight()) {
				//A altura da mensagem está OK, verificar agora a largura
				boolean validWidth = true;
				for(Object obj :  messageBlockBox.getChildren()) {
					Box childBox = (Box) obj;
					if(childBox.getContentWidth() > messageBox.getWidth()) {
						validWidth = false;
						break;
					}	 else {
						if(!isValidWidth(messageBox, childBox)) {
							validWidth = false;
							break;
						}
					}
				}
				
				valid = validWidth;
				
			} else {
				valid = false;
			}
		}
		return valid;
	}
	
	private static Box findBox(Box rootBox, String tagName, String idAttribute) {
		Box box = null;						
		
		Element rootElement = rootBox.getElement();
		
		if(rootElement != null && tagName.equals(rootElement.getTagName()) && idAttribute.equals(rootElement.getAttribute("id"))) {
			box = rootBox;
		} else {
			for(int i = 0; i < rootBox.getChildren().size(); i++) {
				box = findBox((Box) rootBox.getChildren().get(i), tagName, idAttribute);
				if(box != null) {
					break;
				}
			}
		}
		
		return box;
	}
	
	private static boolean isValidWidth(Box rootBox, Box childBox) {
		boolean valid = true;
		
		int width = rootBox.getWidth();
		
		if(childBox.getContentWidth() > width) {
			valid = false;
		} else {
			for(int i = 0; i < childBox.getChildren().size(); i++) {
				valid = isValidWidth(rootBox, childBox.getChild(i));
				if(!valid) {
					break;
				}
			}
		}
		
		return valid;
	}
	
	public static void appendPDF(List<InputStream> list, OutputStream outputStream)
            throws DocumentException, IOException {
		Document document = new Document();
		PdfCopy copy = new PdfCopy(document, outputStream);
		document.open();
		
		PdfReader reader;
		int n;
		// loop over the documents you want to concatenate
		for (InputStream in : list) {
			reader = new PdfReader(in);
			// loop over the pages in that document
			n = reader.getNumberOfPages();
			for (int page = 0; page < n; ) {
			    copy.addPage(copy.getImportedPage(reader, ++page));
			}
			copy.freeReader(reader);
			reader.close();
		}
		
		document.close();		
    }

	

}
